package com.hust.keyRD.system.init;

import com.hust.keyRD.commons.constant.SystemConstant;
import com.hust.keyRD.commons.entities.User;
import com.hust.keyRD.system.service.ApplyService;
import com.hust.keyRD.system.service.UserService;
import com.hust.keyRD.system.utils.AbeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @program: hospitalDataSharingSystem
 * @description:
 * @author: zwh
 * @create: 2021/9/14 10:18
 **/
@Component
public class SystemInit {

    @Autowired
    private AbeUtil abeUtil;

    @Autowired
    private ApplyService applyService;

    @Autowired
    private UserService userService;


    public void abeInit() throws IOException {
        initGp();
        authoritySetUp();
        grantAttrToUser();
    }

    public void initGp(){
        abeUtil.initGp();
    }

    /**
     * 声明全部属性
     * @throws IOException
     */
    public void authoritySetUp() throws IOException {
        List<String> allAttributes = applyService.getAllAttributes();
        abeUtil.authoritySetUp(allAttributes.toArray(new String[0]));
    }

    /**
     * 授权用户属性
     */
    public void grantAttrToUser(){
        List<User> allUser = userService.getAllUserAndAdmin();
        for (User user : allUser) {
            abeUtil.grantAttrToUser(formatAttr(user.getAttributes()), user.getUsername());
        }
    }

    /**
     * 将用户的属性格式化
     * @param allAttribute
     * @return
     */
    public String[] formatAttr(String allAttribute){
        String[] split = allAttribute.split(SystemConstant.SPLIT_SYMBOL);
        String[] attrs = new String[split.length];
        for (int i = 0;i < split.length;i++) {
            String[] attrInfo = split[i].split(":");
//            String attr = attrInfo[0].trim() + SystemConstant.ATTR_CONNECTOR + attrInfo[1].trim();
            String attr = attrInfo[1].trim();
            attrs[i] = attr;
        }
        return attrs;
    }
}
