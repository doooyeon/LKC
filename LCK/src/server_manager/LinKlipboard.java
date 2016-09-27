package server_manager;

public class LinKlipboard {

	/* 그룹, 그룹인원에 대한 static 변수 */
	public static final int MAX_GROUP = 10; // 최대로 생성할 수 있는 그룹 수
	public static final int MAX_CLIENT = 10; // 한 그룹에 최대로 입장할 수 있는 클라이언트 수

	/* ACK */
	public final static int NULL = -1;
	public final static int ACCESS_PERMIT = 200; // 접속 승인
	public final static int READY_TO_TRANSFER = 201; // 전송 준비 됨
	public final static int COMPLETE_APPLY = 202; // 변경 요청 승인

	/* NACK */
	public final static int ERROR_DUPLICATED_GROUPNAME = 400; // 중복된 그룹 이름
	public final static int ERROR_NO_MATCHED_GROUPNAME = 401; // 해당 이름의 그룹 없음
	public final static int ERROR_PASSWORD_INCORRECT = 402; // 패스워드 불일치
	public final static int ERROR_SOCKET_CONNECTION = 403; // 소켓 연결 오류
	public final static int ERROR_DATA_TRANSFER = 404; // 데이터 송수신 오류
	public final static int ERROR_FULL_GROUP = 405; // 생성 가능 그룹 초과
	public final static int ERROR_FULL_CLIENT = 406; // 접속 가능 클라이언트 초과
	public final static int ERROR_TRYCATCH = 407;
	public final static int ERROR_DUPLICATED_IP = 408; // 중복된 IP로 접속
	public final static int ERROR_DUPLICATED_NICKNAME = 409; // 중복된 닉네임
	public final static int ERROR_NOT_SUPPORTED = 410; // 지원하지 않는 형식

	/* 전송 타입에 대한 static 변수 */
	public final static int STRING_TYPE = 10;
	public final static int IMAGE_TYPE = 11;
	public final static int FILE_TYPE = 12;

	/* 구분자 static 변수 */
	public final static String RESPONSE_DELIMITER = ";"; // 응답 구분자

	/* response 구분을 위한 static 변수 */
	public final static String RES_FILENAME = "fileName";
	public final static String RES_NICKNAME = "nickname";
	public final static String RES_SERIAL_NUM = "serialNo";
	public final static String RES_PORT = "portNum";

	/* 통신을 위한 static 변수 */
	public final static String SERVER_IP = "113.198.84.52";
	public final static int HTTP_PORT = 8080;
	// public final static int FTP_PORT = 20;
	public final static String SERVER_PROJECT_NAME = "LinKlipboardServerProject";
	// public final static String URL_To_CALL = "http://localhost:8080/LinKlipboardServerProject";
	public final static String URL_To_CALL = "http://" + SERVER_IP + ":8080/LinKlipboardServerProject";

	/* History크기를 위한 static 변수 */
	public final static int HISTORY_DEFAULT = 10;
	public final static int HISTORY_MAX = 50;

	/* Data 전송을 위한 static 변수 */
	public final static int byteSize = 65536; // byte array 크기 지정

	/* Folder생성을 위한 static 변수 */
	public final static String fileReceiveDir = "C:\\Program Files\\LinKlipboard";

}
