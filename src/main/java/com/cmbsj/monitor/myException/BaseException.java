package com.cmbsj.monitor.myException;

/**
 * Created by lsy on 2017/8/9.
 */
public abstract class BaseException extends Throwable {
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    protected abstract void handleException();
}
