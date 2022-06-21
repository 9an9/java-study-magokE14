package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Client {
	private final int PORT = 2337;
	private Socket socket;
	private ObjectOutputStream objectOutputStream;
	private ObjectInputStream objectInputStream;
	
	private String name;
	private String state;

	JFrame mainFrame;
	
	//Step1
	JLabel nameLabel;
	JTextField nameTextField;
	JButton startButton;
	
	//Step2 - ���� ��� ��
	JLabel watingRoomLabel;
	JLabel playerListLabel;
	JLabel playerStateLabel;
	JLabel myNameLabel;
	Box playerListBox;
	JButton readyButton;
	JButton cancelButton;
	JButton exitButton;
	JLabel stateLabel;
	
	Timer step2Timer;
	
	//Step3 - ���� ����
	JLabel wordLabel;
	JTextField wordTextField;
	JButton gameExitButton;
	Box gamePlayerListBox;
	JLabel gamePlayerStateLabel;
	
	Timer step3Timer;
	
	//Step4 - ���
	JLabel resultTitleLabel;
	JLabel resultSubLabel;
	Box resultListBox;
	JButton resultExitButton;
	
	//Step5 - ���� ����
	
	//Step6 - Step2�� �̵�
	
	
	public Client() {
		drawWindow();
	}
	
	private void drawWindow() {
		mainFrame = new JFrame();
		mainFrame.setTitle("Ÿ�� ���");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setSize(500, 500);
		mainFrame.getContentPane().setLayout(null);
		
		// step1
		nameLabel = new JLabel("�̸�");
		nameLabel.setBounds(5, 30, 30, 30);
		mainFrame.getContentPane().add(nameLabel);
		
		nameTextField = new JTextField();
		nameTextField.setBounds(33, 30, 150, 30);
		mainFrame.getContentPane().add(nameTextField);
		
		startButton = new JButton("�����ϱ�");
		startButton.setBounds(185, 30, 150, 30);
		startButton.addActionListener(event->{
			goToStep2();
		});
		mainFrame.getContentPane().add(startButton);
		
		// step2
		watingRoomLabel = new JLabel("* ���� *");
		watingRoomLabel.setBounds(5, 5, 80, 30);
		mainFrame.getContentPane().add(watingRoomLabel);
		
		playerListLabel = new JLabel("[���� ���]");
		playerListLabel.setBounds(5, 40, 80, 30);
		mainFrame.getContentPane().add(playerListLabel);
		
		playerStateLabel = new JLabel("<html>�غ� / <font color='blue'>�غ�Ϸ�</html>");
		playerStateLabel.setBounds(75, 38, 130, 30);
		mainFrame.getContentPane().add(playerStateLabel);
		
		playerListBox = Box.createVerticalBox();
		playerListBox.setBounds(5, 80, 80, 100);
		mainFrame.getContentPane().add(playerListBox);
		
		readyButton = new JButton("�غ��ϱ�");
		readyButton.setBounds(325, 420, 150, 30);
		readyButton.addActionListener(event->{
			readyBtn();
		});
		mainFrame.getContentPane().add(readyButton);
		
		cancelButton = new JButton("����ϱ�");
		cancelButton.setBounds(325, 420, 150, 30);
		cancelButton.addActionListener(event->{
			cancelBtn();
		});
		mainFrame.getContentPane().add(cancelButton);
		
		exitButton = new JButton("������");
		exitButton.setBounds(325, 5, 150, 30);
		exitButton.addActionListener(event->{
			goToStep1From2();
		});
		mainFrame.getContentPane().add(exitButton);
		
		stateLabel = new JLabel("��� �ÿ��̾ �غ� ��ư�� ������ �����մϴ�.");
		stateLabel.setBounds(25, 420, 600, 30);
		mainFrame.getContentPane().add(stateLabel);
		
		// step3
		wordLabel = new JLabel("��������������������");
		wordLabel.setBounds(180, 120, 250, 50);
		mainFrame.getContentPane().add(wordLabel);
		
		wordTextField = new JTextField();
		wordTextField.setBounds(120, 280, 250, 30);
		wordTextField.addActionListener(event->{
			sendAnswer();
		});
		mainFrame.getContentPane().add(wordTextField);
		
		gameExitButton = new JButton("������");
		gameExitButton.setBounds(325, 5, 150, 30);
		gameExitButton.addActionListener(event->{
			goToStep1From3();
		});
		mainFrame.getContentPane().add(gameExitButton);
		
		gamePlayerStateLabel = new JLabel("<html>�÷��̾� / ���� ����</html>");
		gamePlayerStateLabel.setBounds(5, 5, 130, 30);
		mainFrame.getContentPane().add(gamePlayerStateLabel);
		
		gamePlayerListBox = Box.createVerticalBox();
		gamePlayerListBox.setBounds(5, 30, 80, 100);
		mainFrame.getContentPane().add(gamePlayerListBox);
		
		// step4
		resultTitleLabel = new JLabel("���");
		resultTitleLabel.setBounds(230, 120, 130, 30);
		mainFrame.getContentPane().add(resultTitleLabel);
		
		resultSubLabel = new JLabel("�÷��̾� / ���� ����");
		resultSubLabel.setBounds(180, 140, 130, 30);
		mainFrame.getContentPane().add(resultSubLabel);
		
		resultListBox = Box.createVerticalBox();
		resultListBox.setBounds(180, 170, 130, 200);
		mainFrame.getContentPane().add(resultListBox);
		
		resultExitButton = new JButton("������");
		resultExitButton.setBounds(325, 5, 150, 30);
		resultExitButton.addActionListener(event->{
			goToStep1From4();
		});
		mainFrame.getContentPane().add(resultExitButton);
				 
		mainFrame.setVisible(true);
		
		setStep1();
	}
	
	private void setStep1() {
		this.state = "NOT_ENTER";
		
		// step1
		nameLabel.setVisible(true);
		nameTextField.setVisible(true);
		startButton.setVisible(true);
//		nameLabel.setVisible(false);
//		nameTextField.setVisible(false);
//		startButton.setVisible(false);

		// step2
		watingRoomLabel.setVisible(false);
		playerListLabel.setVisible(false);
		playerStateLabel.setVisible(false);
		playerListBox.setVisible(false);
		readyButton.setVisible(false);
		cancelButton.setVisible(false);
		exitButton.setVisible(false);
		stateLabel.setVisible(false);
		
		// step3
		wordLabel.setVisible(false);
		wordTextField.setVisible(false);
		gameExitButton.setVisible(false);
		gamePlayerStateLabel.setVisible(false);
		gamePlayerListBox.setVisible(false);
		
		// step4
		resultTitleLabel.setVisible(false);
		resultSubLabel.setVisible(false);
		resultListBox.setVisible(false);
		resultExitButton.setVisible(false);
	}
	
	private void goToStep1From2() {
		try {
			Map<String, Object> sendCode = new HashMap<>();
			sendCode.put("CODE", "EXIT");
			sendCode.put("NAME", this.name);
			objectOutputStream.writeObject(sendCode);
			objectOutputStream.flush();
			setStep1();
			step2Timer.stop();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void goToStep2() {
		if(nameTextField.getText().length() < 2) {
			JOptionPane.showMessageDialog(mainFrame, "�̸��� �α��� �̻����� �������ּ���.");
			return;
		}
		
		nameLabel.setVisible(false);
		nameTextField.setVisible(false);
		startButton.setVisible(false);
		this.name = nameTextField.getText();
		
		try {
			if(socket != null) {
				socket.close();
			}
			if(objectOutputStream != null) {
				objectOutputStream.close();
			}
			if(objectInputStream != null) {
				objectInputStream.close();
			}
			
			socket = new Socket("127.0.0.1", PORT);
			
			System.out.println("���� ���� �Ϸ�");

			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

			Map<String, Object> sendData = new HashMap<>();
			sendData.put("CODE", "ENTER");
			sendData.put("NAME", this.name);
			
			objectOutputStream.writeObject(sendData);
			objectOutputStream.flush();

			objectInputStream = new ObjectInputStream(socket.getInputStream());
			Map<String, Object> receiveData = (HashMap<String, Object>)objectInputStream.readObject();
			String code = (String)receiveData.get("CODE");
			if((code).equals("ENTER")) {
				String state = (String)receiveData.get("STATE");
				if(state.equals("ERROR")) {
					String errorCode = (String)receiveData.get("ERROR");
					if(errorCode.equals("OVER_USER_COUNT")) {
						JOptionPane.showMessageDialog(mainFrame, "�����ڰ� �����ϴ�. ����� �ٽ� �õ����ּ���.");
					}else if(errorCode.equals("DUPLICATION")){
						JOptionPane.showMessageDialog(mainFrame, "�ߺ��� �̸��Դϴ�.");
					}else if(errorCode.equals("GAME_START")){
						JOptionPane.showMessageDialog(mainFrame, "�̹� ������ ���۵Ǿ����ϴ�. ����� �ٽ� �õ����ּ���.");
					}
					setStep1();
				}else {
					setStep2();
				}
			}
		}catch (Exception e) {
			System.out.println("���� ���� ����");
			e.printStackTrace();
			return;
		}
	}
	
	private void setStep2() {
		this.state = "WAIT";
		
		// step1
		nameLabel.setVisible(false);
		nameTextField.setVisible(false);
		startButton.setVisible(false);

		// step2
		watingRoomLabel.setVisible(true);
		playerListLabel.setVisible(true);
		playerStateLabel.setVisible(true);
		playerListBox.setVisible(true);
		readyButton.setVisible(true);
		cancelButton.setVisible(true);
		exitButton.setVisible(true);
		stateLabel.setVisible(true);
		
		// step3
		wordLabel.setVisible(false);
		wordTextField.setVisible(false);
		gameExitButton.setVisible(false);
		gamePlayerStateLabel.setVisible(false);
		gamePlayerListBox.setVisible(false);
		
		// step4
		resultTitleLabel.setVisible(false);
		resultSubLabel.setVisible(false);
		resultListBox.setVisible(false);
		resultExitButton.setVisible(false);
		
		runStep2();
	}
	
	private void readyBtn() {
		this.state = "READY";
		this.readyButton.setVisible(false);
		this.cancelButton.setVisible(true);
	}
	
	private void cancelBtn() {
		this.state = "WAIT";
		this.readyButton.setVisible(true);
		this.cancelButton.setVisible(false);
	}
	
	private void runStep2() {
		step2Timer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(state.equals("WAIT") || state.equals("READY") ) {
					try {
						Map<String, Object> sendCode = new HashMap<>();
						
						// ���� ��� �޾ƿ���
						playerListBox.removeAll();
						
						sendCode.put("CODE", "USER_LIST");
						objectOutputStream.writeObject(sendCode);
						objectOutputStream.flush();
						
						List<Map<String, Object>> userList = (List)objectInputStream.readObject();
						
						for(Map<String, Object> userInfo : userList) {
							String name = (String)userInfo.get("NAME");
							String state = (String)userInfo.get("STATE");
							
							JLabel userLabel;
							if(state.equals("READY")) {
								userLabel = new JLabel("<html><font color='blue'>" + name + "</html>");
							}else {
								userLabel = new JLabel(name);
							}
							playerListBox.add(userLabel);
						}
						
						// Ŭ���̾�Ʈ ���� ������
						Map<String, Object> sendCode2 = new HashMap<>();
						sendCode2.put("CODE", "USER_STATE");
						sendCode2.put("STATE", state);
						objectOutputStream.writeObject(sendCode2);
						objectOutputStream.flush();
						
						// ���� ���� �޾ƿ���
						Map<String, Object> sendCode3 = new HashMap<>();
						sendCode3.put("CODE", "SERVER_STATE");
						objectOutputStream.writeObject(sendCode3);
						objectOutputStream.flush();
						
						Map<String, String> receiveData = (Map<String, String>)objectInputStream.readObject();
						String serverState = receiveData.get("SERVER_STATE");
						if(serverState.equals("STEP2_START")) {
							setStep3();
						}else {
							String label = null;
							System.out.println("serverState: " + serverState);
							if(serverState.equals("STEP2_WAIT")) {
								label = "��� �ÿ��̾ �غ� ��ư�� ������ �����մϴ�.";
							}else if(serverState.equals("STEP2_5")){
								label = "5�� �ڿ� �����մϴ�.";
							}else if(serverState.equals("STEP2_4")){
								label = "4�� �ڿ� �����մϴ�.";
							}else if(serverState.equals("STEP2_3")){
								label = "3�� �ڿ� �����մϴ�.";
							}else if(serverState.equals("STEP2_2")){
								label = "2�� �ڿ� �����մϴ�.";
							}else if(serverState.equals("STEP2_1")){
								label = "1�� �ڿ� �����մϴ�.";
							}
							stateLabel.setText(label);
							
							if(serverState.equals("STEP3_START")){
								setStep3();
							}
						}
					} catch (Exception ea) {
						ea.printStackTrace();
					}
				}
			}
		});
		step2Timer.start();
	}
	
	private void setStep3() {
		step2Timer.stop();
		this.state = "START";
		
		// step1
		nameLabel.setVisible(false);
		nameTextField.setVisible(false);
		startButton.setVisible(false);

		// step2
		watingRoomLabel.setVisible(false);
		playerListLabel.setVisible(false);
		playerStateLabel.setVisible(false);
		playerListBox.setVisible(false);
		readyButton.setVisible(false);
		cancelButton.setVisible(false);
		exitButton.setVisible(false);
		stateLabel.setVisible(false);
		
		// step3
		wordLabel.setVisible(true);
		wordTextField.setVisible(true);
		gameExitButton.setVisible(true);
		gamePlayerStateLabel.setVisible(true);
		gamePlayerListBox.setVisible(true);
		
		// step4
		resultTitleLabel.setVisible(false);
		resultSubLabel.setVisible(false);
		resultListBox.setVisible(false);
		resultExitButton.setVisible(false);
		
		runStep3();
	}
	
	private void runStep3() {
		step3Timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Map<String, Object> receiveData = null;
					
					// ��� ������ Ǯ������ ��û
					Map<String, Object> sendCodeA = new HashMap<>();
					sendCodeA.put("CODE", "IS_GAME_OVER");
					objectOutputStream.writeObject(sendCodeA);
					objectOutputStream.flush();
					
					receiveData = (Map<String, Object>)objectInputStream.readObject();
					boolean isGameOver = (boolean)receiveData.get("IS_GAME_OVER");
					
					if(isGameOver) {
						goToStep4();
					}else {
						// ���� ��û�ϱ�
						Map<String, Object> sendCode = new HashMap<>();
						sendCode.put("CODE", "PROBLEM");
						objectOutputStream.writeObject(sendCode);
						objectOutputStream.flush();
						
						// ���� �ޱ�
						receiveData = (Map<String, Object>)objectInputStream.readObject();
						String number = String.valueOf((int)receiveData.get("NUMBER"));
						String problem = (String)receiveData.get("PROBLEM");
						wordLabel.setText(number + ". " + problem);
						
						// ���� ���� ���� ��û�ϱ�
						Map<String, Object> sendCode1 = new HashMap<>();
						sendCode1.put("CODE", "ANSWER_USER_LIST");
						objectOutputStream.writeObject(sendCode1);
						objectOutputStream.flush();
						
						// ���� ���� ���� �ޱ�
						gamePlayerListBox.removeAll();
						receiveData = (Map<String, Object>)objectInputStream.readObject();
						List<Map<String, Object>> answerUserList = (List)receiveData.get("ANSWER_USER_LIST");
						for(Map<String, Object> userAnswerInfo : answerUserList) {
							String userName = (String)userAnswerInfo.get("NAME");
							int userAnswerCount = (int)userAnswerInfo.get("ANSWER_COUNT");
							JLabel userLabel = new JLabel(userName + " / " + userAnswerCount);
							gamePlayerListBox.add(userLabel);
						}
					}
				}catch(Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		step3Timer.start();
	}
	
	private void sendAnswer() {
		try {
			// �������� �� ����
			Map<String, Object> sendCode = new HashMap<>();
			sendCode.put("CODE", "ANSWER");
			sendCode.put("ANSWER", wordTextField.getText());
			sendCode.put("NAME", name);
			objectOutputStream.writeObject(sendCode);
			objectOutputStream.flush();
			wordTextField.setText("");
		}catch(Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private void goToStep1From3() {
		try {
			Map<String, Object> sendCode = new HashMap<>();
			sendCode.put("CODE", "STEP3_EXIT");
			sendCode.put("NAME", this.name);
			objectOutputStream.writeObject(sendCode);
			objectOutputStream.flush();
			setStep1();
			step3Timer.stop();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void goToStep4() {
		step3Timer.stop();
		state = "STEP4";
		setStep4();
	}
	
	private void setStep4() {
		step2Timer.stop();
		this.state = "START";
		
		// step1
		nameLabel.setVisible(false);
		nameTextField.setVisible(false);
		startButton.setVisible(false);

		// step2
		watingRoomLabel.setVisible(false);
		playerListLabel.setVisible(false);
		playerStateLabel.setVisible(false);
		playerListBox.setVisible(false);
		readyButton.setVisible(false);
		cancelButton.setVisible(false);
		exitButton.setVisible(false);
		stateLabel.setVisible(false);
		
		// step3
		wordLabel.setVisible(false);
		wordTextField.setVisible(false);
		gameExitButton.setVisible(false);
		gamePlayerStateLabel.setVisible(false);
		gamePlayerListBox.setVisible(false);
		
		// step4
		resultTitleLabel.setVisible(true);
		resultSubLabel.setVisible(true);
		resultListBox.setVisible(true);
		resultExitButton.setVisible(true);
		
		runStep4();
	}
	
	private void runStep4() {
		try {
			Map<String, Object> receiveData = null;
			// ���� ���� ���� ��û�ϱ�
			Map<String, Object> sendCode1 = new HashMap<>();
			sendCode1.put("CODE", "ANSWER_USER_LIST");
			objectOutputStream.writeObject(sendCode1);
			objectOutputStream.flush();
			
			// ���� ���� ���� �ޱ�
			resultListBox.removeAll();
			receiveData = (Map<String, Object>)objectInputStream.readObject();
			List<Map<String, Object>> answerUserList = (List)receiveData.get("ANSWER_USER_LIST");
			for(Map<String, Object> userAnswerInfo : answerUserList) {
				String userName = (String)userAnswerInfo.get("NAME");
				int userAnswerCount = (int)userAnswerInfo.get("ANSWER_COUNT");
				JLabel userLabel = new JLabel(userName + " / " + userAnswerCount);
				resultListBox.add(userLabel);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void goToStep1From4() {
		try {
			Map<String, Object> sendCode = new HashMap<>();
			sendCode.put("CODE", "STEP4_EXIT");
			sendCode.put("NAME", this.name);
			objectOutputStream.writeObject(sendCode);
			objectOutputStream.flush();
			setStep1();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
