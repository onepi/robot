package com.robot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.robot.dao.InformationDao;
import com.robot.dao.UserDao;
import com.robot.dto.InformationDto;
import com.robot.dto.RelatedReadingDto;
import com.robot.entity.Detail;
import com.robot.entity.Report;
import com.robot.entity.RobotNews;
import com.robot.entity.User;
import com.robot.util.CommonUtil;
import com.robot.util.GsonUtil;
import com.robot.util.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author asce
 * @date 2018/11/11
 */
@Service
public class InformationService {

    @Autowired
    private InformationDao informationDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CommentService commentService;


    /**
     * information类别
     */
    private enum InformationEnum {
        PRODUCT_NEWS(1), PRODUCT_RECOMMEND(2), PRODUCT_EVALUATE(3), BASIC_KNOWLEDGE(4), ENTERPRISE_NEWS(5), INDUSTRY_INFORMATION(6),
        POLICY_INFORMATION(7), MEMBER_NEWS(8), NOTICE(9), ASSOCIATION_NEWS(10), EXPERT_WISDOM(11), CONSULTING_FOCUS(12), NEWS_HOTSPOT_DAY(20),
        NEWS_HOTSPOT_WEEK(21), NEWS_HOTSPOT_MONTH(22), EDUCATION_TRAIN(23), CASE(24);
        private final int id;

