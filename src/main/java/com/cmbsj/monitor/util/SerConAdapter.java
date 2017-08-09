package com.cmbsj.monitor.util;

import com.cmbsj.monitor.model.Func;
import com.cmbsj.monitor.model.Server;
import com.cmbsj.monitor.mybatis.dao.SerConMapper;
import com.cmbsj.monitor.mybatis.entity.ServerConnection;
import com.cmbsj.monitor.repository.ServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by lsy on 2017/8/3.
 */
@Component("serConAdapter")
public class SerConAdapter implements SerConTarget {
    @Autowired
    SerConMapper serConMapper;
    @Autowired
    ServerRepository serverRepository;

    @Override
    public List<ServerConnection> findAllConFromMySQL() {
        return serConMapper.findAll();
    }

    @Override
    public void saveNodesAndRels2Neo() {
        Collection<Server> serverSet = prepareNeo();
        for (Server server : serverSet) {
            serverRepository.save(server);
        }
    }

    @Override
    public List<Server> getAllSerFromNeo() {
        EndResult<Server> all = serverRepository.findAll();
        Iterator<Server> iterator = all.iterator();
        List<Server> resultList = new ArrayList<>();
        while(iterator.hasNext()){
            resultList.add(iterator.next());
        }
        return resultList;
    }

    @Override
    public void deleteAllNodes() {
        serverRepository.deleteAll();
    }

    @Override
    public Set<Server> getWhoInvokeMe(String myName) {
        return serverRepository.getWhoInvokeMe(".*"+myName+".*");
    }

    @Override
    public List<Server> getNodeByName(String serverName) {
        return serverRepository.getServerByName(".*"+serverName+".*");
    }

    /**
     * 将查找到的服务连接构建为Node
     * @return
     */
    private Collection<Server> prepareNeo(){
        //第一步：将查找到的服务连接构建为Node
        List<ServerConnection> connectionList = findAllConFromMySQL();
        Map<Server,Set<Server>> serverMap = new HashMap<>();
        for (ServerConnection serverConnection : connectionList) {
            //保存每行信息
            Server serverSrc = new Server(serverConnection.getSourceClass());
            Server serverTgt = new Server(serverConnection.getTargetClass());
            Set<Server> keySet = serverMap.keySet();
            for (Server server : keySet) {
                serverSrc = server.equals(serverSrc) ? server : serverSrc;
                serverTgt = server.equals(serverTgt) ? server : serverTgt;
            }
            String funcSrcName = serverConnection.getSourceClass()+ ":" + serverConnection.getSourceFunc();
            String funcTgtName = serverConnection.getTargetClass()+ ":" + serverConnection.getTargetFunc();
            serverSrc.getFuncs().add(new Func(funcSrcName));
            serverTgt.getFuncs().add(new Func(funcTgtName));
            String[] connection = new String[3];
            connection[0] = serverConnection.getDescription();
            connection[1] = funcSrcName;
            connection[2] = funcTgtName;
            serverSrc.getConnections().add(connection);
            //将target节点存到source的Set中
            if(serverMap.containsKey(serverSrc))//表示Map中已存在source节点
                serverMap.get(serverSrc).add(serverTgt);
            else {//map中不存在source节点
                Set<Server> targets = new HashSet<>();
                targets.add(serverTgt);
                serverMap.put(serverSrc,targets);
            }
            if(!serverMap.containsKey(serverTgt))
                serverMap.put(serverTgt,new HashSet<Server>());
        }
        Set<Server> serverSet = serverMap.keySet();
        for (Server server : serverSet) {
            server.setTargets(serverMap.get(server));
        }
        return serverSet;
    }
}
