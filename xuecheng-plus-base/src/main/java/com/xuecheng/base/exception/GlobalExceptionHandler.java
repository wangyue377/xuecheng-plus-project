package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 全局异常处理器
 * @author Mr.M
 * @date 2022/9/6 11:29
 * @version 1.0
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

   //对项目的自定义异常进行处理
   @ResponseBody
   @ExceptionHandler(XueChengPlusException.class)  //捕获指定异常
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
   public RestErrorResponse customException(XueChengPlusException e) {
      // 1. 新增：获取异常中的 errCode（从自定义异常中提取）
      int errCode = e.getErrCode();
      log.error("【系统异常】{}",errCode, e.getErrMessage(),e);
      return new RestErrorResponse(e.getErrMessage(), errCode);
   }

   //对非项目自定义异常的捕获
   @ResponseBody
   @ExceptionHandler(Exception.class)
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
   public RestErrorResponse exception(Exception e) {

      log.error("【系统异常】{}",e.getMessage(),e);
      if(e.getMessage().equals("不允许访问")){   //微服务授权，不单独捕获是因为不想再引security的依赖
         return new RestErrorResponse("你没有权限操作此功能");
      }

      return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());
   }


   //MethodArgumentNotValidException - JSR303校验 - 把一些基础校验使用注解的方式写在对象字段上
   @ResponseBody
   @ExceptionHandler(MethodArgumentNotValidException.class)
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
   public RestErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {

      BindingResult bindingResult = e.getBindingResult();
      //存放错误信息
      List<String> errors = new ArrayList<>();
      bindingResult.getFieldErrors().stream().forEach(item->{
         errors.add(item.getDefaultMessage());

      });

      //将list中的错误信息拼接起来
      String errorMessage = StringUtils.join(errors, ",");
      //记录异常
      log.error("【系统异常】{}",e.getMessage(),errorMessage);

      return new RestErrorResponse(errorMessage);

   }
}