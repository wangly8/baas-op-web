package com.ai.baas.op.model;

/**
 * Created by jackieliu on 16/3/1.
 */
public abstract class ApiTemplateBase {
    private static final long serialVersionUID = 1L;

    private int id;

    private String owner;

    private String interfaceName;

    private String method;

    private String returnJavaType;

    private String returnComment;

    private String returnJson;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getReturnJavaType() {
        return returnJavaType;
    }

    public void setReturnJavaType(String returnJavaType) {
        this.returnJavaType = returnJavaType;
    }

    public String getReturnComment() {
        return returnComment;
    }

    public void setReturnComment(String returnComment) {
        this.returnComment = returnComment;
    }

    public String getReturnJson() {
        return returnJson;
    }

    public void setReturnJson(String returnJson) {
        this.returnJson = returnJson;
    }

}
