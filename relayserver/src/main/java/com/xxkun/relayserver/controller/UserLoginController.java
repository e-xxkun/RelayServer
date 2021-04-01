package com.xxkun.relayserver.controller;

import com.xxkun.relayserver.common.CommonResult;
import com.xxkun.relayserver.dto.UserLoginParam;
import com.xxkun.relayserver.service.UserLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@Api("用户登录登出")
@RequestMapping("/man")
public class UserLoginController {
    @Autowired
    private UserLoginService userLoginService;

    @ApiOperation(value = "登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult login(@RequestBody UserLoginParam umsAdminLoginParam, BindingResult result) {
        String res = userLoginService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        if (res == null) {
            return CommonResult.validateFailed("用户名或密码错误");
        }
        return CommonResult.success(res);
    }

    @ApiOperation(value = "登出")
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult logout(@RequestBody String token) {
        boolean success = userLoginService.logout(token);
        if (!success) {
            return CommonResult.validateFailed("用户未登录");
        }
        return CommonResult.success(null, "退出成功");
    }
}
