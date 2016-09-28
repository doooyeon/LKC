package datamanage;

import java.io.Serializable;
import java.util.Vector;

import contents.Contents;

public class ClientInitData implements Serializable{
	private static final long serialVersionUID = -6494489325228283099L;
	
	/** 같은 그룹에 접속되어 있는 클라이언트들 */
	private Vector<String> clients;

	public Vector<String> getClients() {
		return clients;
	}
}