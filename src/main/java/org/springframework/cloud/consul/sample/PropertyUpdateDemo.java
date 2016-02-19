package org.springframework.cloud.consul.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Artem Zhdanov <azhdanov@griddynamics.com>
 * @since 19.02.2016
 */
@Configuration
@EnableAutoConfiguration
@RestController
public class PropertyUpdateDemo {

    @Autowired
    private Configurated configurated;

    @Bean
    @RefreshScope
    public Configurated createConf(@Value("${consul.test.testProperty}") String propValue) {
        return new Configurated(propValue);
    }

    @RequestMapping("/getProp")
    public String getTestProperty() {
        return configurated.getTestProperty();
    }

    public static void main(String[] args) {
        SpringApplication.run(PropertyUpdateDemo.class, args);
    }
}
