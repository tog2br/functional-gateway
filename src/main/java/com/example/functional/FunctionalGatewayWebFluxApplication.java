package com.example.functional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.functional.service.ServiceExample;

@Configuration
@SpringBootApplication
@EnableScheduling
public class FunctionalGatewayWebFluxApplication {

	Logger logger = LoggerFactory.getLogger(CommandLineRunner.class);
	
	@Autowired
	private ServiceExample serviceExample;
	
	/**
	 * Method main to start
	 * 
	 * @param args arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(FunctionalGatewayWebFluxApplication.class, args);
	}
	
	/**
	 * Example method to return the client every 5 seconds
	 */
    @Scheduled(fixedRate = 5000)
    public void scheduledRequestMono() {
    	serviceExample.getPeopleStarWars(2)
                .subscribe(logger::info);
    }

}