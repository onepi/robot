package com.robot.controller;

import com.robot.annotation.Authority;
import com.robot.entity.Report;
import com.robot.entity.RobotNews;
import com.robot.entity.User;
import com.robot.enums.Role;
import com.robot.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author asce
 * @date 2018/11/11
 */
@Controller
@RequestMapping("/information")
public class InformationController {
    @Autowired
    private InformationService informationService;


    @Authority(role = Role.NORMAL)
    @ResponseBody
    @RequestMapping(value = "/getSubscribe", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getSubscribe(HttpSession session){
        return informationService.getIndexSubscribe(session);
    }

    /**
     * 获取行业动态具体内容
     * @author hua
     * @date 2018/9/24
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findInformationInfo",method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String findInformationInfo(String id){
        return informationService.findInformationInfo(id);
    }

    /**
     * 获取分页显示的行业动态
     * @author hua
     * @date 2018/10/17
     * @param pageNum
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findInformationList",method = RequestMethod.GET ,produces = "text/html;charset=UTF-8")
    public String findInformationByPage(Integer pageNum){
        return informationService.findInformationByPage(pageNum);
    }

    /**
     * 获取政策具体内容
     * @author hua
     * @date 2018/10/22
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findPolicyInfo",method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String findPolicyInf(Integer id){
        return informationService.findPolicyInfo(id);
    }

    /**
     * 获取分页显示的政策
     * @author hua
     * @date 2018/10/17
     * @param pageNum
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findPolicyList",method = RequestMethod.GET ,produces = "text/html;charset=UTF-8")
    public String findPolicyByPage(Integer pageNum){
        return informationService.findPolicyByPage(pageNum);
    }

    /**
     * 获取资讯热点具体信息
     * @author hua
     * @date 2018/10/17
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getHotspotInfo",method = RequestMethod.GET ,produces = "text/html;charset=UTF-8")
    public String getHostSpotList(String id){
        return informationService.findHotInfo(id);
    }

    /**
     * 获取资讯热点列表
     * @param pageNum
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findHotspotByPage",method = RequestMethod.GET ,produces = "text/html;charset=UTF-8")
    public String findHotspotByPage(Integer pageNum){
        return informationService.findHotspotByPage(pageNum);
    }

    /**
     * 获取分页显示的行业报告
     * @author chen
     * @date 2019/1/13
     * @param pageNum
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findReportList",method = RequestMethod.GET ,produces = "text/html;charset=UTF-8")
    public String findReportList(Integer pageNum){
        return informationService.findReportList(pageNum);
    }

    /**
     * 获取行业报告具体内容
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findReportInfo",method = RequestMethod.GET ,produces = "text/html;charset=UTF-8")
    public String findReportInfo(Integer id){
        return informationService.findReportInfo(id);
    }

    /**
     * 增加行业报告
     * @Author  肖学明
     * @Date 2020/3/19 21:43 
     * @param session	
     * @param report
     * @return java.lang.String
     */
    @Authority(role = Role.MEMBER_NORMAL)
    @ResponseBody
    @RequestMapping(value = "/manager/addReport",method = RequestMethod.POST ,produces = "text/html;charset=UTF-8")
    public String addReport(HttpSession session, Report report) {
        return informationService.addReport(session, report);
    }

    /**
     * 修改行业报告
     * @Author  xm
     * @Date 2020/3/20 10:58 
     * @param report	
     * @return java.lang.String
     */
    @Authority(role = Role.MEMBER_NORMAL)
    @ResponseBody
    @RequestMapping(value = "/manager/updateReport", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String updateReport(Report report){
        return informationService.updateReport(report);
    }

    /**
     * 删除行业报告
     * @Author  xm
     * @Date 2020/3/20 11:15 
     * @param map
     * @return java.lang.String
     */
    @Authority(role = Role.MEMBER_NORMAL)
    @ResponseBody
    @RequestMapping(value = "/manager/deleteReport", method = RequestMethod.DELETE, produces = "text/html;charset=UTF-8")
    public String deleteReport(@RequestBody Map<String, List<Integer>> map){
        return informationService.deleteReport(map.get("ids"));
    }

    /**
     * 管理页面下获取行业报告
     * @Author  xm
     * @Date 2020/3/20 13:01
     * @param session
     * @param pageNum
     * @param content
     * @return java.lang.String
     */
    @Authority(role = Role.MEMBER_NORMAL)
    @ResponseBody
    @RequestMapping(value = "/manager/findReport", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String findReport(HttpSession session, @RequestParam String pageNum, @RequestParam(required = false) String content) {
        return informationService.findReport(session, pageNum, content);
    }

    /**
     * 专家智点列表
     * @param pageNum
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getExpertArtList",method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getExpertArtInfo(String pageNum){
        return informationService.getExpertArtList(pageNum);
    }
    /**
     * 专家智点具体信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findExpertArtInfo",method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    public String findExpertArtInfo(String id){
        return informationService.getExpertArtInfo(id);
    }

    /**
     * 企业新闻列表
     * @param pageNum
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCompanyNewsList", method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    public String getCompanyNewsList(String pageNum){
        return informationService.getCompanyNewsList(pageNum);
    }

    /**
     * 企业新闻具体信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCompanyNewsInfo", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getCompanyNewsInfo(String id){
        return informationService.getCompanyNewsInfo(id);
    }

    /**
     * 企业动态列表
     * @param pageNum
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCompanyDynamicsList", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getCompanyDynamicsList(String pageNum){
        return informationService.getCompanyDynamicsList(pageNum);
    }

    /**
     * 企业动态具体信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCompanyDynamicsInfo", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getCompanyDynamicsInfo(String id){
        return informationService.getCompanyDynamicsInfo(id);
    }

    /**
     * 协会新闻具体信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAssociationNewsInfo", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getAssociationNewsInfo(Integer id){
        return informationService.getAssociationNewsInfo(id);
    }

    /**
     * 协会新闻列表
     * @param pageNum
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAssociationNewsList", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getAssociationNewsList(String pageNum){
        return informationService.getAssociationNewsList(pageNum);
    }

    /**
     * 通知公告列表
     * @param pageNum
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getNoticeList", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getNotice(String pageNum){
        return informationService.getNoticeList(pageNum);
    }

    /**
     * 通知公告具体信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getNoticeInfo", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getNoticeInfo(String id){
        return informationService.getNoticeInfo(id);
    }

    /**
     * 教育培训列表
     * @param pageNum
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getEducationTrainList",method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    public String getEducationTrainList(String pageNum){
        return informationService.getEducationTrainList(pageNum);
    }

    /**
     * 教育培训具体信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getEducationTrainInfo",method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    public String getEducationTrainInfo(Integer id){
        return informationService.getEducationTrainInfo(id);
    }

    /**
     * 基础知识列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getBasicList", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getBasicList(String pageNum){
        return informationService.getBasicList(pageNum);
    }

    /**
     * 基础知识具体信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getBasicInfo", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getBasicInfo(String id){
        return informationService.getBasicInfo(id);
    }

    /**
     * 技术研讨具体信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDiscussInfo",method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getDiscussInfo(Integer id){
        return informationService.getDiscussInfo(id);
    }

    /**
     * 技术研讨列表
     * @param num
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDiscussList",method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    public String getDiscussList(String num){
        return informationService.getDiscussList(num);
    }

    /**
     * 案例库具体信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getCaseInfo",method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getCaseInfo(Integer id){
        return informationService.getCaseInfo(id);
    }

    /**
     * 案例库列表
     * @param num
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getCaseList",method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    public String getCaseList(String num){
        return informationService.getCaseList(num);
    }

    /***
     * 产品评测列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getEvaluateList", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getEvaluateList(String pageNum){
        return informationService.getEvaluateList(pageNum);
    }

    /**
     * 产品评测具体信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getEvaluateInfo", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getEvaluateInfo(String id){
        return informationService.getEvaluateInfo(id);
    }

    /**
     * 产品新闻列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getProductNewsList", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getProductNewsList(String pageNum){
        return informationService.getProductNewsList(pageNum);
    }

    /**
     * 产品新闻具体信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getProductNewsInfo", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getProductNewsInfo(String id){
        return informationService.getProductNewsInfo(id);
    }

    /**
     * 产品推荐列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getRecommendList", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getRecommendList(String pageNum){
        return informationService.getRecommendList(pageNum);
    }

    /**
     * 产品推荐具体信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getRecommendInfo", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String getRecommendInfo(String id){
        return informationService.getRecommendInfo(id);
    }

    /**
     * 删除文章
     * @param ids
     * @return
     */
    @Authority(role = Role.MEMBER_NORMAL)
    @ResponseBody
    @RequestMapping(value = "/manager/deleteInformation", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String deleteInformation(@RequestParam List<Integer> ids){
        return informationService.deleteInformation(ids);
    }

    /**
     * 修改文章
     * @param robotNews
     * @return
     */
    @Authority(role = Role.MEMBER_NORMAL)
    @ResponseBody
    @RequestMapping(value = "/manager/updateInformation", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String updateInformation(RobotNews robotNews){
        return informationService.updateInformation(robotNews);
    }

    /**
     * 添加文章
     * @param robotNews
     * @return
     */
    @Authority(role = Role.MEMBER_NORMAL)
    @ResponseBody
    @RequestMapping(value = "/manager/addInformation", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    public String addInformation(@SessionAttribute("user") User user, RobotNews robotNews){
        return informationService.addInformation(robotNews,user);
    }

    @ResponseBody
    @RequestMapping(value = "/getInformationInfo", method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    public String getInformationInfo(Integer id){
        return informationService.getInformationDetail(id);
    }

    /**
     * 获取相关热点详细信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRelatedHotInfo",method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    public String getRelatedHotInfo(Integer id){
        return informationService.findRelatedHotInfo(id);
    }

    /**
     * 获取首页新闻热点
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getIndexNewsHotSpot",method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    public String getIndexNewsHotSpot(){
        return informationService.getIndexNewsHotSpot();
    }

    /**
     * 获取新闻热点详细信息
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getNewsHotSpotInfo",method = RequestMethod.GET,produces = "text/html;charset=UTF-8")
    public String getNewsHotSpotInfo(Integer id){
        return informationService.findRelatedHotInfo(id);
    }
}
