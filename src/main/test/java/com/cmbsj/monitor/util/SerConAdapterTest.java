package com.cmbsj.monitor.util;

import com.cmbsj.monitor.model.Server;
import com.cmbsj.monitor.mybatis.dao.SerConMapper;
import com.cmbsj.monitor.mybatis.entity.ServerConnection;
import com.cmbsj.monitor.repository.ServerRepository;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.conversion.QueryResultBuilder;

import java.lang.reflect.Method;
import java.util.*;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

/**
 * Created by lsy on 2017/8/7.
 */
@RunWith(EasyMockRunner.class)
public class SerConAdapterTest {
    @Mock
    private SerConMapper serConMapper = Mockito.mock(SerConMapper.class);
    @Mock
    private ServerRepository serverRepository = Mockito.mock(ServerRepository.class);
    @TestSubject
    private SerConTarget serConAdapter = new SerConAdapter();

    @Test
    public void findAllConFromMySQLTest(){
        expect(serConMapper.findAll()).andReturn(new ArrayList<ServerConnection>());
        replay(serConMapper);
        assertEquals(serConAdapter.findAllConFromMySQL().size(),0);
        verify(serConMapper);
    }

    @Test
    public void saveNodesAndRels2NeoTest() throws Exception{
        Collection<Server> servers = testData();
        assertEquals(servers.size(),6);
        for (Server server : servers) {
            expect(serverRepository.save(server)).andReturn(server);
        }
        replay(serverRepository);
        serConAdapter.saveNodesAndRels2Neo();
        EasyMock.verify(serConMapper);
        EasyMock.verify(serverRepository);

    }

    @Test
    public void getAllSerFromNeoTest(){
        List<Server> para = new ArrayList<>();
        para.add(new Server("class1"));
        EndResult<Server> mockResult = new QueryResultBuilder<>(para);
        expect(serverRepository.findAll()).andReturn(mockResult);
        replay(serverRepository);
        List<Server> list = serConAdapter.getAllSerFromNeo();
        assertEquals(list.size(),1);
        EasyMock.verify(serverRepository);
    }

    @Test
    public void deleteAllNodesTest(){
        serverRepository.deleteAll();
        EasyMock.expectLastCall();
        EasyMock.replay(serverRepository);
        serConAdapter.deleteAllNodes();
        verify(serverRepository);
    }

    @Test
    public void getWhoInvokeMeTest(){
        expect(serverRepository.getWhoInvokeMe(".*class2.*")).andReturn(new HashSet<Server>());
        replay(serverRepository);
        assertEquals(0,serConAdapter.getWhoInvokeMe("class2").size());
        verify(serverRepository);
    }

    private Collection<Server> testData() throws Exception{
        List<ServerConnection> list = new ArrayList<>();
        list.add(new ServerConnection("class1","func1","class2","func1","test1"));
        ServerConnection c1c2 = new ServerConnection("class1", "func2", "class2", "func2");
        c1c2.setDescription("test2");
        list.add(c1c2);
        ServerConnection c2c3 = new ServerConnection();
        c2c3.setSourceClass("class2");
        c2c3.setSourceFunc("func1");
        c2c3.setTargetClass("class3");
        c2c3.setTargetFunc("func1");
        list.add(c2c3);
        ServerConnection c3c1 = new ServerConnection("class3", "func1");
        c3c1.setTargetClass("class1");
        c3c1.setTargetFunc("func2");
        list.add(c3c1);
        list.add(new ServerConnection("class2","func2","class3","func2"));
        list.add(new ServerConnection("class4","func2","class3","func2"));
        list.add(new ServerConnection("class5","func2","class4","func2"));
        list.add(new ServerConnection("class4","func2","class6","func2"));
        list.add(new ServerConnection("class3","func2","class1","func2"));
        list.add(new ServerConnection("class6","func2","class2","func2"));
        Class<SerConAdapter> clazz = SerConAdapter.class;
        Method prepareNeo = clazz.getDeclaredMethod("prepareNeo");
        prepareNeo.setAccessible(true);
        expect(serConMapper.findAll()).andReturn(list).times(2);
        replay(serConMapper);
        Collection<Server> ret = (Collection<Server>)prepareNeo.invoke(serConAdapter);
        return ret;
    }
}
