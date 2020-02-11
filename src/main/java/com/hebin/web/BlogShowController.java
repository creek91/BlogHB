package com.hebin.web;


import com.hebin.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BlogShowController {

    @GetMapping("/blog")
    public String blog(){
        return "index";
    }

}