        InformationEnum(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * 首页information数量
     */
    private enum NumberEnum {
        INFORMATION_NUMBER(15), ASSOCIATION_NUMBER(15), COMPANY_NUMBER(15), PRODUCT_NUMBER(15), KNOWLEDGE(15), EXPERT(15);
        private final int number;

        NumberEnum(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }

    /**
     * 首页information封面要求数量
     */
    private enum CoverEnum {
        PRODUCT_NUMBER(4), KNOWLEDGE_NUMBER(3);
        private final int number;

        CoverEnum(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }
    }

    /**
     * 一列的数量
     */
    private final int PAGE_LENGTH = 12;

    private final int SUBSCRIBE_LENGTH = 15;

    //******************************************管理********************************************//

    /**
     * 添加文章
     *
     * @param robotNews
     * @return
     */
    @Transactional
    public String addInformation(RobotNews robotNews, User user) {
        if (!ValidateUtil.isMatchDate(robotNews.getPostDate())) {
            robotNews.setPostDate(LocalDateTime.now().toString());
        }
        if (1 != informationDao.add(robotNews))
            return GsonUtil.getErrorJson();
        int informationId = robotNews.getId();
        informationDao.insertMemberInformation(informationId, user.getId());
        if (robotNews.getContent() == null) {
            return GsonUtil.getErrorJson("正文内容不能为空");
        }
        for (Detail detail : robotNews.getContent()) {
            HashMap map = new HashMap();
            map.put("id", robotNews.getId());
            map.put("content", detail.getContent());
            map.put("page", detail.getPage());
            if (1 != informationDao.addContent(map)) {
                throw new RuntimeException();
            }
        }
        return GsonUtil.getSuccessJson(robotNews);
    }

    /**
     * 删除文章
     *
     * @param ids
     * @return
     */
    @Transactional
    public String deleteInformation(List<Integer> ids) {
        int count = ids.size();
        commentService.deleteByInformation(ids);
        informationDao.deleteMemberInformation(ids);
        informationDao.deleteContent(ids);
        if (count != informationDao.deleteInformation(ids))
            throw new RuntimeException();
        return GsonUtil.getSuccessJson();
    }

    /**
     * 修改文章
     *
     * @param robotNews
     * @return
     */
    @Transactional
    public String updateInformation(RobotNews robotNews) {
            if (ValidateUtil.isInvalidString(informationDao.findInformationById(robotNews.getId())))
                return GsonUtil.getErrorJson("修改文章不存在");
            if (informationDao.update(robotNews) < 1)
                return GsonUtil.getErrorJson();
            if (robotNews.getContent() != null && robotNews.getContent().size() != 0) {
                for (Detail detail : robotNews.getContent()) {
                    HashMap map = new HashMap();
                    if (robotNews.getContent() != null) {
                        map.put("informationId", robotNews.getId());
                        map.put("content", detail.getContent());
                        map.put("page", detail.getPage());
                        try {
                            if (1 != informationDao.updateContent(map)) {
                                throw new RuntimeException();
                            }
                        } catch (Exception e) {
                            throw new RuntimeException();
                        }
                    }
                }
            }

        return GsonUtil.getSuccessJson();
    }
    //******************************************搜索********************************************//

    /**
     * 搜索
     *
     * @param
     * @return
     * @author asce
     * @date 2018/11/16
     */
    public PageInfo<RobotNews> findInformation(HashMap<String, String> args) {
        int pageNum = CommonUtil.formatPageNum(args.get("pageNum"));
        int category = CommonUtil.formateParmNum(args.get("categoryId"));
        ArrayList<Integer> categoryIds = new ArrayList<>();
        //先判断是否是分类查找
        switch (category) {
            case 0:
                categoryIds.add(InformationEnum.INDUSTRY_INFORMATION.getId());
                categoryIds.add(InformationEnum.CONSULTING_FOCUS.getId());
                categoryIds.add(InformationEnum.POLICY_INFORMATION.getId());
                categoryIds.add(InformationEnum.NOTICE.getId());
                categoryIds.add(InformationEnum.ASSOCIATION_NEWS.getId());
                categoryIds.add(InformationEnum.MEMBER_NEWS.getId());
                categoryIds.add(InformationEnum.ENTERPRISE_NEWS.getId());
                categoryIds.add(InformationEnum.PRODUCT_NEWS.getId());
                categoryIds.add(InformationEnum.PRODUCT_EVALUATE.getId());
                categoryIds.add(InformationEnum.PRODUCT_RECOMMEND.getId());
                categoryIds.add(InformationEnum.BASIC_KNOWLEDGE.getId());
                break;
            case 100:
                categoryIds.add(InformationEnum.INDUSTRY_INFORMATION.getId());
                categoryIds.add(InformationEnum.CONSULTING_FOCUS.getId());
                categoryIds.add(InformationEnum.POLICY_INFORMATION.getId());
                break;
            case 200:
                categoryIds.add(InformationEnum.NOTICE.getId());
                categoryIds.add(InformationEnum.ASSOCIATION_NEWS.getId());
                break;
            case 300:
                categoryIds.add(InformationEnum.MEMBER_NEWS.getId());
                categoryIds.add(InformationEnum.ENTERPRISE_NEWS.getId());
                break;
            case 400:
                categoryIds.add(InformationEnum.PRODUCT_NEWS.getId());
                categoryIds.add(InformationEnum.PRODUCT_EVALUATE.getId());
                categoryIds.add(InformationEnum.PRODUCT_RECOMMEND.getId());
                break;
            case 500:
                categoryIds.add(InformationEnum.BASIC_KNOWLEDGE.getId());
                break;
            default:
                break;
        }
        //如果还是空，则根据具体一个类别查找
        if (categoryIds.size() == 0) {
            categoryIds.add(category);
        }
        //由于泛型，另外封装一个map
        HashMap<String, Object> dataMap = new HashMap();
        dataMap.put("content", args.get("content"));
        dataMap.put("categoryIds", categoryIds);

        //根据权限查找
        int userRole = CommonUtil.formateParmNum(args.get("userRole"));
        int userId = CommonUtil.formateParmNum(args.get("userId"));
        if (userRole != User.ROLE_MANAGER && userRole != 0) {
            List<Integer> ids = informationDao.selectMemberInformation(userId);
            //如果没有直接返回
            if(ids.size() == 0)
                return new PageInfo<>();
            dataMap.put("ids", ids);
        }

        //查找
        PageHelper.startPage(pageNum, PAGE_LENGTH);
        List<RobotNews> informations = informationDao.find(dataMap);
        PageInfo<RobotNews> pageInfo = new PageInfo<>(informations);

        for (RobotNews information : informations) {
            if (information.getContent().size() != 0) {
                information.setReadGuide(CommonUtil.getPreview(information.getContent().get(0).getContent()));
            }
            information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
        }
        return pageInfo;
    }

    /**
     * information搜索总数
     *
     * @param content
     * @return
     */
    public int getSearchCount(String content) {
        return informationDao.searchCount(content);
    }

    /**
     * 获得不同分类下的搜索结果总数
     *
     * @param
     * @return
     * @author asce
     * @date 2018/11/16
     */
    public HashMap<String, Integer> getCategoryCount(String content) {
        HashMap map = new HashMap();
        HashMap countMap = new HashMap();
        map.put("content", content);
        ArrayList categoryIds = new ArrayList();
        //资讯
        categoryIds.add(InformationEnum.INDUSTRY_INFORMATION.getId());
        categoryIds.add(InformationEnum.CONSULTING_FOCUS.getId());
        categoryIds.add(InformationEnum.POLICY_INFORMATION.getId());
        map.put("categoryIds", categoryIds);
        countMap.put("informationCount", informationDao.searchCategoryCount(map));
        map.remove("categoryIds");
        categoryIds.clear();
        //协会
        categoryIds.add(InformationEnum.NOTICE.getId());
        categoryIds.add(InformationEnum.ASSOCIATION_NEWS.getId());
        map.put("categoryIds", categoryIds);
        countMap.put("associationCount", informationDao.searchCategoryCount(map));
        map.remove("categoryIds");
        categoryIds.clear();
        //企业
        categoryIds.add(InformationEnum.MEMBER_NEWS.getId());
        categoryIds.add(InformationEnum.ENTERPRISE_NEWS.getId());
        map.put("categoryIds", categoryIds);
        countMap.put("companyCount", informationDao.searchCategoryCount(map));
        map.remove("categoryIds");
        categoryIds.clear();
        //产品
        categoryIds.add(InformationEnum.PRODUCT_NEWS.getId());
        categoryIds.add(InformationEnum.PRODUCT_EVALUATE.getId());
        categoryIds.add(InformationEnum.PRODUCT_RECOMMEND.getId());
        map.put("categoryIds", categoryIds);
        countMap.put("productCount", informationDao.searchCategoryCount(map));
        map.remove("categoryIds");
        categoryIds.clear();
        //技术
        categoryIds.add(InformationEnum.BASIC_KNOWLEDGE.getId());
        map.put("categoryIds", categoryIds);
        countMap.put("knowledgeCount", informationDao.searchCategoryCount(map));
        map.remove("categoryIds");
        categoryIds.clear();
        return countMap;
    }
    //******************************************订阅内容********************************************//

    /**
     * 获取订阅内容
     *
     * @param
     * @return
     * @author asce
     * @date 2018/11/28
     */
    public String getIndexSubscribe(HttpSession session) {
        User user = (User) session.getAttribute("user");
        ArrayList<RobotNews> information = new ArrayList<>();
        ArrayList<Integer> categoryIds = userDao.getUserSubscribe(user.getId());
        Map<String, Integer> map = new HashMap<>();
        if (categoryIds != null && categoryIds.size() != 0) {
            int average = SUBSCRIBE_LENGTH / categoryIds.size();
            int remainder = SUBSCRIBE_LENGTH % categoryIds.size();
            for (int i = 0; i < categoryIds.size(); i++) {
                int categoryId = categoryIds.get(i);
                if (i == 0) {
                    map.put("number", average + remainder);
                } else {
                    map.put("number", average);
                }
                map.put("categoryId", categoryId);
                information.addAll(informationDao.getIndexInformation(map));
            }
        } else {
            return GsonUtil.getSuccessJson();
        }
        CommonUtil.formateDateTimeToDate(information);
        return GsonUtil.getSuccessJson(information);
    }

    //******************************************协会********************************************//

    /**
     * 获取行业动态的具体内容
     *
     * @param id
     * @return
     * @author hua
     * @date 2018/9/24
     */
    public String findInformationInfo(String id) {
        int infoId;
        if ((infoId = CommonUtil.formatPageNum(id)) == 0) return GsonUtil.getErrorJson();
        RobotNews information = informationDao.findInformationInfo(infoId);
        if (information == null) {
            return GsonUtil.getErrorJson();
        }
        if (1 != informationDao.addCount(infoId))
            throw new RuntimeException();
        information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
        information.setContent(CommonUtil.getAbsolutePath(information.getContent()));
        RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
        relatedReadingDto.setKeywords(informationDao.findRelatedKeyword(infoId));
        relatedReadingDto.setInformation(informationDao.findRelatedInformation(infoId));
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("information", information);
        dataMap.put("related", relatedReadingDto);
        return GsonUtil.getSuccessJson(dataMap);
    }

    /**
     * 获取首页行业动态
     *
     * @return
     * @author hua
     * @date 2018/10/17
     */
    public ArrayList<InformationDto> findInformationTop() {
        Map<String, Integer> map = new HashMap<>();
        map.put("number", NumberEnum.INFORMATION_NUMBER.getNumber());
        map.put("categoryId", InformationEnum.INDUSTRY_INFORMATION.getId());
        ArrayList<InformationDto> informations = informationDao.getIndexInformationWithContent(map);
        for (InformationDto robotNews : informations) {
            if (ValidateUtil.isNullOrEmpty(robotNews.getImg())) {
                robotNews.setImg(CommonUtil.getFirstImgFromContent(robotNews.getContent()));
            }
        }
        CommonUtil.formateDateTimeToDate(informations);
        return informations;
    }

    /**
     * 资讯统计
     * @Author  xm
     * @Date 2020/6/17 14:34
     * @param
     * @return java.lang.Integer
     */
    public Integer countInformation() {
        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(InformationEnum.INDUSTRY_INFORMATION.getId());
        categoryIds.add(InformationEnum.CONSULTING_FOCUS.getId());
        return countInformation(categoryIds);
    }

    public Integer countInformation(List<Integer> categoryIds) {
        return informationDao.countInformation(categoryIds);
    }

    /**
     * 政策统计
     * @Author  xm
     * @Date 2020/6/17 14:34
     * @param
     * @return java.lang.Integer
     */
    public Integer countPolicy() {
        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(InformationEnum.POLICY_INFORMATION.getId());
        return countInformation(categoryIds);
    }

    /**
     * 产品统计
     * @Author  xm
     * @Date 2020/6/17 14:34
     * @param
     * @return java.lang.Integer
     */
    public Integer countProduct() {
        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(InformationEnum.PRODUCT_EVALUATE.getId());
        categoryIds.add(InformationEnum.PRODUCT_NEWS.getId());
        categoryIds.add(InformationEnum.PRODUCT_RECOMMEND.getId());
        return countInformation(categoryIds);
    }

    /**
     * 技术统计
     * @Author  xm
     * @Date 2020/6/17 14:34
     * @param
     * @return java.lang.Integer
     */
    public Integer countTechnology() {
        List<Integer> categoryIds = new ArrayList<>();
        categoryIds.add(InformationEnum.CASE.getId());
        categoryIds.add(InformationEnum.BASIC_KNOWLEDGE.getId());
        return countInformation(categoryIds);
    }

    /**
     * 行业动态列表
     *
     * @param Num
     * @return
     */
    public String findInformationByPage(Integer Num) {
        int pageNum = CommonUtil.formatPageNum(Num + "");
        PageHelper.startPage(pageNum, PAGE_LENGTH);
        List<RobotNews> informations = informationDao.getInformationList(InformationEnum.INDUSTRY_INFORMATION.getId());
        PageInfo<RobotNews> pageInfo = new PageInfo<>(informations);
        for (RobotNews information : informations) {
            information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
            information.setContent(CommonUtil.getPreview(information.getContent()));
        }
        return GsonUtil.getSuccessJson(GsonUtil.getFilterJson(RobotNews.class, "url"), pageInfo);
    }

    /**
     * 获取政策的具体内容
     *
     * @param id
     * @return
     * @author hua
     * @date 2018/10/22
     */
    public String findPolicyInfo(int id) {
        RobotNews information = informationDao.findInformationInfo(id);
        if (information == null) {
            return GsonUtil.getErrorJson();
        }
        if (1 != informationDao.addCount(id))
            throw new RuntimeException();
        information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
        information.setContent(CommonUtil.getAbsolutePath(information.getContent()));
        RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
        relatedReadingDto.setKeywords(informationDao.findRelatedKeyword(id));
        relatedReadingDto.setInformation(informationDao.findRelatedInformation(id));
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("information", information);
        dataMap.put("related", relatedReadingDto);
        return GsonUtil.getSuccessJson(dataMap);
    }

    /**
     * 获取政策的前12条
     *
     * @return
     * @author hua
     * @date 2018/10/22
     */
    /**
     * 获取政策的前12条
     *
     * @return
     * @author hua
     * @date 2018/10/22
     */
    public ArrayList<InformationDto> findPolicyTop() {
        Map<String, Integer> map = new HashMap<>();
        map.put("number", NumberEnum.INFORMATION_NUMBER.getNumber());
        map.put("categoryId", InformationEnum.POLICY_INFORMATION.getId());
        ArrayList<InformationDto> informations = informationDao.getIndexInformationWithContent(map);
//        for (InformationDto robotNews : informations) {
//            robotNews.setImg(CommonUtil.getFirstImgFromContent(robotNews.getContent()));
//        }
        CommonUtil.formateDateTimeToDate(informations);
        return informations;
    }

    /**
     * 获取分页显示的政策
     *
     * @param Num
     * @return
     * @author hua
     * @date 2018/10/22
     */
    public String findPolicyByPage(Integer Num) {
        int pageNum = CommonUtil.formatPageNum(Num + "");
        PageHelper.startPage(pageNum, PAGE_LENGTH);
        List<RobotNews> informations = informationDao.getInformationList(InformationEnum.POLICY_INFORMATION.getId());
        PageInfo<RobotNews> pageInfo = new PageInfo<>(informations);
        for (RobotNews information : informations) {
            information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
            information.setContent(CommonUtil.getPreview(information.getContent()));
        }
        return GsonUtil.getSuccessJson(GsonUtil.getFilterJson(RobotNews.class, "url"), pageInfo);
    }

    /**
     * 首页资讯热点
     *
     * @return
     */
    public ArrayList<RobotNews> getIndexHot() {
        Map<String, Integer> map = new HashMap<>();
        map.put("number", NumberEnum.INFORMATION_NUMBER.getNumber());
        map.put("categoryId", InformationEnum.CONSULTING_FOCUS.getId());
        ArrayList<RobotNews> informations = informationDao.getIndexInformation(map);
        CommonUtil.formateDateTimeToDate(informations);
        return informations;
    }

    /**
     * 资讯热点列表
     *
     * @param Num
     * @return
     */
    public String findHotspotByPage(Integer Num) {
        int pageNum = CommonUtil.formatPageNum(Num + "");
        PageHelper.startPage(pageNum, PAGE_LENGTH);
        List<RobotNews> informations = informationDao.getInformationList(InformationEnum.CONSULTING_FOCUS.getId());
        PageInfo<RobotNews> pageInfo = new PageInfo<>(informations);
        for (RobotNews information : informations) {
            information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
            information.setContent(CommonUtil.getPreview(information.getContent()));
        }
        return GsonUtil.getSuccessJson(GsonUtil.getFilterJson(RobotNews.class, "url"), pageInfo);
    }

    /**
     * 资讯热点具体内容
     *
     * @param id
     * @return
     * @author hua
     * @date 2018/10/22
     */
    public String findHotInfo(String id) {
        int infoId;
        if ((infoId = CommonUtil.formatPageNum(id)) == 0) return GsonUtil.getErrorJson();
        RobotNews information = informationDao.findInformationInfo(infoId);
        information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
        if (information == null) {
            return GsonUtil.getErrorJson();
        }
        if (1 != informationDao.addCount(infoId))
            throw new RuntimeException();
        RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
        relatedReadingDto.setKeywords(informationDao.findRelatedKeyword(infoId));
        relatedReadingDto.setInformation(informationDao.findRelatedInformation(infoId));
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("information", information);
        dataMap.put("related", relatedReadingDto);
        return GsonUtil.getSuccessJson(dataMap);
    }

    /**
     * 首页行业报告
     *
     * @return
     * @author chen
     * @date 2019/1/13
     */
    public ArrayList<Report> findReportTop() {
        ArrayList<Report> reports = informationDao.findReportTop();
        for (Report report : reports) {
            report.setPostDate(CommonUtil.getDate(report.getPostDate()));
        }
        return reports;
    }
    
    /**
     * 统计行业报告
     * @Author  xm
     * @Date 2020/6/17 20:54
     * @param 	
     * @return java.lang.Integer
     */
    public Integer countReport() {
        return informationDao.countReport();
    }

    /**
     * 行业报告列表
     *
     * @param num
     * @return
     * @author chen
     * @date 2019/1/13
     */
    public String findReportList(Integer num) {
        int pageNum = CommonUtil.formatPageNum(num + "");
        PageHelper.startPage(pageNum, PAGE_LENGTH);
        ArrayList<Report> reports = informationDao.findReportList();
        PageInfo<Report> pageInfo = new PageInfo<>(reports);
        for (Report report : reports) {
            report.setPostDate(CommonUtil.getDate(report.getPostDate()));
        }
        return GsonUtil.getSuccessJson(GsonUtil.getFilterJson(Report.class, "url", "industry", "production", "editor", "firstPostDate", "delivery", "reportPage", "reportNum", "graphNum", "content", "keywords"), pageInfo);

    }

    /**
     * 行业报告详情
     *
     * @param id
     * @return
     * @author chen
     * @date 2019/1/13
     */
    public String findReportInfo(int id) {
        Report report = informationDao.findReportInfo(id);
        if (report == null)
            return GsonUtil.getErrorJson();
        if (1 != informationDao.addCountRep(id))
            throw new RuntimeException();
        report.setPostDate(CommonUtil.getDate(report.getPostDate()));
        report.setFirstPostDate(CommonUtil.getDate(report.getFirstPostDate()));
        ArrayList<String> keywords = informationDao.findRepRelatedKeyword(id);
        RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
        relatedReadingDto.setKeywords(keywords);
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("information", report);
        dataMap.put("related", relatedReadingDto);
        return GsonUtil.getSuccessJson(dataMap);
    }

    /**
     * 增加行业报告报告
     * @Author  肖学明
     * @Date 2020/3/20 10:15
     * @param session
     * @param report
     * @return java.lang.String
     */
    @Transactional
    public String addReport(HttpSession session, Report report) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return GsonUtil.getErrorJson("未登录");
        }
        if (ValidateUtil.isInvalidString(report.getTitle())) {
            return GsonUtil.getErrorJson("标题不能为空");
        }
        //手动把前端传过来的空字符串设为null
        if ("".equals(report.getFirstPostDate())) {
            report.setFirstPostDate(null);
        }
        if ("".equals(report.getPostDate())) {
            report.setPostDate(null);
        }
        //如果时间不为空则验证格式
        if (report.getFirstPostDate() != null && !ValidateUtil.isMatchDate(report.getFirstPostDate())) {
            return GsonUtil.getErrorJson("首次出版时间不符合2020-03-20 20:00:00的格式");
        }
        if (report.getPostDate() != null && !ValidateUtil.isMatchDate(report.getPostDate())) {
            return GsonUtil.getErrorJson("最新修订时间不符合2020-03-20 20:00:00的格式");
        }
        //初始浏览量为0
        report.setViewCount(0 + "");
        if (informationDao.addReport(report) != 1) {
            return GsonUtil.getErrorJson("增加行业报告失败");
        }
        int reportId = report.getId();
        informationDao.insertMemberReport(reportId, user.getId());
        return GsonUtil.getSuccessJson(report);
    }

