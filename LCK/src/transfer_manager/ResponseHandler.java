package transfer_manager;

import java.util.StringTokenizer;

import client_manager.LinKlipboardClient;
import server_manager.LinKlipboard;

public class ResponseHandler {

	private LinKlipboardClient client;

	private String responseWholeMsg; // �������Լ� ������� ��ü�޼���

	private static int errorCodeNum; // �����ڵ�
	private String errorMsg = null; // �����ڵ忡 ���� ����

	private int resontentsSerialNum = LinKlipboard.NULL; // Contents�� ������ȣ
	private String resFileName = null; // ���۹��� �����̸�
	private String resNickName = null; // ����Ʈ �г���
	private int resPortNum = LinKlipboard.NULL; // �����κ��� ������� ��Ʈ��ȣ

	/** ResponseHandler ������ */
	public ResponseHandler(String responseWholeMsg, LinKlipboardClient client) {
		this.responseWholeMsg = responseWholeMsg;
		this.client = client;
	}

	/** �Ѱܹ��� ��Ʈ�� ���� �ڵ带 �и��ϴ� �޼ҵ� */
	public void seperateErrorCode() {
		StringTokenizer tokens = new StringTokenizer(responseWholeMsg, LinKlipboard.RESPONSE_DELIMITER);
		String delimiter;

		errorCodeNum = Integer.parseInt(tokens.nextToken());
		setErrorMsg(errorCodeNum);

		while (tokens.hasMoreTokens() == true) {
			delimiter = tokens.nextToken(); // errorCodeNum ������ ���� ������ Ȯ��
			System.out.println("������: " + delimiter);

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
				System.out.println("[ResponseHandler] �� �� ���� ������");
				break;
			}
		}
		System.out.println("[ResponseHandler] resNickName/resFileName/resontentsSerialNum : " + resNickName + "/"
				+ resFileName + "/" + resontentsSerialNum);
		System.out.println("[ResponseHandler] errorCodeNum/errorMsg" + errorCodeNum + "/" + errorMsg);
	}

	/** errorCodeNum�� ��ġ�ϴ� �޼����� set�ϴ� �޼ҵ� */
	public void setErrorMsg(int errorCodeNum) {
		switch (errorCodeNum) {
		case LinKlipboard.ACCESS_PERMIT:
			errorMsg = "���� ���";
			break;
		case LinKlipboard.COMPLETE_APPLY:
			errorMsg = "���� ��û ����";
			break;
		case LinKlipboard.READY_TO_TRANSFER:
			errorMsg = "���� �غ�";
			break;
		case LinKlipboard.ERROR_DUPLICATED_GROUPNAME:
			errorMsg = "�׷�� �ߺ�";
			break;
		case LinKlipboard.ERROR_NO_MATCHED_GROUPNAME:
			errorMsg = "�׷�� ���� ����";
			break;
		case LinKlipboard.ERROR_PASSWORD_INCORRECT:
			errorMsg = "password ����ġ";
			break;
		case LinKlipboard.ERROR_SOCKET_CONNECTION:
			errorMsg = "���� ���� ����";
			break;
		case LinKlipboard.ERROR_DATA_TRANSFER:
			errorMsg = "������ �ۼ��� ����";
			break;
		case LinKlipboard.ERROR_FULL_GROUP:
			errorMsg = "���� ���� �׷� �ʰ�";
			break;
		case LinKlipboard.ERROR_FULL_CLIENT:
			errorMsg = "���� ���� Ŭ���̾�Ʈ �ʰ�";
			break;
		case LinKlipboard.ERROR_TRYCATCH:
			errorMsg = "try catch ����";
			break;
		case LinKlipboard.ERROR_DUPLICATED_IP:
			errorMsg = "�ߺ��� ip �ּ�";
			break;
		case LinKlipboard.ERROR_DUPLICATED_NICKNAME:
			errorMsg = "�ߺ��� �г���";
			break;
		case LinKlipboard.ERROR_NOT_SUPPORTED:
			errorMsg = "�������� �ʴ� ����";
			break;
		case LinKlipboard.NULL:
			errorMsg = "NULL";
			break;
		default:
			System.out.println(errorCodeNum);
			errorMsg = "�˼� ���� ����";
			break;
		}
	}

	/** ���� �� ���ӿ� ���� ���� �ڵ鷯 */
	public void responseHandlerForStart() {
		seperateErrorCode();

		// ���� errorCode�� ACCESS_PERMIT�̸� ����Ʈ �г��Ӱ� ��Ʈ��ȣ�� set
		if (errorCodeNum == LinKlipboard.ACCESS_PERMIT) {
			setDefaultNickName(resNickName);
			setPortNum(resPortNum);
			System.out.println(
					"[ResponseHandler] " + LinKlipboardClient.getGroupName() + "�� " + resNickName + "(�г���)��  ����, ��� ��Ʈ��ȣ: " + resPortNum);
		}
		// ���� errorCode�� ERROR�̸� errorMsg�� �������� set
		else {
			// ����� �������̽��� �������� ǥ��
			client.updateErrorState(errorMsg);
		}
	}

	/** ������ ���� �� ���ſ� ���� ���� �ڵ鷯(READY_TO_TRANSFER�� ������ ����ó��) */
	public void responseHandlerForTransfer() {
		seperateErrorCode();

		// ���� errorCode�� READY_TO_TRANSFER�̸� ������ ����
		if (errorCodeNum == LinKlipboard.READY_TO_TRANSFER) {
			// ���Ͽ��� this.start();�� �ܺο��� ó��
			setFileName(resFileName);
			System.out.println(
					"[ResponseHandler] " + LinKlipboardClient.getGroupName() + "�� " + resFileName + "(���ϸ�)�� ���۹���");
		}
		// ���� errorCode�� ERROR�̸� errorMsg�� �������� set
		else {
			// ����� �������̽��� �������� ǥ��
			client.updateErrorState(errorMsg);
		}
	}

	/** Ŭ���̾�Ʈ�� ����Ʈ �г����� ���� */
	public void setDefaultNickName(String nickName) {
		LinKlipboardClient.setNickName(nickName);
	}

	/** ���۹��� ������ �̸��� ���� */
	public void setFileName(String fileName) {
		client.setFileName(fileName);
	}
	
	/** Ŭ���̾�Ʈ�� ��Ʈ��ȣ�� ���� */
	public void setPortNum(int portNum) {
		client.setPortNum(portNum);
	}

	/** Ŭ���̾�Ʈ�� ������ ������ Contents�� serialNum�� �Ѱܹ޾� ���� */
	public void setSerialNum(int serialNum) {

	}

	/** �����ڵ带 ��ȯ */
	public static int getErrorCodeNum() {
		return errorCodeNum;
	}

	/** Contents�� ������ȣ�� ��ȯ */
	public int getContentsSerialNum() {
		return resontentsSerialNum;
	}
}
