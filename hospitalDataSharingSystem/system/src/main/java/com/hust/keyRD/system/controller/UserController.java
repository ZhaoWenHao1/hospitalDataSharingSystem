package com.hust.keyRD.system.controller;

import cn.hutool.json.JSONObject;
import com.auth0.jwt.JWT;
import com.hust.keyRD.commons.entities.*;
import com.hust.keyRD.commons.myAnnotation.CheckToken;
import com.hust.keyRD.commons.vo.UserChannelVO;
import com.hust.keyRD.system.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.hust.keyRD.commons.myAnnotation.LoginToken;
import com.hust.keyRD.commons.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class UserController{
    @Resource
    private UserService userService;
    @Resource
    private ChannelService channelService;
    @Resource
    private DataAuthorityService dataAuthorityService;
    @Resource
    private SharedDataAuthorityService sharedDataAuthorityService;
    @Resource
    private ChannelDataAuthorityService channelDataAuthorityService;
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
//            if(user.getAttributes()==null){
//                return new CommonResult<>(400,"注册失败,请选择您的属性集",null);
//            }
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
    
    // 获取可以push的用户
    // 例如 userA可以将file1 push到channel2上，则返回channel2上的用户
    @CheckToken
    @GetMapping(value = "/user/getSharedUserOnPush")
    public CommonResult getSharedUserOnPush(@RequestParam Integer dataId,HttpServletRequest httpServletRequest){
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        List<Integer> pushableChannelId = channelDataAuthorityService.findPushableChannelId(userId, dataId);
        List<User> users = userService.findUserByChannel(pushableChannelId).stream().
                filter(user -> !user.getIsAdmin().equals(1))
                .peek(user -> user.setPassword(null)).collect(Collectors.toList());
        
        return new CommonResult(200,"success", users);
    }

    //获取除了该用户以外和不拥有该文件权限的所有用户及其所在的channel名称
    @CheckToken
    @PostMapping(value = "/user/getUserExceptMeOnSharing")
    public CommonResult getUserExceptMeOnSharing(@RequestBody Map<String, String> params,HttpServletRequest httpServletRequest){
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        //System.out.println("userId"+userId);
        Integer sharedDataId = Integer.valueOf(params.get("sharedDataId"));//授权文件Id
        List<User> users = userService.getAllUser();
        List<UserChannelVO> result  = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            if(!user.getId().equals(userId)){//不是他本人
                DataAuthority dataAuthority = new DataAuthority();
                dataAuthority.setUserId(user.getId());
                dataAuthority.setDataSampleId(sharedDataId);
                dataAuthority.setAuthorityKey(1);
                Integer count = dataAuthorityService.checkDataAuthority(dataAuthority);
                SharedDataAuthority sharedDataAuthority = new SharedDataAuthority();
                sharedDataAuthority.setShareUserId(userId);
                sharedDataAuthority.setSharedUserId(user.getId());
                sharedDataAuthority.setSharedDataId(sharedDataId);
                sharedDataAuthority.setAuthorityKey(1);//这里暂时写死
                Integer count2 = sharedDataAuthorityService.checkSharedData(sharedDataAuthority);
                //System.out.println("count"+count);
                //System.out.println("count2"+count2);
                if(count==0&&count2==0){//之前没有此权限  两个表都没有权限
                    UserChannelVO userChannelVO = new UserChannelVO();
                    userChannelVO.setUser(users.get(i));
                    userChannelVO.setChannelName(channelService.findChannelById(users.get(i).getChannelId()).getChannelName());
                    result.add(userChannelVO);
                }
            }
        }
        return new CommonResult<>(200,"获取除了该用户以外和不拥有该文件权限的所有用户及其所在的channel名称",result);
    }

    @ApiOperation("获取 以channel进行分类的user列表")
    @GetMapping("/user/getGroupedUserList")
    public CommonResult<Map<Channel, List<User>>> getGroupedUserList(){
        Map<Channel, List<User>> result = userService.getGroupedUserList();
        return new CommonResult<>(200, "success", result);
    }
}
