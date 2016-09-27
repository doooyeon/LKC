package user_interface;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import client_manager.ClipboardManager;
import client_manager.LinKlipboardClient;
import contents.Contents;
import contents.FileContents;
import contents.StringContents;
import server_manager.LinKlipboard;
import transfer_manager.FileReceiveDataToServer;
import transfer_manager.FileSendDataToServer;
import transfer_manager.SendDataToServer;

public class ConnectionPanel extends BasePanel {
	private JLabel accessGroupNameLabel; // 자신이 속한 그룹명
	private JLabel accessCountLabel = new JLabel(); // 같은 그룹에 들어온 접속자 수

	DefaultListModel<String> model; // client의 Vector<String>값을 저장
	private JScrollPane accessPersonScrollPane;
	private JList<String> accessPersonList; // 접속자 리스트

	private JLabel sharedIcon = new JLabel();
	private JLabel sharedTimeLabel = new JLabel();// 최신 공유한 시간 정보
	private JLabel sharedContentsInfoLabel = new JLabel(); // 최신 공유 Content정보

	private JButton receiveButton = new JButton();

	private SendDataToServer sendStrImg;
	private FileSendDataToServer sendFile;
	private FileReceiveDataToServer receiveFile;

	public ConnectionPanel(LinKlipboardClient client) {
		super(client);

		setLayout(null);
		setSize(320, 360);

		initComponents();
	}

