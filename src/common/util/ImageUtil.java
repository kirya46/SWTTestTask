package common.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

public class ImageUtil {
	

	/**
	 * Resize image to destination width and height.
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public static Image resize(Image image, int width, int height) {
		Image scaled = new Image(Display.getDefault(), width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0, image.getBounds().width,
				image.getBounds().height, 0, 0, width, height);
		gc.dispose();
		image.dispose(); // don't forget about me!
		return scaled;
	}

	public static Image scale(Image image, int width, int height) {


		ImageData data = image.getImageData();

		// Some logic to keep the aspect ratio
		float img_height = data.height;
		float img_width = data.width;
		float container_height = height;
		float container_width = width;

		float dest_height_f = container_height;
		float factor = img_height / dest_height_f;

		int dest_width = (int) Math.floor(img_width / factor );
		int dest_height = (int) dest_height_f;

		if(dest_width > container_width) {
			dest_width = (int) container_width;
			factor = img_width / dest_width;
			dest_height = (int) Math.floor(img_height / factor);

		}

		// Image resize
		data = data.scaledTo(dest_width, dest_height);
		Image scaled = new Image(Display.getDefault(), data);
		image.dispose();
		return scaled;
	}

}
