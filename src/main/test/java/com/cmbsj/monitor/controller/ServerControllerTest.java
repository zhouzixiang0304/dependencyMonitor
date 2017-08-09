package com.cmbsj.monitor.controller;

import com.cmbsj.monitor.myException.repository.ServerNameNotFoundException;
import com.cmbsj.monitor.service.ServerService;
import com.cmbsj.monitor.service.impl.ServerServiceImpl;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.data.neo4j.annotation.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;
import static org.easymock.EasyMock.*;

/**
 * Created by lsy on 2017/8/7.
 */
@RunWith(EasyMockRunner.class)
public class ServerControllerTest {
    @Mock
    private ServerService serverService = Mockito.mock(ServerServiceImpl.class);
    @TestSubject
    private ServerController serverController = new ServerController();

    @Test
    public void indexTest(){
        String res = serverController.index();
        assertEquals("index",res);
    }

    @Test
    public void graphTest(){
        expect(serverService.graph()).andReturn(new HashMap<String,Object>());
        replay(serverService);
        Map<String,Object> map = serverController.graph();
        assertEquals(map.size(),0);
        verify(serverService);
    }

//    @Test
//    public void getInvocationsExceptionTest() throws ServerNameNotFoundException{
//        expect(serverService.getWhoInvokeMeAndMyTargets("class7")).andThrow(new ServerNameNotFoundException());
//        replay(serverService);
//        try {
//            serverController.getInvocations("class7");
//        }catch (ServerNameNotFoundException e){
//            assertEquals("未找到相关服务，请重新确认服务名称",e.getMsg());
//        }finally {
//            verify(serverService);
//        }
//    }

    @Test
    public void getInvocationsTest() throws ServerNameNotFoundException{
        expect(serverService.getWhoInvokeMeAndMyTargets("class1")).andReturn(new HashMap<String, List<String>>());
        replay(serverService);
        Map<String, List<String>> class1 = serverController.getInvocations("class1");
        assertEquals(0,class1.size());
        verify(serverService);
    }
}
