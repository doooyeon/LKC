package user_interface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import client_manager.LinKlipboardClient;
import server_manager.LinKlipboard;
import transfer_manager.ResponseHandler;

public class UserInterfacePage1 extends BasePanel {
	private UserInterfacePage2 page2;
	
	private JLabel mainImgLabel = new JLabel(); // 메인 이미지
	private JLabel groupNameLabel = new JLabel();
	private JLabel groupPWLabel = new JLabel();
	private JTextField groupNameField = new JTextField(); // 그룹이름 필드
	private JPasswordField groupPassWordField = new JPasswordField(); // 그룹패스워드 필드
	private JButton createButton = new JButton(); // 생성버튼
	private JButton joinButton = new JButton(); // 접속버튼
	private JLabel responseState = new JLabel(); // 오류확인 Label

	private String groupName; // 사용자가 입력한 그룹이름
	private String groupPW; // 사용자가 입력한 그룹패스워드
	
	public UserInterfacePage1(LinKlipboardClient client, UserInterfaceManager main, TrayIconManager trayIcon) {
		super(client, main);

		this.page2 = new UserInterfacePage2(client, trayIcon, main, this);
		
		setLayout(null);
		setSize(320, 400);
		
		initField();
		client.setScreen(this);

		initComponents();
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		mainImgLabel.setIcon(new ImageIcon("image/mainImage.png")); 
//		mainImgLabel.setBackground(new Color(255, 132, 0));
//		mainImgLabel.setOpaque(true);
		mainImgLabel.setBounds(0, 0, 320, 250);
		add(mainImgLabel);

		groupNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		groupNameLabel.setText("Group Name");
		groupNameLabel.setBounds(25, 275, 100, 20);
		// groupNameLabel.setBackground(Color.GRAY);
		// groupNameLabel.setOpaque(true);
		add(groupNameLabel);

		groupPWLabel.setHorizontalAlignment(SwingConstants.CENTER);
		groupPWLabel.setText("Group Password");
		groupPWLabel.setBounds(25, 305, 100, 20);
		// groupPWLabel.setBackground(Color.GRAY);
		// groupPWLabel.setOpaque(true);
		add(groupPWLabel);

		groupNameField.setText("");
		groupNameField.setBounds(135, 275, 150, 20);
		add(groupNameField);

		groupPassWordField.setText("");
		groupPassWordField.setBounds(135, 305, 150, 20);
		
		groupPassWordField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();

				switch (keyCode) {
				case KeyEvent.VK_ENTER:
					if (checkAllInputData() == true) {
						responseState.setText("");
						groupName = groupNameField.getText();
						groupPW = new String(groupPassWordField.getPassword());

						// 1. 그룹정보 세팅
						client.setGroupInfo(groupName, groupPW);
						// 2. 그룹생성을 요청
						client.createGroup();

						if (ResponseHandler.getErrorCodeNum() == LinKlipboard.ACCESS_PERMIT)
							main.dealInputnickName(client.getNickName(), page2); // 닉네임 설정
					}
				}
			}
		});
		
		add(groupPassWordField);
		
		responseState.setText("");
		responseState.setBounds(87, 335, 150, 20);
		responseState.setHorizontalAlignment(JLabel.CENTER);
//		responseState.setBackground(Color.GRAY);
		responseState.setOpaque(true);
		add(responseState);

		createButton.setText("Create");
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				createButtonActionPerformed(evt);
			}
		});
		createButton.setBounds(160 - 15 - 80, 365, 80, 23);
		add(createButton);

		joinButton.setText("Join");
		joinButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				joinButtonActionPerformed(evt);
			}
		});
		joinButton.setBounds(160 + 15, 365, 80, 23);
		add(joinButton);
	}
	
	public HistoryPanel getHistoryPanel() {
		return page2.getHistoryPanel();
	}
	
	public ConnectionPanel getConnectionPanel() {
		return page2.getConnectionPanel();
	}


	/**
	 * 그룹을 생성, 접속하기 위해 정보를 모두 입력했는지 확인. 모두 입력 했으면 return true;
	 */
	private boolean checkAllInputData() {
		if (groupNameField.getText().length() == 0 || groupPassWordField.getPassword().length == 0) {
			responseState.setText("필요 정보 입력");
			return false;
		}
		return true;
	}

	/** 입력 필드를 초기화 */
	public void initField() {
		groupNameField.setText("");
		groupPassWordField.setText("");
		responseState.setText("");
		
	}

	// 가운데 정렬(외부에서 사용?) -> LinKlipboardClient에 이 Panel을 넘겨주고 오류코드찍어야함
	/** 인터페이스에 오류 정보 표시 */
	public void updateErrorState(String errorMsg) {
		responseState.setText(errorMsg);
	}

	private void createButtonActionPerformed(ActionEvent evt) {
		if (checkAllInputData() == true) {
			responseState.setText("");
			groupName = groupNameField.getText();
			groupPW = new String(groupPassWordField.getPassword());

			// 1. 그룹정보 세팅
			client.setGroupInfo(groupName, groupPW);
			// 2. 그룹생성을 요청
			client.createGroup();
			
			if(ResponseHandler.getErrorCodeNum() == LinKlipboard.ACCESS_PERMIT)
				main.dealInputnickName(this.client.getNickName(), page2); // 닉네임 설정
			}
	}

	protected void joinButtonActionPerformed(ActionEvent evt) {
		if (checkAllInputData() == true) {
			responseState.setText("");
			groupName = groupNameField.getText();
			groupPW = new String(groupPassWordField.getPassword());

			// 1. 그룹정보 세팅
			client.setGroupInfo(groupName, groupPW);
			// 2. 그룹접속을 요청
			client.joinGroup();
			
			if(ResponseHandler.getErrorCodeNum() == LinKlipboard.ACCESS_PERMIT)
				main.dealInputnickName(this.client.getNickName(), page2); // 닉네임 설정
			}
	}
}



