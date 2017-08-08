package com.cmbsj.monitor.util;

import com.cmbsj.monitor.model.Server;
import com.cmbsj.monitor.service.ServerService;
import com.cmbsj.monitor.service.impl.ServerServiceImpl;
import org.easymock.EasyMockRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.conversion.QueryResultBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.expect;

/**
 * Created by lsy on 2017/8/7.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(EasyMockRunner.class)
@ContextConfiguration(locations = { "/spring/application-context.xml" })
public class ToD3FormatTest {
    ToD3Format<Server> toD3Format = ToD3Format.getInstance();
    @Test
    public void toD3FormatAgainTest(){
        List<Server> list = preparePara();
        Map<String, Object> map = toD3Format.toD3FormatAgain(list);
        Set<String> keySet = map.keySet();
        assertEquals(2,map.size());
        assertTrue(keySet.contains("nodes"));
        assertTrue(keySet.contains("links"));
        assertEquals(3,((List)map.get("nodes")).size());
        assertEquals(3,((List)map.get("links")).size());
    }

    @Test
    public void toCollectionTest(){
        List<Server> para = new ArrayList<>();
        para.add(new Server("class1"));
        EndResult<Server> mockResult = new QueryResultBuilder<>(para);
        Set<Server> servers = toD3Format.toCollection(mockResult);
        assertEquals(1,servers.size());
    }

    private List<Server> preparePara(){
        List<Server> list = new ArrayList<>();
        Server class1 = new Server("class1");
        Server class2 = new Server("class2");
        Server class3 = new Server("class3");
        class1.getTargets().add(class2);
        class1.getTargets().add(class3);
        class2.getTargets().add(class3);
        list.add(class1);
        list.add(class2);
        list.add(class3);
        return list;
    }
}
