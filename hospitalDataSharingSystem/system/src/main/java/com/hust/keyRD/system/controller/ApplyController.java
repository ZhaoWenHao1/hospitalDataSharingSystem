package com.hust.keyRD.system.controller;


import com.auth0.jwt.JWT;
import com.hust.keyRD.commons.Dto.Attribute;
import com.hust.keyRD.commons.constant.SystemConstant;
import com.hust.keyRD.commons.entities.Apply;
import com.hust.keyRD.commons.entities.CommonResult;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.commons.myAnnotation.CheckToken;
import com.hust.keyRD.commons.vo.ApplyVO;
import com.hust.keyRD.commons.vo.AttributesVO;
import com.hust.keyRD.commons.vo.mapper.ApplyVOMapper;
import com.hust.keyRD.system.api.service.FabricService;
import com.hust.keyRD.system.service.ApplyService;
import com.hust.keyRD.system.service.UserService;
import com.hust.keyRD.system.utils.AbeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
public class ApplyController {
    @Resource
    private ApplyService applyService;
    @Resource
    private UserService userService;
    @Resource
    private FabricService fabricService;
    @Resource
    private AbeUtil abeUtil;

    //请求获取属性
    @CheckToken
    @PostMapping(value = "/apply/applyForAttributes")
    public CommonResult applyForAttributes(@RequestBody ApplyVO applyVO, HttpServletRequest httpServletRequest){
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        Apply apply = ApplyVOMapper.INSTANCE.toApply(applyVO);
        apply.setApplierId(userId);
        User user = userService.findUserByUsername(applyVO.getTargetUserName());
        if(user==null){
            return new CommonResult<>(400, "请求用户不存在");
        }
        if(applyVO.getAttributes()==null){
            return new CommonResult<>(400, "请选择获取的属性");
        }
        if(!applyService.checkAttribute(user.getId(),applyVO.getAttributes())){
            return new CommonResult<>(400, "用户"+user.getUsername()+"不存在该属性，请求失败");
        }
        apply.setTargetUserId(user.getId());
        apply.setApplyTime(new Date());
        apply.setResult(0);
        apply.setState(0);
        applyService.save(apply);
        if(apply.getId()==null){
            return new CommonResult<>(400, "请求获取属性失败");
        }else {
            return new CommonResult<>(200, "请求获取属性已提交",apply);
        }
    }
    //获取请求属性列表
    @CheckToken
    @GetMapping(value = "/apply/getApplyList")
    public CommonResult getApplyList(HttpServletRequest httpServletRequest){
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        Integer userId = JWT.decode(token).getClaim("id").asInt();
        List<ApplyVO> res = applyService.getApplyList(userId);
        return new CommonResult<>(200, "获取成功",res);
    }
    //处理请求属性
    @CheckToken
    @PostMapping(value = "/apply/doApply")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult doApply(@RequestBody ApplyVO applyVO){
        //表示已经处理过
        Apply hasDownApply = applyService.findApplyById(applyVO.getId());
        hasDownApply.setResult(applyVO.getResult());
        hasDownApply.setState(1);
        hasDownApply.setCheckRemarks(applyVO.getCheckRemarks());
        applyService.update(hasDownApply);
        //同意授予权限，增加权限
        if(applyVO.getResult()==1){
            String attribute = hasDownApply.getAttributes();
            User applier = userService.findUserById(hasDownApply.getApplierId());
            User targetUser = userService.findUserById(hasDownApply.getTargetUserId());
            log.info("************fabric申请权限开始*****************");
//            fabricService.applyForAttribute(applier.getUsername(), targetUser.getUsername(), Attribute.parse(attribute).getValue());
            log.info("************fabric申请权限结束*****************");
            //不包含属性时才进行添加
            if(StringUtils.isEmpty(applier.getAttributes()) || !applier.getAttributes().contains(attribute)) {
                String newAttr = applier.getAttributes() == null ? attribute : applier.getAttributes() + SystemConstant.SPLIT_SYMBOL + attribute;
                applier.setAttributes(newAttr);
                // 授权属性
                String[] applyAttrs = new String[1];
                applyAttrs[0] = attribute.split(":")[1].trim();
                abeUtil.grantAttrToUser(applyAttrs, applier.getUsername());
                userService.updateAttributes(applier);
            }
        }
        return new CommonResult<>(200, "审批成功",hasDownApply);
    }


    /**
     * 批量处理属性
     *  将list参数转换为json字符串: JSON.stringify(list)
     * @param
     * @return
     */
    @CheckToken
    @PostMapping(value = "/apply/doMultipleApply")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult doMultipleApply(@RequestBody List<ApplyVO> listJSON){
        for (ApplyVO applyVO:listJSON) {
            doApply(applyVO);
        }
        return new CommonResult<>(200, "批量审批成功");
    }
    //获取所有属性列表
    @GetMapping(value = "/apply/getAttributesList")
    public CommonResult getAttributesList(){
        List<AttributesVO> res = applyService.getAttributesList();
        return new CommonResult<>(200, "获取成功",res);
    }
}
