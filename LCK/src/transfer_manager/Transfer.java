package transfer_manager;

import java.net.Socket;

import client_manager.LinKlipboardClient;

/** ������ ���ۿ� ���� Ŭ���� */
public abstract class Transfer extends Thread {
	protected LinKlipboardClient client;
	protected Socket socket; // ������ ������ ����

	/** Transfer ������ */
	public Transfer(LinKlipboardClient client) {
		this.client = client;
	}

	/** ������ ���� Ŭ���̾�Ʈ�� ������ ��ٸ���. */
	abstract public void setConnection();

	/** ���� ������ �ݴ´�. */
	abstract public void closeSocket();

}
