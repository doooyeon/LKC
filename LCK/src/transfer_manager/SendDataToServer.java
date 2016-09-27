package transfer_manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import client_manager.ClipboardManager;
import client_manager.LinKlipboardClient;
import contents.Contents;
import server_manager.LinKlipboard;

public class SendDataToServer extends Thread {

	private Socket socket; // 서버와 연결할 소켓
	private LinKlipboardClient client;

	private String response; // 서버로부터 받은 응답 정보
	private ResponseHandler responseHandler; // 응답에 대한 처리

	private ObjectOutputStream out;

	private int serialNum;
	private Contents sendContents;

	/** SendDataToServer 생성자 */
	public SendDataToServer() {
		System.out.println("디폴트 SendDataToServer1");
	}

	/** SendDataToServer 생성자 */
	public SendDataToServer(LinKlipboardClient client) {
		this.client = client;
	}

	/** 문자열, 이미지 데이터 전송 메소드 (SendDataToServer 서블릿 호출) */
	public void requestSendExpFileData() {
		try {
			// 호출할 서블릿의 주소
			URL url = new URL(LinKlipboard.URL_To_CALL + "/SendDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// 서버에 보낼 데이터(그룹이름, (파일명 존재시 파일명전송))
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + LinKlipboardClient.getGroupName();

			System.out.println("[requestSendExpFileData] 보낼 전체 데이터 확인" + header);

			bout.write(header);
			bout.flush();
			bout.close();

			// 서버로부터 받을 데이터(응답정보)
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response = null;

			if ((response = bin.readLine()) != null) {
				// 서버에서 확인 후 클라이언트가 받은 결과 메세지
				this.response = response;
			}
			System.out.println("[requestSendExpFileData] 서버로부터의 응답 데이터 확인: " + this.response);
			bin.close();

			exceptionHandling(this.response);

			if (ResponseHandler.getErrorCodeNum() == LinKlipboard.READY_TO_TRANSFER) {
				System.out.println("[requestSendExpFileData] 소켓 연결");

				this.start();
			}

			bin.close();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 예외 처리
	 * 
	 * @param response
	 *            클라이언트 요청에 대한 서버의 응답
	 */
	public void exceptionHandling(String response) {
		responseHandler = new ResponseHandler(response, client);
		if (response != null) {
			responseHandler.responseHandlerForTransfer();
		} else {
			System.out.println("[SendDataToServer] Error!!!! 서버가 보낸 response가 null임");
		}

		serialNum = responseHandler.getContentsSerialNum();
	}

	/** 서버와의 연결을 위한 소켓과 스트림 설정 */
	public void setConnection() {
		try {
			// 소켓 접속 설정
			socket = new Socket(LinKlipboard.SERVER_IP, LinKlipboardClient.getPortNum());
			// 스트림 설정
			out = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("[SendDataToServer] 연결 설정 끝");

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 열려있는 소켓과 스트림을 모두 닫는다. */
	public void closeSocket() {
		try {
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		setConnection();
		try {
			sendContents = ClipboardManager.readClipboard(); // 전송할 객체를 시스템 클립보드로부터 가져옴
			
			// 히스토리에 추가할 Contents의 고유번호 세팅
			Contents.setSerialNum(serialNum);
			
			// 공유한 날짜, 시간 설정
			sendContents.setDate();
			sendContents.setSharer(client.getNickName());

			// 자신이 서버에 공유한 Contents를 히스토리에 추가
			client.getHistory().addSharedContentsInHistory(sendContents);
			client.settLatestContents();

			out.writeObject(sendContents); // Contents 객체 전송

			closeSocket();
		} catch (IOException e) {
			e.printStackTrace();
			closeSocket();
		}
	}
}