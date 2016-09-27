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
	private static Clipboard systemClipboard; // 자신의 시스템 클립보드
	private static int type; // 데이터 타입

	/** ClipboardManager 생성자 */
	public ClipboardManager() {
	}

	/**
	 * 시스템 클립보드에서 Transferable 객체를 읽어와 데이터 타입을 알아내고 Contents 객체로 변환
	 * 
	 * @return settingObject 서버에 전송할 Contents 객체
	 */
	public static Contents readClipboard() {
		Transferable contents = getSystmeClipboardContets();
		setDataFlavor(contents);
		Contents settingObject = extractDataFromContents(contents);

		return settingObject;
	}

	/**
	 * 시스템 클립보드의 Transferable 객체 리턴
	 * 
	 * @return 시스템 클립보드에 존재하는 Transferable 객체
	 */
	public static Transferable getSystmeClipboardContets() {
		systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		return systemClipboard.getContents(null);
	}

	/** 현재 클립보드에 존재하는 Transferable객체가 어떤 타입인지 리턴 */
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
	 * 클립보드의 Transferable 객체가 어떤 타입인지 set하고 리턴
	 * 
	 * @param t
	 *            클립보드의 Transferable 객체
	 * @return t의 DataFlavor의 종류
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
	 * 클립보드의 Transferable 객체를 전송객체로 바꿈
	 * 
	 * @param contents
	 *            클립보드의 Transferable 객체
	 * @return sendObject 실제 전송 객체(Contents 타입) 리턴
	 */
	private static Contents extractDataFromContents(Transferable contents) {
		try {
			String extractString = null;
			Image extractImage = null;
			Contents sendObject = null; // 실제 전송 데이터

			// 클립보드의 내용을 추출
			if (type == LinKlipboard.STRING_TYPE) {
				System.out.println("[ClipboardManager]전송 객체의 타입: 문자열");
				extractString = (String) contents.getTransferData(DataFlavor.stringFlavor); // Transferable객체를 String으로 변환
				sendObject = new StringContents(extractString); // 클립보드로 부터 추출한 String으로 전송객체 생성
				System.out.println("서버에 보낼 contents의 타입: " + sendObject.getType());

			} else if (type == LinKlipboard.IMAGE_TYPE) {
				System.out.println("[ClipboardManager]전송 객체의 타입: 이미지");
				extractImage = (Image) contents.getTransferData(DataFlavor.imageFlavor); // Transferable객체를 ImageIcon으로 변환
				sendObject = new ImageContents(extractImage); // 클립보드로 부터 추출한 ImageIcon으로 전송객체 생성
			} else {
				System.out.println("[ClipboardManager]전송 객체의 타입: 문자열, 이미지가 아님");
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
	 * 전송받은 Contents 객체를 Transferable해서 클립보드에 삽입(문자열, 이미지인 경우)
	 * 
	 * @param data
	 *            전송받은 데이터
	 * @param dataType
	 *            전송받은 데이터 타입
	 */
	public static void writeClipboard(Contents data, int dataType) {
		systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		switch (dataType) {
		case LinKlipboard.STRING_TYPE:
			StringContents stringData = (StringContents) data; // Contents를 stringContents로 다운캐스팅
			String tmpString = stringData.getString(); // StringContent의 String을 얻어옴
			StringSelection stringTransferable = new StringSelection(tmpString); // 클립보드에 넣을 수 있는 Transferable 객체 생성
			systemClipboard.setContents(stringTransferable, null); // 시스템 클립보드에 삽입
			break;
		case LinKlipboard.IMAGE_TYPE:
			ImageContents ImageData = (ImageContents) data; // Contents를 ImageContents로 다운캐스팅
			Image tmpImage = ImageData.getImage().getImage(); // ImageContent의 ImageIcon을 얻고 ImageIcon의 Image를 얻어옴
			ImageTransferable Imagetransferable = new ImageTransferable(tmpImage); // 클립보드에 넣을 수 있는 Transferable 객체 생성
			systemClipboard.setContents(Imagetransferable, null); // 시스템 클립보드에 삽입
			break;
		default:
			break;
		}
	}

	/** 전송가능한 Image 객체 */
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
