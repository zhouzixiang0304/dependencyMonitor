package com.cmbsj.monitor.service;

import com.cmbsj.monitor.model.Server;
import com.cmbsj.monitor.mybatis.entity.ServerConnection;
import com.cmbsj.monitor.service.impl.ServerServiceImpl;
import com.cmbsj.monitor.util.SerConAdapter;
import com.cmbsj.monitor.util.SerConTarget;
import org.easymock.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.*;
import static org.mockito.Mockito.*;

/**
 * Created by lsy on 2017/7/11.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(EasyMockRunner.class)
@ContextConfiguration(locations = { "/spring/application-context.xml" })
public class ServerServiceTest extends EasyMockSupport {

    @Mock
    private SerConTarget serConAdapter ;
    @TestSubject
    private ServerService serverService = new ServerServiceImpl();

    @Test
    public void findAllConnectionTest(){
        serConAdapter = EasyMock.createMock(SerConAdapter.class);
        expect(serConAdapter.findAllConFromMySQL()).andReturn(new ArrayList<ServerConnection>(0));
        replay(serConAdapter);
        List<ServerConnection> list = serverService.findAllConnection();
        assertEquals(list.size(),0);
        verify(serConAdapter);
    }

    @Test
    public void saveNodesAndRelationsTest(){
//        serConAdapter = Mockito.mock(SerConAdapter.class);
        serverService.saveNodesAndRelations();
//        when(serConAdapter.saveNodesAndRels2Neo()).getMock();
//        replay(serConAdapter);
        Mockito.verify(serConAdapter).saveNodesAndRels2Neo();
    }

    @Test
    public void getAllServerTest(){
        List<Server> allServerFunc = serverService.getAllServer();
    }

    @Test
    public void deleteAllServerTest(){
        serverService.deleteAllServer();
    }

    @Test
    public void graph(){

    }
}