    /**
     * 修改行业报告
     * @Author  肖学明
     * @Date 2020/3/20 10:22
     * @param report	
     * @return java.lang.String
     */
    public String updateReport(Report report) {
        if (informationDao.updateReport(report) < 1) {
            return GsonUtil.getErrorJson("要修改的文章不存在");
        }
        return GsonUtil.getSuccessJson("修改成功");
    }

    /**
     * 删除行业报告
     * @Author  xm
     * @Date 2020/3/20 11:58 
     * @param ids	
     * @return java.lang.String
     */
    @Transactional
    public String deleteReport(List<Integer> ids) {
        informationDao.deleteMemberReport(ids);
        informationDao.deleteReport(ids);
        return GsonUtil.getSuccessJson("删除成功");
    }

    /**
     * 在管理界面下获取行业报告
     * @Author  xm
     * @Date 2020/3/20 13:53
     * @param session
     * @param num
     * @param content
     * @return com.github.pagehelper.PageInfo<com.robot.entity.Report>
     */
    public String findReport(HttpSession session, String num, String content) {
        int pageNum = CommonUtil.formatPageNum(num);
        User user = (User) session.getAttribute("user");

        //封装查询参数
        HashMap<String, Object> args = new HashMap();
        args.put("content", content);

        //根据权限查找
        int userRole = user.getRole();
        int userId = user.getId();
        if (userRole != User.ROLE_MANAGER && userRole != 0) {
            List<Integer> ids = informationDao.selectMemberReport(userId);
            //如果没有直接返回
            if(ids.size() == 0) {
                return GsonUtil.getErrorJson("无行业报告");
            }
            args.put("ids", ids);
        }

        PageHelper.startPage(pageNum, PAGE_LENGTH);
        ArrayList<Report> reports = informationDao.findReport(args);
        PageInfo<Report> pageInfo = new PageInfo<>(reports);
        for (Report report : reports) {
            report.setPostDate(CommonUtil.getDate(report.getPostDate()));
        }
        return GsonUtil.getSuccessJson(GsonUtil.getFilterJson(Report.class, "viewCount"), pageInfo);

    }

