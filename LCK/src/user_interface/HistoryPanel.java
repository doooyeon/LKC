package user_interface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import client_manager.LinKlipboardClient;
import contents.Contents;
import contents.FileContents;
import contents.ImageContents;
import contents.StringContents;
import datamanage.History;
import datamanage.ReceivePreviousData;
import server_manager.LinKlipboard;

public class HistoryPanel extends BasePanel {
	private ListPanel listPanel;
	private JButton receiveButton = new JButton();
	private ReceivePreviousData receiveData = new ReceivePreviousData();

	public HistoryPanel(LinKlipboardClient client) {
		super(client);

		setLayout(null);
		setSize(320, 360);

		listPanel = new ListPanel(client, receiveData);

		initComponent();
	}

	private void initComponent() {
		listPanel.setLocation(25, 15);
		add(listPanel);

		receiveButton.setText("Receive");
		receiveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = listPanel.getSelectedListIndex();
				System.out.println(index);

				receiveData.ReceiveData(client, listPanel.getSelcetedList());
			}
		});
		receiveButton.setBounds(215, 290, 80, 23);
		add(receiveButton);
	}
	 
	public void update() {
		remove(listPanel);
		listPanel = new ListPanel(client, receiveData);
		listPanel.setLocation(25, 15);
		add(listPanel);
	}
	
}

class ListPanel extends JPanel {
	private History history;
	private LinKlipboardClient client;
	private DefaultListModel<Contents> model;
	private JList<Contents> listContents;
	private JScrollPane scrollPane;
	private String listToolTipString;
	private static int maxNumOfContents = 10;
	private ReceivePreviousData receiveData;

	public ListPanel(LinKlipboardClient client, ReceivePreviousData receiveData) {
		this.client = client;
		this.history = client.getHistory(); //
		this.receiveData = receiveData;

		setLayout(new BorderLayout());
		setSize(270, 260);

		// addContentsInHistory(); // »©¾ßµÊ

		UIManager.put("ToolTip.background", new Color(254, 239, 229));
		initComponents();
	}

	public void initComponents() {
		listContents = createListContents();
		scrollPane = new JScrollPane(listContents);
		add(scrollPane, BorderLayout.CENTER);
		
		scrollPane.repaint();
	}
	
	public JScrollPane getScrollPane() {
		return scrollPane;
	}
	

	// // »©¾ßµÊ
	// private void addContentsInHistory() {
	// history = new History();
	//
	// history.addSharedContentsInHistory(new StringContents("Dooy", "LinKlipboard"));
	//
	// File file1 = new File("C:\\Users\\Administrator\\Desktop\\clipboard.hwp");
	// history.addSharedContentsInHistory(new FileContents("Delf", file1));
	//
	// ImageIcon image1 = new ImageIcon("image/LK.png");
	// history.addSharedContentsInHistory(new ImageContents("Hee", image1));
	//
	// File file2 = new File("C:\\Users\\Administrator\\Desktop\\LinKlipboard.txt");
	// history.addSharedContentsInHistory(new FileContents("Dooy", file2));
	//
	// ImageIcon image2 = new ImageIcon("image/3.png");
	// history.addSharedContentsInHistory(new ImageContents("Hee", image2));
	//
	// history.addSharedContentsInHistory(new StringContents("Dooy", "LinKlipboard"));
	//
	// history.addSharedContentsInHistory(new StringContents("Dooy", "sprout"));
	// }

	private JList<Contents> createListContents() {
		// create List model
		model = new DefaultListModel<>();

		// add item to model
		for (int i = 0; i < history.getSharedContents().size(); i++) {
			addList(history.getSharedContents().elementAt(i));
		}

		// create JList with model
		JList<Contents> list = new JList<Contents>(model);

		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);

		list.setSelectionBackground(new Color(255, 221, 197));

