package com.hust.keyRD.system.controller;

import com.hust.keyRD.commons.entities.ChannelDataAuthority;
import com.hust.keyRD.commons.entities.CommonResult;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.commons.exception.BadRequestException;
import com.hust.keyRD.commons.utils.JwtUtil;
import com.hust.keyRD.commons.vo.ChannelDataAuthorityVO;
import com.hust.keyRD.system.api.service.FabricService;
import com.hust.keyRD.system.service.ChannelDataAuthorityService;
import com.hust.keyRD.system.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: system
 * @description: ChannelDataAuthorityController
 * @author: zwh
 * @create: 2021-03-22 10:37
 **/
@Slf4j
@RestController
@Api(tags = "域间权限Controller")
@RequestMapping("innerChannelAuthority")
public class ChannelDataAuthorityController {
    @Resource
    private UserService userService;
    @Resource
    private ChannelDataAuthorityService channelDataAuthorityService;
    @Resource
    private FabricService fabricService;
    
    @ApiOperation("获取pull权限列表")
    @RequestMapping("getPullAuthorityList")
    public CommonResult<List<ChannelDataAuthorityVO>> getPullAuthorityList(){
        List<ChannelDataAuthorityVO> pullAuthorityList = channelDataAuthorityService.getPullAuthorityList();
        return new CommonResult(200, "success", pullAuthorityList);
    }

    @ApiOperation("获取push权限列表")
    @RequestMapping("getPushAuthorityList")
    public CommonResult<List<ChannelDataAuthorityVO>> getPushAuthorityList(){
        List<ChannelDataAuthorityVO> pushAuthorityList = channelDataAuthorityService.getPushAuthorityList();
        return new CommonResult(200, "success", pushAuthorityList);
    }
    
    
    
    @ApiOperation("添加域间pull权限")
    @PostMapping("addPullAuthority")
    // TODO : 重复的不能添加
    public CommonResult addInterChannelPullAuthority(@RequestBody ChannelDataAuthority channelDataAuthority, HttpServletRequest httpServletRequest){
        // 判断是否是管理员
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        User user = userService.findUserById(userId);
        if(user.getIsAdmin() != 1){
            throw new BadRequestException("非管理员");
        }
        String username = userService.findUserById(channelDataAuthority.getUserId()).getUsername();
        int count = channelDataAuthorityService.countByChannelData(channelDataAuthority);
        if(count > 0){
            throw new BadRequestException("重复添加权限");
        }
       // System.out.println(username);
        // 区块链添加pull权限
        log.info("************fabric增加文件pull权限记录区块链开始*****************");
        fabricService.grantInnerChannelPull(channelDataAuthority.getDataId()+"","role1",username);
        log.info("************fabric增加文件pull权限记录区块链结束*****************");
        channelDataAuthorityService.addPullAuthority(channelDataAuthority);

        return new CommonResult(200, "success");
    }


    @ApiOperation("添加域间push权限")
    @PostMapping("addPushAuthority")
    // TODO：重复的不能添加
    public CommonResult addInterChannelPushAuthority(@RequestBody ChannelDataAuthority channelDataAuthority, HttpServletRequest httpServletRequest){
        // 判断是否是管理员
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        User user = userService.findUserById(userId);
        if(user.getIsAdmin() != 1){
            throw new BadRequestException("非管理员");
        }
        int count = channelDataAuthorityService.countByChannelData(channelDataAuthority);
        if(count > 0){
            throw new BadRequestException("重复添加权限");
        }
        // 区块链添加push权限
        log.info("************fabric增加文件push权限记录区块链开始*****************");
        fabricService.grantInnerChannelPush(channelDataAuthority.getDataId()+"","role1",userService.findUserById(channelDataAuthority.getUserId()).getUsername());
        log.info("************fabric增加文件push权限记录区块链结束*****************");
        channelDataAuthorityService.addPushAuthority(channelDataAuthority);

        return new CommonResult(200, "success");
    }
    
    @ApiOperation("删除域间pull权限")
    @PostMapping("deletePullAuthority")
    public CommonResult deleteInterChannelPullAuthority(@RequestBody ChannelDataAuthority channelDataAuthority, HttpServletRequest httpServletRequest){
        // 判断是否是管理员
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        User user = userService.findUserById(userId);
        if(user.getIsAdmin() != 1){
            throw new BadRequestException("非管理员");
        }
        if(!channelDataAuthority.getType().equals(2)){
            throw new BadRequestException("非pull类型");
        }
        channelDataAuthority = channelDataAuthorityService.findByCondition(channelDataAuthority.getUserId(),
                channelDataAuthority.getDataId(),channelDataAuthority.getChannelId(),channelDataAuthority.getType());
        //System.out.println(channelDataAuthority.getId()+","+channelDataAuthority.getUserId());
        // 区块链删除pull权限
        log.info("************fabric删除文件pull权限记录区块链开始*****************");
        fabricService.revokeInnerChannelPull(channelDataAuthority.getDataId()+"","role1",userService.findUserById(channelDataAuthority.getUserId()).getUsername());
        log.info("************fabric删除文件pull权限记录区块链结束*****************");
        channelDataAuthorityService.deleteById(channelDataAuthority.getId());

        return new CommonResult(200,"success");
    }

    @ApiOperation("删除域间push权限")
    @PostMapping("deletePushAuthority")
    public CommonResult deleteInterChannelPushAuthority(@RequestBody ChannelDataAuthority channelDataAuthority, HttpServletRequest httpServletRequest){
        // 判断是否是管理员
        Integer userId = JwtUtil.getUserId(httpServletRequest);
        User user = userService.findUserById(userId);
        if(user.getIsAdmin() != 1){
            throw new BadRequestException("非管理员");
        }
        if(!channelDataAuthority.getType().equals(1)){
            throw new BadRequestException("非push类型");
        }
        channelDataAuthority = channelDataAuthorityService.findByCondition(channelDataAuthority.getUserId(),
                channelDataAuthority.getDataId(),channelDataAuthority.getChannelId(),channelDataAuthority.getType());
        // 区块链删除push权限
        log.info("************fabric删除文件push权限记录区块链开始*****************");
        fabricService.revokeInnerChannelPush(channelDataAuthority.getDataId()+"","role1",userService.findUserById(channelDataAuthority.getUserId()).getUsername());
        log.info("************fabric删除文件push权限记录区块链结束*****************");
        channelDataAuthorityService.deleteById(channelDataAuthority.getId());
        return new CommonResult(200,"success");
    }
    
    
}
