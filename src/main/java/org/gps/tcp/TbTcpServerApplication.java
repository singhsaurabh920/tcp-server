package org.gps.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TbTcpServerApplication {
	public static final Logger logger = LoggerFactory.getLogger(TbTcpServerApplication.class);
	public static boolean LOG = false;

	public static void main(String[] args) {
		LOG = Boolean.parseBoolean(args[0]);
		SpringApplication.run(TbTcpServerApplication.class, args);
		logger.info("Server application started [ " + LOG + " ]");
		ConnectionService.lunchHelthCheck(7900,LOG);
		ConnectionService.lunchServer(7901);
		ConnectionService.lunchServer(7902);
		ConnectionService.lunchServer(7903);
		ConnectionService.lunchServer(7904);
	}
}
