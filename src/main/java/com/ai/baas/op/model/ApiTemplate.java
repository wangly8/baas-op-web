package com.ai.baas.op.model;

import java.util.Map;

/**
 * Created by jackieliu on 16/2/28.
 */
public class ApiTemplate extends ApiTemplateBase{
    private static final long serialVersionUID = 1L;

    private Map<String,ReqParamTemplate> reqParamTemplates;

    public Map<String, ReqParamTemplate> getReqParamTemplates() {
        return reqParamTemplates;
    }

    public void setReqParamTemplates(Map<String, ReqParamTemplate> reqParamTemplates) {
        this.reqParamTemplates = reqParamTemplates;
    }

}
