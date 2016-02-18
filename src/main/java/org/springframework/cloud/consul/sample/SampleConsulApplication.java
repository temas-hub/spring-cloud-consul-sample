/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.consul.sample;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Spencer Gibb
 */
@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@RestController
@EnableConfigurationProperties
@EnableFeignClients
@Slf4j
public class SampleConsulApplication /*implements ApplicationListener<SimpleRemoteEvent>*/ {

	@Autowired
	private LoadBalancerClient loadBalancer;

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private Environment env;

	@Autowired
	private SampleClient sampleClient;

	@Autowired
	private Configurated configurated;

	@Value("${spring.application.name:testConsulApp}")
	private String appName;

	@Autowired
	private org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope;

	@RequestMapping("/me")
	public ServiceInstance me() {
		return discoveryClient.getLocalServiceInstance();
	}

	@RequestMapping("/")
	public ServiceInstance lb() {
		return loadBalancer.choose(appName);
	}

	@RequestMapping("/choose")
	public String choose() {
		return loadBalancer.choose(appName).getUri().toString();
	}

	@RequestMapping("/myenv")
	public String env(@RequestParam("prop") String prop) {
		return env.getProperty(prop, "Not Found");
	}

	@Bean
	@RefreshScope
	public Configurated createConf(@Value("${consul.test.testProperty}") String propValue) {
		return new Configurated(propValue);
	}

	@RequestMapping("/getProp")
	public String getTestProperty() {
		return configurated.getTestProperty();
	}



	@RequestMapping("/prop")
	public String prop() {
		return sampleProperties().getProp();
	}

	@RequestMapping("/instances")
	public List<ServiceInstance> instances() {
		return discoveryClient.getInstances(appName);
	}

	@RequestMapping("/feign")
	public String feign() {
		return sampleClient.choose();
	}

	/*@Bean
	public SubtypeModule sampleSubtypeModule() {
		return new SubtypeModule(SimpleRemoteEvent.class);
	}*/

	@Bean
	public SampleProperties sampleProperties() {
		return new SampleProperties();
	}


	@Bean
	@RefreshScope
	public ReloadSignalHandler reloadSignalHandler(@Value("${consul.test.signalname}") String signalName) {
		return new ReloadSignalHandler(refreshScope, signalName);
	}

	public static void main(String[] args) {
		//ReloadSignalHandler.install(args[0]);
		SpringApplication.run(SampleConsulApplication.class, args);
	}

	/*@Override
	public void onApplicationEvent(SimpleRemoteEvent event) {
		log.info("Received event: {}", event);
	}*/

	@FeignClient("testConsulApp")
	public interface SampleClient {

		@RequestMapping(value = "/choose", method = RequestMethod.GET)
		String choose();
	}
}
