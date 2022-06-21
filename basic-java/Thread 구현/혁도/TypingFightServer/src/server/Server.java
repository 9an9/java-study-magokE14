package server;

import java.awt.FlowLayout;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.Attributes.Name;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Server{
	private String serverName;
	private int port;
	
	private ServerSocket serverSoket = null;
	private Thread serverThread = null;
	private Map<String, User> userMap = new HashMap<>();
	private String state;
	private Timer timer10;
	private Timer timer1000;
	private Problem problem;
	
	public Server(String serverName, int port) {
		this.serverName = serverName;
		this.port = port;

		drawWindow(serverName);
	}
	
	private void drawWindow(String title) {
		JFrame mainFrame = new JFrame();
		mainFrame.setTitle(title);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setSize(500, 500);
		mainFrame.getContentPane().setLayout(null);
		
		JLabel stateLabel = new JLabel("��������: ����");
		JButton startButton = new JButton("���� ����");
		JButton endButton = new JButton("���� ����");
		mainFrame.getContentPane().add(stateLabel);
		mainFrame.getContentPane().add(startButton);
		mainFrame.getContentPane().add(endButton);
		
		stateLabel.setBounds(5, 0, 120, 30);
		startButton.setBounds(5, 30, 120, 30);
		endButton.setBounds(130, 30, 120, 30);
		
		startButton.addActionListener(event->{
			start();
			stateLabel.setText("��������: ���� ��");
		});
		endButton.addActionListener(event->{
			stop();
			stateLabel.setText("��������: ����");
		});
		
		mainFrame.setVisible(true);
	}

	public void start() {
		try {
			this.serverSoket = new ServerSocket(this.port);
			this.state = "STEP2_WAIT";
		} catch (IOException e) {
			System.out.println("���� ������ �����߽��ϴ�.");
			e.printStackTrace();
			serverSoket = null;
			return;
		}
		
		if(serverThread != null) {
			System.out.println("������ �̹� ���� ���Դϴ�.");
			return;
		}
		
		System.out.println("���� ����");
		serverThread = new Thread(()->{
			while(!Thread.currentThread().isInterrupted()) {
				// System.out.println("���� ���� ��");

				timer10 = new Timer(10, e -> {
					System.out.println("���� ����: " + this.state);
					if(this.state.equals("STEP2_WAIT")) {
						boolean isAllReady = true;
						if(userMap.size() == 0) {
							isAllReady = false;
						}else {
							for(User user : userMap.values()) {
								if(!user.getState().equals("READY")) {
									isAllReady = false;
								}
							}
						}
						
						if(isAllReady) {
							this.state = "STEP2_5";	
						}
					}
				});
				timer10.start();

				timer1000 = new Timer(1000, e -> {
					if(this.state.equals("STEP2_5")) {
						this.state = "STEP2_4";
					}else if(this.state.equals("STEP2_4")) {
						this.state = "STEP2_3";
					}else if(this.state.equals("STEP2_3")) {
						this.state = "STEP2_2";
					}else if(this.state.equals("STEP2_2")) {
						this.state = "STEP2_1";
					}else if(this.state.equals("STEP2_1")) {
						this.state = "STEP3_START";
						problem = new Problem();
					}
				});
				timer1000.start();
				
				try {
						Socket socket = serverSoket.accept();
						Thread thread = new Thread(()->{
							try {
								ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
								ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
								Map<String, Object> getData = (HashMap<String, Object>)objectInputStream.readObject();
								boolean isEnter = false;
							
								String code = (String)getData.get("CODE");
								
								if(code.equals("ENTER")) {
									Map<String, Object> result = new HashMap<>();
									result.put("CODE", "ENTER");
									boolean hasName = false;
									InetAddress inetAddress = socket.getInetAddress();
									String ip = new String(inetAddress.getAddress());
									String name = (String)getData.get("NAME");
									
									if(userMap.size() >= 2) {
										result.put("STATE", "ERROR");
										result.put("ERROR", "OVER_USER_COUNT");
									}else{						
										for(User userValue : userMap.values()) {
											if(userValue.getName().equals(name)) {
												hasName = true;
											}
										}
										
										if(hasName) {
											result.put("STATE", "ERROR");
											result.put("ERROR", "DUPLICATION");
										}if(state.equals("GAME_START")) {
											result.put("STATE", "ERROR");
											result.put("ERROR", "STEP3_START");
										}
										else {
											userMap.put(ip, new User(socket, inetAddress, name));
											result.put("STATE", "SUCCESS");
											isEnter = true;
										}
									}
									objectOutputStream.writeObject(result);
									objectOutputStream.flush();
								}
								
								while(isEnter) {
									getData = (HashMap<String, Object>)objectInputStream.readObject();
									code = (String)getData.get("CODE");
									System.out.println("code: " + code);
									
									if(code.equals("USER_LIST")) {
										System.out.println("USERLIST����");
										List<Map<String, Object>> resultList = new ArrayList<>();
										for(User user : userMap.values()) {
											Map<String, Object> resultMap = new HashMap<>();
											resultMap.put("NAME", user.getName());
											resultMap.put("STATE", user.getState());
											resultList.add(resultMap);
										}
										objectOutputStream.writeObject(resultList);
										objectOutputStream.flush();
									}else if(code.equals("USER_STATE")) {
										System.out.println("USER_STATE����");
										InetAddress userInetAddress = socket.getInetAddress();
										String userIp = new String(userInetAddress.getAddress());
										User user = userMap.get(userIp);
										String userState = (String)getData.get("STATE"); 
										user.setState(userState);
										if(userState.equals("WAIT")) {
											state = "STEP2_WAIT";
										}
									}else if(code.equals("SERVER_STATE")) {
										System.out.println("SERVER_STATE����");
										Map<String, String> resultMap = new HashMap<>();
										resultMap.put("SERVER_STATE", this.state);
										objectOutputStream.writeObject(resultMap);
										objectOutputStream.flush();
									}else if(code.equals("EXIT")) {
										System.out.println("EXIT����");
										String name = (String)getData.get("NAME");
										String ip = null;
										for(User user : userMap.values()) {
											if(user.getName().equals(name)) {
												ip = user.getIp();
												break;
											}
										}
										if(ip != null) {
											userMap.remove(ip);	
										}
										isEnter = false;
									}else if(code.equals("PROBLEM")) {
										System.out.println("PROBLEM����");
										Map<String, Object> resultMap = new HashMap<>();
										resultMap.put("NUMBER", problem.getNumber());
										resultMap.put("PROBLEM", problem.getProblem());
										objectOutputStream.writeObject(resultMap);
										objectOutputStream.flush();
									}else if(code.equals("ANSWER")) {
										System.out.println("ANSWER����");
										String answer = (String)getData.get("ANSWER");
										
										if(answer.equals(problem.getProblem())) {
											String name = (String)getData.get("NAME");
											for(User user : userMap.values()) {
												if(user.getName().equals(name)) {
													int answerCnt = user.getAnswerCount() + 1;
													user.setAnswerCount(answerCnt);
													break;
												}
											}
											problem.answer();
										}
										
										if(problem.isFinish()) {
											state = "STEP4_GAME_OVER";
										}
									}else if(code.equals("ANSWER_USER_LIST")) {
										System.out.println("ANSWER_USER_LIST����");
										List<User> tmpAnswerUserList = new ArrayList<>();
										List<Map<String, Object>> answerUserList = new ArrayList<>();
										for(User user : userMap.values()) {
											tmpAnswerUserList.add(user);
										}
										Collections.sort(tmpAnswerUserList, new Comparator<User>() {
											@Override
											public int compare(User o1, User o2) {
												return o2.getAnswerCount() - o1.getAnswerCount();
											}
										});
										for(User user : tmpAnswerUserList) {
											Map<String, Object> um = new HashMap<>();
											um.put("NAME", user.getName());
											um.put("ANSWER_COUNT", user.getAnswerCount());
											answerUserList.add(um);
										}
										
										Map<String, Object> resultMap = new HashMap<>();
										resultMap.put("ANSWER_USER_LIST", answerUserList);
										objectOutputStream.writeObject(resultMap);
										objectOutputStream.flush();
									}else if(code.equals("IS_GAME_OVER")) {
										System.out.println("IS_GAME_OVER����");
										Map<String, Boolean> resultMap = new HashMap<>();
										if(state.equals("STEP3_START")) {
											resultMap.put("IS_GAME_OVER", false);
										}else {
											resultMap.put("IS_GAME_OVER", true);
										}
										objectOutputStream.writeObject(resultMap);
										objectOutputStream.flush();
									}else if(code.equals("STEP3_EXIT")) {
										System.out.println("STEP3_EXIT����");
										String name = (String)getData.get("NAME");
										String ip = null;
										for(User user : userMap.values()) {
											if(user.getName().equals(name)) {
												ip = user.getIp();
												break;
											}
										}
										if(ip != null) {
											userMap.remove(ip);	
										}
										
										if(userMap.size() == 0) {
											state = "STEP2_WAIT";
										}
										
										isEnter = false;
									}else if(code.equals("STEP4_EXIT")) {
										System.out.println("STEP4_EXIT����");
										String name = (String)getData.get("NAME");
										String ip = null;
										for(User user : userMap.values()) {
											if(user.getName().equals(name)) {
												ip = user.getIp();
												break;
											}
										}
										if(ip != null) {
											userMap.remove(ip);	
										}
										
										if(userMap.size() == 0) {
											state = "STEP2_WAIT";
										}
										
										isEnter = false;
									}
								}
							}catch(Exception e) {
								e.printStackTrace();
							}
						});
						thread.run();
				} catch (Exception e) {
					if(e.getClass().getName().equals("java.net.SocketException")) {
						System.out.println("���� ����");
					}else {
						System.out.println("Ŭ���̾�Ʈ ������ �޴� �������� ������ �߻��߽��ϴ�.");
						e.printStackTrace();	
					}
				}
			}
			
			try {
				timer10.stop();
				timer1000.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
			serverThread = null;
		});
		
		serverThread.start();
	}

	public void stop() {
		serverThread.interrupt();
		try {
			serverSoket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
