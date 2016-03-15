package com.ai.baas.op.api.sandbox.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.baas.op.api.apisearch.IAPISearchSV;
import com.ai.baas.op.api.sandbox.ISandBoxSV;
import com.ai.baas.op.api.sandbox.param.APICallCase;
import com.ai.baas.op.api.sandbox.param.APICallCaseReqParam;
import com.ai.baas.op.api.sandbox.param.APICallSetting;
import com.ai.baas.op.api.sandbox.param.APICallSettingReq;
import com.ai.baas.op.api.sandbox.param.APIRest;
import com.ai.baas.op.api.sandbox.param.APIRestParam;
import com.ai.baas.op.api.sandbox.param.APIRestTestReq;
import com.ai.baas.op.model.ApiTemplate;
import com.ai.baas.op.model.ReqParamTemplate;
import com.ai.baas.op.sandbox.mock.DubboMockUtil;
import com.ai.baas.op.sandbox.mock.HttpClientUtil;
import com.ai.baas.op.sandbox.util.OspSandboxUtil;
import com.ai.baas.op.sandbox.util.ReflectUtil;
import com.ai.baas.op.service.business.interfaces.IApiSandBoxSettingSV;
import com.ai.baas.op.util.ApiTemplateUtils;
import com.ai.baas.op.util.DubboApiSearchUtil;
import com.ai.paas.ipaas.dbs.util.CollectionUtil;
import com.ai.runner.apicollector.util.JavaDocletUtil;
import com.ai.runner.apicollector.vo.APIDoc;
import com.ai.runner.apicollector.vo.APIParamDoc;
import com.ai.runner.base.exception.CallerException;
import com.ai.runner.base.exception.SystemException;
import com.ai.runner.utils.util.StringUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Service
@Component
public class SandBoxSVImpl implements ISandBoxSV {

	@Autowired
	IApiSandBoxSettingSV iApiSandBoxSettingSV;
	@Autowired
	IAPISearchSV iapiSearchSV;

	@Override
	public APICallSetting getAPICallSettingFromES(String indexId) throws CallerException {
		if (StringUtil.isBlank(indexId)) {
			throw new SystemException("获取接口配置出错：API的索引ID为空");
		}
		/* 1.首先从搜索引擎获取相关信息 */
		String data = DubboApiSearchUtil.getAPIVersionNew(indexId);
		if (StringUtil.isBlank(data)) {
			throw new SystemException("获取接口配置出错：此API索引信息不存在");
		}
		APIDoc apiDoc = JSONObject.parseObject(data, APIDoc.class);
		APICallSetting record = new APICallSetting();
		/* 2.封装接口主题信息 */
		ApiTemplate apiTemplate = ApiTemplateUtils.getApiTemplate(apiDoc.getOwner(), apiDoc.getInterfaceName(),
				apiDoc.getMethodName());
		if (apiTemplate != null) {
			BeanUtils.copyProperties(apiTemplate, record);
			record.setFirstSetting(false);
		} else {
			record.setInterfaceName(apiDoc.getInterfaceName());
			record.setMethod(apiDoc.getMethodName());
			record.setFirstSetting(true);
		}
		record.setIndexId(apiDoc.getId());
		record.setOwner(apiDoc.getOwner());
		record.setOwnerType(apiDoc.getOwnerType());
		record.setBriefComment(apiDoc.getBriefComment());
		record.setAuthor(apiDoc.getAuthor());
		record.setDetailComment(apiDoc.getDetailComment());
		record.setPublishDate(apiDoc.getPublishDate());
		// 2.1 从搜索引擎获取最新的接口返回值信息
		if (apiDoc.getReturnAPIParamDoc() != null) {
			record.setReturnComment(apiDoc.getReturnAPIParamDoc().getCommentText());
			record.setReturnJavaType(apiDoc.getReturnAPIParamDoc().getClassName());
			String editorType = OspSandboxUtil.getEditorType(apiDoc.getReturnAPIParamDoc().getClassName());
			record.setEditorType(editorType);
		}

		/* 2.2 封装接口参数信息，从搜索引擎获取最新的参数列表 */
		List<APICallSettingReq> reqSettings = new ArrayList<APICallSettingReq>();
		if (!CollectionUtil.isEmpty(apiDoc.getInAPIParamDocs())) {
			int i = 0;
			Map<String, ReqParamTemplate> reqParamMap = new HashMap<String, ReqParamTemplate>();
			if (apiTemplate != null)
				reqParamMap = apiTemplate.getReqParamTemplates();
			for (APIParamDoc paramDoc : apiDoc.getInAPIParamDocs()) {
				APICallSettingReq req = new APICallSettingReq();
				ReqParamTemplate reqParamTemplate = reqParamMap.get(paramDoc.getName());
				if (reqParamTemplate != null) {// 设置json
					req.setJson(reqParamTemplate.getJson());
				} else {
					// 对于json为空的情况，需要得到id，获取基本的信息，然后为其设置一个默认的模板

					for (APIParamDoc apiParamDoc : apiDoc.getInAPIParamDocs()) {
						// 需要考虑为基本数据类型的情况
						if (ReflectUtil.checkReflectMap(apiParamDoc.getClassName())) {
							String inParamIndex = String.valueOf(apiParamDoc.getId());
							Map<Object, Object> map = ReflectUtil.reflectClass(inParamIndex);
							req.setJson(JSON.toJSONString(map));
						} else {
							req.setJson("");
						}

					}

				}
				String editorType = OspSandboxUtil.getEditorType(paramDoc.getClassName());
				req.setEditorType(editorType);
				req.setInterfaceName(apiDoc.getInterfaceName());
				req.setJavaType(paramDoc.getClassName());
				req.setMethod(apiDoc.getMethodName());
				req.setParamComment(paramDoc.getCommentText());
				req.setParamName(paramDoc.getName());
				req.setSort(i);
				i++;
				reqSettings.add(req);
			}
		}
		record.setReqSettings(reqSettings);
		return record;
	}

