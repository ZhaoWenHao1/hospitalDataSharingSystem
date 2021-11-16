package com.hust.keyRD.system.controller;

import cn.hutool.json.JSONObject;
import com.auth0.jwt.JWT;
import com.google.common.base.Joiner;
import com.hust.keyRD.commons.constant.SystemConstant;
import com.hust.keyRD.commons.entities.Channel;
import com.hust.keyRD.commons.entities.CommonResult;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.commons.myAnnotation.LoginToken;
import com.hust.keyRD.commons.utils.JwtUtil;
import com.hust.keyRD.commons.utils.MergeAttrs;
import com.hust.keyRD.commons.vo.ApplyVO;
import com.hust.keyRD.commons.vo.AttributesVO;
import com.hust.keyRD.system.service.ApplyService;
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
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@RestController
public class UserController{
    @Resource
    private UserService userService;
    @Resource
    private ChannelService channelService;
    @Resource
    private ApplyService applyService;
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
            if(user.getIsAdmin()==0){
                user.setAttributes("position:student");
            }else {
                user.setAttributes("position:teacher");
            }
            user.setFabricUserId(user.getUsername());
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

    @ApiOperation("获取以channel进行分类的user列表")
    @GetMapping("/user/getGroupedUserList")
    public CommonResult<Map<Channel, List<User>>> getGroupedUserList(){
        Map<Channel, List<User>> result = userService.getGroupedUserList();
        return new CommonResult<>(200, "success", result);
    }
    @ApiOperation("获取用户的属性列表")
    @GetMapping("/user/getUserAttributes")
    public CommonResult<List<String>> getUserAttributes(HttpServletRequest httpServletRequest){
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        List<String> result = new ArrayList<>();
        try {
            List<String> origin = applyService.getUserAttributes(userId);
            result = MergeAttrs.mergeAttrs(origin);
        }catch (Exception e){
            return new CommonResult<>(400, "个人属性异常，请联系系统管理员");
        }
        return new CommonResult<>(200, "success", result);
    }
    @ApiOperation("获取用户申请的属性列表")
    @GetMapping("/user/getUserApplyAttributes")
    public CommonResult<List<ApplyVO>> getUserApplyAttributes(HttpServletRequest httpServletRequest){
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        List<ApplyVO> result;
        try {
            result = applyService.getUserApplyAttributes(userId);
        }catch (Exception e){
            return new CommonResult<>(400, "个人申请属性异常，请联系系统管理员");
        }
        return new CommonResult<>(200, "success", result);
    }
    @ApiOperation("获取所有用户,除了他自己")
    @GetMapping("/user/getAllUsersExMe")
    public CommonResult<List<User>> getAllUsersExMe(HttpServletRequest httpServletRequest){
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        List<User> users = userService.getAllUsersExMe(userId);
        return new CommonResult<>(200, "success", users);
    }
    @ApiOperation("获取某一个用户的属性列表")
    @GetMapping("/user/findUserAttributes")
    public CommonResult<List<String>> findUserAttributes(Integer userId){
        List<String> result;
        try {
            result = applyService.getUserAttributes(userId);
        }catch (Exception e){
            return new CommonResult<>(400, "个人属性异常，请联系系统管理员");
        }
        return new CommonResult<>(200, "success", result);
    }
}