    //******************************************协会********************************************//

    /**
     * 获取协会动态详细信息
     *
     * @param id
     * @return
     * @author hua
     * @date 2018/10/11
     */
    public String getAssociationNewsInfo(int id) {
        RobotNews information = informationDao.findInformationInfo(id);
        if (information != null) {
            if (1 != informationDao.addCount(id)) {
                throw new RuntimeException();
            }
            information.setContent(CommonUtil.getAbsolutePath(information.getContent()));
            information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
            RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
            relatedReadingDto.setKeywords(informationDao.findRelatedKeyword(id));
            relatedReadingDto.setInformation(informationDao.findRelatedInformation(id));
            Map<String, Object> dataMap = new HashMap();
            dataMap.put("information", information);
            dataMap.put("related", relatedReadingDto);
            return GsonUtil.getSuccessJson(dataMap);
        } else
            return GsonUtil.getErrorJson();
    }

    /**
     * 协会动态列表
     *
     * @param
     * @return
     */
    public String getAssociationNewsList(String page) {
        int pageNum = CommonUtil.formatPageNum(page);
        PageHelper.startPage(pageNum, PAGE_LENGTH);
        List<RobotNews> news = informationDao.getInformationList(InformationEnum.ASSOCIATION_NEWS.getId());
        PageInfo<RobotNews> pageInfo = new PageInfo<>(news);
        for (RobotNews news1 : news) {
            news1.setPostDate(CommonUtil.formateDbTime(news1.getPostDate()));
            news1.setContent(CommonUtil.getPreview(news1.getContent()));
        }
        return GsonUtil.getSuccessJson(pageInfo);
    }