	private void initComponents() {
		Font labelFont = UIManager.getFont("Label.font");
		System.out.println("Label.font is " + labelFont);

		accessGroupNameLabel = new JLabel();
		accessGroupNameLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		accessGroupNameLabel.setBounds(24, 20, 80, 20);
		// accessGroupNameLabel.setBackground(Color.GRAY);
		// accessGroupNameLabel.setOpaque(true);
		add(accessGroupNameLabel);

		System.out.println("[Connect] " + client.getOtherClients().size());
		// accessCountLabel.setText("현재 " + client.getOtherClients().size() + "명 접속 중");
		accessCountLabel.setBounds(187, 20, 104, 20);
		// accessCountLabel.setBackground(Color.GRAY);
		// accessCountLabel.setOpaque(true);
		add(accessCountLabel);

		initClientList();

		sharedIcon.setIcon(new ImageIcon("image/sharedImage.png"));
		sharedIcon.setBounds(24, 220, 20, 20);
		// sharedIcon.setBackground(Color.GRAY);
		// sharedIcon.setOpaque(true);
		add(sharedIcon);

		// 최신 공유된 Contents가 없으면
		if (LinKlipboardClient.getLatestContents() == null) {
			sharedTimeLabel.setText("[" + now() + "]");
			sharedContentsInfoLabel.setText("최신 공유된 Contents가 없습니다.");
		} else {
			Contents latestContents = LinKlipboardClient.getLatestContents();
			String sharer = latestContents.getSharer();
			String dataType = null;
			String dataInfo = null;

			sharedTimeLabel.setText("[" + latestContents.getDate() + "]");

			if (latestContents.getType() == LinKlipboard.FILE_TYPE) {
				FileContents fc = new FileContents();
				fc = (FileContents) latestContents;
				dataType = "파일";
				dataInfo = "<" + fc.getFileName() + ">";
			} else if (latestContents.getType() == LinKlipboard.STRING_TYPE) {
				StringContents sc = new StringContents();
				sc = (StringContents) latestContents;
				dataType = "텍스트";
				dataInfo = "<" + sc.getString() + ">";
			} else if (latestContents.getType() == LinKlipboard.IMAGE_TYPE) {
				dataType = "이미지";
				dataInfo = "";
			} else {
				dataInfo = "지원하지 않는 컨텐츠";
			}

			if (sharer.length() > 7) {
				sharer = dealLengthOfDataInfo(sharer, 7);
			}
			if (dataInfo.length() > 16) {
				dataInfo = dealLengthOfDataInfo(dataInfo, 16);
			}

			sharedContentsInfoLabel
					.setText(LinKlipboardClient.getLatestContents().getSharer() + "님이 " + dataInfo  + dataType + " 공유");
		}

		sharedTimeLabel.setBounds(50, 220, 150, 20);
		// sharedTimeLabel.setBackground(Color.GRAY);
		// sharedTimeLabel.setOpaque(true);
		add(sharedTimeLabel);

		sharedContentsInfoLabel.setBounds(24, 250, 270, 20);
		sharedContentsInfoLabel.setHorizontalAlignment(JLabel.CENTER);
		// sharedContentsInfoLabel.setBackground(Color.GRAY);
		// sharedContentsInfoLabel.setOpaque(true);
		add(sharedContentsInfoLabel);

		receiveButton.setText("Receive");
		receiveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				receiveButtonActionPerformed(evt);
			}
		});
		receiveButton.setBounds(215, 290, 80, 23);
		add(receiveButton);
	}

	public void initClientList() {
		model = new DefaultListModel<>();
		// add item to model
		for (int i = 0; i < client.getOtherClients().size(); i++) {
			model.add(0, client.getOtherClients().elementAt(i));
		}

		// create JList with model
		accessPersonList = new JList<String>(model);

		accessPersonScrollPane = new JScrollPane(accessPersonList);
		accessPersonScrollPane.setBounds(24, 50, 270, 150);
		add(accessPersonScrollPane);
	}

	public void updateGroupName() {
		accessGroupNameLabel.setText(LinKlipboardClient.getGroupName());
	}

	public void updateAccessGroup() {
		accessCountLabel.setText("현재 " + client.getOtherClients().size() + "명 접속 중");

		remove(accessPersonScrollPane);
		initClientList();
		add(accessPersonScrollPane);
		accessPersonScrollPane.repaint();
	}

	public void updateSharedContents(Contents latestContents) {
		LinKlipboardClient.getLatestContents();
		sharedTimeLabel.setText("[" + latestContents.getDate() + "]");

		String sharer = latestContents.getSharer();
		String dataType = null;
		String dataInfo = null;

		sharedTimeLabel.setText("[" + latestContents.getDate() + "]");

		if (latestContents.getType() == LinKlipboard.FILE_TYPE) {
			FileContents fc = new FileContents();
			fc = (FileContents) latestContents;
			dataType = "파일";
			dataInfo = "<" + fc.getFileName() + ">";
		} else if (latestContents.getType() == LinKlipboard.STRING_TYPE) {
			StringContents sc = new StringContents();
			sc = (StringContents) latestContents;
			dataType = "텍스트";
			dataInfo = "<" + sc.getString()+ ">";
		} else if (latestContents.getType() == LinKlipboard.IMAGE_TYPE) {
			dataType = "이미지";
			dataInfo = "";
		} else {
			dataInfo = "지원하지 않는 컨텐츠";
		}

		if (sharer.length() > 7) {
			sharer = dealLengthOfDataInfo(sharer, 7);
		}
		if (dataInfo.length() > 16) {
			dataInfo = dealLengthOfDataInfo(dataInfo, 16);
		}

		sharedContentsInfoLabel.setText(latestContents.getSharer() + "님이 " + dataInfo  + dataType + " 공유");
	}
	
	public void updateSharedContents() {
		LinKlipboardClient.getLatestContents();
		sharedTimeLabel.setText("[" + now() + "]");
		sharedContentsInfoLabel.setText("최신 공유된 Contents가 없습니다.");
	}

	/** 최신으로 공유된 Contents를 받아온다. */
	private void receiveButtonActionPerformed(ActionEvent evt) {
		client.settLatestContents();

		Contents latestContentsFromServer = LinKlipboardClient.getLatestContents();
		int latestContentsType = LinKlipboardClient.getLatestContents().getType();

		if (latestContentsType == LinKlipboard.FILE_TYPE) {
			receiveFile = new FileReceiveDataToServer(client);
			receiveFile.requestReceiveFileData();
		} else if (latestContentsType == LinKlipboard.STRING_TYPE) {
			ClipboardManager.writeClipboard(latestContentsFromServer, latestContentsType);
		} else if (latestContentsType == LinKlipboard.IMAGE_TYPE) {
			ClipboardManager.writeClipboard(latestContentsFromServer, latestContentsType);
		} else {
			System.out.println("[ConnectionPanel] File, String, Image 어디에도 속하지 않음");
		}
	}

	/** 레이블의 크기를 넘어가면 ...으로 처리 */
	public String dealLengthOfDataInfo(String dataInfo, int cutSize) {
		return dataInfo.substring(0, cutSize) + "..";
	}


	/** @return YYYY-MM-DD HH:MM:SS 형식의 현재 시간 */
	public static String now() {
		Calendar cal = Calendar.getInstance();
		String year = Integer.toString(cal.get(Calendar.YEAR));
		String month = Integer.toString(cal.get(Calendar.MONTH)+1);
		String date = Integer.toString(cal.get(Calendar.DATE));
		String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		if (Integer.parseInt(hour) < 10) {
			hour = "0" + hour;
		}
		if (Integer.parseInt(hour) > 12) {
			hour = "오후 " + Integer.toString(Integer.parseInt(hour) - 12);
		} else {
			hour = "오전 " + hour;
		}

		String minute = Integer.toString(cal.get(Calendar.MINUTE));
		if (Integer.parseInt(minute) < 10) {
			minute = "0" + minute;
		}
		String sec = Integer.toString(cal.get(Calendar.SECOND));
		if (Integer.parseInt(sec) < 10) {
			sec = "0" + sec;
		}

		return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + sec;
	}
}
