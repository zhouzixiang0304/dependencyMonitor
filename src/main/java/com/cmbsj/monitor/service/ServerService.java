package com.cmbsj.monitor.service;


import com.cmbsj.monitor.model.Server;
import com.cmbsj.monitor.myException.repository.ServerNameNotFoundException;
import com.cmbsj.monitor.mybatis.entity.ServerConnection;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by lsy on 2017/7/18.
 */
public interface ServerService {
    /**
     * 查找到所有的链接
     * @return
     */
    List<ServerConnection> findAllConnection();

    /**
     * 保存节点和关系
     */
    void saveNodesAndRelations();

    /**
     * 得到所有服务
     * @return
     */
    List<Server> getAllServer();

    /**
     * 删除所有服务
     */
    void deleteAllServer();

    /**
     * 查找用户并以图形式返回
     * @return
     */
    Map<String,Object> graph();

    Map<String,List<String>> getWhoInvokeMeAndMyTargets(String serverName) throws ServerNameNotFoundException;
}