	@Override
	public void saveAPICallSetting(APICallSetting setting) throws CallerException {
		if (setting == null) {
			throw new SystemException("保存服务出入参数模板出错：参数为空");
		}
		if (StringUtil.isBlank(setting.getOwner())) {
			throw new SystemException("保存服务出入参数模板出错：服务提供者为空");
		}
		if (StringUtil.isBlank(setting.getInterfaceName())) {
			throw new SystemException("保存服务出入参数模板出错：服务接口为空");
		}
		if (StringUtil.isBlank(setting.getMethod())) {
			throw new SystemException("保存服务出入参数模板出错：服务方法为空");
		}
//		ApiTemplate apiTemplate = ApiTemplateUtils.getApiTemplate(setting.getOwner(), setting.getInterfaceName(),
//				setting.getMethod());
		if (ApiTemplateUtils.getApiTemplate(setting.getOwner(), setting.getInterfaceName(),
				setting.getMethod()) != null) {
			/* throw new SystemException("保存服务出入参数模板出错:不允许添加模板"); */
			// 获取api的入参参数列表
			ApiTemplate apiTemplate = ApiTemplateUtils.getApiTemplate(setting.getOwner(), setting.getInterfaceName(),
					setting.getMethod());
			APIParamDoc[] inParamDocs = DubboApiSearchUtil.getAPIInAPIParamDocs(setting.getOwnerType(),
					setting.getOwner(), setting.getInterfaceName(), setting.getMethod());
			List<APICallSettingReq> reqSettings = setting.getReqSettings();
			int size1 = CollectionUtil.isEmpty(inParamDocs) ? 0 : inParamDocs.length;
			int size2 = CollectionUtil.isEmpty(reqSettings) ? 0 : reqSettings.size();
			if (size1 != size2) {
				throw new SystemException("保存服务出入参数模板出错：模板设定的入参个数与实际不相符");
			}
			// 获取api的返回值
			APIParamDoc returnAPIParamDoc = DubboApiSearchUtil.getAPIReturnAPIParamDocs(setting.getOwnerType(),
					setting.getOwner(), setting.getInterfaceName(), setting.getMethod());
			if (returnAPIParamDoc != null) {
				setting.setReturnJavaType(returnAPIParamDoc.getClassName());
				setting.setReturnComment(returnAPIParamDoc.getCommentText());
			}
			iApiSandBoxSettingSV.saveAPICallSetting(setting, apiTemplate);
		} else {
			ApiTemplate apiTemplate=new ApiTemplate();
            int id = (setting.getOwner()+"."+setting.getInterfaceName()+"."+setting.getMethod()).hashCode();

			apiTemplate.setId(id);
			apiTemplate.setInterfaceName(setting.getInterfaceName());
			apiTemplate.setMethod(setting.getMethod());
			apiTemplate.setOwner(setting.getOwner());
		
			apiTemplate.setReturnComment(setting.getReturnComment());
			apiTemplate.setReturnJavaType(setting.getReturnJavaType());
			apiTemplate.setReturnJson(setting.getReturnJson());
			// List<APICallSettingReq> reqSetting = req.getReqSettings();
			iApiSandBoxSettingSV.addAPICallSetting(setting, apiTemplate);
		}
		
	}

