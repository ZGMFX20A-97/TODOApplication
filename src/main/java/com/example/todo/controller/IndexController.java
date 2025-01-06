package com.example.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    //インデックスページのリクエストを受け付けるエンドポイント
    @GetMapping("/")
    public String index(){
        return "index";
    }
    
}
