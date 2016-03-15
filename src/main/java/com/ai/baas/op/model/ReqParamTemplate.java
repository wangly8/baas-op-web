package com.ai.baas.op.model;

/**
 * Created by jackieliu on 16/3/1.
 */
public class ReqParamTemplate {
    private String JavaType;
    private String json;
    private String paramName;

    public String getJavaType() {
        return JavaType;
    }

    public void setJavaType(String javaType) {
        JavaType = javaType;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
}
