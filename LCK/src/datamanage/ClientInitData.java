package datamanage;

import java.io.Serializable;
import java.util.Vector;

import contents.Contents;

public class ClientInitData implements Serializable{
	private static Vector<Contents> history;
	private static Vector<String> clients;

	public Vector<Contents> getHistory() {
		return ClientInitData.history;
	}

	public Vector<String> getClients() {
		return ClientInitData.clients;
	}
}