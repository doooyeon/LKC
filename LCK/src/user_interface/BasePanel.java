package user_interface;

import javax.swing.JPanel;

import client_manager.LinKlipboardClient;

public abstract class BasePanel extends JPanel {

	protected LinKlipboardClient client;
	protected UserInterfaceManager main;
	TrayIconManager trayIcon;
	
	public BasePanel(LinKlipboardClient client) {
		this.client = client;
	}
	
	public BasePanel(LinKlipboardClient client, UserInterfaceManager main) {
		this.client = client;
		this.main = main;
	}
	
	public BasePanel(LinKlipboardClient client, TrayIconManager trayIcon) {
		this.client = client;
		this.trayIcon = trayIcon;
	}

	public BasePanel(LinKlipboardClient client, TrayIconManager trayIcon, UserInterfaceManager main) {
		this.client = client;
		this.trayIcon = trayIcon;
		this.main = main;
	}

	public LinKlipboardClient getClient() {
		return this.client;
	}
}
