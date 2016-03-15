package com.ai.baas.op.api.apisearch.impl;

import com.ai.baas.op.api.apisearch.IAPISearchSV;
import com.ai.baas.op.api.apisearch.param.*;
import com.ai.baas.op.constants.ElasticIndex;
import com.ai.baas.op.constants.ElasticType;
import com.ai.baas.op.sandbox.util.ReflectUtil;
import com.ai.baas.op.util.ApiTemplateUtils;
import com.ai.baas.op.util.DubboApiSearchUtil;
import com.ai.baas.op.util.ElasticSearchUtil;
import com.ai.paas.ipaas.dbs.util.CollectionUtil;
import com.ai.runner.apicollector.vo.APIDoc;
import com.ai.runner.apicollector.vo.APIOwnerDoc;
import com.ai.runner.base.exception.CallerException;
import com.ai.runner.base.exception.SystemException;
import com.ai.runner.base.vo.PageInfo;
import com.ai.runner.utils.util.BeanUtils;
import com.ai.runner.utils.util.StringUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Service
@Component
public class APISearchSVImpl implements IAPISearchSV {

    @Override
    public List<APIOwnerType> getAPIOwnerTypes() throws CallerException {
        List<APIOwnerType> list = DubboApiSearchUtil.getAPIOwnerTypes();
        if (!CollectionUtil.isEmpty(list)) {
            for (APIOwnerType ownerType : list) {
                List<APIOwnerStatistics> result = DubboApiSearchUtil
                        .getAPIOwnerStatistics(ownerType.getOwnerType());

                int serviceCount = 0;
                Set<String> ownerSet = new HashSet<String>();
                if (!CollectionUtil.isEmpty(result)) {
                    for (APIOwnerStatistics stat : result) {
                        ownerSet.add(stat.getOwner());
                        serviceCount += stat.getApiCount();
                    }
                }
                ownerType.setServiceCount(serviceCount);
                ownerType.setOwnerCount(ownerSet.size());
                ownerType.setOwnerStatistics(result);
            }
        }
        return list;
    }

