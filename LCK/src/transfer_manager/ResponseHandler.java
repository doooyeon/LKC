package transfer_manager;

import java.util.StringTokenizer;

import client_manager.LinKlipboardClient;
import server_manager.LinKlipboard;

public class ResponseHandler {

	private LinKlipboardClient client;

	private String responseWholeMsg; // 서버에게서 응답받은 전체메세지

	private static int errorCodeNum; // 에러코드
	private String errorMsg = null; // 에러코드에 대한 정보

	private int resontentsSerialNum = LinKlipboard.NULL; // Contents의 고유번호
	private String resFileName = null; // 전송받은 파일이름
	private String resNickName = null; // 디폴트 닉네임
	private int resPortNum = LinKlipboard.NULL; // 서버로부터 응답받은 포트번호

	/** ResponseHandler 생성자 */
	public ResponseHandler(String responseWholeMsg, LinKlipboardClient client) {
		this.responseWholeMsg = responseWholeMsg;
		this.client = client;
	}

	/** 넘겨받은 스트링 에러 코드를 분리하는 메소드 */
	public void seperateErrorCode() {
		StringTokenizer tokens = new StringTokenizer(responseWholeMsg, LinKlipboard.RESPONSE_DELIMITER);
		String delimiter;

		errorCodeNum = Integer.parseInt(tokens.nextToken());
		setErrorMsg(errorCodeNum);

		while (tokens.hasMoreTokens() == true) {
			delimiter = tokens.nextToken(); // errorCodeNum 다음에 오는 구분자 확인
			System.out.println("구분자: " + delimiter);

			switch (delimiter) {
			case LinKlipboard.RES_NICKNAME:
				resNickName = tokens.nextToken();
				break;
			case LinKlipboard.RES_FILENAME:
				resFileName = tokens.nextToken();
				break;
			case LinKlipboard.RES_SERIAL_NUM:
				resontentsSerialNum = Integer.parseInt(tokens.nextToken());
				break;
			case LinKlipboard.RES_PORT:
				resPortNum = Integer.parseInt(tokens.nextToken());
				//client.setPortNum(resPortNum);
				System.out.println("resPortNum: "+ resPortNum);
				client.createstartThread(resPortNum);
				break;
			default:
				System.out.println("[ResponseHandler] 알 수 없는 구분자");
				break;
			}
		}
		System.out.println("[ResponseHandler] resNickName/resFileName/resontentsSerialNum : " + resNickName + "/"
				+ resFileName + "/" + resontentsSerialNum);
		System.out.println("[ResponseHandler] errorCodeNum/errorMsg" + errorCodeNum + "/" + errorMsg);
	}

	/** errorCodeNum와 일치하는 메세지를 set하는 메소드 */
	public void setErrorMsg(int errorCodeNum) {
		switch (errorCodeNum) {
		case LinKlipboard.ACCESS_PERMIT:
			errorMsg = "접속 허용";
			break;
		case LinKlipboard.COMPLETE_APPLY:
			errorMsg = "변경 요청 승인";
			break;
		case LinKlipboard.READY_TO_TRANSFER:
			errorMsg = "전송 준비";
			break;
		case LinKlipboard.ERROR_DUPLICATED_GROUPNAME:
			errorMsg = "그룹명 중복";
			break;
		case LinKlipboard.ERROR_NO_MATCHED_GROUPNAME:
			errorMsg = "그룹명 존재 안함";
			break;
		case LinKlipboard.ERROR_PASSWORD_INCORRECT:
			errorMsg = "password 불일치";
			break;
		case LinKlipboard.ERROR_SOCKET_CONNECTION:
			errorMsg = "소켓 연결 오류";
			break;
		case LinKlipboard.ERROR_DATA_TRANSFER:
			errorMsg = "데이터 송수신 오류";
			break;
		case LinKlipboard.ERROR_FULL_GROUP:
			errorMsg = "생성 가능 그룹 초과";
			break;
		case LinKlipboard.ERROR_FULL_CLIENT:
			errorMsg = "접속 가능 클라이언트 초과";
			break;
		case LinKlipboard.ERROR_TRYCATCH:
			errorMsg = "try catch 오류";
			break;
		case LinKlipboard.ERROR_DUPLICATED_IP:
			errorMsg = "중복된 ip 주소";
			break;
		case LinKlipboard.ERROR_DUPLICATED_NICKNAME:
			errorMsg = "중복된 닉네임";
			break;
		case LinKlipboard.ERROR_NOT_SUPPORTED:
			errorMsg = "지원하지 않는 형식";
			break;
		case LinKlipboard.NULL:
			errorMsg = "NULL";
			break;
		default:
			System.out.println(errorCodeNum);
			errorMsg = "알수 없는 오류";
			break;
		}
	}

	/** 생성 및 접속에 대한 응답 핸들러 */
	public void responseHandlerForStart() {
		seperateErrorCode();

		// 만약 errorCode가 ACCESS_PERMIT이면 디폴트 닉네임과 포트번호를 set
		if (errorCodeNum == LinKlipboard.ACCESS_PERMIT) {
			setDefaultNickName(resNickName);
			setPortNum(resPortNum);
			System.out.println(
					"[ResponseHandler] " + LinKlipboardClient.getGroupName() + "의 " + resNickName + "(닉네임)가  접속, 사용 포트번호: " + resPortNum);
		}
		// 만약 errorCode가 ERROR이면 errorMsg에 오류정보 set
		else {
			// 사용자 인터페이스에 에러상태 표시
			client.updateErrorState(errorMsg);
		}
	}

	/** 데이터 전송 및 수신에 대한 응답 핸들러(READY_TO_TRANSFER를 제외한 에러처리) */
	public void responseHandlerForTransfer() {
		seperateErrorCode();

		// 만약 errorCode가 READY_TO_TRANSFER이면 소켓을 연다
		if (errorCodeNum == LinKlipboard.READY_TO_TRANSFER) {
			// 소켓연결 this.start();는 외부에서 처리
			setFileName(resFileName);
			System.out.println(
					"[ResponseHandler] " + LinKlipboardClient.getGroupName() + "의 " + resFileName + "(파일명)을 전송받음");
		}
		// 만약 errorCode가 ERROR이면 errorMsg에 오류정보 set
		else {
			// 사용자 인터페이스에 에러상태 표시
			client.updateErrorState(errorMsg);
		}
	}

	/** 클라이언트의 디폴트 닉네임을 세팅 */
	public void setDefaultNickName(String nickName) {
		LinKlipboardClient.setNickName(nickName);
	}

	/** 전송받을 파일의 이름을 세팅 */
	public void setFileName(String fileName) {
		client.setFileName(fileName);
	}
	
	/** 클라이언트의 포트번호를 세팅 */
	public void setPortNum(int portNum) {
		client.setPortNum(portNum);
	}

	/** 클라이언트가 서버에 전송한 Contents의 serialNum를 넘겨받아 세팅 */
	public void setSerialNum(int serialNum) {

	}

	/** 에러코드를 반환 */
	public static int getErrorCodeNum() {
		return errorCodeNum;
	}

	/** Contents의 고유번호를 반환 */
	public int getContentsSerialNum() {
		return resontentsSerialNum;
	}
}
