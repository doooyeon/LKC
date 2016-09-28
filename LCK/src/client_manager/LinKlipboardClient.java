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
	private UserInterfacePage1 screen1; // 사용자 인터페이스(for 오류 정보 표시)

	private static String groupName; // 그룹이름
	private String password; // 패스워드
	private static String nickName = null; // 닉네임
	private static int portNum; // 서버와 통신할 포트번호
	private static Vector<String> otherClients = new Vector<String>(); // 같은 그룹
																		// 접속자들의
																		// 닉네임

	private static String firstShortcutForSend = "Ctrl"; // 전송 첫번째 단축키
	private static String secondShortcutForSend = "Q"; // 전송 두번째 단축키
	private static String firstShortcutForReceive = "Alt"; // 수신 첫번째 단축키
	private static String secondShortcutForReceive = "Q"; // 수신 두번째 단축키

	private static String fileName = null; // 전송받을 파일이름

	private static History history = new History(); // 히스토리
	private static Contents latestContents = null; // 최신데이터

	private ConnectionPanel connectionPanel;

	StartToProgram startHandler; // 프로그램 시작에 대한 핸들러

	private static File fileReceiveFolder; // 받은 FileContents를 임시로 저장할 폴더
	ReceiveContents receiveContentsThread;// 서버로부터 받을 Contents

	/** LinKlipboardClient 생성자 */
	public LinKlipboardClient(UserInterfaceManager main) {
		System.out.println("<디폴트 클라이언트 생성>");

		createFileReceiveFolder(); // LinKlipboard folder 생성

		this.main = main;
	}

	/**
	 * LinKlipboardClient 생성자
	 * 
	 * @param groupName
	 * @param groupPassword
	 */
	public LinKlipboardClient(String groupName, String groupPassword) {
		System.out.println("<클라이언트 생성> groupName: " + groupName + " groupPassword: " + groupPassword);

		LinKlipboardClient.groupName = groupName;
		this.password = groupPassword;

		createFileReceiveFolder(); // LinKlipboard folder 생성
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

	/** 사용자가 입력한 그룹정보를 세팅 */
	public void setGroupInfo(String groupName, String groupPassword) {
		System.out.println("[LinKlipboardClient] 그룹정보 세팅 메소드 호출");
		LinKlipboardClient.groupName = groupName;
		this.password = groupPassword;
	}

	public void startThread() {
		receiveContentsThread.start();
	}

	/** 오류코드를 입력할 인터페이스 설정 */
	public void setScreen(UserInterfacePage1 screen) {
		System.out.println("[LinKlipboardClient] 오류코드 입력 인터페이스 세팅 메소드 호출");
		this.screen1 = screen;
	}

	/** 전송받은 파일을 저장할 폴더(LinKlipboard) 생성 */
	private void createFileReceiveFolder() {
		fileReceiveFolder = new File(LinKlipboard.fileReceiveDir);

		// C:\\Program Files에 LinKlipboard폴더가 존재하지 않으면
		if (!fileReceiveFolder.exists()) {
			fileReceiveFolder.mkdir(); // 폴더 생성
			System.out.println("[FolderManager] C:\\Program Files에 LinKlipboard 폴더 생성");
		}
	}

	/** 폴더 안의 파일들을 삭제(파일인 경우만 생각.) */
	public static void initDir() {
		File[] innerFile = fileReceiveFolder.listFiles(); // 폴더 내 존재하는 파일을
															// innerFile에 넣음

		for (File file : innerFile) { // innerFile의 크기만큼 for문을 돌면서
			file.delete(); // 파일 삭제
			System.out.println("[FolderManager] C:\\Program Files\\LinKlipboard 폴더 안의 파일 삭제");
		}

		// Dir안에 파일이 하나만 있는 경우에 사용 가능
		// innerFile[0].delete();
	}

	// 생성버튼을 누르면 이 메소드가 실행
	/** 그룹생성 메소드 */
	public void createGroup() {
		new StartToProgram(this, "create");
	}

	// 접속버튼을 누르면 이 메소드가 실행
	/** 그룹접속 메소드 */
	public void joinGroup() {
		new StartToProgram(this, "join");
	}

	/** 그룹 정보를 초기화 */
	public void initGroupInfo() {
		LinKlipboardClient.groupName = null;
		this.password = null;
	}

	/** 사용자 인터페이스에 오류 정보 표시 */
	public void updateErrorState(String response) {
		this.screen1.updateErrorState(response);
	}

	/** 클라이언트가 입력한 그룹이름 반환 */
	public static String getGroupName() {
		return groupName;
	}

	/** 클라이언트가 입력한 그룹패스워드 반환 */
	public String getGroupPassword() {
		return password;
	}

	/** 클라이언트가 입력한 닉네임 반환 */
	public String getNickName() {
		return nickName;
	}

	/** 클라이언트가 전송받을 파일 이름 반환 */
	public static String getFileName() {
		return fileName;
	}

	/** 클라이언트가 공유한 최근 Contents 반환 */
	public static Contents getLatestContents() {
		return latestContents;
	}

	/** 클라이언트의 history 반환 */
	public History getHistory() {
		return history;
	}

	/** 서버와 통신할 포트번호 반환 */
	public static int getPortNum() {
		return LinKlipboardClient.portNum;
	}

	/** 전송 첫번째 단축키 세팅 */
	public static void setFirstShortcutForSend(String firstShortcutForSend) {
		LinKlipboardClient.firstShortcutForSend = firstShortcutForSend;
	}

	/** 전송 두번째 단축키 세팅 */
	public static void setSecondShortcutForSend(String secondShortcutForSend) {
		LinKlipboardClient.secondShortcutForSend = secondShortcutForSend;
	}

	/** 수신 첫번째 단축키 세팅 */
	public static void setFirstShortcutForReceive(String firstShortcutForReceive) {
		LinKlipboardClient.firstShortcutForReceive = firstShortcutForReceive;
	}

	/** 수신 두번째 단축키 세팅 */
	public static void setSecondShortcutForReceive(String secondShortcutForReceive) {
		LinKlipboardClient.secondShortcutForReceive = secondShortcutForReceive;
	}

	/** 전송 첫번째 단축키 반환 */
	public static String getFirstShortcutForSend() {
		return firstShortcutForSend;
	}

	/** 전송 두번째 단축키 반환 */
	public static String getSecondShortcutForSend() {
		return secondShortcutForSend;
	}

	/** 수신 첫번째 단축키 반환 */
	public static String getFirstShortcutForReceive() {
		return firstShortcutForReceive;
	}

	/** 수신 두번째 단축키 반환 */
	public static String getSecondShortcutForReceive() {
		return secondShortcutForReceive;
	}

	/** 클라이언트의 닉네임을 세팅 */
	public static void setNickName(String nickName) {
		LinKlipboardClient.nickName = nickName;
	}

	/** 클라이언트가 전송받을 파일 이름을 세팅 */
	public void setFileName(String fileName) {
		LinKlipboardClient.fileName = fileName;
	}

	/** 서버와 통신할 포트번호를 세팅 */
	public void setPortNum(int portNum) {
		LinKlipboardClient.portNum = portNum;
	}

	/** 클라이언트 히스토리의 마지막 Contest를 가장 최근에 공유한 Contents로 세팅 */
	public void settLatestContents() {
		LinKlipboardClient.latestContents = history.getlastContents();
	}

	/** 클라이언트의 history를 세팅 */
	public static void setHistory() {
		history.setHistory();
	}

	/* 새로운 클라이언트 접속 시 기존의 history를 넘겨주지 않는다. */
	/**
	 * 클라이언트의 history를 세팅
	 * 
	 * @param updateHistory
	 *            server에서 받아온 Vector<Contents>
	 */
	public static void setHistory(Vector<Contents> updateHistory) {
		System.out.println("[LlinKlipboardClient] setHistory메소드 호출");
		history.setHistory(updateHistory);
	}

	/**
	 * 같은 그룹 접속자들의 닉네임 세팅 받은 접속자들의 크기만큼 String 벡터를 세팅
	 */
	public static void setOtherClients(Vector<String> clients) {
		// LinKlipboardClient.otherClients = new Vector<String>(clients);

		LinKlipboardClient.otherClients.removeAllElements();

		for (int i = 0; i < clients.size(); i++) {
			LinKlipboardClient.otherClients.add(clients.elementAt(i));
		}

	}

	/** 같은 그룹 접속자들의 닉네임을 반환 */
	public static Vector<String> getOtherClients() {
		return LinKlipboardClient.otherClients;
	}

	/** 클라이언트의 모든 정보를 초기화 */
	public void initAllInfo() {
		otherClients.removeAllElements();
		; // 같은 그룹 접속자들의 닉네임 초기화

		firstShortcutForSend = "Ctrl"; // 전송 첫번째 단축키
		secondShortcutForSend = "Q"; // 전송 두번째 단축키
		firstShortcutForReceive = "Alt"; // 수신 첫번째 단축키
		secondShortcutForReceive = "Q"; // 수신 두번째 단축키

		fileName = null; // 전송받을 파일이름

		history.removeAllHistory(); // 히스토리 초기화

		latestContents = null; // 최신데이터
	}

	/**
	 * 서버가 전송하는 Contents를 받는 클래스 스레드를 상속받아 서버에서 전달해주는 메세지를 기다린다.
	 */
	class ReceiveContents extends Thread {
		private ServerSocket listener;
		private Socket socket;
		private ObjectInputStream in; // Contents를 받기 위한 스트림

		/** 서버의 연결을 기다리는 소켓 설정 */
		public void waitToServer() {
			try {
				System.out.println("[ReceiveContents] 사용 포트번호: " + (LinKlipboardClient.getPortNum() + 1));
				listener = new ServerSocket((LinKlipboardClient.getPortNum() + 1));
				socket = listener.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void setPort(int port) {
			setPortNum(port);
		}

		/** 서버와의 연결을 위한 스트림 설정 */
		public void setConnection() {
			try {
				in = new ObjectInputStream(socket.getInputStream()); // 스트림 설정
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("[ReceiveContents] 연결 설정 끝");
		}

		@Override
		public void run() {
			while (true) {
				waitToServer();
				setConnection();
				try {
					latestContents = (Contents) in.readObject(); // Contents
																	// 객체수신
					int latestContentsType = latestContents.getType(); // Contents
																		// 객체의
																		// 타입

					System.out.println("최근에 받은 컨텐츠의 타입:" + latestContentsType);

					if (latestContentsType == LinKlipboard.NULL) {
						// sharer을 확인
						StringTokenizer tokens = new StringTokenizer(latestContents.getSharer(),
								LinKlipboard.RESPONSE_DELIMITER);
						String inoutClientInfo = tokens.nextToken();

						// join이면 Vector otherClients에 추가
						if (inoutClientInfo.equals("join")) {
							String inClientNickname = tokens.nextToken();

							System.out.println("join한 클라이언트의 닉네임:" + inClientNickname);

							otherClients.add(inClientNickname);
							connectionPanel.updateAccessGroup();
						}
						// exit이면 join이면 Vector otherClients에서 제거
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
						history.addSharedContentsInHistory(latestContents); // 공유받은
																			// 최신Contents를
																			// history에
																			// 추가

						if (SettingPanel.notification) {// 도연 추가
							if (latestContentsType == LinKlipboard.FILE_TYPE) {
								main.getTrayIcon().showMsg("Shared <File> Contents");
							} else if (latestContentsType == LinKlipboard.STRING_TYPE) {
								main.getTrayIcon().showMsg("Shared <Text> Contents");
							} else if (latestContentsType == LinKlipboard.IMAGE_TYPE) {
								main.getTrayIcon().showMsg("Shared <Image> Contents");
							} else {
								System.out.println("[LinKlipboardClient_알림] File, String, Image 어디에도 속하지 않음");
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