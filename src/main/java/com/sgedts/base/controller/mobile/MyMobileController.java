package com.sgedts.base.controller.mobile;

import com.sgedts.base.bean.GeneralWrapper;
import com.sgedts.base.constant.PermissionConstant;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mobile")
public class MyMobileController {

    @PreAuthorize("@auth.isMobile()")
    @GetMapping("/hello")
    public GeneralWrapper<String> hello(@RequestParam(required = false) String name) {
        return new GeneralWrapper<String>().success("Hello " + name);
    }
}
