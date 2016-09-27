package user_interface;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import client_manager.ClipboardManager;
import client_manager.LinKlipboardClient;
import contents.Contents;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.keyboard.event.GlobalKeyAdapter;
import lc.kra.system.keyboard.event.GlobalKeyEvent;
import server_manager.LinKlipboard;
import start_manager.GetInitDataFromServer;
import start_manager.StartToProgram;
import transfer_manager.FileReceiveDataToServer;
import transfer_manager.FileSendDataToServer;
import transfer_manager.ResponseHandler;
import transfer_manager.SendDataToServer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class UserInterfaceManager extends JFrame {
	private LinKlipboardClient client = new LinKlipboardClient(this);
	private TrayIconManager trayIcon = new TrayIconManager(this, client);

	private UserInterfacePage1 page1 = new UserInterfacePage1(client, this, trayIcon);
	private UserInterfacePage2 page2;

	private NicknameDialog inputNickNameDialog; // 사용자가 원하는 닉네임 입력 다이얼로그

	// private Container contentPane;

	private static final int endXOfFrame = 326;
	private static final int endYOfFrame = 430;

	public UserInterfaceManager() {
		setTitle("LinKlipboard");
		setSize(endXOfFrame, endYOfFrame);

		setting();

		trayIcon.addTrayIconInSystemTray();
		setHooker(client);
		// setHooker();

		this.setContentPane(page1);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
	}

	public void setting() {
		client.setting(this.getHistoryPanel(), this.getConnectionPanel());

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { // 윈도우 창의 X(닫기)를 누르면
				if (SettingPanel.notification) {
					trayIcon.showRunningMsg("LinKlipboard is running.");
					setVisible(false); // frame을 보이지 않게 함
				}
			}
		});
	}

	/** 단축키(초기값[Ctrl + Q] / [Alt + Q])를 누르면 서버에 데이터 전송, 최신 데이터 수신 */
	public static void setHooker(LinKlipboardClient client) {
		GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();

		keyboardHook.addKeyListener(new GlobalKeyAdapter() {
			@Override
			public void keyPressed(GlobalKeyEvent event) {
				char secondShortcutForSend = LinKlipboardClient.getSecondShortcutForSend().charAt(0);
				int getKeyCodeForSend = (int) secondShortcutForSend; // int
				// keyCode를
				// 얻어와
				// event,VK_
				// 꼴로
				// 만들어야함

				char secondShortcutForReceive = LinKlipboardClient.getSecondShortcutForReceive().charAt(0);
				int getKeyCodeForReceive = (int) secondShortcutForReceive;

				if (LinKlipboardClient.getFirstShortcutForSend().equals("Ctrl")) {
					if (event.isControlPressed()) {
						if (event.getVirtualKeyCode() == getKeyCodeForSend) {
							System.out.println("[Ctrl + " + secondShortcutForSend + "] is detected.");
							// 전송한다.
							if (ClipboardManager.getClipboardDataTypeNow() == LinKlipboard.FILE_TYPE) {
								new FileSendDataToServer(client).requestSendFileData();
							} else {
								new SendDataToServer(client).requestSendExpFileData();
							}
						}
					}
				} else if (LinKlipboardClient.getFirstShortcutForSend().equals("Alt")) {
					if (event.isMenuPressed()) {
						if (event.getVirtualKeyCode() == getKeyCodeForSend) {
							System.out.println("[Ctrl + " + secondShortcutForSend + "] is detected.");
							// 전송한다.
							if (ClipboardManager.getClipboardDataTypeNow() == LinKlipboard.FILE_TYPE) {
								new FileSendDataToServer(client).requestSendFileData();
							} else {
								new SendDataToServer(client).requestSendExpFileData();
							}
						}
					}
				} else {
					System.out.println("[setHookerForSend] 단축키 오류");
				}

				if (LinKlipboardClient.getFirstShortcutForReceive().equals("Ctrl")) {
					if (event.isControlPressed()) {
						if (event.getVirtualKeyCode() == getKeyCodeForReceive) {
							System.out.println("[Ctrl + " + secondShortcutForReceive + "] is detected.");
							receiveData(client);
						}
					}
				} else if (LinKlipboardClient.getFirstShortcutForReceive().equals("Alt")) {
					if (event.isMenuPressed()) {
						if (event.getVirtualKeyCode() == getKeyCodeForReceive) {
							System.out.println("[Alt + " + secondShortcutForReceive + "] is detected.");
							receiveData(client);
						}
					}
				} else {
					System.out.println("[setHookerForReceive] 단축키 오류");
				}
			}
		});
	}

	/** 최신으로 받은 Contents를 내 시스템 클립보드에 넣음 */
	public static void receiveData(LinKlipboardClient client) {
		client.settLatestContents();

		Contents latestContentsFromServer = client.getLatestContents();
		int latestContentsType = client.getLatestContents().getType();

		if (latestContentsType == LinKlipboard.FILE_TYPE) {
			new FileReceiveDataToServer(client).requestReceiveFileData();
		} else if (latestContentsType == LinKlipboard.STRING_TYPE) {
			ClipboardManager.writeClipboard(latestContentsFromServer, latestContentsType);
		} else if (latestContentsType == LinKlipboard.IMAGE_TYPE) {
			ClipboardManager.writeClipboard(latestContentsFromServer, latestContentsType);
		} else {
			System.out.println("[ConnectionPanel] File, String, Image 어디에도 속하지 않음");
		}
	}

	/** 다이얼로그에서 입력받은 닉네임을 처리 */
	public void dealInputnickName(String defaulNickname, UserInterfacePage2 page2) {
		this.page2 = page2;
		inputNickNameDialog = new NicknameDialog(this, "Set Nickname", defaulNickname, client, page1, page2);
		inputNickNameDialog.setSize(250, 130);
		inputNickNameDialog.setVisible(true);

		// String nickname = inputNickNameDialog.getInput();
		// client.setNickName(nickname);

		// new StartToProgram(client).requestChangeInfoToServer(nickname);

		// page2로 넘어간 후에 다이얼로그 띄우기
	}

	/** TrayIcon을 반환 */
	public TrayIconManager getTrayIcon() {
		return this.trayIcon;
	}

	public HistoryPanel getHistoryPanel() {
		return page1.getHistoryPanel();
	}

	public ConnectionPanel getConnectionPanel() {
		return page1.getConnectionPanel();
	}

	public static void main(String[] args) {
		new UserInterfaceManager();
	}
}

