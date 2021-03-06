package com.robot.controller;

import com.robot.annotation.Authority;
import com.robot.entity.Enterprise;
import com.robot.entity.Member;
import com.robot.entity.RepresentativeWork;
import com.robot.entity.User;
import com.robot.enums.Role;
import com.robot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author hua
 * @date 2018/11/2
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "getPermission", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getPermission(HttpSession session) {
        return userService.getPermission(session);
    }

    @ResponseBody
    @RequestMapping(value = "/getAllSubscribe", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getAllSubscribe() {
        return userService.getAllSubscribe();
    }

    @Authority(role = Role.NORMAL)
    @ResponseBody
    @RequestMapping(value = "/getSubscribeList", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getSubscribeList(HttpSession session) {
        return userService.getSubscribeList(session);
    }

    @Authority(role = Role.NORMAL)
    @ResponseBody
    @RequestMapping(value = "/deleteSubscribe", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String deleteSubscribe(@RequestParam int categoryId, HttpSession session) {
        return userService.deleteSubscribe(categoryId, session);
    }

    @Authority(role = Role.NORMAL)
    @ResponseBody
    @RequestMapping(value = "/addSubscribe", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String addSubscribe(@RequestParam int categoryId, HttpSession session) {
        return userService.addSubscribe(categoryId, session);
    }

    @Authority(role = Role.NORMAL)
    @ResponseBody
    @RequestMapping(value = "/addSubscribes", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String addSubscribes(@RequestParam List<Integer> categoryIds,HttpSession session){
        return userService.addSubscribes(categoryIds,session);
    }

    @RequestMapping(value = "/loginOut", method = RequestMethod.GET)
    public String loginOut() {
        return "login-out";
    }

    /**
     * 用户搜索
     *
     * @param
     * @return
     * @author asce
     * @date 2018/11/15
     */
    @Authority(role = Role.MEMBER)
    @ResponseBody
    @RequestMapping(value = "/manager/find", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String findUser(User user, String pageNum) {
        return userService.findUser(user, pageNum);
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
    public String login(User user, HttpSession session, HttpServletRequest request) {
        return userService.login(user, session, request);
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
    public String register(User user, HttpSession session,String memberName) {
        return userService.validate(user, session,memberName);
    }

    /**
     * 注册时验证码验证
     *
     * @param checkCode
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "register", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String validateRegister(@RequestParam(value = "checkCode") String checkCode, HttpSession session) {
        return userService.register(checkCode, session);
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
        return userService.resetPassword(password, session);
    }

    /**
     * 发送验证码
     * @Author  xm
     * @Date 2020/6/8 11:49
     * @param session	
     * @return java.lang.String
     */
    @Authority(role = Role.NORMAL)
    @ResponseBody
    @RequestMapping(value = "sendCode", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String sendCode(HttpSession session) {
        return userService.sendCode(session);
    }

    /**
     * 修改或绑定邮箱
     * @Author  xm
     * @Date 2020/6/8 11:22 
     * @param code	
     * @param email	
     * @param session	
     * @return java.lang.String
     */
    @Authority(role = Role.NORMAL)
    @ResponseBody
    @RequestMapping(value = "editEmail", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String editEmail(@RequestParam (required = false) String code, String email, HttpSession session) {
        return userService.editEmail(code, email, session);
    }

    @InitBinder
    public void init(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    /**
     * 注册企业会员
     *
     * @param member
     * @param authenticationDatas
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "insertNewMember", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String insertNewMember(HttpSession session, Member member, Enterprise enterprise,
                                  @RequestParam(value = "authenticationDatas", required = false) MultipartFile authenticationDatas,
                                  @RequestParam(value = "contactInfoDatas", required = false) MultipartFile contactInfoDatas) {
        return userService.insertNewMember(session, member, enterprise, authenticationDatas, contactInfoDatas);
    }

    /**
     * 填写会员的产品
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "insertRepresentativeWork", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String insertRepresentativeWork(HttpSession session, RepresentativeWork representativeWork) {
        return userService.insertRepresentativeWork(session, representativeWork);
    }

    /**
     * 插入会员用户
     *
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "insertMemberUser", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String insertMemberUser(User user, HttpSession session) {
        return userService.insertMemberUser(user, session);
    }

    /**
     * 获取会员注册信息
     * @Author  xm
     * @Date 2020/5/10 19:08 
     * @param session	
     * @return java.lang.String
     */
    @Authority(role = Role.MEMBER_NORMAL)
    @ResponseBody
    @RequestMapping(value = "member", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getMemberRegisterInfo(HttpSession session) {
        return userService.getMemberRegisterInfo(session);
    }

    /**
     * 更新会员注册信息
     * @Author  xm
     * @Date 2020/5/12 21:03
     * @param session
     * @param enterprise
     * @param authenticationDatas
     * @param contactInfoDatas
     * @return java.lang.String
     */
    @Authority(role = Role.MEMBER)
    @ResponseBody
    @RequestMapping(value = "updateMember", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String updateMember(HttpSession session, Enterprise enterprise,
                               @RequestParam(value = "authenticationDatas", required = false) MultipartFile authenticationDatas,
                               @RequestParam(value = "contactInfoDatas", required = false) MultipartFile contactInfoDatas) {
        return userService.updateMember(session, enterprise, authenticationDatas, contactInfoDatas);
    }

    /**
     * 获取会员填写的代表作品
     * @Author  xm
     * @Date 2020/5/14 16:50
     * @param session	
     * @return java.lang.String
     */
    @Authority(role = Role.MEMBER)
    @ResponseBody
    @RequestMapping(value = "getRepresentativeWork", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getRepresentativeWork(HttpSession session) {
        return userService.getRepresentativeWork(session);
    }

    /**
     * 新增或修改会员代表作品
     * @Author  xm
     * @Date 2020/5/14 17:19
     * @param session
     * @param representativeWork
     * @return java.lang.String
     */
    @Authority(role = Role.MEMBER)
    @ResponseBody
    @RequestMapping(value = "updateRepresentativeWork", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String updateRepresentativeWork(HttpSession session, RepresentativeWork representativeWork) {
        return userService.updateRepresentativeWork(session, representativeWork);
    }

    /**
     * 查看会员列表
     *
     * @return
     */
//    @Authority(role = Role.ASSOCIATION)
    @ResponseBody
    @RequestMapping(value = "getMemberList", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getMemberList(String pageNum) {
        return userService.getMemberList(pageNum);
    }

    /**
     * @param memberId
     * @return java.lang.String
     * @function 查看会员详情
     * @author gdrcn
     * @date 2019/7/16
     */
    @ResponseBody
    @RequestMapping(value = "getMemberInfo", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getMemberInfo(Integer memberId) {
        return userService.getMemberInfo(memberId);
    }

    /**
     * @param user
     * @return java.lang.String
     * @function 添加会员旗下的用户
     * @author gdrcn
     * @date 2019/7/6
     */
    @ResponseBody
    @RequestMapping(value = "addMemberUser", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String addMemberUser(User user, Integer memberId) {
        return userService.addMemberUser(user, memberId);
    }

    /**
     * @return
     * @function 展示会员用户的详情
     * @author gdrcn
     * @date 2019/7/16
     */
    @ResponseBody
    @RequestMapping(value = "getMemberListStatus", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getMemberListStatus(String pageNum) {
        return userService.getMemberListStatus(pageNum);
    }

    /**
     * 判断会员通过
     *
     * @return
     */
//    @Authority(role = Role.ASSOCIATION)
    @ResponseBody
    @RequestMapping(value = "judgeMember", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String judgeMember(User user, Member member, Integer userId, Integer memberId) {
        return userService.judgeMember(user, member, userId, memberId);
    }

    /**
     * @function 文件下载
     * @author gdrcn
     * @date 2019/7/16
     * @param realFileName
     * @param tag
     * @return java.lang.String
     */
    @ResponseBody
    @RequestMapping(value = "downloadMemberFile", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String downloadMemberFile(String realFileName, Integer tag){
        return userService.downloadMemberFile(realFileName, tag);
    }
}