    /**
     * 首页协会动态
     *
     * @return
     */
    public List<InformationDto> getIndexAssociationNews() {
        Map<String, Integer> map = new HashMap<>();
        map.put("number", NumberEnum.ASSOCIATION_NUMBER.getNumber());
        map.put("categoryId", InformationEnum.ASSOCIATION_NEWS.getId());
        ArrayList<InformationDto> informations = informationDao.getIndexInformationWithContent(map);
        for (InformationDto robotNews : informations) {
            if (ValidateUtil.isNullOrEmpty(robotNews.getImg())) {
                robotNews.setImg(CommonUtil.getFirstImgFromContent(robotNews.getContent()));
            }
        }
        CommonUtil.formateDateTimeToDate(informations);
        return informations;
    }


    /**
     * 首页公告
     *
     * @return
     */
    public List<InformationDto> getIndexNotice() {
        Map<String, Integer> map = new HashMap<>();
        map.put("number", NumberEnum.ASSOCIATION_NUMBER.getNumber());
        map.put("categoryId", InformationEnum.NOTICE.getId());
        ArrayList<InformationDto> informations = informationDao.getIndexInformationWithContent(map);
        for (InformationDto robotNews : informations) {
            if (ValidateUtil.isNullOrEmpty(robotNews.getImg())) {
                robotNews.setImg(CommonUtil.getFirstImgFromContent(robotNews.getContent()));
            }
        }
        CommonUtil.formateDateTimeToDate(informations);
        return informations;
    }

    /**
     * 公告列表
     *
     * @param page
     * @return
     * @author asce
     * @date 2018/10/12
     */
    public String getNoticeList(String page) {
        int pageNum = CommonUtil.formatPageNum(page);
        PageHelper.startPage(pageNum, PAGE_LENGTH);
        List<RobotNews> notices = informationDao.getInformationList(InformationEnum.NOTICE.getId());
        PageInfo<RobotNews> pageInfo = new PageInfo<>(notices);
        for (RobotNews notice : notices) {
            notice.setPostDate(CommonUtil.formateDbTime(notice.getPostDate()));
            notice.setContent(CommonUtil.getPreview(notice.getContent()));
        }
        return GsonUtil.getSuccessJson(pageInfo);
    }

    /**
     * 公告详细信息
     *
     * @param id
     * @return
     */
    public String getNoticeInfo(String id) {
        int infoId;
        if ((infoId = CommonUtil.formatPageNum(id)) == 0) return GsonUtil.getErrorJson();
        RobotNews information = informationDao.findInformationInfo(infoId);
        if (information == null)
            return GsonUtil.getErrorJson();
        if (1 != informationDao.addCount(infoId))
            throw new RuntimeException();
        information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
        information.setContent(CommonUtil.getAbsolutePath(information.getContent()));
        RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
        relatedReadingDto.setKeywords(informationDao.findRelatedKeyword(infoId));
        relatedReadingDto.setInformation(informationDao.findRelatedInformation(infoId));
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("information", information);
        dataMap.put("related", relatedReadingDto);
        return GsonUtil.getSuccessJson(dataMap);
    }

    /**
     * 首页教育培训
     *
     * @return
     * @author chen
     * @date 2019/1/16
     */
    public List<RobotNews> getEducationTrain() {
        Map<String, Integer> map = new HashMap<>();
        map.put("number", NumberEnum.ASSOCIATION_NUMBER.getNumber());
        map.put("categoryId", InformationEnum.EDUCATION_TRAIN.getId());
        ArrayList<RobotNews> informations = informationDao.getIndexInformation(map);
        CommonUtil.formateDateTimeToDate(informations);
        return informations;
    }

    /**
     * 教育培训列表
     *
     * @param page
     * @return
     * @author chen
     * @date 2019/1/13
     */
    public String getEducationTrainList(String page) {
        int pageNum = CommonUtil.formatPageNum(page);
        PageHelper.startPage(pageNum, PAGE_LENGTH);
        List<RobotNews> notices = informationDao.getInformationList(InformationEnum.EDUCATION_TRAIN.getId());
        PageInfo<RobotNews> pageInfo = new PageInfo<>(notices);
        for (RobotNews notice : notices) {
            notice.setPostDate(CommonUtil.formateDbTime(notice.getPostDate()));
        }
        return GsonUtil.getSuccessJson(pageInfo);
    }

    /**
     * 教育培训详细信息
     *
     * @param id
     * @return
     * @author chen
     * @date 2019/1/13
     */
    public String getEducationTrainInfo(int id) {
        RobotNews information = informationDao.findInformationInfo(id);
        if (information == null)
            return GsonUtil.getErrorJson();
        if (1 != informationDao.addCount(id))
            throw new RuntimeException();
        information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
        information.setContent(CommonUtil.getAbsolutePath(information.getContent()));
        RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
        relatedReadingDto.setKeywords(informationDao.findRelatedKeyword(id));
        relatedReadingDto.setInformation(informationDao.findRelatedInformation(id));
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("information", information);
        dataMap.put("related", relatedReadingDto);
        return GsonUtil.getSuccessJson(dataMap);
    }
    //******************************************企业********************************************//

    /**
     * 企业新闻(首页)
     */
    public ArrayList<RobotNews> getCompanyNews() {
        Map<String, Integer> map = new HashMap<>();
        map.put("number", NumberEnum.COMPANY_NUMBER.getNumber());
        map.put("categoryId", InformationEnum.ENTERPRISE_NEWS.getId());
        ArrayList<RobotNews> companyNews = informationDao.getIndexInformation(map);
        if (CommonUtil.judgeCover(companyNews, CoverEnum.KNOWLEDGE_NUMBER.getNumber())) {
            map.put("number", CoverEnum.KNOWLEDGE_NUMBER.getNumber());
            companyNews.addAll(informationDao.getIndexCover(map));
        }
        CommonUtil.formateDateTimeToDate(companyNews);
        return companyNews;
    }

    /**
     * 企业新闻列表
     */
    public String getCompanyNewsList(String pageNum) {
        int page = CommonUtil.formatPageNum(pageNum);
        PageHelper.startPage(page, PAGE_LENGTH);
        ArrayList<RobotNews> companyNewsList = informationDao.getInformationList(InformationEnum.ENTERPRISE_NEWS.getId());
        for (RobotNews robotNews : companyNewsList) {
            robotNews.setPostDate(CommonUtil.formateDbTime(robotNews.getPostDate()));
        }
        PageInfo<RobotNews> pageInfo = new PageInfo<>(companyNewsList);
        return GsonUtil.getSuccessJson(pageInfo);
    }

