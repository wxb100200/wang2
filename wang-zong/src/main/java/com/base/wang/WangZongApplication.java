package com.base.wang;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class WangZongApplication {

	private static String YML_PATH = "application.yml";
	private static String YML_PATH_DEV = "d:\\application.yml";
	private static String YML_PATH_TEST = "/home/yscredit/properties/szjx_test_application.yml";
	private static String YML_PATH_PROD = "/home/yscredit/properties/szjx_application.yml";

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		configurer.setLocalOverride(true);
		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
		yaml.setResources(getYmlResource());
		configurer.setProperties(yaml.getObject());
		return configurer;
	}

	private static Resource getYmlResource() {
		FileSystemResource fileSystemResource = new FileSystemResource(YML_PATH_PROD);
		if (fileSystemResource.exists()) {
			return fileSystemResource;
		}
		fileSystemResource = new FileSystemResource(YML_PATH_TEST);
		if (fileSystemResource.exists()) {
			return fileSystemResource;
		}
		fileSystemResource = new FileSystemResource(YML_PATH_DEV);
		if (fileSystemResource.exists()) {
			return fileSystemResource;
		}
		return new ClassPathResource(YML_PATH);
	}

	public static void main(String[] args) {
		SpringApplication.run(WangZongApplication.class, args);
	}

}
