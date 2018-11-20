package com.robot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.robot.dao.PositionDao;
import com.robot.entity.Area;
import com.robot.entity.Industry;
import com.robot.entity.Position;
import com.robot.util.CommonUtil;
import com.robot.util.Constant;
import com.robot.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author asce
 * @date 2018/11/20
 */
@Service
public class PositionService {
    @Autowired
    PositionDao positionDao;

    private final int PAGE_LENGTH = 15;

    /**
     * 首页
     * @author asce
     * @date 2018/11/20
     * @param
     * @return
     */
    public String getIndex(){
        return GsonUtil.getSuccessJson(positionDao.getIndex());
    }

    /**
     * 搜索
     * @author asce
     * @date 2018/11/20
     * @param
     * @return
     */
    public String search(HashMap<String,String> args){
        int pageNum = CommonUtil.formatPageNum(args.get("pageNum"));
        PageHelper.startPage(pageNum, PAGE_LENGTH);
        List<Position> positions = positionDao.search(args);
        PageInfo<Position> pageInfo = new PageInfo<>(positions);
        return GsonUtil.getSuccessJson(pageInfo);
    }
    /**
     * 职位详细信息
     * @author asce
     * @date 2018/11/20
     * @param
     * @return
     */
    public String getPositionInfo(int id){
        Position position = positionDao.getPositionInfo(id);
        if (position==null){
            return GsonUtil.getErrorJson();
        }
        return GsonUtil.getSuccessJson(position);
    }
    /**
     * 各级地区
     * @author asce
     * @date 2018/11/20
     * @param
     * @return
     */
    public String getLevelArea(String parentId){
        ArrayList<Area> areas;
        if (CommonUtil.isNullOrEmpty(parentId)||!CommonUtil.simpleMatch(Constant.RULE_NUMBER,parentId)){
            areas = positionDao.getAreas(0);
        }else {
            areas = positionDao.getAreas(Integer.parseInt(parentId));
        }
        return GsonUtil.getSuccessJson(areas);
    }
    /**
     * 各级行业
     * @author asce
     * @date 2018/11/20
     * @param
     * @return
     */
    public String getLevelIndustry(String parentId){
        ArrayList<Industry> industries;
        if (CommonUtil.isNullOrEmpty(parentId)||!CommonUtil.simpleMatch(Constant.RULE_NUMBER,parentId)){
            industries = positionDao.getIndustry(0);
        }else {
            industries = positionDao.getIndustry(Integer.parseInt(parentId));
        }
        return GsonUtil.getSuccessJson(industries);
    }

}