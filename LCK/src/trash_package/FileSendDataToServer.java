package trash_package;
//package transfer_manager;
//
//import java.io.DataOutputStream;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.net.Socket;
//import java.net.UnknownHostException;
//
//import client_manager.LinKlipboardClient;
//import contents.Contents;
//import contents.FileContents;
//import server_manager.LinKlipboard;
//
//public class FileSendDataToServer extends Transfer {
//	// ���濡�� ����Ʈ �迭�� �ְ� �ޱ����� ������ ��Ʈ�� ����
//	private DataOutputStream dos;
//	private FileInputStream fis;
//	
//	private int serialNum;
//	
//	// �ڽ��� �����丮�� ������ FileContents
//	private FileContents fileContents = new FileContents(CommunicatingWithServer.getSendFile()); 
//
//	/** FileSendDataToServer ������ */
//	public FileSendDataToServer(LinKlipboardClient client, int serialNum) {
//		super(client);
//		this.client = client;
//		this.serialNum = serialNum;
//		this.start();
//	}
//
//	/** �������� ������ ���� ���ϰ� ��Ʈ�� ���� */
//	@Override
//	public void setConnection() {
//		try {
//			// ���� ���� ����
//			socket = new Socket(LinKlipboard.SERVER_IP, LinKlipboardClient.getPortNum());
//			// ��Ʈ�� ����
//			dos = new DataOutputStream(socket.getOutputStream()); // ����Ʈ �迭�� ������ ���� �����ͽ�Ʈ�� ����
//
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/** �����ִ� ���ϰ� ��Ʈ���� ��� �ݴ´�. */
//	@Override
//	public void closeSocket() {
//		try {
//			dos.close();
//			fis.close();
//			socket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void run() {
//		setConnection();
//
//		try {
//			byte[] sendFileTobyteArray = new byte[LinKlipboard.byteSize]; // ����Ʈ �迭 ����
//			int EndOfFile = 0; // ������ ��(-1)�� �˸��� ���� ����
//
//			fis = new FileInputStream(CommunicatingWithServer.getSendFile()); // ���Ͽ��� �о���� ���� ��Ʈ�� ����
//
//			/*
//			 * sendFileTobyteArray�� ũ���� 1024����Ʈ ��ŭ ���Ͽ��� �о�� ����Ʈ �迭�� ����, EndOfFile���� 1024�� ������� ������ ���� �ٴٸ���(EndOfFile=-1 �� ��)���� �ݺ�
//			 */
//			while ((EndOfFile = fis.read(sendFileTobyteArray)) != -1) {
//				// sendFileTobyteArray�� ����ִ� ����Ʈ�� 0~EndOfFile=1024 ��ŭ DataOutputStream���� ����
//				dos.write(sendFileTobyteArray, 0, EndOfFile);
//			}
//			
//			// �����丮�� �߰��� Contents�� ������ȣ ����
//			Contents.setSerialNum(serialNum);
//			
//			fileContents.setDate();
//			fileContents.setSharer(client.getNickName());
//			// �ڽ��� ������ ������ Contents�� �����丮�� �߰�
//			client.getHistory().addSharedContentsInHistory(fileContents);
//
//			closeSocket();
//
//		} catch (IOException e1) {
//			closeSocket();
//			return;
//		}
//	}
//}
