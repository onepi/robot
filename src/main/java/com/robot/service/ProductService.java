package com.robot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.robot.dao.ProductDao;
import com.robot.entity.Product;
import com.robot.util.CommonUtil;
import com.robot.util.Constant;
import com.robot.util.GsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author asce
 * @date 2018/9/22
 */
@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public int getSearchCount(String content){
        return productDao.getSearchCount(content);
    }

    public String deleteProduct(List<Integer> ids){
        int result = productDao.deleteProduct(ids);
        if (result==1){
            return GsonUtil.getSuccessJson();
        }
        return GsonUtil.getErrorJson();
    }

    public String updateProduct(Product product){
        int result = productDao.updateProduct(product);
        if (result==1){
            return GsonUtil.getSuccessJson();
        }
        return GsonUtil.getErrorJson();
    }

    /**
     * 添加商品
     * @author asce
     * @date 2018/12/6
     * @param
     * @return
     */
    public String addProduct(Product product){
        int result = productDao.addProduct(product);
        if (result==1){
            return GsonUtil.getSuccessJson(product);
        }
        return GsonUtil.getErrorJson();
    }
    /**
     * 搜索产品
     * @author asce
     * @date 2018/11/15
     * @param
     * @return
     */
    public PageInfo<Product> searchProduct(Map<String,String> args){
        int pageNum = CommonUtil.formatPageNum(args.get("pageNum"));
        PageHelper.startPage(pageNum,Constant.PRODUCT_PAGE_COUNT);
        List<Product> products = productDao.findProductIf(args);
        PageInfo<Product> pageInfo = new PageInfo<>(products);
        return pageInfo;
    }

    /**
     * 产品
     * @author asce
     * @date 2018/9/22
     * @param args
     * @param pageNumStr
     * @return java.lang.String
     */
    public String getProductList(Map<String,String> args,String pageNumStr){
        int pageNum = CommonUtil.formatPageNum(pageNumStr);
        PageHelper.startPage(pageNum,Constant.PRODUCT_PAGE_COUNT);
        List<Product> products = productDao.findProductIf(args);
        PageInfo<Product> pageInfo = new PageInfo<>(products);
        return GsonUtil.getSuccessJson(GsonUtil.getFilterJson(Product.class,"company","load","axis","imgs","effectTime","lastUpdateTime"),pageInfo);
    }

    /**
     *
     * 产品具体信息
     * @author asce
     * @date 2018/9/22
     * @param idStr     产品id
     * @return
     */
    public String getProductInfo(String idStr){
        if (!CommonUtil.simpleMatch(Constant.RULE_NUMBER,idStr))
            return GsonUtil.getErrorJson();
        int id = Integer.parseInt(idStr);
        Product product = productDao.getProductInfo(id);
        return GsonUtil.getSuccessJson(product);
    }

    /**
     * 首页产品库
     * @author Ning
     * @data 2018/10/24
     * @return java.lang.String
     */
    public ArrayList<Product> getProductLibrary() {
        return productDao.getProductLibrary();
    }

    /**
     * 查找热门产品
     * @author chen
     * @date 2019/1/13
     * @return
     */
    public String findHotProduct(){
         ArrayList<Product> products = productDao.findHotProduct();
         return GsonUtil.getSuccessJson(GsonUtil.getFilterJson(Product.class, "price", "brand","introduction","company","load","axis","imgs","effectTime","lastUpdateTime","summary","pattern","type","residence","format","viewCount","img"),products);
    }
}
