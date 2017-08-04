package com.cmbsj.monitor.repository;

import com.cmbsj.monitor.model.Server;
import org.springframework.data.neo4j.repository.GraphRepository;

/**
 * Created by lsy on 2017/7/18.
 */
public interface ServerRepository extends GraphRepository<Server> {
}
