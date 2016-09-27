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
	
	private final SystemTray systemTray = SystemTray.getSystemTray(); // 시스템트레이 얻어옴

	private Image trayIconImage; // 트레이아이콘 이미지
	private PopupMenu trayIconMenu; // 트레이아이콘 우클릭 메뉴
	private MenuItem menuItem; // 트레이아이콘 우클릭 메뉴 항목
	private MouseListener mouseListener; // 트레이아이콘 마우스 리스너
	private TrayIcon trayIcon; // 트레이아이콘

	public TrayIconManager(UserInterfaceManager userInterfaceManager, LinKlipboardClient client) {
		this.frame = userInterfaceManager;
		this.client = client;
		trayIconImage = Toolkit.getDefaultToolkit().getImage("image/LK.png"); // 트레이아이콘 이미지
		trayIconMenu = new PopupMenu();
		trayIcon = new TrayIcon(trayIconImage, "LinKlipboard", trayIconMenu);
	}

	// frame 보여줌
	public void displayMainApp() {
		frame.setVisible(true);
		frame.setExtendedState(Frame.NORMAL); // 윈도우 창 제일 위에 frame이 뜨게 함
	}

	/** 트레이아이콘을 시스템트레이에 추가 */
	public void addTrayIconInSystemTray() {
		if (SystemTray.isSupported()) { // 시스템 트레이가 지원되면
			setMouseEvent();
			setMenu();

			try {
				systemTray.add(trayIcon); // 시스템 트레이에 트레이 아이콘 추가
				trayIcon.setImageAutoSize(true); // 트레이 아이콘 크기 자동 조절
				trayIcon.addMouseListener(mouseListener); // 트레이 아이콘에 마우스 리스너 추가
			} catch (AWTException e) {
				e.printStackTrace();
			}

		} else {
			System.err.println("Tray unavailable");
		}
	}

	/** 트레이아이콘 마우스 이벤트 설정 */
	public void setMouseEvent() {
		/* 트레이 아이콘 마우스 리스너 */
		mouseListener = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) { // 트레이 아이콘을 더블 클릭하면
					displayMainApp(); // frame을 보여줌
				}
			}
		};
	}

	/** 트레이아이콘 우클릭 메뉴 설정 */
	public void setMenu() {

		// menuItem = new MenuItem("Main APP"); // frame 보여줌
		// menuItem.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// //displayMainApp();
		// }
		// });
		// trayIconMenu.add(item);

		menuItem = new MenuItem("Close"); // 프로그램 종료
		menuItem.addActionListener(new ActionListener() { // Close 메뉴에 대한 액션 리스너
			public void actionPerformed(ActionEvent e) {
				new NotifyLogoutToServer(client).requestReportExit();
				
				systemTray.remove(trayIcon); // 시스템트레이에서 트레이아이콘 제거
				System.exit(0); // 프로그램 종료
			}
		});

		trayIconMenu.add(menuItem);
	}

	/** 트레이아이콘 메시지 띄우기 */
	public void showMsg(String msg) {
		trayIcon.displayMessage("** Arrived Shared Contents **", msg, TrayIcon.MessageType.INFO);
	}
	
	/** 트레이아이콘 메시지 띄우기 */
	public void showRunningMsg(String msg) {
		trayIcon.displayMessage("** Notification **", msg, TrayIcon.MessageType.INFO);
	}
}
