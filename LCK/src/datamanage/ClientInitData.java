package datamanage;

import java.io.Serializable;
import java.util.Vector;

import contents.Contents;

public class ClientInitData implements Serializable{
	private static final long serialVersionUID = -6494489325228283099L;
	
	/** ���� �׷쿡 ���ӵǾ� �ִ� Ŭ���̾�Ʈ�� */
	private Vector<String> clients;

	public Vector<String> getClients() {
		return clients;
	}
}