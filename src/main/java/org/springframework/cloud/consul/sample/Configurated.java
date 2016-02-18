package org.springframework.cloud.consul.sample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Artem Zhdanov <azhdanov@griddynamics.com>
 * @since 17.02.2016
 */
public class Configurated {


    private String testProperty;

    public Configurated(final String testProperty) {
        this.testProperty = testProperty;
    }

//    @RequestMapping("/getProp")
    public String getTestProperty() {
        return testProperty;
    }
}
