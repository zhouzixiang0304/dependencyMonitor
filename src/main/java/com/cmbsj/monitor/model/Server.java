package com.cmbsj.monitor.model;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lsy on 2017/7/18.
 */
@NodeEntity
public class Server extends IdentifiableEntity {

    @Indexed(unique = true)
    private String serverId;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "serverName")
    private String serverName;

    @Indexed
    private List<String> connections = new ArrayList<>();

    @Indexed
    private Set<Func> funcs = new HashSet<>();

    @Fetch
    @RelatedTo(type = "Invoke", direction = Direction.OUTGOING)
    private Set<Server> targets = new HashSet<>();

    public Server(String serverName) {
        this.serverName = serverName;
        this.serverId = "" + this.hashCode();
    }

    public Server() {
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Set<Func> getFuncs() {
        return funcs;
    }

    public void setFuncs(Set<Func> funcs) {
        this.funcs = funcs;
    }

    public List<String> getConnections() {
        return connections;
    }

    public void setConnections(List<String> connections) {
        this.connections = connections;
    }

    public Set<Server> getTargets() {
        return targets;
    }

    public void setTargets(Set<Server> targets) {
        this.targets = targets;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Server &&
                ((Server)obj).serverName.equals(this.serverName);
    }

    @Override
    public int hashCode() {
        return  serverName.hashCode();
    }
}