class NicknameDialog extends JDialog {
	JLabel label = new JLabel("Please enter your nickname.");
	JLabel errorLabel = new JLabel();
	JTextField inputNicknameField = new JTextField();
	JButton okButton = new JButton("OK");
	// static boolean permit = false;

	public NicknameDialog(JFrame jf, String title, String defaulNickname, LinKlipboardClient client,
			UserInterfacePage1 page1, UserInterfacePage2 page2) {
		super(jf, title, true);

		setLayout(null);

		label.setBounds(40, 10, 170, 20);
		// label.setBackground(Color.yellow);
		// label.setOpaque(true);
		add(label);

		errorLabel.setBounds(40, 70, 100, 20);
		errorLabel.setForeground(Color.red);
		add(errorLabel);

		inputNicknameField.setBounds(55, 40, 130, 25);
		inputNicknameField.setText(defaulNickname);
		inputNicknameField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();

				switch (keyCode) {
				case KeyEvent.VK_ENTER:
					if (getInput().length() == 0) {
						errorLabel.setText("닉네임 필수 입력");
					} else {
						// new
						// StartToProgram(client).requestChangeInfoToServer(getInput());
						// // 닉네임 중복처리

						// 승인되면
						LinKlipboardClient.setNickName(getInput());

						client.getOtherClients().add(getInput()); // 자신도 추가
						System.out.println("[page1] " + client.getOtherClients().size());
						page2.getConnectionPanel().updateGroupName();
						page2.getConnectionPanel().updateAccessGroup();
						jf.setContentPane(page2);
						setVisible(false);
						page1.initField();
					}
				}
			}
		});
		add(inputNicknameField);

		okButton.setBounds(150, 70, 80, 23);
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {

				System.out
						.println("nickname = " + inputNicknameField.getText() + inputNicknameField.getText().length());
				System.out.println("getInput" + getInput());

				   if (getInput().length() == 0) {
		                 errorLabel.setText("닉네임 필수 입력");
		              }
		              // 사용자가 뭐라도 입력했으면
		              else {
		                 System.out.println("[NicknameDialog] 변경요청 전");
		                 // 서버에 정보 변경 요청
		                 new StartToProgram(client).requestChangeInfoToServer(getInput());
		        
		                 // 전달받은 respoonse가 COMPLETE_APPLY이면
		                 if (ResponseHandler.getErrorCodeNum() == LinKlipboard.COMPLETE_APPLY) {
		                    // 접속한 사람들의 닉네임 벡터를 얻어온다.
		                    new GetInitDataFromServer(client, page2.getConnectionPanel());
		        
		                    // 클라이언트 닉네임을 세팅
		                    LinKlipboardClient.setNickName(getInput());
		        
		                    // 접속자에 자신도 추가
		                    client.getOtherClients().add(getInput());
		                    System.out.println("[NicknameDialog] 접속자 수: " +  client.getOtherClients().size());
		                    page2.getConnectionPanel().updateGroupName();
		                    page2.getConnectionPanel().updateAccessGroup();
		                    jf.setContentPane(page2);
		                    setVisible(false);
		                    page1.initField();
		                 }
		                 // 닉네임 중복오류 포함한 오류이면
		                 else if (ResponseHandler.getErrorCodeNum() == LinKlipboard.ERROR_DUPLICATED_NICKNAME) {
		                    errorLabel.setText("닉네임 중복");
		                 }
		              }
			}
		});
		add(okButton);

		setResizable(false);
	}

	/** 사용자가 입력한 닉네임 반환 */
	String getInput() {
		// if (inputNicknameField.getText().length() == 0) {
		// return null;
		// } else {
		return inputNicknameField.getText();
		// }
	}
}
