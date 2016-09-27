package datamanage;

import java.util.Vector;

import javax.swing.ImageIcon;

import contents.Contents;
import contents.ImageContents;
import server_manager.LinKlipboard;
import user_interface.ConnectionPanel;
import user_interface.HistoryPanel;

public class History {
	private Vector<Contents> sharedContents;
	private Vector<ImageIcon> resizingImgContents;
	private static int historySize = LinKlipboard.HISTORY_DEFAULT;; // ����ڰ� ���ϴ� �����丮 ũ�� // ���� ����
	
	public HistoryPanel historyPanel;
	public ConnectionPanel connectionPanel;
	
	
	/** History ������ */
	public History() {
		sharedContents = new Vector<Contents>();
		resizingImgContents = new Vector<ImageIcon>();
	}

	/**
	 * ���޹��� latestContents�� Vector<Contents>�� �ְ� Contents�� Ÿ���� �̹��� �̸� Vector<ImageIcon> �� resizing�� �̹����� ����
	 * 
	 * @param latestContents
	 *            ���� �������� �ֽ��� Contents
	 */
	public void addSharedContentsInHistory(Contents latestContents) {
		removeContents();
		this.sharedContents.add(latestContents);
		setResizingImgContents(latestContents);
		
		
		historyPanel.update();
		connectionPanel.updateSharedContents(latestContents);
	}
	
	public void setHistoryPanel(HistoryPanel historyPanel) {
		this.historyPanel = historyPanel;
	}
	
	public void setConnectionPanel(ConnectionPanel connectionPanel) {
		this.connectionPanel = connectionPanel;
	}

	/** �����丮�� �����Ͱ� historySize��ŭ ���� ������ �ϳ� ���� */
	public void removeContents() {
		System.out.println("[History] ���� �� sharedContents�� ũ��: " + this.sharedContents.size());
		if (this.sharedContents.size() == History.historySize) { // ���� ����
			this.sharedContents.remove(0);
		}
	}
	
	public void removeAllHistory() {
		sharedContents.removeAllElements();
		resizingImgContents.removeAllElements();
	}

	/** ���޹��� Contents�� � Ÿ�������� ���� resizingImgContents�� ���� */
	public void setResizingImgContents(Contents contents) {
		if (contents.getType() == LinKlipboard.IMAGE_TYPE) {
			//������¡ �̹��� ����
			ImageContents ImageData = (ImageContents) contents;
			this.resizingImgContents.add(ImageData.getResizingImageIcon());
		} else {
			//�� �̹��� ��ü ����
			System.out.println("[History] setResizingImgContents �� �̹��� ��ü ���� ��");
			this.resizingImgContents.add(new ImageIcon());
			System.out.println("[History] setResizingImgContents �� �̹��� ��ü ���� ��");
		}
	}

	/** ���� Vector<Contents>�� sharedContents �ʱ�ȭ */
	public void initSharedContents(Vector<Contents> updateHistory) {
		System.out.println("[History] initSharedContents �޼ҵ� ���� ");
		//sharedContents = updateHistory;
		sharedContents = new Vector<Contents>(updateHistory);
		System.out.println("[History]" + sharedContents.get(0).getType());
	}

	/** Vector<Contents>�� �ִ� Contents���� � Ÿ�������� ���� resizingImgContents �ʱ�ȭ */
	public void InitResizingImgContents() {
		System.out.println("[History] InitResizingImgContents �޼ҵ� ���� ");
		System.out.println("[History] sharedContents.size(): " + sharedContents.size());
		
		// Vector<Contents> ���� ���鼭 Vector<ImageIcon>�� ä�� �ִ´�.
		for (int i = 0; i < sharedContents.size(); i++) {
			setResizingImgContents(sharedContents.elementAt(i));
		}
	}

	/** ����ڰ� ���ϴ� �����丮�� ũ�� ���� */
	public static void setHistorySize(int historySize) { // ���� ����
		History.historySize = historySize;
	}

	// /** Vector<Contents>�� Vector<ImageIcon>�� ���� */
	// public void setVector(Vector<Contents> sharedContents, Vector<ImageIcon> resizingImgContents) {
	// this.sharedContents = sharedContents;
	// this.resizingImgContents = resizingImgContents;
	// }

	/** �����丮�� Contents�� ������ȣ�� ���� */
	public void setContentsSerialNum(Contents sharedContents, int serialNum) {
		sharedContents.setSerialNum(serialNum);
	}

	public void setHistory() {
		sharedContents = new Vector<Contents>();
		resizingImgContents = new Vector<ImageIcon>();
	}

	public void setHistory(Vector<Contents> updateHistory) {
		System.out.println("[History] setHistory�޼ҵ� ȣ��");
		sharedContents = new Vector<Contents>();
		resizingImgContents = new Vector<ImageIcon>();
		initSharedContents(updateHistory);
		InitResizingImgContents();
	}

	/** index�� �ش��ϴ� Contents�� ��ȯ */
	public Contents getRequestContents(int index) {
		return sharedContents.elementAt(index);
	}

	/** �����丮�� ������ Contents�� ��ȯ */
	public Contents getlastContents() {
		return sharedContents.lastElement();
	}

	/** ����ڿ��� �������� �����丮 ũ�� ��ȯ */
	public int getHistorySize() {
		return History.historySize; // ���� ����
	}

	/** ������� ���� �����丮�� ����ִ� Contents ũ�� ��ȯ */
	public int getSizeOfContentsInHistory() {
		return this.sharedContents.size();
	}

	/** Vector<Contents> sharedContents�� ��ȯ */
	public Vector<Contents> getSharedContents() {
		return this.sharedContents;
	}

	/** Vector<ImageIcon> resizingImgContents�� ��ȯ */
	public Vector<ImageIcon> getResizingImgContents() {
		return this.resizingImgContents;
	}
}

class HistoryViewr {
	public void setViewer() {

	}
}