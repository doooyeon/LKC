package client_manager;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

import contents.Contents;
import datamanage.History;
import server_manager.LinKlipboard;
import start_manager.StartToProgram;
import user_interface.ConnectionPanel;
import user_interface.HistoryPanel;
import user_interface.SettingPanel;
import user_interface.UserInterfaceManager;
import user_interface.UserInterfacePage1;

public class LinKlipboardClient {
	private UserInterfaceManager main;
	private UserInterfacePage1 screen1; // ����� �������̽�(for ���� ���� ǥ��)

	private static String groupName; // �׷��̸�
	private String password; // �н�����
	private static String nickName = null; // �г���
	private static int portNum; // ������ ����� ��Ʈ��ȣ
	private static Vector<String> otherClients = new Vector<String>(); // ���� �׷�
																		// �����ڵ���
																		// �г���

	private static String firstShortcutForSend = "Ctrl"; // ���� ù��° ����Ű
	private static String secondShortcutForSend = "Q"; // ���� �ι�° ����Ű
	private static String firstShortcutForReceive = "Alt"; // ���� ù��° ����Ű
	private static String secondShortcutForReceive = "Q"; // ���� �ι�° ����Ű

	private static String fileName = null; // ���۹��� �����̸�

	private static History history = new History(); // �����丮
	private static Contents latestContents = null; // �ֽŵ�����

	private ConnectionPanel connectionPanel;

	StartToProgram startHandler; // ���α׷� ���ۿ� ���� �ڵ鷯

	private static File fileReceiveFolder; // ���� FileContents�� �ӽ÷� ������ ����
	ReceiveContents receiveContentsThread;// �����κ��� ���� Contents

	/** LinKlipboardClient ������ */
	public LinKlipboardClient(UserInterfaceManager main) {
		System.out.println("<����Ʈ Ŭ���̾�Ʈ ����>");

		createFileReceiveFolder(); // LinKlipboard folder ����

		this.main = main;
	}

	/**
	 * LinKlipboardClient ������
	 * 
	 * @param groupName
	 * @param groupPassword
	 */
	public LinKlipboardClient(String groupName, String groupPassword) {
		System.out.println("<Ŭ���̾�Ʈ ����> groupName: " + groupName + " groupPassword: " + groupPassword);

		LinKlipboardClient.groupName = groupName;
		this.password = groupPassword;

		createFileReceiveFolder(); // LinKlipboard folder ����
	}

	public void createstartThread(int port) {
		setPortNum(port);
		receiveContentsThread = new ReceiveContents();
		receiveContentsThread.start();
	}

	public void setting(HistoryPanel historyPanel, ConnectionPanel connectionPanel) {
		history.setHistoryPanel(historyPanel);
		history.setConnectionPanel(connectionPanel);
		this.connectionPanel = connectionPanel;
	}

	/** ����ڰ� �Է��� �׷������� ���� */
	public void setGroupInfo(String groupName, String groupPassword) {
		System.out.println("[LinKlipboardClient] �׷����� ���� �޼ҵ� ȣ��");
		LinKlipboardClient.groupName = groupName;
		this.password = groupPassword;
	}

	public void startThread() {
		receiveContentsThread.start();
	}

	/** �����ڵ带 �Է��� �������̽� ���� */
	public void setScreen(UserInterfacePage1 screen) {
		System.out.println("[LinKlipboardClient] �����ڵ� �Է� �������̽� ���� �޼ҵ� ȣ��");
		this.screen1 = screen;
	}

	/** ���۹��� ������ ������ ����(LinKlipboard) ���� */
	private void createFileReceiveFolder() {
		fileReceiveFolder = new File(LinKlipboard.fileReceiveDir);

		// C:\\Program Files�� LinKlipboard������ �������� ������
		if (!fileReceiveFolder.exists()) {
			fileReceiveFolder.mkdir(); // ���� ����
			System.out.println("[FolderManager] C:\\Program Files�� LinKlipboard ���� ����");
		}
	}

	/** ���� ���� ���ϵ��� ����(������ ��츸 ����.) */
	public static void initDir() {
		File[] innerFile = fileReceiveFolder.listFiles(); // ���� �� �����ϴ� ������
															// innerFile�� ����

		for (File file : innerFile) { // innerFile�� ũ�⸸ŭ for���� ���鼭
			file.delete(); // ���� ����
			System.out.println("[FolderManager] C:\\Program Files\\LinKlipboard ���� ���� ���� ����");
		}

		// Dir�ȿ� ������ �ϳ��� �ִ� ��쿡 ��� ����
		// innerFile[0].delete();
	}

