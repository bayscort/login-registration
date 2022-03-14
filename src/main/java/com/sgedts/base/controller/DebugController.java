package com.sgedts.base.controller;

import com.sgedts.base.bean.GeneralWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/hello")
    public GeneralWrapper<String> hello(@RequestParam(required = false) String name) {
        return new GeneralWrapper<String>().success("Hello " + name);
    }
}