    @Override
    public PageInfo<APISearchResult> searchAPIDocs(APISearchKey searchKey) throws CallerException {
        if (searchKey == null) {
            throw new SystemException("查询服务出错：参数为空");
        }
        PageInfo<APISearchResult> pageInfo = searchKey.getPageInfo();
        if (pageInfo == null) {
            throw new SystemException("查询服务出错：分页参数为空");
        }
        SearchResponse response = DubboApiSearchUtil.searchAPIsHighLight(searchKey,
                pageInfo.getStartRowIndex() - 1, pageInfo.getEndRowIndex());
        SearchHits hits = response.getHits();
        // 获取总数
        long total = hits.getTotalHits();
        List<APISearchResult> list = new ArrayList<APISearchResult>();
        for (SearchHit hit : hits) {
            String sourceString = hit.getSourceAsString();
            APIDoc source = JSON.parseObject(sourceString, APIDoc.class);
            Map<String, HighlightField> hightLightFields = hit.getHighlightFields();
            APISearchResult bo = new APISearchResult();
            BeanUtils.copyProperties(bo, source);

            String apiCodeHighlight = ElasticSearchUtil.getHightLightFieldValue(hightLightFields,
                    "apiCode");
            String interfaceNameHighlight = ElasticSearchUtil.getHightLightFieldValue(
                    hightLightFields, "interfaceName");
            String methodNameHighlight = ElasticSearchUtil.getHightLightFieldValue(
                    hightLightFields, "methodName");
            String ownerHighlight = ElasticSearchUtil.getHightLightFieldValue(hightLightFields,
                    "owner");
            String groupIdHighlight = ElasticSearchUtil.getHightLightFieldValue(hightLightFields,
                    "groupId");
            String artifactIdHighlight = ElasticSearchUtil.getHightLightFieldValue(
                    hightLightFields, "artifactId");
            String briefCommentHighlight = ElasticSearchUtil.getHightLightFieldValue(
                    hightLightFields, "briefComment");
            String detailCommentHighlight = ElasticSearchUtil.getHightLightFieldValue(
                    hightLightFields, "detailComment");
            String authorHighlight = ElasticSearchUtil.getHightLightFieldValue(hightLightFields,
                    "author");
            String restRelativeURLHighlight = ElasticSearchUtil.getHightLightFieldValue(
                    hightLightFields, "restRelativeURL");

            bo.setRestRelativeURLHighlight(StringUtil.isBlank(restRelativeURLHighlight) ? bo
                    .getRestRelativeURL() : restRelativeURLHighlight);
            bo.setApiCodeHighlight(StringUtil.isBlank(apiCodeHighlight) ? bo.getApiCode()
                    : apiCodeHighlight);
            bo.setInterfaceNameHighlight(StringUtil.isBlank(interfaceNameHighlight) ? bo
                    .getInterfaceName() : interfaceNameHighlight);
            bo.setMethodNameHighlight(StringUtil.isBlank(methodNameHighlight) ? bo.getMethodName()
                    : methodNameHighlight);
            bo.setOwnerHighlight(StringUtil.isBlank(ownerHighlight) ? bo.getOwner()
                    : ownerHighlight);
            bo.setGroupIdHighlight(StringUtil.isBlank(groupIdHighlight) ? bo.getGroupId()
                    : groupIdHighlight);
            bo.setArtifactIdHighlight(StringUtil.isBlank(artifactIdHighlight) ? bo.getArtifactId()
                    : artifactIdHighlight);
            bo.setBriefCommentHighlight(StringUtil.isBlank(briefCommentHighlight) ? bo
                    .getBriefComment() : briefCommentHighlight);
            bo.setDetailCommentHighlight(StringUtil.isBlank(detailCommentHighlight) ? bo
                    .getDetailComment() : detailCommentHighlight);
            bo.setAuthorHighlight(StringUtil.isBlank(authorHighlight) ? bo.getAuthor()
                    : authorHighlight);
            boolean isSetted = checkAPISetting(bo.getOwner(), bo.getInterfaceName(),
                    bo.getMethodName());
            bo.setSetted(isSetted);
            // 获取API的所有版本信息
            List<APIVersion> versions = DubboApiSearchUtil.getAPIAllVersions(bo.getOwnerType(),
                    bo.getOwner(), bo.getInterfaceName(), bo.getMethodName());
            bo.setApiVersions(versions);
            list.add(bo);

        }
        pageInfo.setCount(Integer.parseInt(StringUtil.toString(total)));
        pageInfo.setPageNo(pageInfo.getPageNo());
        pageInfo.setPageSize(pageInfo.getPageSize());
        pageInfo.setPageCount(pageInfo.getPageCount());
        pageInfo.setResult(list);
        return pageInfo;
    }

    /**
     * 判断API是否已经设置过了
     * 
     * @param interfaceName
     * @param method
     * @return
     * @author zhangchao
     */
    private boolean checkAPISetting(String owner, String interfaceName, String method) {
        String template = ApiTemplateUtils.getTemplate(owner,interfaceName,method);
        return StringUtils.isNotBlank(template);
    }

    @Override
    public String getAPIVersionNew(String apiDetailIndexId) throws CallerException {
        if (StringUtil.isBlank(apiDetailIndexId)) {
            throw new SystemException("获取API详细错误:缺少索引ID");
        }
        return DubboApiSearchUtil.getAPIVersionNew(apiDetailIndexId);
    }

    @Override
    public String getAPIClassDetail(String apiParamIndexId) throws CallerException {
        if (StringUtil.isBlank(apiParamIndexId)) {
            throw new SystemException("获取API参数类的属性列表错误:缺少参数类索引ID");
        }
        return DubboApiSearchUtil.getAPIClassDetail(apiParamIndexId);
    }

    @Override
    public String getAPIStatistics() throws CallerException {
        int ownerTypeCount = DubboApiSearchUtil.getAPIOwnerTypeCount();
        int ownerCount = DubboApiSearchUtil.getAPIOwnerCount();
        long apiCount = DubboApiSearchUtil.getAPICount();
        JSONObject data = new JSONObject();
        data.put("ownerTypeCount", ownerTypeCount);
        data.put("ownerCount", ownerCount);
        data.put("apiCount", apiCount);
        return data.toString();
    }

    @Override
    public void deleteAPINew(String indexId) throws CallerException {
        if (StringUtil.isBlank(indexId)) {
            throw new SystemException("作废服务失败：没有传入索引信息");
        }
        Client client = ElasticSearchUtil.getElasticSearcher().getClient(ElasticIndex.API);
        client.prepareDelete(ElasticIndex.API.name().toLowerCase(),
                ElasticType.API_VERSION_NEW.name().toLowerCase(), indexId).execute().actionGet();
    }

}
