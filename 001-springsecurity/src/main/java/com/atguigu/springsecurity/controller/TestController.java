package com.atguigu.springsecurity.controller;

import com.atguigu.springsecurity.entity.Users;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @GetMapping("/")
    public String test(){
        return "Hello Spring Security";
    }

    @GetMapping("/index")
    public String main(){
        return "Hello Index";
    }

    @GetMapping("/update")
    //@Secured({"ROLE_sale","ROLE_manager"})
    //方法之前
    //@PreAuthorize("hasAnyAuthority('admin')")
    //方法之后
    @PostAuthorize("hasAuthority('admins')")
    public String update(){
        System.out.println("Update");
        return "Update";
    }

    @GetMapping("/getAll")
    public List<Users> getAllUser(){
        ArrayList<Users> list = new ArrayList<>();
        list.add(new Users());
        return list;
    }
}