	// ������ư�� ������ �� �޼ҵ尡 ����
	/** �׷���� �޼ҵ� */
	public void createGroup() {
		new StartToProgram(this, "create");
	}

	// ���ӹ�ư�� ������ �� �޼ҵ尡 ����
	/** �׷����� �޼ҵ� */
	public void joinGroup() {
		new StartToProgram(this, "join");
	}

	/** �׷� ������ �ʱ�ȭ */
	public void initGroupInfo() {
		LinKlipboardClient.groupName = null;
		this.password = null;
	}

	/** ����� �������̽��� ���� ���� ǥ�� */
	public void updateErrorState(String response) {
		this.screen1.updateErrorState(response);
	}

	/** Ŭ���̾�Ʈ�� �Է��� �׷��̸� ��ȯ */
	public static String getGroupName() {
		return groupName;
	}

	/** Ŭ���̾�Ʈ�� �Է��� �׷��н����� ��ȯ */
	public String getGroupPassword() {
		return password;
	}

	/** Ŭ���̾�Ʈ�� �Է��� �г��� ��ȯ */
	public String getNickName() {
		return nickName;
	}

	/** Ŭ���̾�Ʈ�� ���۹��� ���� �̸� ��ȯ */
	public static String getFileName() {
		return fileName;
	}

	/** Ŭ���̾�Ʈ�� ������ �ֱ� Contents ��ȯ */
	public static Contents getLatestContents() {
		return latestContents;
	}

	/** Ŭ���̾�Ʈ�� history ��ȯ */
	public History getHistory() {
		return history;
	}

	/** ������ ����� ��Ʈ��ȣ ��ȯ */
	public static int getPortNum() {
		return LinKlipboardClient.portNum;
	}

	/** ���� ù��° ����Ű ���� */
	public static void setFirstShortcutForSend(String firstShortcutForSend) {
		LinKlipboardClient.firstShortcutForSend = firstShortcutForSend;
	}

	/** ���� �ι�° ����Ű ���� */
	public static void setSecondShortcutForSend(String secondShortcutForSend) {
		LinKlipboardClient.secondShortcutForSend = secondShortcutForSend;
	}

	/** ���� ù��° ����Ű ���� */
	public static void setFirstShortcutForReceive(String firstShortcutForReceive) {
		LinKlipboardClient.firstShortcutForReceive = firstShortcutForReceive;
	}

	/** ���� �ι�° ����Ű ���� */
	public static void setSecondShortcutForReceive(String secondShortcutForReceive) {
		LinKlipboardClient.secondShortcutForReceive = secondShortcutForReceive;
	}

	/** ���� ù��° ����Ű ��ȯ */
	public static String getFirstShortcutForSend() {
		return firstShortcutForSend;
	}

	/** ���� �ι�° ����Ű ��ȯ */
	public static String getSecondShortcutForSend() {
		return secondShortcutForSend;
	}

	/** ���� ù��° ����Ű ��ȯ */
	public static String getFirstShortcutForReceive() {
		return firstShortcutForReceive;
	}

	/** ���� �ι�° ����Ű ��ȯ */
	public static String getSecondShortcutForReceive() {
		return secondShortcutForReceive;
	}

	/** Ŭ���̾�Ʈ�� �г����� ���� */
	public static void setNickName(String nickName) {
		LinKlipboardClient.nickName = nickName;
	}

	/** Ŭ���̾�Ʈ�� ���۹��� ���� �̸��� ���� */
	public void setFileName(String fileName) {
		LinKlipboardClient.fileName = fileName;
	}

	/** ������ ����� ��Ʈ��ȣ�� ���� */
	public void setPortNum(int portNum) {
		LinKlipboardClient.portNum = portNum;
	}

	/** Ŭ���̾�Ʈ �����丮�� ������ Contest�� ���� �ֱٿ� ������ Contents�� ���� */
	public void settLatestContents() {
		LinKlipboardClient.latestContents = history.getlastContents();
	}

	/** Ŭ���̾�Ʈ�� history�� ���� */
	public static void setHistory() {
		history.setHistory();
	}

	/* ���ο� Ŭ���̾�Ʈ ���� �� ������ history�� �Ѱ����� �ʴ´�. */
	/**
	 * Ŭ���̾�Ʈ�� history�� ����
	 * 
	 * @param updateHistory
	 *            server���� �޾ƿ� Vector<Contents>
	 */
	public static void setHistory(Vector<Contents> updateHistory) {
		System.out.println("[LlinKlipboardClient] setHistory�޼ҵ� ȣ��");
		history.setHistory(updateHistory);
	}

