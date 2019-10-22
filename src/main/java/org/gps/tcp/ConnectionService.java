package org.gps.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ConnectionService {
	public static final Logger logger = LoggerFactory.getLogger(TbTcpServerApplication.class);
	
	public static void lunchHelthCheck(int port,boolean log) {
		Runnable runnable = () -> {
			try {
				ServerSocket serverSocket = new ServerSocket(port);
				while (true) {
					if (log) {
						logger.info("Waiting [" + port + "]...........");
					}
					Socket socket = serverSocket.accept();
					createHelthCheckConnection(socket,log);
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
				logger.info("Host error : " + e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.info("I/O error : " + e.getMessage());
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}
	
	public static void createHelthCheckConnection(Socket socket,boolean log) {
		new Thread( () -> {
			try {
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "US-ASCII"));
				while (true) {
					try {
						String msg = br.readLine();
						if (log) {
							logger.info(msg);
						}
						out.println(Thread.currentThread().getName() + " recevied[ " + msg + " ]");
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("Read line exception : " + e);
					}
					break;
				}
				br.close();
				out.close();
			} catch (Exception e) {
				logger.info("Exception" + e.getMessage());
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static void lunchServer(int port) {
		Runnable runnable = () -> {
			try {
				ServerSocket serverSocket = new ServerSocket(port);
				while (true) {
					logger.info("Waiting [" + port + "]...........");
					Socket socket = serverSocket.accept();
					createConnection(socket);
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
				logger.info("Host error : " + e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				logger.info("I/O error : " + e.getMessage());
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	public static void createConnection(Socket socket) {
		Runnable runnable = () -> {
			logger.info("Client connected : " + socket);
			try {
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "US-ASCII"));
				while (true) {
					try {
						String msg = br.readLine();
						logger.info(msg);
						if (msg==null) {
							out.println("Connection closed");
							out.flush();
							break;
						}
						out.println("Recevied[ " + msg + " ]");
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("Read line exception : " + e);
						break;
					}
				}
				br.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.info("I/O error : " + e.getMessage());
			}
			finally {
				try {
					socket.close();
					logger.info("Connection closed");
				} catch (IOException e) {
					e.printStackTrace();
					logger.info("Socket close exception : " + e.getMessage());
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}
	// BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
}
