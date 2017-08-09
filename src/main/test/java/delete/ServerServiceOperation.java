package delete;

import com.cmbsj.monitor.myException.repository.ServerNameNotFoundException;
import com.cmbsj.monitor.service.ServerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * Created by lsy on 2017/8/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/application-context.xml" })
public class ServerServiceOperation {
    @Autowired
    ServerService serverService;

    @Test
    public void saveNodesAndRelations(){
        serverService.saveNodesAndRelations();
    }

    @Test
    public void getWhoInvokeMeAndMyTargetsTest() throws ServerNameNotFoundException{
        Map<String, List<String>> class4 = serverService.getWhoInvokeMeAndMyTargets("class4");
        List<String> fatherInvocations = class4.get("fatherInvocations");
        List<String> sonInvocations = class4.get("sonInvocations");
        for (String fatherInvocation :
                fatherInvocations) {
            System.out.println(fatherInvocation);
        }
        for (String sonInvocation :
                sonInvocations) {
            System.out.println(sonInvocation);
        }
    }
}
