package com.cmbsj.monitor.repository;

import com.cmbsj.monitor.model.Server;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collection;
import java.util.Set;

/**
 * Created by lsy on 2017/8/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/application-context.xml" })
public class ServerRepositoryTest {
    @Autowired
    ServerRepository serverRepository;
    @Test
    public void getSomeNodesTest(){
        String serverName = "4";
        Set<String> nodes = serverRepository.getMyTargets(".*"+serverName+".*");
        System.out.println(nodes.size());
        for (String server :
                nodes) {
            System.out.println(server);
        }
    }
}
