package com.ai.baas.op.api.sandbox;

import com.ai.baas.op.api.sandbox.param.*;
import com.ai.runner.base.exception.CallerException;
import com.ai.runner.base.vo.PageInfo;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import java.util.List;

@Path("SandBox")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface ISandBoxSV {

    boolean checkRegistryAvailable(String registryURL) throws CallerException;

    /**
     * 根据索引ID从搜索引擎读取配置
     * 
     * @param indexId
     * @return
     * @author zhangchao
     * @ApiDocMethod
     */
    APICallSetting getAPICallSettingFromES(String indexId) throws CallerException;

    /**
     * 保存API的设置信息
     *
     * @param setting
     * @author zhangchao
     */
    void saveAPICallSetting(APICallSetting setting) throws CallerException;


    /**
     * 发起DUBBO的MOCK测试
     * 
     * @param callCase
     * @param registryURL
     * @return
     * @author zhangchao
     */
    String excuteTest(APICallCase callCase, String registryURL) throws CallerException;

    /**
     * 根据索引获取API-REST信息
     * 
     * @param indexId
     * @return
     * @author zhangchao
     * @ApiDocMethod
     */
    APIRest getAPIRest(String indexId) throws CallerException;

    /**
     * 测试REST服务
     * 
     * @param restTestReq
     * @return
     * @author zhangchao
     * @ApiDocMethod
     */
    String testRest(APIRestTestReq restTestReq) throws CallerException;

}
