package com.robot.interceptor;

import com.robot.annotation.PermissionsCheck;
import com.robot.entity.User;
import com.robot.enums.PermissionsModel;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限过滤
 * @author asce
 * @date 2018/11/14
 */
public class PermissionsInterceptor extends HandlerInterceptorAdapter {

    private static String[] excludedUrlPrefix = {"/manager"};

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/json");
//
//        String path = request.getServletPath();
//
//        if (isExcludedUrlPrefix(path)) {
//            return true;
//        }
//
//        User account = (User) request.getSession().getAttribute("account");
//
//        if (account == null) {
//            response.sendRedirect(request.getContextPath() + "/account/login");
//            return false;
//        }
//
//        return super.preHandle(request, response, handler);
        //判读是否Controller方法
        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request,response,handler);
        }
        HandlerMethod method = (HandlerMethod) handler;
        //获取方法上的注解
        PermissionsCheck methodPermission = method.getMethodAnnotation(PermissionsCheck.class);
        if (methodPermission!=null) {//检测注解
            if(methodPermission.access() == PermissionsModel.MANAGER){
                Integer rank = (Integer) request.getSession().getAttribute("rank");
                if (rank==null||rank!=3){
                    handle(request,response);
                    return false;
                }
            }else if(methodPermission.access() == PermissionsModel.ORGANIZER){
                Integer rank = (Integer) request.getSession().getAttribute("rank");
                if (rank==null||(rank!=2&&rank!=3)){
                    handle(request,response);
                    return false;
                }
            }else if(methodPermission.access() == PermissionsModel.USER){
                User user = (User) request.getSession().getAttribute("user");
                if (user==null){
                    handle(request,response);
                    return false;
                }
            }

        }
        return super.preHandle(request, response, handler);
    }

    private boolean isExcludedUrlPrefix(String path) {
        for (String prefix: excludedUrlPrefix) {
            if (path.startsWith(prefix))
                return false;
        }
        return true;
    }

    public boolean handle(HttpServletRequest request,HttpServletResponse response) throws IOException,ServletException {
        request.getRequestDispatcher("/404.jsp").forward(request,response);
        return false;
    }
}
