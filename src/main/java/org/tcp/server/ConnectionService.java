package org.tcp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

public interface ConnectionService {
	public static final Logger log = LoggerFactory.getLogger(TcpServerApplication.class);

	public static void lunchServer(int port) {
		Runnable runnable = () -> {
			ServerSocket serverSocket=null;
			try {
				serverSocket = new ServerSocket(port);
				while (true) {
					//log.info("Waiting [" + port + "]...........");
					Socket socket = serverSocket.accept();
					createConnection(socket);
				}
			} catch (UnknownHostException e) {
				log.error("UnknownHostException - ", e);
			} catch (IOException e) {
				log.error("IOException - " + e);
			}finally {
				try {
					serverSocket.close();
				} catch (IOException e) {
					log.error("IOException - ", e);
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	public static void createConnection(Socket socket) {
		Runnable runnable = () -> {
			log.info("Client connected : " + socket);
			try {
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "US-ASCII"));
				while (true) {
					try {
						String msg = br.readLine();
						log.info(msg);
						if (msg==null) {
							out.println("Connection closed");
							out.flush();
							break;
						}
						out.println("Recevied[ " + msg + " ]");
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
						log.info("Read line exception : " + e);
						break;
					}
				}
				br.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				log.info("I/O error : " + e.getMessage());
			}
			finally {
				try {
					socket.close();
					log.info("Connection closed");
				} catch (IOException e) {
					e.printStackTrace();
					log.info("Socket close exception : " + e.getMessage());
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}
	// BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
}
