package com.ai.baas.op.service.business.interfaces;

import com.ai.baas.op.api.sandbox.param.APICallCase;
import com.ai.baas.op.api.sandbox.param.APICallSetting;
import com.ai.baas.op.model.ApiTemplate;

public interface IApiSandBoxSettingSV {

    void saveAPICallSetting(APICallSetting req,ApiTemplate apiTemplate);

    String mockTest(APICallCase callCase,String registryURL);
    
    
    void addAPICallSetting(APICallSetting req,ApiTemplate apiTemplate);

}
