package org.tcp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TcpServerApplication {
	public static final Logger logger = LoggerFactory.getLogger(TcpServerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TcpServerApplication.class, args);
		logger.info("Server application started .............");
		ConnectionService.lunchServer(7901);
		ConnectionService.lunchServer(7902);
		ConnectionService.lunchServer(7903);
		ConnectionService.lunchServer(7904);
	}
}
