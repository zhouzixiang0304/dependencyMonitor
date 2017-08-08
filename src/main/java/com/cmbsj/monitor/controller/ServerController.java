package com.cmbsj.monitor.controller;

import com.cmbsj.monitor.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