    /**
     * 企业新闻具体信息
     */
    public String getCompanyNewsInfo(String id) {
        int infoId;
        if ((infoId = CommonUtil.formatPageNum(id)) == 0) return GsonUtil.getErrorJson();
        RobotNews information = informationDao.findInformationInfo(infoId);
        if (information == null)
            return GsonUtil.getErrorJson();
        if (1 != informationDao.addCount(infoId))
            throw new RuntimeException();
        information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
        information.setContent(CommonUtil.getAbsolutePath(information.getContent()));
        //相关
        RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
        relatedReadingDto.setKeywords(informationDao.findRelatedKeyword(infoId));
        relatedReadingDto.setInformation(informationDao.findRelatedInformation(infoId));
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("information", information);
        dataMap.put("related", relatedReadingDto);
        return GsonUtil.getSuccessJson(dataMap);
    }

    /**
     * 会员动态(首页)
     */
    public ArrayList<RobotNews> getCompanyDynamics() {
        Map<String, Integer> map = new HashMap<>();
        map.put("number", NumberEnum.COMPANY_NUMBER.getNumber());
        map.put("categoryId", InformationEnum.MEMBER_NEWS.getId());
        ArrayList<RobotNews> companyNews = informationDao.getIndexInformation(map);
        for (RobotNews robotNews : companyNews) {
            if (ValidateUtil.isNullOrEmpty(robotNews.getImg())) {
                robotNews.setImg(CommonUtil.getFirstImgFromContent(informationDao.findInformationInfo(robotNews.getId()).getContent()));
            }
        }
        CommonUtil.formateDateTimeToDate(companyNews);
        return companyNews;
    }

    /**
     * 会员动态列表
     */
    public String getCompanyDynamicsList(String pageNum) {
        int page = CommonUtil.formatPageNum(pageNum);
        PageHelper.startPage(page, PAGE_LENGTH);
        ArrayList<RobotNews> companyNewsList = informationDao.getInformationList(InformationEnum.MEMBER_NEWS.getId());
        for (RobotNews robotNews : companyNewsList) {
            robotNews.setPostDate(CommonUtil.formateDbTime(robotNews.getPostDate()));
        }
        PageInfo<RobotNews> pageInfo = new PageInfo<>(companyNewsList);
        return GsonUtil.getSuccessJson(pageInfo);
    }

    /**
     * 会员动态具体信息
     */
    public String getCompanyDynamicsInfo(String id) {
        int infoId;
        if ((infoId = CommonUtil.formatPageNum(id)) == 0) return GsonUtil.getErrorJson();
        RobotNews information = informationDao.findInformationInfo(infoId);
        if (information == null)
            return GsonUtil.getErrorJson();
        if (1 != informationDao.addCount(infoId))
            throw new RuntimeException();
        information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
        //相关
        RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
        relatedReadingDto.setKeywords(informationDao.findRelatedKeyword(infoId));
        relatedReadingDto.setInformation(informationDao.findRelatedInformation(infoId));
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("information", information);
        dataMap.put("related", relatedReadingDto);
        return GsonUtil.getSuccessJson(dataMap);
    }
    //***************************技术********************************//

    /**
     * 首页基础知识
     *
     * @param
     * @return
     * @author asce
     * @date 2018/11/2
     */
    public List<RobotNews> getIndexBasic() {
        Map<String, Integer> map = new HashMap<>();
        map.put("number", NumberEnum.KNOWLEDGE.getNumber());
        map.put("categoryId", InformationEnum.BASIC_KNOWLEDGE.getId());
        ArrayList<RobotNews> basices = informationDao.getIndexInformation(map);
        if (CommonUtil.judgeCover(basices, CoverEnum.KNOWLEDGE_NUMBER.getNumber())) {
            map.put("number", CoverEnum.KNOWLEDGE_NUMBER.getNumber());
            basices.addAll(informationDao.getIndexCover(map));
        }
        CommonUtil.formateDateTimeToDate(basices);
        return basices;
    }

    /**
     * 基础知识列表
     */
    public String getBasicList(String pageNum) {
        int page = CommonUtil.formatPageNum(pageNum);
        PageHelper.startPage(page, PAGE_LENGTH);
        ArrayList<RobotNews> companyNewsList = informationDao.getInformationList(InformationEnum.BASIC_KNOWLEDGE.getId());
        for (RobotNews robotNews : companyNewsList) {
            robotNews.setPostDate(CommonUtil.formateDbTime(robotNews.getPostDate()));
        }
        PageInfo<RobotNews> pageInfo = new PageInfo<>(companyNewsList);
        return GsonUtil.getSuccessJson(pageInfo);
    }

    /**
     * 基础知识具体信息
     */
    public String getBasicInfo(String id) {
        int infoId;
        if ((infoId = CommonUtil.formatPageNum(id)) == 0) return GsonUtil.getErrorJson();
        RobotNews information = informationDao.findInformationInfo(infoId);
        if (information == null)
            return GsonUtil.getErrorJson();
        if (1 != informationDao.addCount(infoId))
            throw new RuntimeException();
        information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
        //相关
        RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
        relatedReadingDto.setKeywords(informationDao.findRelatedKeyword(infoId));
        relatedReadingDto.setInformation(informationDao.findRelatedInformation(infoId));
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("information", information);
        dataMap.put("related", relatedReadingDto);
        return GsonUtil.getSuccessJson(dataMap);
    }
    //*************************产品****************************//

    /**
     * 首页产品评测
     *
     * @return java.lang.String
     * @data 2018/10/24
     */
    public ArrayList<RobotNews> getIndexEvaluate() {
        Map<String, Integer> map = new HashMap<>();
        map.put("number", NumberEnum.PRODUCT_NUMBER.getNumber());
        map.put("categoryId", InformationEnum.PRODUCT_EVALUATE.getId());
        ArrayList<RobotNews> evaluation = informationDao.getIndexInformation(map);
        if (CommonUtil.judgeCover(evaluation, CoverEnum.PRODUCT_NUMBER.getNumber())) {
            map.put("number", CoverEnum.PRODUCT_NUMBER.getNumber());
            evaluation.addAll(informationDao.getIndexCover(map));
        }
        CommonUtil.formateDateTimeToDate(evaluation);
        return evaluation;
    }

    /**
     * 产品评测列表
     */
    public String getEvaluateList(String pageNum) {
        int page = CommonUtil.formatPageNum(pageNum);
        PageHelper.startPage(page, PAGE_LENGTH);
        ArrayList<RobotNews> companyNewsList = informationDao.getInformationList(InformationEnum.PRODUCT_EVALUATE.getId());
        PageInfo<RobotNews> pageInfo = new PageInfo<>(companyNewsList);
        for (RobotNews robotNews : companyNewsList) {
            robotNews.setPostDate(CommonUtil.formateDbTime(robotNews.getPostDate()));
        }
        return GsonUtil.getSuccessJson(pageInfo);
    }

    /**
     * 产品评测具体信息
     */
    public String getEvaluateInfo(String id) {
        int infoId;
        if ((infoId = CommonUtil.formatPageNum(id)) == 0) return GsonUtil.getErrorJson();
        RobotNews information = informationDao.findInformationInfo(infoId);
        if (information == null)
            return GsonUtil.getErrorJson();
        if (1 != informationDao.addCount(infoId))
            throw new RuntimeException();
        information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
        //相关
        RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
        relatedReadingDto.setKeywords(informationDao.findRelatedKeyword(infoId));
        relatedReadingDto.setInformation(informationDao.findRelatedInformation(infoId));
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("information", information);
        dataMap.put("related", relatedReadingDto);
        return GsonUtil.getSuccessJson(dataMap);
    }

