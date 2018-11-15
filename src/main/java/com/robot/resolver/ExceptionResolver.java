package com.robot.resolver;

import com.robot.util.GsonUtil;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**rdc
 * 异常解析器，以Json形式返回 <code> {result:"error"; message:"xxx"} </code>
 * Created by ZJH on 2017/8/11.
 */

@ControllerAdvice
public class ExceptionResolver {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public String handle(Exception e){
        e.printStackTrace();
        if (e instanceof NullPointerException){
            return GsonUtil.getErrorJson("你的网络不好，请稍后再试");
        }
        if (e instanceof HttpRequestMethodNotSupportedException){
            return GsonUtil.getErrorJson("HTTP method error!");
        }
        if (e instanceof MissingServletRequestParameterException){
            return GsonUtil.getErrorJson("参数错误");
        }
        return GsonUtil.getErrorJson("服务器繁忙，请稍后再试");
    }


}
