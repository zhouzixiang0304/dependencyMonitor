package com.cmbsj.monitor.controller;

import com.cmbsj.monitor.model.Server;
import com.cmbsj.monitor.myException.repository.ServerNameNotFoundException;
import com.cmbsj.monitor.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by lsy on 2017/7/12.
 */
@Controller
@RequestMapping("/server")
public class ServerController {
    @Autowired
    ServerService serverService;

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/graph",method = RequestMethod.GET,produces="application/json")
    public Map<String,Object> graph(){
        return serverService.graph();
    }

    @ResponseBody
    @RequestMapping(value = "/getInvocations/{serverName}",method = RequestMethod.GET,produces = "application/json")
    public Map<String,List<String>> getInvocations(@PathVariable String serverName) throws ServerNameNotFoundException{
        return serverService.getWhoInvokeMeAndMyTargets(serverName);
    }
}
