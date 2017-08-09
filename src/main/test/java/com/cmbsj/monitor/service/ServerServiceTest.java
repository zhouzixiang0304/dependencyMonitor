package com.cmbsj.monitor.service;

import com.cmbsj.monitor.model.Server;
import com.cmbsj.monitor.myException.repository.ServerNameNotFoundException;
import com.cmbsj.monitor.mybatis.entity.ServerConnection;
import com.cmbsj.monitor.service.impl.ServerServiceImpl;
import com.cmbsj.monitor.util.SerConAdapter;
import com.cmbsj.monitor.util.SerConTarget;
import com.cmbsj.monitor.util.ToD3Format;
import org.easymock.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.*;

/**
 * Created by lsy on 2017/7/11.
 */
@RunWith(EasyMockRunner.class)
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
        Map<String, Object> graph = serverService.graph();
        assertEquals(0,graph.size());
        EasyMock.verify(toD3FormatMock);
    }

    @Test
    public void getWhoInvokeMeAndMyTargetsTest() throws ServerNameNotFoundException{
        expect(serConAdapter.getWhoInvokeMe("class1")).andReturn(prepareFatherServers());
        expect(serConAdapter.getNodeByName("class1")).andReturn(prepareFindByName());
        replay(serConAdapter);
        Map<String, List<String>> map = serverService.getWhoInvokeMeAndMyTargets("class1");
        Set<String> keySet = map.keySet();
        assertTrue(keySet.contains("fatherInvocations"));
        assertTrue(keySet.contains("sonInvocations"));
        assertEquals(3,map.get("fatherInvocations").size());
        assertEquals(3,map.get("sonInvocations").size());
        verify(serConAdapter);
    }

    @Test
    public void getWhoInvokeMeAndMyTargetsExceptionTest() throws ServerNameNotFoundException{
        expect(serConAdapter.getWhoInvokeMe("class1")).andReturn(prepareFatherServers());
        expect(serConAdapter.getNodeByName("class1")).andReturn(new ArrayList<Server>());
        replay(serConAdapter);
        try {
            serverService.getWhoInvokeMeAndMyTargets("class1");
        }catch (ServerNameNotFoundException e){
            assertEquals("未找到相关服务，请重新确认服务名称",e.getMsg());
        }finally {
            verify(serConAdapter);
        }
    }

    private Set<Server> prepareFatherServers(){
        HashSet<Server> fatherServers = new HashSet<>();
        Server class2 = new Server("class2");
        class2.getConnections().add(new String[]{"test1","class2:func1","class1:func1"});
        class2.getConnections().add(new String[]{"test2","class2:func2","class1:func3"});
        class2.getConnections().add(new String[]{"test3","class2:func3","class3:func1"});
        fatherServers.add(class2);
        Server class3 = new Server("class3");
        class3.getConnections().add(new String[]{"test4","class3:func1","class1:func2"});
        class3.getConnections().add(new String[]{"test5","class3:func2","class2:func3"});
        class3.getConnections().add(new String[]{"test6","class3:func3","class2:func1"});
        fatherServers.add(class3);
        return fatherServers;
    }

    private List<Server> prepareFindByName(){
        Server server = new Server("class1");
        List<String[]> connections = new ArrayList<>();
        String[] connection1 = new String[]{"test7","class1:func1","class4:func1"};
        String[] connection2 = new String[]{"test7","class1:func2","class5:func1"};
        String[] connection3 = new String[]{"test7","class1:func3","class6:func1"};
        connections.add(connection1);
        connections.add(connection2);
        connections.add(connection3);
        server.setConnections(connections);
        ArrayList<Server> list = new ArrayList<>();
        list.add(server);
        return list;
    }
}
