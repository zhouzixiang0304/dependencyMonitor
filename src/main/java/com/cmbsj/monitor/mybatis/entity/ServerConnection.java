package com.cmbsj.monitor.mybatis.entity;

/**
 * Created by lsy on 2017/7/11.
 */
public class ServerConnection {
    private String sourceClass;
    private String sourceFunc;
    private String targetClass;
    private String targetFunc;
    private String description;

    public String getSourceClass() {
        return sourceClass;
    }

    public void setSourceClass(String sourceClass) {
        this.sourceClass = sourceClass;
    }

    public String getSourceFunc() {
        return sourceFunc;
    }

    public void setSourceFunc(String sourceFunc) {
        this.sourceFunc = sourceFunc;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public String getTargetFunc() {
        return targetFunc;
    }

    public void setTargetFunc(String targetFunc) {
        this.targetFunc = targetFunc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ServerConnection{" +
                "sourceClass='" + sourceClass + '\'' +
                ", sourceFunc='" + sourceFunc + '\'' +
                ", targetClass='" + targetClass + '\'' +
                ", targetFunc='" + targetFunc + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public ServerConnection() {
    }
    public ServerConnection(String sourceClass) {
        this.sourceClass = sourceClass;
    }
    public ServerConnection(String sourceClass, String sourceFunc) {
        this.sourceClass = sourceClass;
        this.sourceFunc = sourceFunc;
    }
    public ServerConnection(String sourceClass, String sourceFunc, String targetClass, String targetFunc) {
        this.sourceClass = sourceClass == null? null : sourceClass;
        this.sourceFunc = sourceFunc == null? null : sourceClass;
        this.targetClass = targetClass;
        this.targetFunc = targetFunc;
    }

    public ServerConnection(String sourceClass, String sourceFunc, String targetClass, String targetFunc, String description) {
        this.sourceClass = sourceClass;
        this.sourceFunc = sourceFunc;
        this.targetClass = targetClass;
        this.targetFunc = targetFunc;
        this.description = description;
    }
}
