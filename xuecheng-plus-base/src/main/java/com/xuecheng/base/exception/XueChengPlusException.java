package com.xuecheng.base.exception;


/**
 * @description 学成在线项目异常类
 * @author Mr.M
 * @date 2022/9/6 11:29
 * @version 1.0
 */
public class XueChengPlusException extends RuntimeException {

   private String errMessage;

   private int errCode = 0;

   public XueChengPlusException() {
      super();
   }

   public XueChengPlusException(String errMessage) {
      super(errMessage);
      this.errMessage = errMessage;
   }

   public XueChengPlusException(String errMessage,int errCode) {
      this.errMessage = errMessage;
      this.errCode = errCode;
   }

   public String getErrMessage() {
      return errMessage;
   }

   public int getErrCode(){
      return errCode;
   }

   public void setErrCode(int errCode){
      this.errCode = errCode;
   }

   public static void cast(CommonError commonError){
       throw new XueChengPlusException(commonError.getErrMessage());
   }
   public static void cast(String errMessage){
       throw new XueChengPlusException(errMessage);
   }

   public static void cast(String errMessage, int errCode) {
      throw new XueChengPlusException(errMessage,errCode);
   }

}