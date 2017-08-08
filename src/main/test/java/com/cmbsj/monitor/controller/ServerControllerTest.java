package com.cmbsj.monitor.controller;

import com.cmbsj.monitor.service.ServerService;
import com.cmbsj.monitor.service.impl.ServerServiceImpl;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.HashMap;
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

}
