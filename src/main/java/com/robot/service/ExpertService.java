package com.robot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.robot.dao.ExpertDao;
import com.robot.entity.Article;
import com.robot.entity.Expert;
import com.robot.entity.University;
import com.robot.util.CommonUtil;
import com.robot.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hua
 * @date 2018/9/25
 */
@Service
public class ExpertService {

    @Autowired
    private ExpertDao expertDao;

    /**
     * 获取专家具体信息
     * @author hua
     * @date 2018/9/25
     * @param id
     * @return
     */
    public String findExpertInf(Integer id){
        Expert expert = expertDao.findExpertInf(id);
        return GsonUtil.getSuccessJson(expert);
    }

    /**
     * 获取高校具体信息
     * @author hua
     * @date 2018/9/25
     * @param id
     * @return
     */
    public String findUniversityInf(Integer id){
        University university = expertDao.findUniversityInf(id);
        return GsonUtil.getSuccessJson(university);
    }

    /**
     * 获取专家智点文章前八条
     * @author hua
     * @date 2018/9/27
     * @return
     */
    public ArrayList<Article> findExpertArtTop(){
        ArrayList<Article> articles = expertDao.findExpertArtTop();
        return articles;
    }

    /**
     * 获取所有专家
     * @author hua
     * @date 2018/10/15
     * @return
     */
    public List<Expert> findAllExpert(){
        return expertDao.findAllExpert();
    }

    /**
     * 获取所有高校
     * @author hua
     * @date 2018/10/15
     * @return
     */
    public List<University> findAllUniversity(){
        return expertDao.findAllUniversity();
    }

    /**
     * 获取专家的分页显示
     * @author hua
     * @date 2018/10/20
     * @param pageNum
     * @return
     */
    public String findExpertByPage(Integer pageNum){
        PageHelper.startPage(pageNum,12);
        ArrayList<Expert> experts = expertDao.findAllExpert();
        HashMap dataMap = new HashMap();
        dataMap.put("experts",experts);
        return GsonUtil.getSuccessJson(dataMap);
    }

    /**
     * 获取高校的分页显示
     * @author hua
     * @date 2018/10/20
     * @param pageNum
     * @return
     */
    public String findUniversityByPage(Integer pageNum){
        PageHelper.startPage(pageNum,12);
        ArrayList<University> schools = expertDao.findAllUniversity();
        HashMap dataMap = new HashMap();
        dataMap.put("schools",schools);
        return GsonUtil.getSuccessJson(dataMap);
    }

    /**
     * 获取专家智点具体信息
     * @param id
     * @return
     */
    public String findExpertArtInf(Integer id){
        Article article = expertDao.findExpertArtInf(id);
        if(null == article)
            return GsonUtil.getErrorJson();
        return GsonUtil.getSuccessJson(GsonUtil.getFilterJson(Article.class,"summary","type"),article);
    }

    public String getExpertArtByPage(Integer Num){
        int pageNum = CommonUtil.formatPageNum(Num +"");
        PageHelper.startPage(pageNum, 12);
        List<Article> articles = expertDao.findExpertArtByPage();
        PageInfo<Article> pageInfo = new PageInfo<>(articles);
        for(Article article:articles){
            article.setContent(CommonUtil.getPreview(article.getContent()));
        }

        return GsonUtil.getSuccessJson(GsonUtil.getFilterJson(Article.class, "summary","type"), pageInfo);
    }
}