	/**
	 * ���� �׷� �����ڵ��� �г��� ���� ���� �����ڵ��� ũ�⸸ŭ String ���͸� ����
	 */
	public static void setOtherClients(Vector<String> clients) {
		// LinKlipboardClient.otherClients = new Vector<String>(clients);

		LinKlipboardClient.otherClients.removeAllElements();

		for (int i = 0; i < clients.size(); i++) {
			LinKlipboardClient.otherClients.add(clients.elementAt(i));
		}

	}

	/** ���� �׷� �����ڵ��� �г����� ��ȯ */
	public static Vector<String> getOtherClients() {
		return LinKlipboardClient.otherClients;
	}

	/** Ŭ���̾�Ʈ�� ��� ������ �ʱ�ȭ */
	public void initAllInfo() {
		otherClients.removeAllElements();
		; // ���� �׷� �����ڵ��� �г��� �ʱ�ȭ

		firstShortcutForSend = "Ctrl"; // ���� ù��° ����Ű
		secondShortcutForSend = "Q"; // ���� �ι�° ����Ű
		firstShortcutForReceive = "Alt"; // ���� ù��° ����Ű
		secondShortcutForReceive = "Q"; // ���� �ι�° ����Ű

		fileName = null; // ���۹��� �����̸�

		history.removeAllHistory(); // �����丮 �ʱ�ȭ

		latestContents = null; // �ֽŵ�����
	}

	/**
	 * ������ �����ϴ� Contents�� �޴� Ŭ���� �����带 ��ӹ޾� �������� �������ִ� �޼����� ��ٸ���.
	 */
	class ReceiveContents extends Thread {
		private ServerSocket listener;
		private Socket socket;
		private ObjectInputStream in; // Contents�� �ޱ� ���� ��Ʈ��

		/** ������ ������ ��ٸ��� ���� ���� */
		public void waitToServer() {
			try {
				System.out.println("[ReceiveContents] ��� ��Ʈ��ȣ: " + (LinKlipboardClient.getPortNum() + 1));
				listener = new ServerSocket((LinKlipboardClient.getPortNum() + 1));
				socket = listener.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void setPort(int port) {
			setPortNum(port);
		}

		/** �������� ������ ���� ��Ʈ�� ���� */
		public void setConnection() {
			try {
				in = new ObjectInputStream(socket.getInputStream()); // ��Ʈ�� ����
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("[ReceiveContents] ���� ���� ��");
		}

		@Override
		public void run() {
			while (true) {
				waitToServer();
				setConnection();
				try {
					latestContents = (Contents) in.readObject(); // Contents
																	// ��ü����
					int latestContentsType = latestContents.getType(); // Contents
																		// ��ü��
																		// Ÿ��

					System.out.println("�ֱٿ� ���� �������� Ÿ��:" + latestContentsType);

					if (latestContentsType == LinKlipboard.NULL) {
						// sharer�� Ȯ��
						StringTokenizer tokens = new StringTokenizer(latestContents.getSharer(),
								LinKlipboard.RESPONSE_DELIMITER);
						String inoutClientInfo = tokens.nextToken();

						// join�̸� Vector otherClients�� �߰�
						if (inoutClientInfo.equals("join")) {
							String inClientNickname = tokens.nextToken();

							System.out.println("join�� Ŭ���̾�Ʈ�� �г���:" + inClientNickname);

							otherClients.add(inClientNickname);
							connectionPanel.updateAccessGroup();
						}
						// exit�̸� join�̸� Vector otherClients���� ����
						if (inoutClientInfo.equals("exit")) {
							String outClientNickname = tokens.nextToken();
							for (int i = 0; i < otherClients.size(); i++) {
								if (otherClients.get(i).equals(outClientNickname)) {
									otherClients.remove(i);
									connectionPanel.updateAccessGroup();
									return;
								}
							}
						}
					} else {
						history.addSharedContentsInHistory(latestContents); // ��������
																			// �ֽ�Contents��
																			// history��
																			// �߰�

						if (SettingPanel.notification) {// ���� �߰�
							if (latestContentsType == LinKlipboard.FILE_TYPE) {
								main.getTrayIcon().showMsg("Shared <File> Contents");
							} else if (latestContentsType == LinKlipboard.STRING_TYPE) {
								main.getTrayIcon().showMsg("Shared <Text> Contents");
							} else if (latestContentsType == LinKlipboard.IMAGE_TYPE) {
								main.getTrayIcon().showMsg("Shared <Image> Contents");
							} else {
								System.out.println("[LinKlipboardClient_�˸�] File, String, Image ��𿡵� ������ ����");
							}
						}
					}

					closeSocket();

				} catch (ClassNotFoundException e) {
					this.start();
				} catch (IOException e) {
					this.start();
				}
			}
		}

		public void closeSocket() {
			try {
				in.close();
				socket.close();
				listener.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}