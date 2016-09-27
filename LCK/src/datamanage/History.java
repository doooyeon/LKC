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
	private static int historySize = LinKlipboard.HISTORY_DEFAULT;; // 사용자가 원하는 히스토리 크기 // 도연 수정
	
	public HistoryPanel historyPanel;
	public ConnectionPanel connectionPanel;
	
	
	/** History 생성자 */
	public History() {
		sharedContents = new Vector<Contents>();
		resizingImgContents = new Vector<ImageIcon>();
	}

	/**
	 * 전달받은 latestContents를 Vector<Contents>에 넣고 Contents의 타입이 이미지 이면 Vector<ImageIcon> 에 resizing된 이미지를 넣음
	 * 
	 * @param latestContents
	 *            새로 공유받은 최신의 Contents
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

	/** 히스토리의 데이터가 historySize만큼 차면 데이터 하나 삭제 */
	public void removeContents() {
		System.out.println("[History] 들어가기 전 sharedContents의 크기: " + this.sharedContents.size());
		if (this.sharedContents.size() == History.historySize) { // 도연 수정
			this.sharedContents.remove(0);
		}
	}
	
	public void removeAllHistory() {
		sharedContents.removeAllElements();
		resizingImgContents.removeAllElements();
	}

	/** 전달받은 Contents가 어떤 타입인지에 따라서 resizingImgContents를 세팅 */
	public void setResizingImgContents(Contents contents) {
		if (contents.getType() == LinKlipboard.IMAGE_TYPE) {
			//리사이징 이미지 삽입
			ImageContents ImageData = (ImageContents) contents;
			this.resizingImgContents.add(ImageData.getResizingImageIcon());
		} else {
			//빈 이미지 객체 삽입
			System.out.println("[History] setResizingImgContents 빈 이미지 객체 삽입 전");
			this.resizingImgContents.add(new ImageIcon());
			System.out.println("[History] setResizingImgContents 빈 이미지 객체 삽입 후");
		}
	}

	/** 받은 Vector<Contents>로 sharedContents 초기화 */
	public void initSharedContents(Vector<Contents> updateHistory) {
		System.out.println("[History] initSharedContents 메소드 들어옴 ");
		//sharedContents = updateHistory;
		sharedContents = new Vector<Contents>(updateHistory);
		System.out.println("[History]" + sharedContents.get(0).getType());
	}

	/** Vector<Contents>에 있는 Contents들이 어떤 타입인지에 따라서 resizingImgContents 초기화 */
	public void InitResizingImgContents() {
		System.out.println("[History] InitResizingImgContents 메소드 들어옴 ");
		System.out.println("[History] sharedContents.size(): " + sharedContents.size());
		
		// Vector<Contents> 값을 돌면서 Vector<ImageIcon>를 채워 넣는다.
		for (int i = 0; i < sharedContents.size(); i++) {
			setResizingImgContents(sharedContents.elementAt(i));
		}
	}

	/** 사용자가 원하는 히스토리의 크기 세팅 */
	public static void setHistorySize(int historySize) { // 도연 수정
		History.historySize = historySize;
	}

	// /** Vector<Contents>와 Vector<ImageIcon>를 세팅 */
	// public void setVector(Vector<Contents> sharedContents, Vector<ImageIcon> resizingImgContents) {
	// this.sharedContents = sharedContents;
	// this.resizingImgContents = resizingImgContents;
	// }

	/** 히스토리의 Contents에 고유번호를 세팅 */
	public void setContentsSerialNum(Contents sharedContents, int serialNum) {
		sharedContents.setSerialNum(serialNum);
	}

	public void setHistory() {
		sharedContents = new Vector<Contents>();
		resizingImgContents = new Vector<ImageIcon>();
	}

	public void setHistory(Vector<Contents> updateHistory) {
		System.out.println("[History] setHistory메소드 호출");
		sharedContents = new Vector<Contents>();
		resizingImgContents = new Vector<ImageIcon>();
		initSharedContents(updateHistory);
		InitResizingImgContents();
	}

	/** index에 해당하는 Contents를 반환 */
	public Contents getRequestContents(int index) {
		return sharedContents.elementAt(index);
	}

	/** 히스토리의 마지막 Contents를 반환 */
	public Contents getlastContents() {
		return sharedContents.lastElement();
	}

	/** 사용자에게 보여지는 히스토리 크기 반환 */
	public int getHistorySize() {
		return History.historySize; // 도연 수정
	}

	/** 사용자의 실제 히스토리에 들어있는 Contents 크기 반환 */
	public int getSizeOfContentsInHistory() {
		return this.sharedContents.size();
	}

	/** Vector<Contents> sharedContents를 반환 */
	public Vector<Contents> getSharedContents() {
		return this.sharedContents;
	}

	/** Vector<ImageIcon> resizingImgContents를 반환 */
	public Vector<ImageIcon> getResizingImgContents() {
		return this.resizingImgContents;
	}
}

class HistoryViewr {
	public void setViewer() {

	}
}