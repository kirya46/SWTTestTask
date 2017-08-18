package common;

import java.util.ArrayList;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class MyCanvas extends Canvas {
	
	private static final String TAG = MyCanvas.class.getSimpleName();

	private ArrayList<Image> image;
	public MyCanvas(Composite parent, int style, ArrayList<Image> images) {
		super(parent, style);
		this.addPaintListener(new PaintListener(){

			@Override
			public void paintControl(PaintEvent e) {
					Log.d(TAG, "Redraw");			
			}});
	}
	
//	 WorldWidget() {
//	        addPaintListener(new PaintListener() {
//	            @Override
//	        public void paintControl(PaintEvent e) {
//	                WorldWidget.this.paintControl(e);
//	            }
//	        });
//	    }
//
//	    protected void paintControl(PaintEvent e) {
//	        GC gc = e.gc;
//	        for (short y = 0; y < world.getHeight(); y++) {
//	            for (short x = 0; x < world.getWidth(); x++) {
//	                final ITile tile = world.getTile(x, y);
//	                final Image image = ImageCache.getImage(tile);
//	                gc.drawImage(image, x * tileSize, y * tileSize);
//	            }
//	        }
//
//	        // Here is used a similar loop, to draw world objects
//	    }
//	
}
