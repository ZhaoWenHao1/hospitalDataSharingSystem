package com.hust.keyRD.system.controller;

import cn.hutool.json.JSONObject;
import com.hust.keyRD.commons.entities.Channel;
import com.hust.keyRD.commons.entities.CommonResult;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.commons.myAnnotation.LoginToken;
import com.hust.keyRD.commons.utils.JwtUtil;
import com.hust.keyRD.system.service.ChannelService;
import com.hust.keyRD.system.service.DataService;
import com.hust.keyRD.system.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController{
    @Resource
    private UserService userService;
    @Resource
    private ChannelService channelService;
    @Resource
    private DataService dataService;
    //登录
    @PostMapping(value = "/user/login")
    @LoginToken
    public CommonResult login(@RequestBody User user){
        JSONObject jsonObject = new JSONObject();
        boolean result = userService.login(user);
        if(result) {
            User uresult = userService.findUserByUsername(user.getUsername());
            if(uresult.getIsAdmin()==1){
                return new CommonResult<>(500,"您不是普通用户，请选择正确的登录方式",null);
            }
            String token = JwtUtil.createJWT(Integer.MAX_VALUE, uresult);
            String hospitalName = channelService.findChannelById(uresult.getChannelId()).getHospitalName();
            jsonObject.put("user", uresult);
            jsonObject.put("hospitalName", hospitalName);
            jsonObject.put("token", token);
            return new CommonResult<>(200,"登录成功",jsonObject);
        }
        else {
            return new CommonResult<>(400,"登录失败,用户不存在或用户名或密码错误",null);
        }
    }
    //管理员登录
    @PostMapping(value = "/user/adminLogin")
    @LoginToken
    public CommonResult adminLogin(@RequestBody User user){
        JSONObject jsonObject = new JSONObject();
        boolean result = userService.login(user);
        if(result) {
            User uresult = userService.findUserByUsername(user.getUsername());
            if(uresult.getIsAdmin()==0){
                return new CommonResult<>(500,"您不是管理员，请选择正确的登录方式",null);
            }
            String token = JwtUtil.createJWT(Integer.MAX_VALUE, uresult);
            String hospitalName = channelService.findChannelById(uresult.getChannelId()).getHospitalName();
            jsonObject.put("user", uresult);
            jsonObject.put("hospitalName", hospitalName);
            jsonObject.put("token", token);
            return new CommonResult<>(200,"登录成功",jsonObject);
        }
        else {
            return new CommonResult<>(400,"登录失败,用户不存在或用户名或密码错误",null);
        }
    }
    //注册
    @PostMapping(value = "/user/register")
    @LoginToken
    public CommonResult register(@RequestBody User user){
            JSONObject jsonObject = new JSONObject();
            if(userService.findUserByUsername(user.getUsername())!=null) {
                return new CommonResult<>(400,"注册失败,用户名已存在",null);
            }
            boolean result = userService.register(user);
            if(user.getChannelId()==null){
                return new CommonResult<>(400,"注册失败,请选择一个合适的通道",null);
            }
            if(result) {
                String token = JwtUtil.createJWT(Integer.MAX_VALUE, user);
                String channelName = channelService.findChannelById(user.getChannelId()).getChannelName();
                jsonObject.put("token", token);
                jsonObject.put("channelName", channelName);
                jsonObject.put("user", user);
                return new CommonResult<>(200,"注册成功",jsonObject);
            }
            else{
                return new CommonResult<>(400,"注册失败,请联系系统管理员",null);
            }
    }


    @ApiOperation("获取 以channel进行分类的user列表")
    @GetMapping("/user/getGroupedUserList")
    public CommonResult<Map<Channel, List<User>>> getGroupedUserList(){
        Map<Channel, List<User>> result = userService.getGroupedUserList();
        return new CommonResult<>(200, "success", result);
    }
}
