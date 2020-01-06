package com.xxy.sfl.controller;

import com.xxy.sfl.entity.UserEntity;
import com.xxy.sfl.pub.controller.BaseRestController;
import com.xxy.sfl.pub.utils.JsonBackData;
import com.xxy.sfl.service.IUserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户管理控制器
 *
 * @author xg
 */
@RestController
@RequestMapping(value = "user")
public class UserController extends BaseRestController<IUserService, UserEntity> {

    @PostMapping(value = "login")
    public JsonBackData login(HttpServletRequest request, @RequestBody Map<String, String> params) {
        JsonBackData back = new JsonBackData();
        String username = params.get("username");
        String password = params.get("password");
        if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
            UserEntity user = service.login(request, username, password);
            if (user != null && StringUtils.hasText(user.getId())) {
                back.setBackData(user);
                back.setBackMsg("登录成功");
            } else {
                back.setSuccess(false);
                back.setBackMsg("登录失败:未知异常,请联系系统管理员");
            }
        } else {
            back.setSuccess(false);
            back.setBackMsg("登录失败:用户名或密码不能为空");
        }
        return back;
    }

    @GetMapping(value = "findByCode")
    public JsonBackData findByCode(@RequestParam String userCode) {
        JsonBackData back = new JsonBackData();
        UserEntity result = service.findByCode(userCode);
        back.setBackData(result);
        back.setBackMsg("查询成功");
        return back;
    }
}
