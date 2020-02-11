package com.spiralforge.ForXTransfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ForXTransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForXTransferApplication.class, args);
	}

}
