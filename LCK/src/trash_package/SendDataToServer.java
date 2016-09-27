package trash_package;
//package transfer_manager;
//
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//import java.net.UnknownHostException;
//
//import client_manager.ClipboardManager;
//import client_manager.LinKlipboardClient;
//import contents.Contents;
//import server_manager.LinKlipboard;
//
//public class SendDataToServer extends Transfer {
//	private ObjectOutputStream out;
//	private int serialNum;
//	
//	private Contents sendContents;
//
//	/** SendDataToServer ������ */
//	public SendDataToServer(LinKlipboardClient client, int serialNum) {
//		super(client);
//
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
//			out = new ObjectOutputStream(socket.getOutputStream());
//			System.out.println("[SendDataToServer] ���� ���� ��");
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
//			out.close();
//			socket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void run() {
//		setConnection();
//		try {
//			sendContents = ClipboardManager.readClipboard(); // ������ ��ü�� �ý��� Ŭ������κ��� ������
//			
//			// �����丮�� �߰��� Contents�� ������ȣ ����
//			Contents.setSerialNum(serialNum);
//
//			
//			//������ ��¥, �ð� ����
//			sendContents.setDate();
//			sendContents.setSharer(client.getNickName());
//			
//			// �ڽ��� ������ ������ Contents�� �����丮�� �߰�
//			client.getHistory().addSharedContentsInHistory(sendContents);
//			
//			out.writeObject(sendContents); // Contents ��ü ����
//
//			closeSocket();
//		} catch (IOException e) {
//			e.printStackTrace();
//			closeSocket();
//		}
//	}
//
////	/** ������ ���� Contents�� ��ȯ */
////	public Contents getSendContents() {
////		return sendContents;
////	}
//}
