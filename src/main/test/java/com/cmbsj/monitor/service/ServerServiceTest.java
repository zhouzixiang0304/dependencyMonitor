package com.cmbsj.monitor.service;

import com.cmbsj.monitor.model.Server;
import com.cmbsj.monitor.mybatis.entity.ServerConnection;
import com.cmbsj.monitor.service.impl.ServerServiceImpl;
import com.cmbsj.monitor.util.SerConAdapter;
import com.cmbsj.monitor.util.SerConTarget;
import com.cmbsj.monitor.util.ToD3Format;
import org.easymock.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.*;

/**
 * Created by lsy on 2017/7/11.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(EasyMockRunner.class)
//@ContextConfiguration(locations = { "/spring/application-context.xml" })
public class ServerServiceTest extends EasyMockSupport {

    @Mock
    private SerConTarget serConAdapter = Mockito.mock(SerConAdapter.class);
    @TestSubject
    private ServerService serverService = new ServerServiceImpl();

    @Test
    public void findAllConnectionTest(){
        expect(serConAdapter.findAllConFromMySQL()).andReturn(new ArrayList<ServerConnection>());
        replay(serConAdapter);
        List<ServerConnection> list = serverService.findAllConnection();
        assertEquals(0,list.size());
        EasyMock.verify(serConAdapter);
    }

    @Test
    public void saveNodesAndRelationsTest(){
        serConAdapter.saveNodesAndRels2Neo();
        EasyMock.expectLastCall();
        EasyMock.replay(serConAdapter);
        serverService.saveNodesAndRelations();
        EasyMock.verify(serConAdapter);
    }

    @Test
    public void getAllServerTest(){
        expect(serConAdapter.getAllSerFromNeo()).andReturn(new ArrayList<Server>());
        replay(serConAdapter);
        List<Server> list = serverService.getAllServer();
        assertEquals(0,list.size());
        EasyMock.verify(serConAdapter);
    }

    @Test
    public void deleteAllServerTest(){
        serConAdapter.deleteAllNodes();
        EasyMock.expectLastCall();
        EasyMock.replay(serConAdapter);
        serverService.deleteAllServer();
        EasyMock.verify(serConAdapter);
    }

    @Test
    public void graph() throws Exception{
        ToD3Format toD3FormatMock = EasyMock.createMock(ToD3Format.class);
        Class<ServerServiceImpl> clazz = ServerServiceImpl.class;
        Field toD3Format = clazz.getDeclaredField("toD3Format");
        toD3Format.setAccessible(true);
        toD3Format.set(serverService,toD3FormatMock);
        List<Server> list = new ArrayList<>();
        expect(toD3FormatMock.toD3FormatAgain(null)).andReturn(new HashMap());
        replay(toD3FormatMock);
        Map<String, Object> graph = serverService.graph(100);
        assertEquals(0,graph.size());
        EasyMock.verify(toD3FormatMock);
    }
}
