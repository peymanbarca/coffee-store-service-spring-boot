package com.demo.coffee_store_service.profile;

import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public final class DefaultProfileUtil {


    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> defProperties = new HashMap();
        defProperties.put("spring.profiles.default", "dev");
        app.setDefaultProperties(defProperties);
    }
}
