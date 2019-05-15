package com.scor.fileManager.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller()
public class SAPController {


    @RequestMapping(value={"/","/study/**","/home/**","/tables/**"})
    public String SPAPage(){
        return "/index.html";
    }
}
