package trash_package;
//package transfer_manager;
//
//import java.awt.Toolkit;
//import java.awt.datatransfer.Clipboard;
//import java.awt.datatransfer.ClipboardOwner;
//import java.awt.datatransfer.DataFlavor;
//import java.awt.datatransfer.Transferable;
//import java.awt.datatransfer.UnsupportedFlavorException;
//import java.io.DataInputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.Socket;
//import java.net.UnknownHostException;
//import java.util.ArrayList;
//
//import client_manager.LinKlipboardClient;
//import server_manager.LinKlipboard;
//
//public class FileReceiveDataToServer extends Transfer {
//
//	// ������ �а� �������� ���� ��Ʈ�� ����
//	private FileOutputStream fos;
//	private DataInputStream dis;
//
//	// ���۹��� ������ ���
//	private static String receiveFilePath;
//	// �ܺο��� ��𼭾��̴��� Ȯ��!!!!!!!!!!!
//
//	/** FileReceiveDataToServer ������ */
//	public FileReceiveDataToServer(LinKlipboardClient client) {
//		super(client);
//		this.start();
//	}
//
//	/** FileReceiveDataToServer ������ */
//	public FileReceiveDataToServer(LinKlipboardClient client, String fileName) {
//		super(client);
//		FileReceiveDataToServer.receiveFilePath = LinKlipboard.fileReceiveDir + "\\" + fileName;
//	}
//
//	/** �������� ������ ���� ���ϰ� ��Ʈ�� ���� */
//	@Override
//	public void setConnection() {
//		try {
//			// ���� ���� ����
//			socket = new Socket(LinKlipboard.SERVER_IP, LinKlipboardClient.getPortNum());
//			// ��Ʈ�� ����
//			dis = new DataInputStream(socket.getInputStream()); // ����Ʈ �迭�� �ޱ� ���� �����ͽ�Ʈ�� ����
//			System.out.println("[FileReceiveDataToServer] ���� ���� ��");
//
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	/** �����ִ� ���ϰ� ��Ʈ���� ��� �ݴ´�. */
//	@Override
//	public void closeSocket() {
//		try {
//			dis.close();
//			fos.close();
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
//			LinKlipboardClient.initDir(); // ���ϻ��� �� ���� �ʱ�ȭ
//
//			byte[] ReceiveByteArrayToFile = new byte[LinKlipboard.byteSize]; // ����Ʈ �迭 ����
//			int EndOfFile = 0; // ������ ��(-1)�� �˸��� ���� ����
//
//			System.out.println("[FileReceiveDataToServer] ���� ���: " + receiveFilePath);
//			fos = new FileOutputStream(receiveFilePath); // ������ ��ο� ����Ʈ �迭�� �������� ���� ��Ʈ�� ����
//
//			/*
//			 * ReceiveByteArrayToFile�� ũ���� 1024����Ʈ ��ŭ DataInputStream���� ����Ʈ�� �о� ����Ʈ �迭�� ����, EndOfFile���� 1024�� ������� DataInputStream���� ����Ʈ�� �� �о�� ��(EndOfFile=-1 �� ��)���� �ݺ�
//			 */
//			while ((EndOfFile = dis.read(ReceiveByteArrayToFile)) != -1) {
//				// ReceiveByteArrayToFile�� ����ִ� ����Ʈ�� 0~EndOfFile=1024 ��ŭ FileOutputStream���� ����
//				fos.write(ReceiveByteArrayToFile, 0, EndOfFile);
//			}
//
//			closeSocket();
//
//			setFileInClipboard(receiveFilePath);
//			System.out.println("[FileReceiveDataToServer] Ŭ������ ���� �Ϸ�");
//
//		} catch (IOException e) {
//			closeSocket();
//			return;
//		}
//	}
//
//	/** ���۰����� File ��ü */
//	static class FileTransferable implements Transferable {
//		private ArrayList<File> listOfFiles;
//
//		public FileTransferable(ArrayList<File> listOfFiles) {
//			this.listOfFiles = listOfFiles;
//		}
//
//		@Override
//		public DataFlavor[] getTransferDataFlavors() {
//			return new DataFlavor[] { DataFlavor.javaFileListFlavor };
//		}
//
//		@Override
//		public boolean isDataFlavorSupported(DataFlavor flavor) {
//			return DataFlavor.javaFileListFlavor.equals(flavor);
//		}
//
//		@Override
//		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
//			return listOfFiles;
//		}
//	}
//
//	/**
//	 * ������ ��ο� ��ġ�� ������ �ý��� Ŭ�����忡 �����Ѵ�.
//	 * 
//	 * @param receiveFilePath
//	 *            ������ ������ ���
//	 */
//	public void setFileInClipboard(String receiveFilePath) {
//		File file = new File(receiveFilePath);
//		ArrayList<File> listOfFiles = new ArrayList<File>();
//		listOfFiles.add(file);
//
//		FileTransferable ft = new FileTransferable(listOfFiles);
//
//		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ft, new ClipboardOwner() {
//			@Override
//			public void lostOwnership(Clipboard clipboard, Transferable contents) {
//				System.out.println("Lost ownership");
//			}
//		});
//	}
//
//	/** �����κ��� ���� �����̸����� ���ϰ�θ� �ٽ� ���� */
//	public static void setFilePath() {
//		receiveFilePath = LinKlipboard.fileReceiveDir + "\\" + LinKlipboardClient.getFileName();
//	}
//}
