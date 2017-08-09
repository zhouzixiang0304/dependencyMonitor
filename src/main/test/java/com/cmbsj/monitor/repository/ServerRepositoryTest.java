package com.cmbsj.monitor.repository;

import com.cmbsj.monitor.model.Server;
import com.cmbsj.monitor.util.SerConTarget;
import com.cmbsj.monitor.util.ToD3Format;
import com.sun.xml.internal.ws.transport.http.server.ServerAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by lsy on 2017/8/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/application-context.xml" })
public class ServerRepositoryTest {
    @Autowired
    private ServerRepository serverRepository;
    @Autowired
    private SerConTarget serConAdapter;
    ToD3Format<Server> toD3Format = ToD3Format.getInstance();

    @Test
    public void getSomeNodesTest(){
        String serverName = "class2";
        Set<Server> nodes = serConAdapter.getWhoInvokeMe(serverName);
        System.out.println(nodes.size());
        for (Server server :
                nodes) {
            System.out.println(server.getServerName());
        }
    }

    @Test
    public void getServerByNameTest(){
        List<Server> set = serverRepository.getServerByName(".*class.*");

        for (Server server : set) {
            System.out.println(server.getServerName());
        }
    }

    @Test
    public void deleteAllServer(){
        serverRepository.deleteAll();
    }
}
