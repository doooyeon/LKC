package datamanage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import client_manager.ClipboardManager;
import client_manager.LinKlipboardClient;
import contents.Contents;
import contents.FileContents;
import server_manager.LinKlipboard;
import transfer_manager.FileReceiveDataToServer;
import transfer_manager.ResponseHandler;

public class ReceivePreviousData {

	private LinKlipboardClient client;

	private Contents previousData; // Ŭ�����忡 ������ Contents(Ŭ���̾�Ʈ�� �����丮���� ������ Contents)
	private FileContents fileContents;

	private String response; // �����κ��� ���� ���� ����
	private ResponseHandler responseHandler; // ���信 ���� ó��

	private int dataType;
	private int serialNum;

	private FileReceiveDataToServer fileReceive;

	/** ReceivePreviousData ������ */
	public ReceivePreviousData() {
		System.out.println("����Ʈ ReceivePreviousData ������ ȣ��");
	}

	/**
	 * ReceivePreviousData ������
	 * 
	 * @param history
	 *            ����ڰ� ������ �ִ� history ����
	 * @param listIndex
	 *            history���� �ޱ⸦ ���ϴ� Contents�� index��
	 */
	public void ReceiveData(LinKlipboardClient client, Contents previousData) {
		this.client = client;
		this.previousData = previousData;
		this.dataType = previousData.getType();
		this.serialNum = previousData.getSerialNum();

		receiveDataInClipboard();
	}

	/** ��û�� Contents�� �ý��� Ŭ�����忡 ���� */
	public void receiveDataInClipboard() {
		switch (dataType) {
		case LinKlipboard.STRING_TYPE:
		case LinKlipboard.IMAGE_TYPE:
			ClipboardManager.writeClipboard(previousData, dataType); // ���Ź��� �����͸� Ŭ�����忡 ����
			break;
		case LinKlipboard.FILE_TYPE:
			fileContents = (FileContents) previousData; // ���Ź��� ������ �̸��� �˾Ƴ��� ���� Contents�� FileContents�� ĳ����
			fileReceive = new FileReceiveDataToServer(client, fileContents.getFileName()); // �����̸��� ���ڷ� ������ FileReceiveDataToServer ��ü ����
			requestReceiveFileData(); // ������ index ������ ���� �ް� ���� �޾Ƽ� Ŭ�����忡 ����
			break;
		}
	}

	/** ���� ������ ���� �޼ҵ�(FileReceiveDataToServerInHistory ���� ȣ��) */
	public void requestReceiveFileData() {
		try {
			// ȣ���� ������ �ּ�
			URL url = new URL(LinKlipboard.URL_To_CALL + "/ReceiveDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// ������ ���� ������(�׷��̸�)
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + LinKlipboardClient.getGroupName() + "&";
			header += "serialNum=" + serialNum + "\r\n";

			System.out.println("[ReceivePreviousData] ���� ��ü ������ Ȯ��" + header);

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
			System.out.println("[ReceivePreviousData] �����κ����� ���� ������ Ȯ��: " + this.response); // delf
			bin.close();

			exceptionHandling(this.response);

			if (ResponseHandler.getErrorCodeNum() == LinKlipboard.READY_TO_TRANSFER) {
				System.out.println("[ReceivePreviousData] ���� ����");
				fileReceive.start();
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
			System.out.println("[exceptionHandling] Error!!!! ������ ���� response�� null��");
		}
	}
}
