package com.cmbsj.monitor.model;

import org.springframework.data.neo4j.annotation.GraphId;

/**
 * @author: rkowalewski
 */
public abstract class IdentifiableEntity {
    @GraphId
    private Long graphId;

    public Long getGraphId() {
        return graphId;
    }

}
