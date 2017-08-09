package com.cmbsj.monitor.myException.repository;

import com.cmbsj.monitor.myException.BaseException;

/**
 * Created by lsy on 2017/8/9.
 */
public class ServerNameNotFoundException extends BaseException {
    public ServerNameNotFoundException() {
        handleException();
    }

    @Override
    public void handleException() {
        setMsg("未找到相关服务，请重新确认服务名称");
    }
}