		list.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				JList l = (JList) e.getSource();
				ListModel m = l.getModel();
				int index = l.locationToIndex(e.getPoint());
				if (index > -1) {
					l.setToolTipText(setListToolTipString((Contents) m.getElementAt(index)));
				}
			}
		});
		
		list.addMouseListener(new MouseAdapter() {
 			@Override
 			public void mousePressed(MouseEvent e) {
 			if (e.getClickCount() == 2) {
 			receiveData.ReceiveData(client, getSelcetedList());
 			}
 		}
 	});

		// set cell renderer
		list.setCellRenderer(new ContentsRenderer());

		return list;
	}

	public void addList(Contents contents) {
		if (model.getSize() == maxNumOfContents) {
			model.remove(maxNumOfContents - 1);
		}

		model.add(0, contents);
	}

	public String setListToolTipString(Contents contents) {

		switch (contents.getType()) {
		case LinKlipboard.STRING_TYPE:
			StringContents stringContents = (StringContents) contents;
			if (stringContents.getString().length() > 200) {
				listToolTipString = "<html><p width=200>" + stringContents.getString().substring(0, 200) + "..."
						+ "<br><br>Added: " + stringContents.getDate() + "</p></html>";
			} else {
				listToolTipString = "<html><p width=200>" + stringContents.getString() + "<br><br>Added: "
						+ stringContents.getDate() + "</p></html>";
			}
			break;
		case LinKlipboard.IMAGE_TYPE:
			ImageContents imageContenst = (ImageContents) contents;
			listToolTipString = "<html><p width=200>" + "Image<br><br>Added: " + imageContenst.getDate()
					+ "</p></html>";
			break;
		case LinKlipboard.FILE_TYPE:
			FileContents fileContenst = (FileContents) contents;
			listToolTipString = "<html><p width=200>" + fileContenst.getFileName() + "<br><br>size:  "
					+ fileContenst.getFileSize() + "byte<br>Added: " + fileContenst.getDate() + "</p></html>";
			break;
		}

		return listToolTipString;
	}

	public int getSelectedListIndex() {
		return history.getSizeOfContentsInHistory() - 1 - listContents.getSelectedIndex();
	}

	public Contents getSelcetedList() {
		return listContents.getSelectedValue();
	}

	public static void setMaxNumOfContents(int num) {
		maxNumOfContents = num;
	}

	class ContentsRenderer extends JPanel implements ListCellRenderer<Contents> {

		private JLabel lbSharer = new JLabel();
		private JLabel lbType = new JLabel();
		private JLabel lbContents = new JLabel();

		public ContentsRenderer() {
			setLayout(new BorderLayout(10, 10));

			JPanel panelText = new JPanel(new GridLayout(0, 1));

			panelText.add(lbSharer);
			panelText.add(lbType);

			add(lbContents, BorderLayout.EAST);
			add(panelText, BorderLayout.CENTER);
		}

		@Override
		public Component getListCellRendererComponent(JList<? extends Contents> list, Contents contents, int index,
				boolean isSelected, boolean cellHasFocus) {

			Font font1 = new Font("¸¼Àº °íµñ", Font.BOLD, 15);
			Font font2 = new Font("¸¼Àº °íµñ", Font.BOLD, 12);

			lbSharer.setText(contents.getSharer());
			lbSharer.setFont(font1);

			switch (contents.getType()) {
			case LinKlipboard.STRING_TYPE:
				StringContents stringContenst = (StringContents) contents;
				lbType.setText("<String>");
				lbType.setFont(font2);
				if (stringContenst.getString().length() > 12) {
					lbContents.setText(stringContenst.getString().substring(0, 11) + "...");
				} else {
					lbContents.setText(stringContenst.getString());
				}
				lbContents.setIcon(null);
				lbContents.setFont(font1);
				break;
			case LinKlipboard.IMAGE_TYPE:
				ImageContents imageContenst = (ImageContents) contents;
				lbType.setText("<Image>");
				lbType.setFont(font2);
				lbContents.setText(null);
				lbContents.setIcon(imageContenst.getResizingImageIcon());
				break;
			case LinKlipboard.FILE_TYPE:
				FileContents fileContenst = (FileContents) contents;
				lbType.setText("<File>");
				lbType.setFont(font2);
				if (fileContenst.getFileName().length() > 12) {
					lbContents.setText(fileContenst.getFileName().substring(0, 11) + "...");
				} else {
					lbContents.setText(fileContenst.getFileName());
				}
				lbContents.setText(fileContenst.getFileName());
				lbContents.setIcon(null);
				lbContents.setFont(font1);
				break;
			}

			lbSharer.setForeground(new Color(254, 97, 0));

			// set Opaque to change background color of JLabel
			lbSharer.setOpaque(true);
			lbType.setOpaque(true);
			lbContents.setOpaque(true);

			// when select item
			if (isSelected) {
				lbSharer.setBackground(list.getSelectionBackground());
				lbType.setBackground(list.getSelectionBackground());
				lbContents.setBackground(list.getSelectionBackground());
				setBackground(list.getSelectionBackground());
			} else { // when don't select
				lbSharer.setBackground(list.getBackground());
				lbType.setBackground(list.getBackground());
				lbContents.setBackground(list.getBackground());
				setBackground(list.getBackground());
			}

			return this;
		}
	}
}
