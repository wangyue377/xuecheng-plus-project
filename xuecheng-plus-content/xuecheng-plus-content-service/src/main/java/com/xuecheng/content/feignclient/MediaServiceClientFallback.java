package com.xuecheng.content.feignclient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class MediaServiceClientFallback implements MediaServiceClient {

    @Override
    public String uploadFile(MultipartFile filedata, String objectName) {
        return null;
    }
}