package com.robot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.robot.dao.PositionDao;
import com.robot.entity.Area;
import com.robot.entity.Industry;
import com.robot.entity.Position;
import com.robot.entity.User;
import com.robot.util.CommonUtil;
import com.robot.util.Constant;
import com.robot.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author asce
 * @date 2018/11/20
 */
@Service
public class PositionService {
    @Autowired
    PositionDao positionDao;

    private final int PAGE_LENGTH = 15;

    //最顶层地址的id
    private static final int TOP_AREA_ID = 1;

    @Transactional
    public String deletePosition(List<Integer> ids){
        positionDao.deletePositionRegion(ids);
        positionDao.deleteMemberPosition(ids);
        positionDao.deletePosition(ids);
        return GsonUtil.getSuccessJson();
    }

    @Transactional
    public String updatePosition(Position position,List<Integer> regionIds){
        positionDao.updatePosition(position);
        Map<String,String> map = new HashMap<>();
        map.put("positionId",position.getId()+"");
        List<Integer> list = new ArrayList<>();
        list.add(position.getId());
        positionDao.deletePositionRegion(list);
        for(int regionId:regionIds) {
            map.put("regionId",regionId+"");
            positionDao.addPositionRegion(map);
        }
        return GsonUtil.getSuccessJson();
    }

    @Transactional
    public String addPosition(Position position, List<Integer> regionIds, HttpSession session){
        User user = (User) session.getAttribute("user");
        position.setCreateTime(LocalDateTime.now().toString());
        positionDao.addPosition(position);
        positionDao.insertMemberPosition(position.getId(), user.getId());
        Map<String,String> map = new HashMap<>();
        map.put("positionId",position.getId()+"");
        for(int regionId:regionIds) {
            map.put("regionId",regionId+"");
            positionDao.addPositionRegion(map);
        }
        return GsonUtil.getSuccessJson(position.getId());
    }

    public String findPosition(Integer pageNum, String content, HttpSession session){
        User user = (User) session.getAttribute("user");

        //封装查询参数
        HashMap<String, Object> args = new HashMap();
        args.put("content", content);

        //根据权限查找
        int userRole = user.getRole();
        int userId = user.getId();
        if (userRole != User.ROLE_MANAGER && userRole != 0) {
            List<Integer> ids = positionDao.selectMemberPosition(userId);
            //如果没有直接返回
            if(ids.size() == 0) {
                return GsonUtil.getErrorJson("无招聘信息");
            }
            args.put("ids", ids);
        }
        PageHelper.startPage(pageNum, PAGE_LENGTH);
        List<Position> positions = positionDao.findPosition(args);
        //设置完整的地址
        completeArea(positions);
        PageInfo<Position> pageInfo = new PageInfo<>(positions);
        return GsonUtil.getSuccessJson(pageInfo);
    }

    /**
     * 首页
     * @author asce
     * @date 2018/11/20
     * @param
     * @return
     */
    public ArrayList<Position> getIndex(){
        ArrayList<Position> positions = positionDao.getIndex();
        //设置完整的地址
        completeArea(positions);
        return positions;
    }

    /**
     * 搜索
     * @author asce
     * @date 2018/11/20
     * @param
     * @return
     */
    public String search(Map args){
        int pageNum = CommonUtil.formatPageNum((String) args.get("pageNum"));
        PageHelper.startPage(pageNum, PAGE_LENGTH);
        List<Position> positions = positionDao.search(args);
        //设置完整的地址
        completeArea(positions);
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
        //设置完整的地址
        completeArea(position);
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
            areas = positionDao.getAreas(1);
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
            industries = positionDao.getIndustry(1);
        }else {
            industries = positionDao.getIndustry(Integer.parseInt(parentId));
        }
        return GsonUtil.getSuccessJson(industries);
    }

    /**
     * 获取完整地址
     * @Author  xm
     * @Date 2020/4/5 18:17
     * @param position
     * @return com.robot.entity.Position
     */
    public void completeArea(Position position){
        if (position == null || position.getJobArea() == null || position.getJobArea().isEmpty()) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        Area fatherArea;
        for (Area area : position.getJobArea()) {
            //不断获取父地址
            fatherArea = area;
            //地址名插到最前面
            sb.insert(0, fatherArea.getName() + "  ");
            while (fatherArea.getId() != TOP_AREA_ID && fatherArea.getParentId() != TOP_AREA_ID) {
                fatherArea = positionDao.getFatherArea(fatherArea.getParentId());
                sb.insert(0, fatherArea.getName());
            }
        }

        //设置完整地址
        position.setAreas(sb.toString());
    }

    /**
     *
     * @Author  xm
     * @Date 2020/4/5 18:55
     * @param positions
     * @return com.robot.entity.Position
     */
    public void completeArea(List<Position> positions){
        if (positions == null || positions.isEmpty()) {
            return;
        }

        for (Position position : positions) {
            completeArea(position);
        }
    }

}