	@Override
	public String excuteTest(APICallCase callCase, String registryURL) throws CallerException {
		if (callCase == null) {
			throw new SystemException("API服务测试失败：没有数据");
		}
		if (StringUtil.isBlank(callCase.getInterfaceName())) {
			throw new SystemException("API服务测试失败：服务接口为空");
		}
		if (StringUtil.isBlank(callCase.getMethod())) {
			throw new SystemException("API服务测试失败：服务方法为空");
		}
		if (StringUtil.isBlank(registryURL)) {
			throw new SystemException("API服务测试失败：没有选择注册中心");
		}

		List<APICallCaseReqParam> reqParams = callCase.getReqParams();
		if (!CollectionUtil.isEmpty(reqParams)) {
			for (APICallCaseReqParam p : reqParams) {
				if (StringUtil.isBlank(p.getParamName())) {
					throw new SystemException("API服务测试失败：参数名称不能为空");
				}
			}
		}
		String result = iApiSandBoxSettingSV.mockTest(callCase, registryURL);
		return result;
	}

	@Override
	public boolean checkRegistryAvailable(String registryURL) throws CallerException {
		boolean check = DubboMockUtil.checkRegistryAvailable(registryURL);
		return check;
	}

	@Override
	public APIRest getAPIRest(String indexId) throws CallerException {
		if (StringUtil.isBlank(indexId)) {
			throw new SystemException("从索引获取API信息失败，缺少索引信息");
		}
		String data = DubboApiSearchUtil.getAPIVersionNew(indexId);
		if (StringUtil.isBlank(data)) {
			throw new SystemException("从索引获取API信息失败：此API索引信息不存在");
		}
		APIDoc apiDoc = JSONObject.parseObject(data, APIDoc.class);
		APIRest apiRest = new APIRest();
		BeanUtils.copyProperties(apiDoc, apiRest);
		ApiTemplate apiTemplate = ApiTemplateUtils.getApiTemplate(apiDoc.getOwner(), apiDoc.getInterfaceName(),
				apiDoc.getMethodName());
		if (apiTemplate != null) {
			apiRest.setReturnJson(apiTemplate.getReturnJson());
		}
		if (apiDoc.getReturnAPIParamDoc() != null) {
			apiRest.setReturnComment(apiDoc.getReturnAPIParamDoc().getCommentText());
			apiRest.setReturnJavaType(apiDoc.getReturnAPIParamDoc().getClassName());
			String editorType = OspSandboxUtil.getEditorType(apiDoc.getReturnAPIParamDoc().getClassName());
			apiRest.setEditorType(editorType);
		}
		List<APIRestParam> restParams = new ArrayList<APIRestParam>();
		if (!CollectionUtil.isEmpty(apiDoc.getInAPIParamDocs())) {
			Map<String, ReqParamTemplate> reqParamMap = new HashMap<String, ReqParamTemplate>();
			if (apiTemplate != null)
				reqParamMap = apiTemplate.getReqParamTemplates();
			for (APIParamDoc paramDoc : apiDoc.getInAPIParamDocs()) {
				APIRestParam req = new APIRestParam();
				ReqParamTemplate reqParamTemplate = reqParamMap.get(paramDoc.getName());
				if (reqParamTemplate != null) {
					req.setParamValue(reqParamTemplate.getJson());
				}
				String editorType = OspSandboxUtil.getEditorType(paramDoc.getClassName());
				req.setEditorType(editorType);
				req.setJavaType(paramDoc.getClassName());
				req.setParamComment(paramDoc.getCommentText());
				req.setParamName(paramDoc.getName());
				restParams.add(req);
			}
		}
		apiRest.setRestParams(restParams);
		return apiRest;
	}

	@Override
	public String testRest(APIRestTestReq restTestReq) throws CallerException {
		if (restTestReq == null) {
			throw new SystemException("测试REST服务失败，参数为空");
		}
		if (StringUtil.isBlank(restTestReq.getRestMethod())) {
			throw new SystemException("测试REST服务失败，REST请求方式为空");
		}
		if (StringUtil.isBlank(restTestReq.getRestURL())) {
			throw new SystemException("测试REST服务失败，REST请求地址为空");
		}
		String data = "";
		if (!CollectionUtil.isEmpty(restTestReq.getRestParams())) {
			APIRestParam p = restTestReq.getRestParams().get(0);
			data = p.getParamValue();
		}
		JSONObject obj = HttpClientUtil.sendPostRequest(restTestReq.getRestURL(), data);
		return obj.toJSONString();
	}

}
