package com.example.jariBean.config;


import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Allconfig {

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor(){
        return beanFactory -> {
            for (String beanName : beanFactory.getBeanDefinitionNames()){
                beanFactory.getBeanDefinition(beanName).setLazyInit(true);
            }
        };
    }
}
