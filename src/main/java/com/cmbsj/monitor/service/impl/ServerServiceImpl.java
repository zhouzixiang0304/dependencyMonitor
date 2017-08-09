package com.cmbsj.monitor.service.impl;

import com.cmbsj.monitor.model.Func;
import com.cmbsj.monitor.model.Server;
import com.cmbsj.monitor.myException.repository.ServerNameNotFoundException;
import com.cmbsj.monitor.mybatis.dao.SerConMapper;
import com.cmbsj.monitor.mybatis.entity.ServerConnection;
import com.cmbsj.monitor.repository.ServerRepository;
import com.cmbsj.monitor.service.ServerService;
import com.cmbsj.monitor.util.SerConTarget;
import com.cmbsj.monitor.util.ToD3Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by lsy on 2017/7/18.
 */
@Service("serverService")
public class ServerServiceImpl implements ServerService {
    @Autowired
    SerConTarget serConAdapter;
    private ToD3Format<Server> toD3Format = ToD3Format.getInstance();

    @Override
    public List<ServerConnection> findAllConnection() {
        return serConAdapter.findAllConFromMySQL();
    }

    @Override
    public void saveNodesAndRelations() {
        serConAdapter.saveNodesAndRels2Neo();
    }

    @Override
    public List<Server> getAllServer() {
        return serConAdapter.getAllSerFromNeo();
    }

    @Override
    public void deleteAllServer() {
        serConAdapter.deleteAllNodes();
    }

    @Override
    public Map<String, Object> graph() {
        return toD3Format.toD3FormatAgain(getAllServer());
    }

    @Override
    public Map<String,List<String>> getWhoInvokeMeAndMyTargets(String serverName) throws ServerNameNotFoundException{
        Map<String,List<String>> map = new HashMap<>(2);
        List<String> fatherInvocations = new ArrayList<>();
        List<String> sonInvocations = new ArrayList<>();
        //TODO:找父调用关系
        Set<Server> fatherServers = serConAdapter.getWhoInvokeMe(serverName);
        for (Server fatherServer : fatherServers) {
            List<String[]> connections = fatherServer.getConnections();
            for (String[] connection : connections) {
                if(connection[2].contains(serverName)){
                    String invocation = connection[0] + " : " + connection[1] + "->" + connection[2];
                    fatherInvocations.add(invocation);
                }
            }
        }
        //TODO:END
        //TODO:找子调用关系
        List<Server> servers = serConAdapter.getNodeByName(serverName);
        Server server;
        if(servers.size()==0) throw new ServerNameNotFoundException();
        server = servers.get(0);
        List<String[]> connections = server.getConnections();
        for (String[] connection : connections) {
            String invocation = connection[0] + " : " + connection[1] + "->" + connection[2];
            sonInvocations.add(invocation);
        }
        //TODO:END
        map.put("fatherInvocations",fatherInvocations);
        map.put("sonInvocations",sonInvocations);
        return map;
    }
}
