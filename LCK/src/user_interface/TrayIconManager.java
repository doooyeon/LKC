package user_interface;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import client_manager.LinKlipboardClient;
import transfer_manager.NotifyLogoutToServer;

public class TrayIconManager {
	UserInterfaceManager frame;
	LinKlipboardClient client;
	
	private final SystemTray systemTray = SystemTray.getSystemTray(); // �ý���Ʈ���� ����

	private Image trayIconImage; // Ʈ���̾����� �̹���
	private PopupMenu trayIconMenu; // Ʈ���̾����� ��Ŭ�� �޴�
	private MenuItem menuItem; // Ʈ���̾����� ��Ŭ�� �޴� �׸�
	private MouseListener mouseListener; // Ʈ���̾����� ���콺 ������
	private TrayIcon trayIcon; // Ʈ���̾�����

	public TrayIconManager(UserInterfaceManager userInterfaceManager, LinKlipboardClient client) {
		this.frame = userInterfaceManager;
		this.client = client;
		trayIconImage = Toolkit.getDefaultToolkit().getImage("image/LK.png"); // Ʈ���̾����� �̹���
		trayIconMenu = new PopupMenu();
		trayIcon = new TrayIcon(trayIconImage, "LinKlipboard", trayIconMenu);
	}

	// frame ������
	public void displayMainApp() {
		frame.setVisible(true);
		frame.setExtendedState(Frame.NORMAL); // ������ â ���� ���� frame�� �߰� ��
	}

	/** Ʈ���̾������� �ý���Ʈ���̿� �߰� */
	public void addTrayIconInSystemTray() {
		if (SystemTray.isSupported()) { // �ý��� Ʈ���̰� �����Ǹ�
			setMouseEvent();
			setMenu();

			try {
				systemTray.add(trayIcon); // �ý��� Ʈ���̿� Ʈ���� ������ �߰�
				trayIcon.setImageAutoSize(true); // Ʈ���� ������ ũ�� �ڵ� ����
				trayIcon.addMouseListener(mouseListener); // Ʈ���� �����ܿ� ���콺 ������ �߰�
			} catch (AWTException e) {
				e.printStackTrace();
			}

		} else {
			System.err.println("Tray unavailable");
		}
	}

	/** Ʈ���̾����� ���콺 �̺�Ʈ ���� */
	public void setMouseEvent() {
		/* Ʈ���� ������ ���콺 ������ */
		mouseListener = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) { // Ʈ���� �������� ���� Ŭ���ϸ�
					displayMainApp(); // frame�� ������
				}
			}
		};
	}

	/** Ʈ���̾����� ��Ŭ�� �޴� ���� */
	public void setMenu() {

		// menuItem = new MenuItem("Main APP"); // frame ������
		// menuItem.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// //displayMainApp();
		// }
		// });
		// trayIconMenu.add(item);

		menuItem = new MenuItem("Close"); // ���α׷� ����
		menuItem.addActionListener(new ActionListener() { // Close �޴��� ���� �׼� ������
			public void actionPerformed(ActionEvent e) {
				new NotifyLogoutToServer(client).requestReportExit();
				
				systemTray.remove(trayIcon); // �ý���Ʈ���̿��� Ʈ���̾����� ����
				System.exit(0); // ���α׷� ����
			}
		});

		trayIconMenu.add(menuItem);
	}

	/** Ʈ���̾����� �޽��� ���� */
	public void showMsg(String msg) {
		trayIcon.displayMessage("** Arrived Shared Contents **", msg, TrayIcon.MessageType.INFO);
	}
	
	/** Ʈ���̾����� �޽��� ���� */
	public void showRunningMsg(String msg) {
		trayIcon.displayMessage("** Notification **", msg, TrayIcon.MessageType.INFO);
	}
}
