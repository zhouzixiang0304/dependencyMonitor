package com.cmbsj.monitor.mybatis.dao;


import com.cmbsj.monitor.mybatis.entity.ServerConnection;

import java.util.List;

/**
 * Created by lsy on 2017/7/11.
 */
public interface SerConMapper {
    List<ServerConnection> findAll();

    List<ServerConnection> findByColumn(ServerConnection serverConnection);

    int saveServerConnection(ServerConnection serverConnection);
}
