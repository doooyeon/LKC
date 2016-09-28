package start_manager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import client_manager.LinKlipboardClient;
import datamanage.ClientInitData;
import server_manager.LinKlipboard;
import transfer_manager.Transfer;
import user_interface.ConnectionPanel;

public class GetInitDataFromServer extends Transfer {

	private ObjectInputStream in;

	private ClientInitData initData;

	private ConnectionPanel connectionPanel;

	/** GetTotalHistoryFromServer ������ */
	public GetInitDataFromServer(LinKlipboardClient client, ConnectionPanel connectionPanel) {
		super(client);
		this.connectionPanel = connectionPanel;
		this.start();
	}

	/** �������� ������ ���� ���ϰ� ��Ʈ�� ���� */
	@Override
	public void setConnection() {
		try {
			// ���� ���� ����
			socket = new Socket(LinKlipboard.SERVER_IP, LinKlipboardClient.getPortNum());
			// ��Ʈ�� ����
			in = new ObjectInputStream(socket.getInputStream());
			System.out.println("[GetTotalHistoryFromServer] ���� ���� ��");

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** �����ִ� ���ϰ� ��Ʈ���� ��� �ݴ´�. */
	@Override
	public void closeSocket() {
		try {
			in.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		setConnection();
		// �����κ��� Vector<Contents>�� �޾ƿ´�.
		try {
			System.out.println("[GetTotalHistoryFromServer] Vector<Contents> ���� ��");
			initData = (ClientInitData) in.readObject();
			System.out.println("[GetTotalHistoryFromServer] Vector<Contents> ���� ��");

			System.out.println("[GetTotalHistoryFromServer] ���� initData�� ũ��: " + initData.getClients().size());

			// �����κ��� ���� �����ڵ��� ����
			LinKlipboardClient.setOtherClients(initData.getClients());

			System.out.println("[GetTotalHistoryFromServer] �����κ��� ���� �� ������ �� : " + LinKlipboardClient.getOtherClients().size());

			// �����г��� repaint
			connectionPanel.updateAccessGroup();
			connectionPanel.updateGroupName();
			connectionPanel.updateSharedContents();

			closeSocket();

			System.out.println("[GetTotalHistoryFromServer] Ŭ���̾�Ʈ �����丮 �ʱ�ȭ �Ϸ�");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			closeSocket();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			closeSocket();
			return;
		}
	}
}
