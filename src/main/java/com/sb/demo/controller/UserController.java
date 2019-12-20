package com.sb.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.sb.demo.pub.utils.JsonBackData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/login")
    public JsonBackData doLogin(){
        JsonBackData back = new JsonBackData();
        JSONObject obj = new JSONObject();
        obj.put("name","熊高");
        obj.put("authority","superAdmin");
        back.setBackData(obj);
        return back;
    }
}
