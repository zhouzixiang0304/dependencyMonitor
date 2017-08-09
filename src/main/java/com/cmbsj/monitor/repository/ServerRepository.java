package com.cmbsj.monitor.repository;

import com.cmbsj.monitor.model.Server;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by lsy on 2017/7/18.
 */
public interface ServerRepository extends GraphRepository<Server> {

    /**
     * 根据姓名得到我的父节点
     * @param serverName
     * @return
     */
    @Query("MATCH (s1:Server)-[:Invoke]->(s2:Server) WHERE s2.serverName=~{serverName} RETURN s1")
    Set<Server> getWhoInvokeMe(@Param("serverName") String serverName);

    @Query("MATCH (n:Server) WHERE n.serverName=~{serverName} RETURN n")
    List<Server> getServerByName(@Param("serverName") String serverName);
}
