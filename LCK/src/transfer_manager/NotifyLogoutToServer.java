package transfer_manager;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import client_manager.LinKlipboardClient;
import server_manager.LinKlipboard;

public class NotifyLogoutToServer {
	private LinKlipboardClient client;
	
	public NotifyLogoutToServer(LinKlipboardClient client) {
		this.client = client;
	}
	
	/** ���Ḧ �˸��� ���� (ReportExit ���� ȣ��) */
	public void requestReportExit() {
		try {
			// ȣ���� ������ �ּ�
			URL url = new URL(LinKlipboard.URL_To_CALL + "/ReportExit");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// ������ ���� ������(�׷��̸�)
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + LinKlipboardClient.getGroupName();

			System.out.println("[requestReportExit] ���� ��ü ������ Ȯ��" + header);

			bout.write(header);
			bout.flush();
			bout.close();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
