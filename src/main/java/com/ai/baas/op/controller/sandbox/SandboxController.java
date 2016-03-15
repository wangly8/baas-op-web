package com.ai.baas.op.controller.sandbox;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ai.baas.op.api.apisearch.IAPISearchSV;
import com.ai.baas.op.api.sandbox.ISandBoxSV;
import com.ai.baas.op.api.sandbox.param.APICallCase;
import com.ai.baas.op.api.sandbox.param.APICallSetting;
import com.ai.baas.op.api.sandbox.param.APIRest;
import com.ai.baas.op.api.sandbox.param.APIRestTestReq;
import com.ai.baas.op.base.exception.SystemException;
import com.ai.baas.op.base.model.ResponseData;
import com.ai.baas.op.base.util.StringUtil;
import com.ai.baas.op.sandbox.util.ReflectUtil;
import com.ai.runner.apicollector.util.JavaDocletUtil;
import com.ai.runner.apicollector.vo.APIDoc;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/sandbox")
public class SandboxController {

    private static final Logger LOG = Logger.getLogger(SandboxController.class);
    @Autowired
    ISandBoxSV iSandBoxSV;
    @Autowired
    IAPISearchSV iapiSearchSV;
    /**
     * 显示模板参数修改页面
     * @param request
     * @return
     */
    @RequestMapping("/apireqparamset.html")
    public ModelAndView apireqparamset(HttpServletRequest request) {
        String indexId = request.getParameter("indexId");
        APICallSetting apiCallSetting = iSandBoxSV.getAPICallSettingFromES(indexId);
        request.setAttribute("apiCallSetting", apiCallSetting);
        ModelAndView view = new ModelAndView("sandbox/apireqparamset");
        return view;
    }

    /**
     * 显示rest测试页面
     * @param request
     * @return
     */
    @RequestMapping("/resttest.html")
    public ModelAndView toTestRest(HttpServletRequest request) {
        String indexId = request.getParameter("indexId");
        APIRest apiRest = iSandBoxSV.getAPIRest(indexId);
        request.setAttribute("apiRest", apiRest);
        ModelAndView view = new ModelAndView("sandbox/resttest");
        return view;
    }

    /**
     * 进行rest方式测试
     * @param data
     * @return
     */
    @RequestMapping("/restTest")
    public ResponseData<JSONObject> restTest(String data) {
        ResponseData<JSONObject> responseData = null;
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isBlank(data)) {
                throw new SystemException("没有传入待测试的数据");
            }
            /* 转换成被测试数据对象 */
            APIRestTestReq vo = JSON.parseObject(data, APIRestTestReq.class);
            if (vo == null) {
                throw new SystemException("没有传入待测试的数据");
            }
            String testResult = iSandBoxSV.testRest(vo);
            json.put("actualCode", "success");
            json.put("actualResult", testResult);
        } catch (Exception ex) {
            json.put("actualCode", "failure");
            json.put("actualResult", ex.getMessage());
        }
        responseData = new ResponseData<JSONObject>(ResponseData.AJAX_STATUS_SUCCESS, "服务测试成功",
                json);
        return responseData;
    }

    /**
     * 更新参数模板
     * @param data
     * @return
     */
    @RequestMapping("/saveAPICallSetting")
    public ResponseData<String> saveAPICallSetting(String data) {
        ResponseData<String> responseData = null;
        try {
            if (StringUtil.isBlank(data)) {
                throw new SystemException("没有传入数据包");
            }
            APICallSetting apiCallSetting = JSON.parseObject(data, APICallSetting.class);
            /* 转换成被测试数据对象 */
            if (apiCallSetting == null) {
                throw new SystemException("没有传入任何信息");
            }

            iSandBoxSV.saveAPICallSetting(apiCallSetting);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "服务参数模板设置成功",
                    null);
        } catch (Exception e) {
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "服务参数模板设置失败:"
                    + e.getMessage());
        }
        return responseData;
    }

    /**
     * 注册中心连接检查
     * @param registryURL
     * @return
     */
    @RequestMapping("/checkRegistryAvailable")
    public ResponseData<String> checkRegistryAvailable(String registryURL) {
        ResponseData<String> responseData = null;
        try {
            if (StringUtil.isBlank(registryURL)) {
                throw new SystemException("没有传入注册中心地址");
            }
            boolean result = iSandBoxSV.checkRegistryAvailable(registryURL);
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS,
                    "测试注册中心连通性成功", result ? "1" : "0");
        } catch (Exception e) {
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE,
                    "测试注册中心连通性失败,请检查");
        }
        return responseData;
    }

    @RequestMapping("/loadAPISetting")
    public ResponseData<APICallSetting> loadAPISetting(String indexId) {
        ResponseData<APICallSetting> responseData = null;
        try {
            APICallSetting data = iSandBoxSV.getAPICallSettingFromES(indexId);
            responseData = new ResponseData<APICallSetting>(ResponseData.AJAX_STATUS_SUCCESS,
                    "获取服务设置信息成功", data);
        } catch (Exception e) {
            LOG.error(e);
            responseData = new ResponseData<APICallSetting>(ResponseData.AJAX_STATUS_FAILURE,
                    "获取服务设置信息失败" + e.getMessage());
        }
        return responseData;

    }

    @RequestMapping("/toMockTest.html")
    public ModelAndView toMockTest(HttpServletRequest request) {
        String indexId = request.getParameter("indexId");
        APICallSetting apiCallSetting = iSandBoxSV.getAPICallSettingFromES(indexId);
        request.setAttribute("apiCallSetting", apiCallSetting);
        ModelAndView view = new ModelAndView("sandbox/mocktest");
        return view;
    }

    @RequestMapping("/mockTest")
    public ResponseData<JSONObject> mockTest(String data, String registryURL) {
        ResponseData<JSONObject> responseData = null;
        JSONObject json = new JSONObject();
        try {
            if (StringUtil.isBlank(data)) {
                throw new SystemException("没有传入待测试的数据");
            }
            /* 转换成被测试数据对象 */
            APICallCase vo = JSON.parseObject(data, APICallCase.class);
            if (vo == null) {
                throw new SystemException("没有传入待测试的数据");
            }
            String testResult = iSandBoxSV.excuteTest(vo,registryURL);
            json.put("actualCode", "success");
            json.put("actualResult", testResult);
        } catch (Exception ex) {
            json.put("actualCode", "failure");
            json.put("actualResult", ex.getMessage());
        }
        responseData = new ResponseData<JSONObject>(ResponseData.AJAX_STATUS_SUCCESS, "服务测试成功",
                json);
        return responseData;
    }

}
