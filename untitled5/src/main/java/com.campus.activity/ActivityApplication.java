// ActivityApplication.java
package com.campus.activity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ActivityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ActivityApplication.class, args);
        System.out.println("✅ 校园活动管理系统启动成功！");
        System.out.println("🌐 访问地址: http://localhost:8088");
    }
}