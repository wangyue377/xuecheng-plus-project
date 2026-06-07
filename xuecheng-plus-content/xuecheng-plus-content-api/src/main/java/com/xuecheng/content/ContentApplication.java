package com.xuecheng.content;


import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

/*
* 内容管理服务启动类
* */
@EnableSwagger2Doc
@SpringBootApplication
@ComponentScan(basePackages = {"com.xuecheng.content", "com.xuecheng.messagesdk"}) // 新增
@EnableFeignClients(basePackages={"com.xuecheng.content.feignclient"})
public class ContentApplication {
   public static void main(String[] args) {
      SpringApplication.run(ContentApplication.class, args);


   }
}