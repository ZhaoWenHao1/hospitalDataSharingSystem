package com.hust.keyRD.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableFeignClients("com.hust.keyRD.system.api.feign")
@ComponentScan({"com.hust.keyRD.system", "com.hust.keyRD.commons"})
//@EnableRabbit //开启基于注解的rabbitmq
public class KeyRDMain {
    public static void main(String[] args) {
        SpringApplication.run(KeyRDMain.class,args);
    }
}
