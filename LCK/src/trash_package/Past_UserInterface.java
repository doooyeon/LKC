package trash_package;
//package user_interface;
//
//import java.awt.Color;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JTextField;
//
//import client_manager.LinKlipboardClient;
//import lc.kra.system.keyboard.GlobalKeyboardHook;
//import lc.kra.system.keyboard.event.GlobalKeyAdapter;
//import lc.kra.system.keyboard.event.GlobalKeyEvent;
//
//public class Past_UserInterface extends JFrame {
//
//	private LinKlipboardClient client;
//
//	JLabel groupNameLabel = new JLabel("Group Name");
//	JLabel groupPassWordLabel = new JLabel("Password");
//
//	JTextField groupNameField = new JTextField();
//	JTextField groupPassWordField = new JTextField();
//
//	JLabel responseState = new JLabel("ERROR STATE"); // 오류확인 Label
//
//	JButton createGroupBtn = new JButton("CREATE");
//	JButton joinGroupBtn = new JButton("JOIN");
//
//	public Past_UserInterface() {
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setHooker();
//		setComponents();
//		setSize(340, 500);
//		setVisible(true);
//	}
//
//	/** 단축키(초기값[Ctrl + T])를 누르면 서버에 데이터 전송 */
//	private void setHooker() {
//		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
//
//		keyboardHook.addKeyListener(new GlobalKeyAdapter() {
//			@Override
//			public void keyPressed(GlobalKeyEvent event) {
//				if (event.isControlPressed()) {
//					if (event.getVirtualKeyCode() == event.VK_T) {
//						System.out.println("[Ctrl + T] is detected.");
//						// client.sendDateToServer();
//						// 도연이 클래스
//					}
//				}
//			}
//		});
//	}
//
//	/** components의 위치 지정 */
//	private void setComponents() {
//		setLayout(null);
//
//		int X1 = 30;
//		int Y1 = 10;
//		int X2 = 40;
//		int Y2 = 70;
//
//		groupNameLabel.setBounds(X2, Y2, 150, 30);
//		groupPassWordLabel.setBounds(X2, Y2 + 40, 150, 30);
//
//		groupNameField.setBounds(X2 + 80, Y2, 150, 30);
//		groupPassWordField.setBounds(X2 + 80, Y2 + 40, 150, 30);
//
//		add(groupNameLabel);
//		add(groupPassWordLabel);
//
//		add(groupNameField);
//		add(groupPassWordField);
//
//		// 오류 확인 Label
//		responseState.setBounds(X2 + 20, Y2 + 150, 200, 30);
//		responseState.setBackground(Color.yellow);
//		responseState.setOpaque(true);
//		add(responseState);
//
//		// 생성 버튼 생성, 부착
//		createGroupBtn.setBounds(X1, 400, 100, 40);
//		add(createGroupBtn);
//
//		// CREATE 버튼에 대한 처리
//		createGroupBtn.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (groupNameField.getText().isEmpty() || groupPassWordField.getText().isEmpty()) {
//					responseState.setText("필요정보 입력");
//					return;
//				}
//
//				// 1. LinKlipboardClient 생성
//				createClient(groupNameField.getText(), groupPassWordField.getText());
//				// 2. 그룹생성을 요청
//				client.createGroup();
//			}
//		});
//
//		// 접속 버튼 생성, 부착
//		joinGroupBtn.setBounds(X1 + 150, 400, 100, 40);
//		add(joinGroupBtn);
//
//		// JOIN 버튼에 대한 처리
//		joinGroupBtn.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (groupNameField.getText().isEmpty() || groupPassWordField.getText().isEmpty()) {
//					responseState.setText("필요정보 입력");
//					return;
//				}
//
//				// 1. LinKlipboardClient 생성
//				createClient(groupNameField.getText(), groupPassWordField.getText());
//				// 2. 그룹접속을 요청
//				client.joinGroup();
//			}
//		});
//	}
//
//	/** 클라이언트 생성 */
//	private void createClient(String groupName, String groupPassword) {
//		client = new LinKlipboardClient(groupName, groupPassword, this);
//	}
//
//	/** 인터페이스에 오류 정보 표시 */
//	public void updateErrorState(String response) {
//		responseState.setText(response);
//	}
//
//	/** 입력 필드 초기화 */
//	public void initInputField() {
//		groupNameField.setText("");
//		groupPassWordField.setText("");
//	}
//
////	/** 클라이언트 정보 초기화 */
////	public void initClientInfo(LinKlipboardClient client) {
////		client.initGroupInfo();
////	}
//
//	public static void main(String[] args) {
//		new Past_UserInterface();
//	}
//}