package contents;

import java.io.Serializable;
import java.util.Calendar;

import server_manager.LinKlipboard;

// 테스트
public abstract class Contents implements Serializable {

	private static final long serialVersionUID = 4131370422438049456L;

	private static int serialNum = LinKlipboard.NULL;

	protected  String date;
	protected  String sharer;
	protected int type;
	

	public Contents() {
	}

	public Contents(String sharer) {
		this();
		this.sharer = sharer;
	}

	public void setSharer(String sharer) {
		this.sharer = sharer;
	}

	public static void setSerialNum(int serialNum) {
		Contents.serialNum = serialNum;
	}
	
	public void setDate() {
		this.date = now();
	}

	public int getSerialNum() {
		return serialNum;
	}

	public String getSharer() {
		return sharer;
	}

	public String getDate() {
		return date;
	}
	
	public int getType() {
		return type;
	}

	/** @return YYYY-MM-DD HH:MM:SS 형식의 현재 시간 */
	public static String now() {
		Calendar cal = Calendar.getInstance();
		String year = Integer.toString(cal.get(Calendar.YEAR));
		String month = Integer.toString(cal.get(Calendar.MONTH)+1);
		
		String date = Integer.toString(cal.get(Calendar.DATE));
		String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		if(Integer.parseInt(hour) < 10) {
			hour = "0" + hour;
		}
		if(Integer.parseInt(hour) > 12) {
			hour = "오후 " + Integer.toString(Integer.parseInt(hour)-12);
		}
		else {
			hour = "오전 " + hour;
		}
		
		String minute = Integer.toString(cal.get(Calendar.MINUTE));
		if(Integer.parseInt(minute) < 10) {
			minute = "0" + minute;
		}
		String sec = Integer.toString(cal.get(Calendar.SECOND));
		if(Integer.parseInt(sec) < 10) {
			sec = "0" + sec;
		}

		return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + sec;
	}

}