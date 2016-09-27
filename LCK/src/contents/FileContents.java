package contents;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import server_manager.LinKlipboard;

public class FileContents extends Contents implements Serializable {
	private static final long serialVersionUID = 1559329719938703224L;
	private String fileName;
	private long fileSize;
	private String filePath;

	public FileContents() {
		super();
		type = LinKlipboard.FILE_TYPE;
		// createSendFile();
		setDate();
	}

	public FileContents(String sharer) {
		super(sharer);
		setDate();
	}
	
	public FileContents(File file) {
		fileName = file.getName();
		fileSize = file.length();
		type = LinKlipboard.FILE_TYPE;
		setDate();
	}
	
	public FileContents(String sharer, File file) {
		super(sharer);
		fileName = file.getName();
		fileSize = file.length();
		type = LinKlipboard.FILE_TYPE;
		setDate();
	}

	public FileContents(String sharer, String path) {
		this(sharer);
		type = LinKlipboard.FILE_TYPE;
		setDate();
	}


	/** 보낼 파일의 이름을 반환 */
	public String getFileName() {
		return this.fileName;
	}

	/** 보낼 파일의 크기를 반환 */
	public long getFileSize() {
		return this.fileSize;
	}

}