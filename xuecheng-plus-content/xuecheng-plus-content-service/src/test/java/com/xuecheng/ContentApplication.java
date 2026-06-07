package com.xuecheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*
* 内容管理服务启动类
* */
@SpringBootApplication
//@ComponentScan(basePackages = {
//        "com.xuecheng.content",  // 当前模块
//        "com.xuecheng.base"      // 基础模块
//})
@EnableFeignClients(basePackages={"com.xuecheng.content.feignclient"})
public class ContentApplication {
   public static void main(String[] args) {
      SpringApplication.run(ContentApplication.class, args);


   }
}