package start_manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeoutException;

import client_manager.LinKlipboardClient;
import server_manager.LinKlipboard;
import transfer_manager.ResponseHandler;

public class StartToProgram {

	private LinKlipboardClient client;

	private String response; // �����κ��� ���� ���� ����
	private ResponseHandler responseHandler; // ���信 ���� ó��

	private String groupName; // �׷��̸�
	private String password; // �н�����
	private String orderMsg; // ����ڰ� ���ϴ� ���(create/join)

	/**
	 * StartToProgram ������
	 * 
	 * @param client
	 *            ���α׷��� �����ϴ� �����
	 */
	public StartToProgram(LinKlipboardClient client, String orderMsg) {
		this.client = client;
		this.groupName = LinKlipboardClient.getGroupName();
		this.password = client.getGroupPassword();
		this.orderMsg = orderMsg;
		this.response = null;

		startProgram();
	}

	public StartToProgram(LinKlipboardClient client) {
		this.client = client;
		this.groupName = LinKlipboardClient.getGroupName();
		this.password = client.getGroupPassword();
		this.response = null;
	}

	public void startProgram() {
		// ������ư�� ������ �� �޼ҵ尡 ����
		if (orderMsg.equals("create")) {
			sendGroupInfoToServer("/CreateGroup");
		}
		// ���ӹ�ư�� ������ �� �޼ҵ尡 ����
		else if (orderMsg.equals("join")) {
			sendGroupInfoToServer("/JoinGroup");
		}
	}

	/** �׷� ������ ������ ������ ����(response)�޴� �޼ҵ� */
	public void sendGroupInfoToServer(String servletName) {
		String response = Integer.toString(LinKlipboard.ERROR_TRYCATCH);

		try {
			// ȣ���� ������ URL
			URL url = new URL(LinKlipboard.URL_To_CALL + servletName);
			URLConnection conn = url.openConnection();
			System.out.println(conn);

			// servlet�� doPostȣ��
			conn.setDoOutput(true);

			// ������ ���� ������(�׷�����)
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + groupName + "&" + "password=" + password;

			// server�� �׷��̸��� �н����� ����(servlet�� �޴� ������: &)
			bout.write(header);
			bout.flush();
			bout.close();

			// �����κ��� ���� ������(��������)
			InputStream is = conn.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader bin = new BufferedReader(isr);

			if ((response = bin.readLine()) != null) {
				// �������� Ȯ�� �� Ŭ���̾�Ʈ�� ���� ��� �޼���
				this.response = response;
			}

			// // ���� TEST ����
			// // ������ ���� ������(�׷�����)
			// String message = "doy";
			//
			// // server�� �׷��̸��� �н����� ����(servlet�� �޴� ������: &)
			// bout.write(message);
			// bout.flush();
			// bout.close();
			// ���� TEST ��

			bin.close();

			exceptionHandling(this.response);
			System.out.println("[sendGroupInfoToServer] " + ResponseHandler.getErrorCodeNum());

			if (ResponseHandler.getErrorCodeNum() == LinKlipboard.ACCESS_PERMIT) {
				// if (orderMsg.equals("create")) {
				// LinKlipboardClient.setHistory();
				//
				// } else if (orderMsg.equals("join")) {
				// // ������ �ִ� Vector<Contents>�� �޴´�.
				// LinKlipboardClient.setHistory();
				// new GetInitDataFromServer(client);
				// }

				LinKlipboardClient.setHistory();
			}

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		this.response = response;
	}

	/** Ŭ���̾�Ʈ ������ �����ϱ� ���� �޼ҵ�(���� �г��Ӹ�) */
	public void requestChangeInfoToServer(String nickName) {
		String response = Integer.toString(LinKlipboard.ERROR_TRYCATCH);

		try {

			// ȣ���� ������ URL
			URL url = new URL(LinKlipboard.URL_To_CALL + "/ChangeSettingOfClient");
			URLConnection conn = url.openConnection();

			// servlet�� doPostȣ��
			conn.setDoOutput(true);
			// conn.setDoInput(true);

			// ������ ���� ������(�г���)
			BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			String header = "groupName=" + groupName + "&" + "nickname=" + nickName;

			// server�� �׷��̸��� �н����� ����(servlet�� �޴� ������: &)
			bout.write(header);
			bout.flush();
			bout.close();

			// �����κ��� ���� ������(��������)
			BufferedReader bin = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			// if ((response = bin.readLine()) != null) {
			// // �������� Ȯ�� �� Ŭ���̾�Ʈ�� ���� ��� �޼���
			// //this.response = new String(response.getBytes("utf-8"),
			// "utf-8");
			// this.response = response;
			// System.out.println(this.response);
			// }
			// else {
			// System.out.println("�� ���̾� ¥�ľ�");
			// }
			// bin.close();
			this.response = Integer.toString(LinKlipboard.COMPLETE_APPLY);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			exceptionHandling(this.response);
			System.out.println("[requestChangeInfoToServer] " + ResponseHandler.getErrorCodeNum());

			if (ResponseHandler.getErrorCodeNum() == LinKlipboard.COMPLETE_APPLY) {
				// ���� �г����� �����Ѵ�.
				LinKlipboardClient.setNickName(nickName);
			}

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		this.response = response;
	}

	/**
	 * ���� ó��
	 * 
	 * @param response
	 *            Ŭ���̾�Ʈ ��û�� ���� ������ ����
	 */
	public void exceptionHandling(String response) {
		responseHandler = new ResponseHandler(response, client);
		if (response != null) {
			responseHandler.responseHandlerForStart();
		} else {
			System.out.println("[StartToProgram] Error!!!! ������ ���� response�� null��");
		}
	}

	// /** �����κ��� ���� ���� ������ ��ȯ */
	// public String getResponse() {
	// return response;
	// }
	// //UI�� �� �ʿ��ҵ�

	public static String convert(String str) throws IOException {
		ByteArrayOutputStream requestOutputStream = new ByteArrayOutputStream();
		requestOutputStream.write(str.getBytes("EUC-KR"));
		return requestOutputStream.toString("EUC-KR");
	}
}
