package client_manager;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import contents.Contents;
import contents.ImageContents;
import contents.StringContents;
import server_manager.LinKlipboard;

public class ClipboardManager {
	private static Clipboard systemClipboard; // �ڽ��� �ý��� Ŭ������
	private static int type; // ������ Ÿ��

	/** ClipboardManager ������ */
	public ClipboardManager() {
	}

	/**
	 * �ý��� Ŭ�����忡�� Transferable ��ü�� �о�� ������ Ÿ���� �˾Ƴ��� Contents ��ü�� ��ȯ
	 * 
	 * @return settingObject ������ ������ Contents ��ü
	 */
	public static Contents readClipboard() {
		Transferable contents = getSystmeClipboardContets();
		setDataFlavor(contents);
		Contents settingObject = extractDataFromContents(contents);

		return settingObject;
	}

	/**
	 * �ý��� Ŭ�������� Transferable ��ü ����
	 * 
	 * @return �ý��� Ŭ�����忡 �����ϴ� Transferable ��ü
	 */
	public static Transferable getSystmeClipboardContets() {
		systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		return systemClipboard.getContents(null);
	}

	/** ���� Ŭ�����忡 �����ϴ� Transferable��ü�� � Ÿ������ ���� */
	public static int getClipboardDataTypeNow() {
		DataFlavor type = setDataFlavor(getSystmeClipboardContets());

		if (type.equals(DataFlavor.stringFlavor)) {
			return LinKlipboard.STRING_TYPE;
		} else if (type.equals(DataFlavor.imageFlavor)) {
			return LinKlipboard.IMAGE_TYPE;
		} else if (type.equals(DataFlavor.javaFileListFlavor)) {
			return LinKlipboard.FILE_TYPE;
		} else {
			return -1;
		}
	}

	/**
	 * Ŭ�������� Transferable ��ü�� � Ÿ������ set�ϰ� ����
	 * 
	 * @param t
	 *            Ŭ�������� Transferable ��ü
	 * @return t�� DataFlavor�� ����
	 */
	public static DataFlavor setDataFlavor(Transferable t) {
		DataFlavor[] flavors = t.getTransferDataFlavors();

		for (int i = 0; i < flavors.length; i++) {

			if (flavors[i].equals(DataFlavor.stringFlavor)) {
				type = LinKlipboard.STRING_TYPE;
				return DataFlavor.stringFlavor;
			} else if (flavors[i].equals(DataFlavor.imageFlavor)) {
				type = LinKlipboard.IMAGE_TYPE;
				return DataFlavor.imageFlavor;
			} else if (flavors[i].equals(DataFlavor.javaFileListFlavor)) {
				type = LinKlipboard.FILE_TYPE;
				return DataFlavor.javaFileListFlavor;
			} else {
			}
		}
		return null;
	}

	/**
	 * Ŭ�������� Transferable ��ü�� ���۰�ü�� �ٲ�
	 * 
	 * @param contents
	 *            Ŭ�������� Transferable ��ü
	 * @return sendObject ���� ���� ��ü(Contents Ÿ��) ����
	 */
	private static Contents extractDataFromContents(Transferable contents) {
		try {
			String extractString = null;
			Image extractImage = null;
			Contents sendObject = null; // ���� ���� ������

			// Ŭ�������� ������ ����
			if (type == LinKlipboard.STRING_TYPE) {
				System.out.println("[ClipboardManager]���� ��ü�� Ÿ��: ���ڿ�");
				extractString = (String) contents.getTransferData(DataFlavor.stringFlavor); // Transferable��ü�� String���� ��ȯ
				sendObject = new StringContents(extractString); // Ŭ������� ���� ������ String���� ���۰�ü ����
				System.out.println("������ ���� contents�� Ÿ��: " + sendObject.getType());

			} else if (type == LinKlipboard.IMAGE_TYPE) {
				System.out.println("[ClipboardManager]���� ��ü�� Ÿ��: �̹���");
				extractImage = (Image) contents.getTransferData(DataFlavor.imageFlavor); // Transferable��ü�� ImageIcon���� ��ȯ
				sendObject = new ImageContents(extractImage); // Ŭ������� ���� ������ ImageIcon���� ���۰�ü ����
			} else {
				System.out.println("[ClipboardManager]���� ��ü�� Ÿ��: ���ڿ�, �̹����� �ƴ�");
			}

			return sendObject;

		} catch (UnsupportedFlavorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���۹��� Contents ��ü�� Transferable�ؼ� Ŭ�����忡 ����(���ڿ�, �̹����� ���)
	 * 
	 * @param data
	 *            ���۹��� ������
	 * @param dataType
	 *            ���۹��� ������ Ÿ��
	 */
	public static void writeClipboard(Contents data, int dataType) {
		systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		switch (dataType) {
		case LinKlipboard.STRING_TYPE:
			StringContents stringData = (StringContents) data; // Contents�� stringContents�� �ٿ�ĳ����
			String tmpString = stringData.getString(); // StringContent�� String�� ����
			StringSelection stringTransferable = new StringSelection(tmpString); // Ŭ�����忡 ���� �� �ִ� Transferable ��ü ����
			systemClipboard.setContents(stringTransferable, null); // �ý��� Ŭ�����忡 ����
			break;
		case LinKlipboard.IMAGE_TYPE:
			ImageContents ImageData = (ImageContents) data; // Contents�� ImageContents�� �ٿ�ĳ����
			Image tmpImage = ImageData.getImage().getImage(); // ImageContent�� ImageIcon�� ��� ImageIcon�� Image�� ����
			ImageTransferable Imagetransferable = new ImageTransferable(tmpImage); // Ŭ�����忡 ���� �� �ִ� Transferable ��ü ����
			systemClipboard.setContents(Imagetransferable, null); // �ý��� Ŭ�����忡 ����
			break;
		default:
			break;
		}
	}

	/** ���۰����� Image ��ü */
	static class ImageTransferable implements Transferable {
		private Image image;

		public ImageTransferable(Image image) {
			this.image = image;
		}

		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
			if (isDataFlavorSupported(flavor)) {
				return image;
			} else {
				throw new UnsupportedFlavorException(flavor);
			}
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor == DataFlavor.imageFlavor;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.imageFlavor };
		}
	}

}
