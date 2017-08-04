package com.cmbsj.monitor.service.impl;

import com.cmbsj.monitor.model.Func;
import com.cmbsj.monitor.model.Server;
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
    public Map<String, Object> graph(int limit) {
        return toD3Format.toD3FormatAgain(getAllServer());
    }
}
