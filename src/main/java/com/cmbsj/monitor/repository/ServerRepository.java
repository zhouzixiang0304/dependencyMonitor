package com.cmbsj.monitor.repository;

import com.cmbsj.monitor.model.Server;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Set;

/**
 * Created by lsy on 2017/7/18.
 */
public interface ServerRepository extends GraphRepository<Server> {
    @Query("MATCH (s1:Server)-[:Invoke]->(s2:Server) WHERE s1.serverName=~{serverName} RETURN s2.serverName")
    Set<String> getMyTargets(@Param("serverName") String serverName);
}
