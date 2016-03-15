package com.ai.baas.op.util;

import com.ai.baas.op.constants.ElasticIndex;
import com.ai.baas.op.constants.ElasticType;
import com.ai.baas.op.model.ApiTemplate;
import com.ai.baas.op.model.ApiTemplateBase;
import com.ai.baas.op.model.ApiTemplateIndex;
import com.ai.baas.op.model.ReqParamTemplate;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

/**
 * 参数模板查询类<br>
 * Date: 2016年02月28日 <br>
 * Copyright (c) 2016 asiainfo.com <br>
 *
 * @author liutong
 */
public class ApiTemplateUtils {

    public static ApiTemplate getApiTemplate(
            String owner,String interfaceName, String method){
        String template = getTemplate(owner,interfaceName,method);
        ApiTemplate apiTemplate = null;
        //若能查询到模板,则填充实体
        if (StringUtils.isNotBlank(template)){
            apiTemplate = new ApiTemplate();
            ApiTemplateIndex templateIndex = JSONObject.parseObject(template,ApiTemplateIndex.class);
            BeanUtils.copyProperties(templateIndex, apiTemplate);
            Map<String,ReqParamTemplate> reqParamTemplates = new HashMap<String,ReqParamTemplate>();
            List<ReqParamTemplate> reqParamTempList = templateIndex.getReqParamTemplates();
            if (reqParamTempList!=null&&reqParamTempList.size()>0) {
                for (ReqParamTemplate reqParamTemplate:reqParamTempList){
                    reqParamTemplates.put(reqParamTemplate.getParamName(),reqParamTemplate);
                }
            }
            apiTemplate.setReqParamTemplates(reqParamTemplates);
        }
        return apiTemplate;
    }

    /**
     * 根据索引ID获取模板数据
     * @param templateId
     * @return
     */
    public static String getTemplateById(String templateId){
        Client client = ElasticSearchUtil.getElasticSearcher().getClient(ElasticIndex.API);
        SearchResponse response = client.prepareSearch(ElasticIndex.API.name().toLowerCase())
                .setTypes(ElasticType.API_TEMPLATES.name().toLowerCase())
                .setQuery(termQuery("_id", templateId)).execute().actionGet();

        SearchHits hits = response.getHits();
        return hits.getTotalHits()>0?hits.getHits()[0].getSourceAsString():null;
    }

    /**
     * 根据owner,interfaceName,methodName获取模板数据
     * @param owner
     * @param interfaceName
     * @param method
     * @return
     */
    public static String getTemplate(
            String owner,String interfaceName, String method
    ){
        Client client = ElasticSearchUtil.getElasticSearcher().getClient(ElasticIndex.API);
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        if (!StringUtils.isBlank(owner)) {
            builder.must(QueryBuilders.termQuery("owner", owner.toLowerCase()));
        }
        if (!StringUtils.isBlank(interfaceName)) {
            builder.must(QueryBuilders.termQuery("interfaceName", interfaceName.toLowerCase()));
        }
        if (!StringUtils.isBlank(method)) {
            builder.must(QueryBuilders.termQuery("method", method.toLowerCase()));
        }

        /* 2.获取所有的API_TEMPLATES信息 */
        SearchResponse response = client.prepareSearch(ElasticIndex.API.name().toLowerCase())
                .setTypes(ElasticType.API_TEMPLATES.name().toLowerCase()).setFrom(0)
                .setSize(1).setQuery(builder).execute().actionGet();

        SearchHits hits = response.getHits();
        return hits.getTotalHits()>0?hits.getHits()[0].getSourceAsString():null;
    }
}
