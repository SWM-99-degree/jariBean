package com.example.jariBean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JariBeanApplicationTests {

	@Autowired
	DefaultListableBeanFactory df;

	@Test
	void contextLoads() {
		int i =0;
		for (String name : df.getBeanDefinitionNames()){
			System.out.println(i);
			i++;
			System.out.println(df.getBean(name).getClass().getName());
		}

	}

}
