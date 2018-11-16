package com.robot.controller;

import com.robot.annotation.PermissionsCheck;
import com.robot.entity.User;

import com.robot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @author hua
 * @date 2018/11/2
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户搜索
     * @author asce
     * @date 2018/11/15
     * @param
     * @return
     */
    @PermissionsCheck
    @ResponseBody
    @RequestMapping(value = "/manager/find", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String findUser(User user,String pageNum){
        return userService.findUser(user,pageNum);
    }

    /**
     * 管理员登录
     * @author asce
     * @date 2018/11/15
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/manager/login", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String adminLogin(User user, HttpSession session){
        return userService.adminLogin(user, session);
    }

    /**
     * 用户登录
     *
     * @param user
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String login(User user, HttpSession session) {
        return userService.login(user, session);
    }

    /**
     * 用户注册信息判断
     *
     * @param user
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/validate", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String register(User user, HttpSession session) {
        return userService.validate(user, session);
    }

    /**
     * 注册时验证码验证
     *
     * @param checkCode
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "register",method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    public String validateRegister(@RequestParam(value = "checkCode") String checkCode,HttpSession session){
        return userService.register(checkCode,session);
    }

    /**
     * 忘记密码
     *
     * @param email
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "forgetPassword", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String forgetPassword(String email, HttpSession session) {
        return userService.forgetPassword(email, session);
    }

    /**
     * 重置密码时邮箱验证
     *
     * @param checkCode
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "validateEmail", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String validateEmail(@RequestParam String checkCode, HttpSession session) {
        return userService.validateEmail(checkCode, session);
    }

    /**
     * 重置密码
     *
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "resetPassword", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String resetPassword(String password, HttpSession session) {
        return userService.resetPassword(password,session);
    }
}