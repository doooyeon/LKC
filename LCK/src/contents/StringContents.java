package contents;

import java.io.Serializable;

import server_manager.LinKlipboard;

public class StringContents extends Contents implements Serializable {
	private static final long serialVersionUID = 8197233165098147502L;
	private String stringData;

	public StringContents() {
		super();
		type = LinKlipboard.STRING_TYPE;
		setDate();
	}

	public StringContents(String data) {
		this();
		stringData = data;
		setDate();
	}

	public StringContents(String sharer, String data) {
		super(sharer);
		type = LinKlipboard.STRING_TYPE;
		this.stringData = data;
		setDate();
	}

	public String getString() {
		return stringData;
	}

}