    /**
     * 首页产品新闻
     *
     * @param
     * @return
     * @author asce
     * @date 2018/10/25
     */
    public ArrayList<RobotNews> getIndexProductNews() {
        Map<String, Integer> map = new HashMap<>();
        map.put("number", NumberEnum.PRODUCT_NUMBER.getNumber());
        map.put("categoryId", InformationEnum.PRODUCT_NEWS.getId());
        ArrayList<RobotNews> news = informationDao.getIndexInformation(map);
        if (CommonUtil.judgeCover(news, CoverEnum.PRODUCT_NUMBER.getNumber())) {
            map.put("number", CoverEnum.PRODUCT_NUMBER.getNumber());
            news.addAll(informationDao.getIndexCover(map));
        }
        CommonUtil.formateDateTimeToDate(news);
        return news;
    }

    /**
     * 产品新闻列表
     */
    public String getProductNewsList(String pageNum) {
        int page = CommonUtil.formatPageNum(pageNum);
        PageHelper.startPage(page, PAGE_LENGTH);
        ArrayList<RobotNews> companyNewsList = informationDao.getInformationList(InformationEnum.PRODUCT_NEWS.getId());
        PageInfo<RobotNews> pageInfo = new PageInfo<>(companyNewsList);
        for (RobotNews robotNews : companyNewsList) {
            robotNews.setPostDate(CommonUtil.formateDbTime(robotNews.getPostDate()));
        }
        return GsonUtil.getSuccessJson(pageInfo);
    }

    /**
     * 产品新闻具体信息
     */
    public String getProductNewsInfo(String id) {
        int infoId;
        if ((infoId = CommonUtil.formatPageNum(id)) == 0) return GsonUtil.getErrorJson();
        RobotNews information = informationDao.findInformationInfo(infoId);
        if (information == null)
            return GsonUtil.getErrorJson();
        if (1 != informationDao.addCount(infoId))
            throw new RuntimeException();
        information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
        //相关
        RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
        relatedReadingDto.setKeywords(informationDao.findRelatedKeyword(infoId));
        relatedReadingDto.setInformation(informationDao.findRelatedInformation(infoId));
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("information", information);
        dataMap.put("related", relatedReadingDto);
        return GsonUtil.getSuccessJson(dataMap);
    }

    /**
     * 首页产品推荐
     *
     * @param
     * @return
     * @author asce
     * @date 2018/10/25
     */
    public ArrayList<RobotNews> getIndexRecommend() {
        Map<String, Integer> map = new HashMap<>();
        map.put("number", NumberEnum.PRODUCT_NUMBER.getNumber());
        map.put("categoryId", InformationEnum.PRODUCT_RECOMMEND.getId());
        ArrayList<RobotNews> recommend = informationDao.getIndexInformation(map);
        if (CommonUtil.judgeCover(recommend, CoverEnum.PRODUCT_NUMBER.getNumber())) {
            map.put("number", CoverEnum.PRODUCT_NUMBER.getNumber());
            recommend.addAll(informationDao.getIndexCover(map));
        }
        CommonUtil.formateDateTimeToDate(recommend);
        return recommend;
    }

    /**
     * 产品推荐列表
     */
    public String getRecommendList(String pageNum) {
        int page = CommonUtil.formatPageNum(pageNum);
        PageHelper.startPage(page, PAGE_LENGTH);
        ArrayList<RobotNews> companyNewsList = informationDao.getInformationList(InformationEnum.PRODUCT_RECOMMEND.getId());
        PageInfo<RobotNews> pageInfo = new PageInfo<>(companyNewsList);
        for (RobotNews robotNews : companyNewsList) {
            robotNews.setPostDate(CommonUtil.formateDbTime(robotNews.getPostDate()));
        }
        return GsonUtil.getSuccessJson(pageInfo);
    }

    /**
     * 产品推荐具体信息
     */
    public String getRecommendInfo(String id) {
        int infoId;
        if ((infoId = CommonUtil.formatPageNum(id)) == 0) return GsonUtil.getErrorJson();
        RobotNews information = informationDao.findInformationInfo(infoId);
        if (information == null)
            return GsonUtil.getErrorJson();
        if (1 != informationDao.addCount(infoId))
            throw new RuntimeException();
        information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
        //相关
        RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
        relatedReadingDto.setKeywords(informationDao.findRelatedKeyword(infoId));
        relatedReadingDto.setInformation(informationDao.findRelatedInformation(infoId));
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("information", information);
        dataMap.put("related", relatedReadingDto);
        return GsonUtil.getSuccessJson(dataMap);
    }
    //*************************专家智点****************************//

    /**
     * 首页专家智点
     *
     * @return
     * @author hua
     * @date 2018/9/27
     */
    public ArrayList<RobotNews> getIndexExpertArt() {
        Map<String, Integer> map = new HashMap<>();
        map.put("number", NumberEnum.EXPERT.getNumber());
        map.put("categoryId", InformationEnum.EXPERT_WISDOM.getId());
        ArrayList<RobotNews> articles = informationDao.getIndexInformation(map);
        CommonUtil.formateDateTimeToDate(articles);
        return articles;
    }

    /**
     * 获取专家智点具体信息
     *
     * @param id
     * @return
     */
    public String getExpertArtInfo(String id) {
        int infoId;
        if ((infoId = CommonUtil.formatPageNum(id)) == 0) return GsonUtil.getErrorJson();
        RobotNews information = informationDao.findInformationInfo(infoId);
        if (null == information)
            return GsonUtil.getErrorJson();
        information.setContent(CommonUtil.getAbsolutePath(information.getContent()));
        information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
        //相关
        RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
        relatedReadingDto.setKeywords(informationDao.findRelatedKeyword(infoId));
        relatedReadingDto.setInformation(informationDao.findRelatedInformation(infoId));
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("information", information);
        dataMap.put("related", relatedReadingDto);
        return GsonUtil.getSuccessJson(dataMap);
    }

    /**
     * 专家智点列表
     *
     * @param Num
     * @return
     */
    public String getExpertArtList(String Num) {
        int pageNum = CommonUtil.formatPageNum(Num);
        PageHelper.startPage(pageNum, PAGE_LENGTH);
        List<RobotNews> articles = informationDao.getInformationList(InformationEnum.EXPERT_WISDOM.getId());
        PageInfo<RobotNews> pageInfo = new PageInfo<>(articles);
        for (RobotNews article : articles) {
            article.setContent(CommonUtil.getPreview(article.getContent()));
            article.setPostDate(CommonUtil.formateDbTime(article.getPostDate()));
        }
        return GsonUtil.getSuccessJson(GsonUtil.getFilterJson(RobotNews.class, "url"), pageInfo);
    }


