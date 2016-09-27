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

	private Socket socket; // ������ ������ ����
	private LinKlipboardClient client;

	private String response; // �����κ��� ���� ���� ����
	private ResponseHandler responseHandler; // ���信 ���� ó��

	private ObjectOutputStream out;

	private int serialNum;
	private Contents sendContents;

	/** SendDataToServer ������ */
	public SendDataToServer() {
		System.out.println("����Ʈ SendDataToServer1");
	}

	/** SendDataToServer ������ */
	public SendDataToServer(LinKlipboardClient client) {
		this.client = client;
	}

	/** ���ڿ�, �̹��� ������ ���� �޼ҵ� (SendDataToServer ���� ȣ��) */
	public void requestSendExpFileData() {
		try {
			// ȣ���� ������ �ּ�
			URL url = new URL(LinKlipboard.URL_To_CALL + "/SendDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// ������ ���� ������(�׷��̸�, (���ϸ� ����� ���ϸ�����))
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + LinKlipboardClient.getGroupName();

			System.out.println("[requestSendExpFileData] ���� ��ü ������ Ȯ��" + header);

			bout.write(header);
			bout.flush();
			bout.close();

			// �����κ��� ���� ������(��������)
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response = null;

			if ((response = bin.readLine()) != null) {
				// �������� Ȯ�� �� Ŭ���̾�Ʈ�� ���� ��� �޼���
				this.response = response;
			}
			System.out.println("[requestSendExpFileData] �����κ����� ���� ������ Ȯ��: " + this.response);
			bin.close();

			exceptionHandling(this.response);

			if (ResponseHandler.getErrorCodeNum() == LinKlipboard.READY_TO_TRANSFER) {
				System.out.println("[requestSendExpFileData] ���� ����");

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
	 * ���� ó��
	 * 
	 * @param response
	 *            Ŭ���̾�Ʈ ��û�� ���� ������ ����
	 */
	public void exceptionHandling(String response) {
		responseHandler = new ResponseHandler(response, client);
		if (response != null) {
			responseHandler.responseHandlerForTransfer();
		} else {
			System.out.println("[SendDataToServer] Error!!!! ������ ���� response�� null��");
		}

		serialNum = responseHandler.getContentsSerialNum();
	}

	/** �������� ������ ���� ���ϰ� ��Ʈ�� ���� */
	public void setConnection() {
		try {
			// ���� ���� ����
			socket = new Socket(LinKlipboard.SERVER_IP, LinKlipboardClient.getPortNum());
			// ��Ʈ�� ����
			out = new ObjectOutputStream(socket.getOutputStream());
			System.out.println("[SendDataToServer] ���� ���� ��");

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** �����ִ� ���ϰ� ��Ʈ���� ��� �ݴ´�. */
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
			sendContents = ClipboardManager.readClipboard(); // ������ ��ü�� �ý��� Ŭ������κ��� ������
			
			// �����丮�� �߰��� Contents�� ������ȣ ����
			Contents.setSerialNum(serialNum);
			
			// ������ ��¥, �ð� ����
			sendContents.setDate();
			sendContents.setSharer(client.getNickName());

			// �ڽ��� ������ ������ Contents�� �����丮�� �߰�
			client.getHistory().addSharedContentsInHistory(sendContents);
			client.settLatestContents();

			out.writeObject(sendContents); // Contents ��ü ����

			closeSocket();
		} catch (IOException e) {
			e.printStackTrace();
			closeSocket();
		}
	}
}