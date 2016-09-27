package transfer_manager;

import java.awt.HeadlessException;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import client_manager.ClipboardManager;
import client_manager.LinKlipboardClient;
import contents.Contents;
import contents.FileContents;
import server_manager.LinKlipboard;

public class FileSendDataToServer extends Thread {

	private Socket socket; // ������ ������ ����
	private LinKlipboardClient client;

	private String response; // �����κ��� ���� ���� ����
	private ResponseHandler responseHandler; // ���信 ���� ó��

	// ���濡�� ����Ʈ �迭�� �ְ� �ޱ����� ������ ��Ʈ�� ����
	private DataOutputStream dos;
	private FileInputStream fis;

	private int serialNum;
	private static File sendFile; // static-> FileSendDataToServer���� ���

	// �ڽ��� �����丮�� ������ FileContents
	private FileContents fileContents;

	/** FileSendDataToServer ������ */
	public FileSendDataToServer(LinKlipboardClient client) {
		this.client = client;
	}

	/** ���� ������ ���� �޼ҵ� (SendDataToServer ���� ȣ��) */
	public void requestSendFileData() {
		try {
			// ȣ���� ������ �ּ�
			URL url = new URL(LinKlipboard.URL_To_CALL + "/SendDataToServer");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// ������ ���� ������(�׷��̸�, (���ϸ� ����� ���ϸ�����))
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String groupName = "groupName=" + LinKlipboardClient.getGroupName();

			sendFile = new File(getFilePathInSystemClipboard());
			fileContents = new FileContents(getSendFile());
			String fileName = "fileName=" + sendFile.getName();
			

			String header = groupName + "&" + fileName;
			System.out.println("[requestSendFileData] ���� ��ü ������ Ȯ��" + header);

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
			System.out.println("[requestSendFileData] �����κ����� ���� ������ Ȯ��: " + this.response);
			bin.close();

			exceptionHandling(this.response);

			if (ResponseHandler.getErrorCodeNum() == LinKlipboard.READY_TO_TRANSFER) {
				System.out.println("[requestSendFileData] ���� ����");

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
			System.out.println("[exceptionHandling] Error!!!! ������ ���� response�� null��");
		}

		serialNum = responseHandler.getContentsSerialNum();
	}

	/** �������� ������ ���� ���ϰ� ��Ʈ�� ���� */
	public void setConnection() {
		try {
			// ���� ���� ����
			socket = new Socket(LinKlipboard.SERVER_IP, LinKlipboardClient.getPortNum());
			// ��Ʈ�� ����
			dos = new DataOutputStream(socket.getOutputStream()); // ����Ʈ �迭�� ������ ���� �����ͽ�Ʈ�� ����

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** �����ִ� ���ϰ� ��Ʈ���� ��� �ݴ´�. */
	public void closeSocket() {
		try {
			dos.close();
			fis.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		setConnection();

		try {
			byte[] sendFileTobyteArray = new byte[LinKlipboard.byteSize]; // ����Ʈ �迭 ����
			int EndOfFile = 0; // ������ ��(-1)�� �˸��� ���� ����

			fis = new FileInputStream(getSendFile()); // ���Ͽ��� �о���� ���� ��Ʈ�� ����

			/*
			 * sendFileTobyteArray�� ũ���� 1024����Ʈ ��ŭ ���Ͽ��� �о�� ����Ʈ �迭�� ����, EndOfFile���� 1024�� ������� ������ ���� �ٴٸ���(EndOfFile=-1 �� ��)���� �ݺ�
			 */
			while ((EndOfFile = fis.read(sendFileTobyteArray)) != -1) {
				// sendFileTobyteArray�� ����ִ� ����Ʈ�� 0~EndOfFile=1024 ��ŭ DataOutputStream���� ����
				dos.write(sendFileTobyteArray, 0, EndOfFile);
			}

			// �����丮�� �߰��� Contents�� ������ȣ ����
			Contents.setSerialNum(serialNum);

			fileContents.setDate();
			fileContents.setSharer(client.getNickName());
			// �ڽ��� ������ ������ Contents�� �����丮�� �߰�
			client.getHistory().addSharedContentsInHistory(fileContents);
			client.settLatestContents();

			closeSocket();

		} catch (IOException e1) {
			closeSocket();
			return;
		}
	}

	/** @return Ŭ�����忡 �ִ� ������ ��θ� */
	public static String getFilePathInSystemClipboard() {

		try {
			// �ý��� Ŭ�����忡�� ������ ����
			Transferable contents = ClipboardManager.getSystmeClipboardContets();

			String fileTotalPath = contents.getTransferData(ClipboardManager.setDataFlavor(contents)).toString();

			// ��θ� ������ ���� �� ���� []�� ����
			return fileTotalPath.substring(1, fileTotalPath.length() - 1);

		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** Ŭ���̾�Ʈ�� ������ ���� ���� ������ ���� */
	public static File getSendFile() {
		return sendFile;
	}
	
	/** Ŭ���̾�Ʈ�� ������ ���� ���� ������ �̸��� ���� */
	public static String getSendFileName() {
		return sendFile.getName();
	}
}
