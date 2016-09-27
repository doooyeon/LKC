package trash_package;
//package transfer_manager;
//
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//import java.net.UnknownHostException;
//
//import client_manager.ClipboardManager;
//import client_manager.LinKlipboardClient;
//import contents.Contents;
//import server_manager.LinKlipboard;
//
//public class SendDataToServer extends Transfer {
//	private ObjectOutputStream out;
//	private int serialNum;
//	
//	private Contents sendContents;
//
//	/** SendDataToServer 생성자 */
//	public SendDataToServer(LinKlipboardClient client, int serialNum) {
//		super(client);
//
//		this.serialNum = serialNum;
//		this.start();
//	}
//
//	/** 서버와의 연결을 위한 소켓과 스트림 설정 */
//	@Override
//	public void setConnection() {
//		try {
//			// 소켓 접속 설정
//			socket = new Socket(LinKlipboard.SERVER_IP, LinKlipboardClient.getPortNum());
//			// 스트림 설정
//			out = new ObjectOutputStream(socket.getOutputStream());
//			System.out.println("[SendDataToServer] 연결 설정 끝");
//
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/** 열려있는 소켓과 스트림을 모두 닫는다. */
//	@Override
//	public void closeSocket() {
//		try {
//			out.close();
//			socket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void run() {
//		setConnection();
//		try {
//			sendContents = ClipboardManager.readClipboard(); // 전송할 객체를 시스템 클립보드로부터 가져옴
//			
//			// 히스토리에 추가할 Contents의 고유번호 세팅
//			Contents.setSerialNum(serialNum);
//
//			
//			//공유한 날짜, 시간 설정
//			sendContents.setDate();
//			sendContents.setSharer(client.getNickName());
//			
//			// 자신이 서버에 공유한 Contents를 히스토리에 추가
//			client.getHistory().addSharedContentsInHistory(sendContents);
//			
//			out.writeObject(sendContents); // Contents 객체 전송
//
//			closeSocket();
//		} catch (IOException e) {
//			e.printStackTrace();
//			closeSocket();
//		}
//	}
//
////	/** 서버에 보낼 Contents를 반환 */
////	public Contents getSendContents() {
////		return sendContents;
////	}
//}
