package start_manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeoutException;

import client_manager.LinKlipboardClient;
import server_manager.LinKlipboard;
import transfer_manager.ResponseHandler;

public class StartToProgram {

	private LinKlipboardClient client;

	private String response; // 서버로부터 받은 응답 정보
	private ResponseHandler responseHandler; // 응답에 대한 처리

	private String groupName; // 그룹이름
	private String password; // 패스워드
	private String orderMsg; // 사용자가 원하는 명령(create/join)

	/**
	 * StartToProgram 생성자
	 * 
	 * @param client
	 *            프로그램을 시작하는 사용자
	 */
	public StartToProgram(LinKlipboardClient client, String orderMsg) {
		this.client = client;
		this.groupName = LinKlipboardClient.getGroupName();
		this.password = client.getGroupPassword();
		this.orderMsg = orderMsg;
		this.response = null;

		startProgram();
	}

	public StartToProgram(LinKlipboardClient client) {
		this.client = client;
		this.groupName = LinKlipboardClient.getGroupName();
		this.password = client.getGroupPassword();
		this.response = null;
	}

	public void startProgram() {
		// 생성버튼을 누르면 이 메소드가 실행
		if (orderMsg.equals("create")) {
			sendGroupInfoToServer("/CreateGroup");
		}
		// 접속버튼을 누르면 이 메소드가 실행
		else if (orderMsg.equals("join")) {
			sendGroupInfoToServer("/JoinGroup");
		}
	}

	/** 그룹 정보를 서버에 보내고 응답(response)받는 메소드 */
	public void sendGroupInfoToServer(String servletName) {
		String response = Integer.toString(LinKlipboard.ERROR_TRYCATCH);

		try {
			// 호출할 서블릿의 URL
			URL url = new URL(LinKlipboard.URL_To_CALL + servletName);
			URLConnection conn = url.openConnection();
			System.out.println(conn);

			// servlet의 doPost호출
			conn.setDoOutput(true);

			// 서버에 보낼 데이터(그룹정보)
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + groupName + "&" + "password=" + password;

			// server에 그룹이름과 패스워드 전송(servlet이 받는 구분자: &)
			bout.write(header);
			bout.flush();
			bout.close();

			// 서버로부터 받을 데이터(응답정보)
			InputStream is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader bin = new BufferedReader(isr);

			if ((response = bin.readLine()) != null) {
				// 서버에서 확인 후 클라이언트가 받은 결과 메세지
				this.response = response;
			}

			// // 상훈 TEST 시작
			// // 서버에 보낼 데이터(그룹정보)
			// String message = "doy";
			//
			// // server에 그룹이름과 패스워드 전송(servlet이 받는 구분자: &)
			// bout.write(message);
			// bout.flush();
			// bout.close();
			// 상훈 TEST 끝

			bin.close();

			exceptionHandling(this.response);
			System.out.println("[sendGroupInfoToServer] " + ResponseHandler.getErrorCodeNum());

			if (ResponseHandler.getErrorCodeNum() == LinKlipboard.ACCESS_PERMIT) {
				// if (orderMsg.equals("create")) {
				// LinKlipboardClient.setHistory();
				//
				// } else if (orderMsg.equals("join")) {
				// // 서버에 있는 Vector<Contents>를 받는다.
				// LinKlipboardClient.setHistory();
				// new GetInitDataFromServer(client);
				// }

				LinKlipboardClient.setHistory();
			}

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		this.response = response;
	}

	/** 클라이언트 정보를 변경하기 위한 메소드(현재 닉네임만) */
	public void requestChangeInfoToServer(String nickName) {
		String response = Integer.toString(LinKlipboard.ERROR_TRYCATCH);

		try {

			// 호출할 서블릿의 URL
			URL url = new URL(LinKlipboard.URL_To_CALL + "/ChangeSettingOfClient");
			URLConnection conn = url.openConnection();

			// servlet의 doPost호출
			conn.setDoOutput(true);
			// conn.setDoInput(true);

			// 서버에 보낼 데이터(닉네임)
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + groupName + "&" + "nickname=" + nickName;

			// server에 그룹이름과 패스워드 전송(servlet이 받는 구분자: &)
			bout.write(header);
			bout.flush();
			bout.close();

			// 서버로부터 받을 데이터(응답정보)
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			// if ((response = bin.readLine()) != null) {
			// // 서버에서 확인 후 클라이언트가 받은 결과 메세지
			// //this.response = new String(response.getBytes("utf-8"),
			// "utf-8");
			// this.response = response;
			// System.out.println(this.response);
			// }
			// else {
			// System.out.println("왜 널이야 짜식아");
			// }
			// bin.close();
			this.response = Integer.toString(LinKlipboard.COMPLETE_APPLY);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			exceptionHandling(this.response);
			System.out.println("[requestChangeInfoToServer] " + ResponseHandler.getErrorCodeNum());

			if (ResponseHandler.getErrorCodeNum() == LinKlipboard.COMPLETE_APPLY) {
				// 나의 닉네임을 변경한다.
				LinKlipboardClient.setNickName(nickName);
			}

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		this.response = response;
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
			responseHandler.responseHandlerForStart();
		} else {
			System.out.println("[StartToProgram] Error!!!! 서버가 보낸 response가 null임");
		}
	}

	// /** 서버로부터 받은 오류 정보를 반환 */
	// public String getResponse() {
	// return response;
	// }
	// //UI할 때 필요할듯

	public static String convert(String str) throws IOException {
		ByteArrayOutputStream requestOutputStream = new ByteArrayOutputStream();
		requestOutputStream.write(str.getBytes("EUC-KR"));
		return requestOutputStream.toString("EUC-KR");
	}
}
