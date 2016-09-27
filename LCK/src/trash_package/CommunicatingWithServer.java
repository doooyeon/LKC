package trash_package;
//package transfer_manager;
//
//import java.awt.HeadlessException;
//import java.awt.datatransfer.Transferable;
//import java.awt.datatransfer.UnsupportedFlavorException;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//
//import client_manager.ClipboardManager;
//import client_manager.LinKlipboardClient;
//import contents.FileContents;
//import server_manager.LinKlipboard;
//
//public class CommunicatingWithServer {
//
//	private LinKlipboardClient client;
//
//	private String response; // 서버로부터 받은 응답 정보
//	private ResponseHandler responseHandler; // 응답에 대한 처리
//
//	private int serialNum; // 서버로부터 응답 받은 Contents의 SerialNum
//
//	private static File sendFile; // static-> FileSendDataToServer에서 사용
//
//	/** CommunicatingWithServer 생성자 */
//	public CommunicatingWithServer(LinKlipboardClient client) {
//		this.client = client;
//	}
//
//	/** 문자열, 이미지 데이터 전송 메소드 (SendDataToServer 서블릿 호출) */
//	public void requestSendExpFileData() {
//		try {
//			// 호출할 서블릿의 주소
//			URL url = new URL(LinKlipboard.URL_To_CALL + "/SendDataToServer");
//			URLConnection conn = url.openConnection();
//
//			conn.setDoOutput(true);
//
//			// 서버에 보낼 데이터(그룹이름, (파일명 존재시 파일명전송))
//			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//			String header = "groupName=" + LinKlipboardClient.getGroupName();
//
//			System.out.println("[requestSendExpFileData] 보낼 전체 데이터 확인" + header);
//
//			bout.write(header);
//			bout.flush();
//			bout.close();
//
//			// 서버로부터 받을 데이터(응답정보)
//			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			String response = null;
//
//			if ((response = bin.readLine()) != null) {
//				// 서버에서 확인 후 클라이언트가 받은 결과 메세지
//				this.response = response;
//			}
//			System.out.println("[requestSendExpFileData] 서버로부터의 응답 데이터 확인: " + this.response);
//			bin.close();
//
//			exceptionHandling(this.response);
//
//			if (ResponseHandler.getErrorCodeNum() == LinKlipboard.READY_TO_TRANSFER) {
//				System.out.println("[requestSendExpFileData] 소켓 연결");
//
//				new SendDataToServer(client, serialNum);
//			}
//
//			bin.close();
//		} catch (MalformedURLException ex) {
//			ex.printStackTrace();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//	}
//
//	/** 파일 데이터 전송 메소드 (SendDataToServer 서블릿 호출) */
//	public void requestSendFileData() {
//		try {
//			// 호출할 서블릿의 주소
//			URL url = new URL(LinKlipboard.URL_To_CALL + "/SendDataToServer");
//			URLConnection conn = url.openConnection();
//
//			conn.setDoOutput(true);
//
//			// 서버에 보낼 데이터(그룹이름, (파일명 존재시 파일명전송))
//			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//			String groupName = "groupName=" + LinKlipboardClient.getGroupName();
//
//			sendFile = new File(getFilePathInSystemClipboard());
//			String fileName = "fileName=" + sendFile.getName();
//
//			String header = groupName + "&" + fileName;
//			System.out.println("[requestSendFileData] 보낼 전체 데이터 확인" + header);
//
//			bout.write(header);
//			bout.flush();
//			bout.close();
//
//			// 서버로부터 받을 데이터(응답정보)
//			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			String response = null;
//
//			if ((response = bin.readLine()) != null) {
//				// 서버에서 확인 후 클라이언트가 받은 결과 메세지
//				this.response = response;
//			}
//			System.out.println("[requestSendFileData] 서버로부터의 응답 데이터 확인: " + this.response);
//			bin.close();
//
//			exceptionHandling(this.response);
//
//			if (responseHandler.getErrorCodeNum() == LinKlipboard.READY_TO_TRANSFER) {
//				System.out.println("[requestSendFileData] 소켓 연결");
//
//				new FileSendDataToServer(client, serialNum);
//			}
//			bin.close();
//
//		} catch (MalformedURLException ex) {
//			ex.printStackTrace();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//	}
//
//	/** 파일 데이터 수신 메소드 (ReceiveDataToServer 서블릿 호출) */
//	public void requestReceiveFileData() {
//		try {
//			// 호출할 서블릿의 주소
//			URL url = new URL(LinKlipboard.URL_To_CALL + "/ReceiveDataToServer");
//			URLConnection conn = url.openConnection();
//
//			conn.setDoOutput(true);
//
//			// 서버에 보낼 데이터(그룹이름)
//			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//			String header = "groupName=" + LinKlipboardClient.getGroupName();
//
//			System.out.println("[requestReceiveFileData] 보낼 전체 데이터 확인" + header);
//
//			bout.write(header);
//			bout.flush();
//			bout.close();
//
//			// 서버로부터 받을 데이터(응답정보)
//			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			String response = null;
//
//			if ((response = bin.readLine()) != null) {
//				// 서버에서 확인 후 클라이언트가 받은 결과 메세지
//				this.response = response;
//			}
//			System.out.println("[requestReceiveFileData] 서버로부터의 응답 데이터 확인: " + this.response);
//			bin.close();
//
//			exceptionHandling(this.response);
//			FileReceiveDataToServer.setFilePath();
//
//			if (responseHandler.getErrorCodeNum() == LinKlipboard.READY_TO_TRANSFER) {
//				System.out.println("[requestReceiveFileData] 소켓 연결");
//				new FileReceiveDataToServer(client);
//			}
//
//			bin.close();
//		} catch (MalformedURLException ex) {
//			ex.printStackTrace();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//	}
//	
//	/** 종료를 알리는 서블릿 (ReportExit 서블릿 호출) */
//	public void requestReportExit() {
//		try {
//			// 호출할 서블릿의 주소
//			URL url = new URL(LinKlipboard.URL_To_CALL + "/ReportExit");
//			URLConnection conn = url.openConnection();
//
//			conn.setDoOutput(true);
//
//			// 서버에 보낼 데이터(그룹이름)
//			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
//			String header = "groupName=" + LinKlipboardClient.getGroupName();
//
//			System.out.println("[requestReportExit] 보낼 전체 데이터 확인" + header);
//
//			bout.write(header);
//			bout.flush();
//			bout.close();
//		} catch (MalformedURLException ex) {
//			ex.printStackTrace();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//	}
//
//	/**
//	 * 예외 처리
//	 * 
//	 * @param response
//	 *            클라이언트 요청에 대한 서버의 응답
//	 */
//	public void exceptionHandling(String response) {
//		responseHandler = new ResponseHandler(response, client);
//		if (response != null) {
//			responseHandler.responseHandlerForTransfer();
//		} else {
//			System.out.println("[exceptionHandling] Error!!!! 서버가 보낸 response가 null임");
//		}
//
//		serialNum = responseHandler.getContentsSerialNum();
//	}
//
//	/** @return 클립보드에 있는 파일의 경로명 */
//	public static String getFilePathInSystemClipboard() {
//
//		try {
//			// 시스템 클립보드에서 내용을 추출
//			Transferable contents = ClipboardManager.getSystmeClipboardContets();
//
//			String fileTotalPath = contents.getTransferData(ClipboardManager.setDataFlavor(contents)).toString();
//
//			// 경로명만 얻어오기 위해 양 끝의 []를 제거
//			return fileTotalPath.substring(1, fileTotalPath.length() - 1);
//
//		} catch (HeadlessException e) {
//			e.printStackTrace();
//		} catch (UnsupportedFlavorException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	/** 클라이언트가 서버에 보낼 실제 파일을 리턴 */
//	public static File getSendFile() {
//		return sendFile;
//	}
//}
