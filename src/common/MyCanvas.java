package common;

import java.util.ArrayList;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class MyCanvas extends Canvas {
	
	private static final String TAG = MyCanvas.class.getSimpleName();

	private ArrayList<ImageWraper> wrapers;
	
	public MyCanvas(Composite parent, int style, ArrayList<ImageWraper> images) {
		super(parent, style);
		this.addPaintListener(new PaintListener(){

			@Override
			public void paintControl(PaintEvent e) {
					Log.d(TAG, "Redraw");	
					//TODO: redraw all existing images
					GC gc = e.gc;
					
					for(ImageWraper wraper: getWrapers())
					gc.drawImage(wraper.getImage(), wraper.getX(), wraper.getY());
					
			}});
		
		
		//TODO: save arguments to local variables
		this.wrapers = images;
	}
	public void redrawWithNewImage(ImageWraper wraper){
		this.getWrapers().add(wraper);
		this.redraw();
	}
	public ArrayList<ImageWraper> getWrapers() {
		return wrapers;
	}

	public void setWrapers(ArrayList<ImageWraper> wrapers) {
		this.wrapers = wrapers;
	}
	
	
	public void removeImage(){
		
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


//WorldWidget() {
//addPaintListener(new PaintListener() {
//    @Override
//public void paintControl(PaintEvent e) {
//        WorldWidget.this.paintControl(e);
//    }
//});
//}
//
//protected void paintControl(PaintEvent e) {
//GC gc = e.gc;
//for (short y = 0; y < world.getHeight(); y++) {
//    for (short x = 0; x < world.getWidth(); x++) {
//        final ITile tile = world.getTile(x, y);
//        final Image image = ImageCache.getImage(tile);
//        gc.drawImage(image, x * tileSize, y * tileSize);
//    }
//}
//
//// Here is used a similar loop, to draw world objects
//}
//