    //************************技术研讨**********************************************//

    /**
     * 首页技术研讨
     *
     * @return
     * @author hua
     * @date 2018/9/27
     */
    public ArrayList<InformationDto> getIndexDiscuss() {
        ArrayList<InformationDto> discuss = informationDao.getIndexDiscuss();
        return discuss;
    }

    /**
     * 技术研讨数量
     * @Author  xm
     * @Date 2020/6/17 21:25
     * @param
     * @return java.lang.Integer
     */
    public Integer countDiscuss() {
        return informationDao.countDiscuss();
    }

    /**
     * 获取技术研讨详细信息
     *
     * @param id
     * @return
     */
    public String getDiscussInfo(Integer id) {
        InformationDto discuss = informationDao.getDiscussInfo(id);
        discuss.setPostDate(CommonUtil.formateDbTime(discuss.getPostDate()));
        if (1 != informationDao.addCountDis(id))
            throw new RuntimeException();
        if (discuss == null)
            return GsonUtil.getErrorJson();
        return GsonUtil.getSuccessJson(discuss);
    }

    /**
     * 获取技术研讨列表
     *
     * @param Num
     * @return
     */
    public String getDiscussList(String Num) {
        int pageNum = CommonUtil.formatPageNum(Num);
        PageHelper.startPage(pageNum, PAGE_LENGTH);
        List<InformationDto> discussList = informationDao.getDiscussList();
        for (InformationDto informationDto : discussList) {
            informationDto.setPostDate(CommonUtil.formateDbTime(informationDto.getPostDate()));
        }
        PageInfo<InformationDto> pageInfo = new PageInfo<>(discussList);
        return GsonUtil.getSuccessJson(pageInfo);
    }
    //************************案列库************************************************//

    /**
     * 首页案列库
     *
     * @return
     * @author hua
     * @date 2018/9/27
     */
    public ArrayList<RobotNews> getIndexCase() {
        Map<String, Integer> map = new HashMap<>();
        map.put("number", NumberEnum.EXPERT.getNumber());
        map.put("categoryId", InformationEnum.CASE.getId());
        ArrayList<RobotNews> articles = informationDao.getIndexInformation(map);
        CommonUtil.formateDateTimeToDate(articles);
        return articles;
    }

    /**
     * 获取案例库具体信息
     *
     * @param infoId
     * @return
     */
    public String getCaseInfo(Integer infoId) {
        RobotNews information = informationDao.findInformationInfo(infoId);
        if (null == information)
            return GsonUtil.getErrorJson();
        information.setContent(CommonUtil.getAbsolutePath(information.getContent()));
        information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
        //相关
        RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
        relatedReadingDto.setKeywords(informationDao.findRelatedKeyword(infoId));
        relatedReadingDto.setInformation(informationDao.findRelatedInformation(infoId));
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("information", information);
        dataMap.put("related", relatedReadingDto);
        return GsonUtil.getSuccessJson(dataMap);
    }

    /**
     * 案例库列表
     *
     * @param Num
     * @return
     */
    public String getCaseList(String Num) {
        int pageNum = CommonUtil.formatPageNum(Num);
        PageHelper.startPage(pageNum, PAGE_LENGTH);
        List<RobotNews> articles = informationDao.getInformationList(InformationEnum.CASE.getId());
        PageInfo<RobotNews> pageInfo = new PageInfo<>(articles);
        for (RobotNews article : articles) {
            article.setContent(CommonUtil.getPreview(article.getContent()));
            article.setPostDate(CommonUtil.formateDbTime(article.getPostDate()));
        }
        return GsonUtil.getSuccessJson(GsonUtil.getFilterJson(RobotNews.class, "url"), pageInfo);
    }


    /**
     * 获取资讯具体信息【安卓】
     *
     * @param
     * @return
     * @author asce
     * @date 2018/12/4
     */
    public String getInformationDetail(int id) {
        RobotNews information = informationDao.findInformationInfo(id);
        if (null == information)
            return GsonUtil.getErrorJson();
        information.setContent(CommonUtil.getAbsolutePath(information.getContent()));
        information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
        //相关
        RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
        relatedReadingDto.setKeywords(informationDao.findRelatedKeyword(id));
        relatedReadingDto.setInformation(informationDao.findRelatedInformation(id));
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("information", information);
        dataMap.put("related", relatedReadingDto);
        return GsonUtil.getSuccessJson(dataMap);
    }

    /**
     * 查找首页相关热点
     *
     * @return
     * @author chen
     * @date 2019/1/13
     */
    public ArrayList<RobotNews> findIndexRelatedHot() {
        ArrayList<RobotNews> informations = informationDao.findRelatedHot();
        return informations;
    }

    /**
     * 查找相关热点详情
     *
     * @param id
     * @return
     */
    public String findRelatedHotInfo(int id) {
        RobotNews information = informationDao.findInformationInfo(id);
        if (information == null) {
            return GsonUtil.getErrorJson();
        }
        if (1 != informationDao.addCount(id))
            throw new RuntimeException();
        information.setPostDate(CommonUtil.formateDbTime(information.getPostDate()));
        information.setContent(CommonUtil.getAbsolutePath(information.getContent()));
        RelatedReadingDto relatedReadingDto = new RelatedReadingDto();
        relatedReadingDto.setKeywords(informationDao.findRelatedKeyword(id));
        relatedReadingDto.setInformation(informationDao.findRelatedInformation(id));
        Map<String, Object> dataMap = new HashMap();
        dataMap.put("information", information);
        dataMap.put("related", relatedReadingDto);
        return GsonUtil.getSuccessJson(dataMap);
    }


    /**
     * 获取首页新闻热点
     *
     * @return
     * @author chen
     * @date 2019/1/16
     */
    public String getIndexNewsHotSpot() {
        Map<String, Integer> map = new HashMap<>();
        Map<String, List<RobotNews>> results = new HashMap<>();
        map.put("number", NumberEnum.ASSOCIATION_NUMBER.getNumber());
        map.put("categoryId", InformationEnum.NEWS_HOTSPOT_DAY.getId());
        ArrayList<RobotNews> result1 = informationDao.getIndexInformation(map);
        map.put("categoryId", InformationEnum.NEWS_HOTSPOT_WEEK.getId());
        ArrayList<RobotNews> result2 = informationDao.getIndexInformation(map);
        map.put("categoryId", InformationEnum.NEWS_HOTSPOT_MONTH.getId());
        ArrayList<RobotNews> result3 = informationDao.getIndexInformation(map);
        CommonUtil.formateDateTimeToDate(result1);
        CommonUtil.formateDateTimeToDate(result2);
        CommonUtil.formateDateTimeToDate(result3);
        results.put("newsHotSpotDay", result1);
        results.put("newsHotSpotWeek", result2);
        results.put("newsHotSpotMonth", result3);
        return GsonUtil.getSuccessJson(results);
    }

}
