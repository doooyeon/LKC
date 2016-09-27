package contents;

import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.swing.ImageIcon;

import server_manager.LinKlipboard;

public class ImageContents extends Contents implements Serializable {
	private static final long serialVersionUID = 8711040755102213189L;
	private ImageIcon imageData;

	public ImageContents() {
		super();
		type = LinKlipboard.IMAGE_TYPE;
		setDate();
	}

	public ImageContents(String sharer) {
		this();
		type = LinKlipboard.IMAGE_TYPE;
		setDate();
	}

	public ImageContents(ImageIcon image) {
		this();
		this.imageData = image;
		setDate();
	}

	public ImageContents(Image image) {
		this();
		this.imageData = new ImageIcon(image);
		setDate();
	}

	public ImageContents(String sharer, ImageIcon data) {
		super(sharer);
		type = LinKlipboard.IMAGE_TYPE;
		this.imageData = data;
		setDate();
	}

	public ImageIcon getImage() {
		return imageData;
	}

	public ImageIcon getResizingImageIcon() {
		Image resizingImage = imageData.getImage(); // ImageIcon을 Image로 변환.
		resizingImage = resizingImage.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH); // resize
		ImageIcon resizingImageIcon = new ImageIcon(resizingImage); // Image로 ImageIcon 생성

		return resizingImageIcon;
	}

}