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
	
	/** 종료를 알리는 서블릿 (ReportExit 서블릿 호출) */
	public void requestReportExit() {
		try {
			// 호출할 서블릿의 주소
			URL url = new URL(LinKlipboard.URL_To_CALL + "/ReportExit");
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			// 서버에 보낼 데이터(그룹이름)
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + LinKlipboardClient.getGroupName();

			System.out.println("[requestReportExit] 보낼 전체 데이터 확인" + header);

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
