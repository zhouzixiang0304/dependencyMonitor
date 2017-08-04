package com.cmbsj.monitor.util;

import com.cmbsj.monitor.model.Server;
import com.cmbsj.monitor.mybatis.entity.ServerConnection;

import java.util.List;

/**
 * Created by lsy on 2017/8/3.
 * 适配器模式，将两个数据库的基本操作封装在一个接口内
 */

public interface SerConTarget {
    /**
     * 从MySQL中查找到所有的链接
     * @return
     */
    List<ServerConnection> findAllConFromMySQL();

    /**
     * 保存节点和关系到neo中
     */
    void saveNodesAndRels2Neo();

    /**
     * 从neo中得到所有服务节点
     * @return
     */
    List<Server> getAllSerFromNeo();

    /**
     * 从neo中删除所有服务节点
     */
    void deleteAllNodes();
}
