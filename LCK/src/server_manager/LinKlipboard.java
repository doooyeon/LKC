package server_manager;

public class LinKlipboard {

	/* �׷�, �׷��ο��� ���� static ���� */
	public static final int MAX_GROUP = 10; // �ִ�� ������ �� �ִ� �׷� ��
	public static final int MAX_CLIENT = 10; // �� �׷쿡 �ִ�� ������ �� �ִ� Ŭ���̾�Ʈ ��

	/* ACK */
	public final static int NULL = -1;
	public final static int ACCESS_PERMIT = 200; // ���� ����
	public final static int READY_TO_TRANSFER = 201; // ���� �غ� ��
	public final static int COMPLETE_APPLY = 202; // ���� ��û ����

	/* NACK */
	public final static int ERROR_DUPLICATED_GROUPNAME = 400; // �ߺ��� �׷� �̸�
	public final static int ERROR_NO_MATCHED_GROUPNAME = 401; // �ش� �̸��� �׷� ����
	public final static int ERROR_PASSWORD_INCORRECT = 402; // �н����� ����ġ
	public final static int ERROR_SOCKET_CONNECTION = 403; // ���� ���� ����
	public final static int ERROR_DATA_TRANSFER = 404; // ������ �ۼ��� ����
	public final static int ERROR_FULL_GROUP = 405; // ���� ���� �׷� �ʰ�
	public final static int ERROR_FULL_CLIENT = 406; // ���� ���� Ŭ���̾�Ʈ �ʰ�
	public final static int ERROR_TRYCATCH = 407;
	public final static int ERROR_DUPLICATED_IP = 408; // �ߺ��� IP�� ����
	public final static int ERROR_DUPLICATED_NICKNAME = 409; // �ߺ��� �г���
	public final static int ERROR_NOT_SUPPORTED = 410; // �������� �ʴ� ����

	/* ���� Ÿ�Կ� ���� static ���� */
	public final static int STRING_TYPE = 10;
	public final static int IMAGE_TYPE = 11;
	public final static int FILE_TYPE = 12;

	/* ������ static ���� */
	public final static String RESPONSE_DELIMITER = ";"; // ���� ������

	/* response ������ ���� static ���� */
	public final static String RES_FILENAME = "fileName";
	public final static String RES_NICKNAME = "nickname";
	public final static String RES_SERIAL_NUM = "serialNo";
	public final static String RES_PORT = "portNum";

	/* ����� ���� static ���� */
	public final static String SERVER_IP = "113.198.84.52";
	public final static int HTTP_PORT = 8080;
	// public final static int FTP_PORT = 20;
	public final static String SERVER_PROJECT_NAME = "LinKlipboardServerProject";
	// public final static String URL_To_CALL = "http://localhost:8080/LinKlipboardServerProject";
	public final static String URL_To_CALL = "http://" + SERVER_IP + ":8080/LinKlipboardServerProject";

	/* Historyũ�⸦ ���� static ���� */
	public final static int HISTORY_DEFAULT = 10;
	public final static int HISTORY_MAX = 50;

	/* Data ������ ���� static ���� */
	public final static int byteSize = 65536; // byte array ũ�� ����

	/* Folder������ ���� static ���� */
	public final static String fileReceiveDir = "C:\\Program Files\\LinKlipboard";

}
