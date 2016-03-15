package com.ai.baas.op.model;

import java.util.List;

/**
 * 服务模板索引数据类
 * Created by jackieliu on 16/3/1.
 */
public class ApiTemplateIndex extends ApiTemplateBase {
    private List<ReqParamTemplate> reqParamTemplates;

    public List<ReqParamTemplate> getReqParamTemplates() {
        return reqParamTemplates;
    }

    public void setReqParamTemplates(List<ReqParamTemplate> reqParamTemplates) {
        this.reqParamTemplates = reqParamTemplates;
    }
}
