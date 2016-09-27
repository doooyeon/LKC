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
//	JLabel responseState = new JLabel("ERROR STATE"); // ����Ȯ�� Label
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
//	/** ����Ű(�ʱⰪ[Ctrl + T])�� ������ ������ ������ ���� */
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
//						// ������ Ŭ����
//					}
//				}
//			}
//		});
//	}
//
//	/** components�� ��ġ ���� */
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
//		// ���� Ȯ�� Label
//		responseState.setBounds(X2 + 20, Y2 + 150, 200, 30);
//		responseState.setBackground(Color.yellow);
//		responseState.setOpaque(true);
//		add(responseState);
//
//		// ���� ��ư ����, ����
//		createGroupBtn.setBounds(X1, 400, 100, 40);
//		add(createGroupBtn);
//
//		// CREATE ��ư�� ���� ó��
//		createGroupBtn.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (groupNameField.getText().isEmpty() || groupPassWordField.getText().isEmpty()) {
//					responseState.setText("�ʿ����� �Է�");
//					return;
//				}
//
//				// 1. LinKlipboardClient ����
//				createClient(groupNameField.getText(), groupPassWordField.getText());
//				// 2. �׷������ ��û
//				client.createGroup();
//			}
//		});
//
//		// ���� ��ư ����, ����
//		joinGroupBtn.setBounds(X1 + 150, 400, 100, 40);
//		add(joinGroupBtn);
//
//		// JOIN ��ư�� ���� ó��
//		joinGroupBtn.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (groupNameField.getText().isEmpty() || groupPassWordField.getText().isEmpty()) {
//					responseState.setText("�ʿ����� �Է�");
//					return;
//				}
//
//				// 1. LinKlipboardClient ����
//				createClient(groupNameField.getText(), groupPassWordField.getText());
//				// 2. �׷������� ��û
//				client.joinGroup();
//			}
//		});
//	}
//
//	/** Ŭ���̾�Ʈ ���� */
//	private void createClient(String groupName, String groupPassword) {
//		client = new LinKlipboardClient(groupName, groupPassword, this);
//	}
//
//	/** �������̽��� ���� ���� ǥ�� */
//	public void updateErrorState(String response) {
//		responseState.setText(response);
//	}
//
//	/** �Է� �ʵ� �ʱ�ȭ */
//	public void initInputField() {
//		groupNameField.setText("");
//		groupPassWordField.setText("");
//	}
//
////	/** Ŭ���̾�Ʈ ���� �ʱ�ȭ */
////	public void initClientInfo(LinKlipboardClient client) {
////		client.initGroupInfo();
////	}
//
//	public static void main(String[] args) {
//		new Past_UserInterface();
//	}
//}