package com.ai.baas.op.service.business.impl;

import com.ai.baas.op.api.sandbox.param.*;
import com.ai.baas.op.constants.ElasticIndex;
import com.ai.baas.op.constants.ElasticType;
import com.ai.baas.op.model.ApiTemplate;
import com.ai.baas.op.model.ApiTemplateIndex;
import com.ai.baas.op.model.ReqParamTemplate;
import com.ai.baas.op.sandbox.mock.DubboMockUtil;
import com.ai.baas.op.service.business.interfaces.IApiSandBoxSettingSV;
import com.ai.baas.op.util.ApiTemplateUtils;
import com.ai.baas.op.util.ElasticSearchUtil;
import com.ai.paas.ipaas.dbs.util.CollectionUtil;
import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import org.apache.lucene.queryparser.flexible.core.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ApiSandBoxSettingSVImpl implements IApiSandBoxSettingSV {

    @Override
    public void saveAPICallSetting(APICallSetting req,ApiTemplate apiTemplate) {
        //
        ApiTemplateIndex templateIndex = new ApiTemplateIndex();
        BeanUtils.copyProperties(apiTemplate,templateIndex);
        templateIndex.setReturnJson(req.getReturnJson());
        //获取请求参数
        List<APICallSettingReq> reqSetting = req.getReqSettings();
        Map<String,ReqParamTemplate> reqParamMap = apiTemplate.getReqParamTemplates();
        //遍历请求参数
        for (APICallSettingReq settingReq : reqSetting){
            ReqParamTemplate reqParam = reqParamMap.get(settingReq.getParamName());
            reqParam.setJson(settingReq.getJson());
        }
        List<ReqParamTemplate> reqParamList =
                new ArrayList<>(reqParamMap.values());
        templateIndex.setReqParamTemplates(reqParamList);
        //更新索引数据
        ElasticSearchUtil.getElasticSearcher().updateIndex(
                ElasticIndex.API, ElasticType.API_TEMPLATES,
                StringUtils.toString(apiTemplate.getId()),JSONObject.toJSONString(templateIndex));
    }

    @Override
    public String mockTest(APICallCase callCase, String registryURL) {
        String[] reqParamJavaTypes = null;
        String[] qs = null;
        List<APICallCaseReqParam> reqParams = callCase.getReqParams();
        if (!CollectionUtil.isEmpty(reqParams)) {
            ApiTemplate apiTemplate = ApiTemplateUtils.getApiTemplate(callCase.getOwner(),
                    callCase.getInterfaceName(), callCase.getMethod());
            Map<String,ReqParamTemplate> reqParamMap =
                    new HashMap<String,ReqParamTemplate>();
            if (apiTemplate!=null)
                reqParamMap = apiTemplate.getReqParamTemplates();
            reqParamJavaTypes = new String[reqParams.size()];
            qs = new String[reqParams.size()];
            for (APICallCaseReqParam bo : reqParams) {
                ReqParamTemplate reqParamTemplate = reqParamMap.get(bo.getParamName());
                int index = reqParams.indexOf(bo);
                reqParamJavaTypes[index] = reqParamTemplate.getJavaType();
                qs[index] = bo.getJson();
            }
        }
        Object result = DubboMockUtil.mockCall(registryURL, callCase.getOwner(),
                callCase.getInterfaceName(), callCase.getMethod(), reqParamJavaTypes, qs);
        return JSON.toJSONString(result);
    }

	@Override
	public void addAPICallSetting(APICallSetting req, ApiTemplate apiTemplate) {
		 //
        ApiTemplateIndex templateIndex = new ApiTemplateIndex();
        BeanUtils.copyProperties(apiTemplate,templateIndex);
        templateIndex.setReturnJson(req.getReturnJson());
        //获取请求参数
        List<APICallSettingReq> reqSetting = req.getReqSettings();
        
    	Map<String,ReqParamTemplate> reqParamTemplates=new HashMap<String,ReqParamTemplate>();
		for(APICallSettingReq setReq:reqSetting){
			ReqParamTemplate template=new ReqParamTemplate();
			template.setJavaType(setReq.getJavaType());
			template.setJson(setReq.getJson());
			template.setParamName(setReq.getParamName());
			reqParamTemplates.put(setReq.getParamName(),template);
		
			
		}
       
        List<ReqParamTemplate> reqParamList =
                new ArrayList<>(reqParamTemplates.values());
        templateIndex.setReqParamTemplates(reqParamList);
        //更新索引数据
        ElasticSearchUtil.getElasticSearcher().addIndex(
                ElasticIndex.API, ElasticType.API_TEMPLATES,
                StringUtils.toString(apiTemplate.getId()),JSONObject.toJSONString(templateIndex));
		
	}
}
