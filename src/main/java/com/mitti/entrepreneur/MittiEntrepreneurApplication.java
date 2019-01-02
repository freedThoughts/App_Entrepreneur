package com.mitti.entrepreneur;

import com.mitti.entrepreneur.service.MainMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.mitti.entrepreneur"})
public class MittiEntrepreneurApplication implements CommandLineRunner {
	private static final Logger LOGGER = LoggerFactory.getLogger(MittiEntrepreneurApplication.class);

	@Autowired
	private MainMenuService mainMenuService;

	public static void main(String[] args) {
		SpringApplication.run(MittiEntrepreneurApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try {
			mainMenuService.initializer();
			System.out.println("\n\n \t CLOSING GAME");
		} catch (Exception e) {
			System.out.println("\n\n \t CLOSING GAME");
			LOGGER.error("Error encountered ", e);
		}
	}
}

