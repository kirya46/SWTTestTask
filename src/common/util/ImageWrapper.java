package common.util;

import org.eclipse.swt.graphics.Image;

import java.util.Random;

public class ImageWrapper {

	private int id;
	private Image image;
	private Image preview;
	private int x,y;
	private boolean selected = false;
	private String fileName;
	private String filePath;

	public ImageWrapper(Image image, Image preview, int x, int y, String fileName, String filePath) {
		this.id = new Random().nextInt(Integer.MAX_VALUE);
		this.image = image;
		this.preview = preview;
		this.x = x;
		this.y = y;
		this.fileName = fileName;
		this.filePath = filePath;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getId() {
		return id;
	}

	public Image getPreview() {
		return preview;
	}

	public void setPreview(Image preview) {
		this.preview = preview;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String toString() {
		return "ImageWrapper{" +
				"id=" + id +
				", image=" + image +
				", preview=" + preview +
				", x=" + x +
				", y=" + y +
				", selected=" + selected +
				", fileName='" + fileName + '\'' +
				", filePath='" + filePath + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ImageWrapper that = (ImageWrapper) o;

		return id == that.id;
	}

	@Override
	public int hashCode() {
		return id;
	}
}
