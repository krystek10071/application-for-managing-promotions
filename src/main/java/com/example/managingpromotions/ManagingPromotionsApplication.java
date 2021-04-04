package com.example.managingpromotions;

import com.example.managingpromotions.ParsersData.controllers.ParserManageController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ManagingPromotionsApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ManagingPromotionsApplication.class, args);
		ParserManageController parseManage = ctx.getBean("parserManageController", ParserManageController.class);
		parseManage.parse();

	}

}
