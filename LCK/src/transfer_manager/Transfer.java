package transfer_manager;

import java.net.Socket;

import client_manager.LinKlipboardClient;

/** 데이터 전송에 대한 클래스 */
public abstract class Transfer extends Thread {
	protected LinKlipboardClient client;
	protected Socket socket; // 서버와 연결할 소켓

	/** Transfer 생성자 */
	public Transfer(LinKlipboardClient client) {
		this.client = client;
	}

	/** 소켓을 열고 클라이언트의 접속을 기다린다. */
	abstract public void setConnection();

	/** 열린 소켓을 닫는다. */
	abstract public void closeSocket();

}
