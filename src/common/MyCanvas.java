package common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import java.util.ArrayList;

public class MyCanvas extends Canvas implements Listener {

    private static final String TAG = MyCanvas.class.getSimpleName();

    private static boolean selected = false;

    private ArrayList<ImageWraper> wrapers;

    public MyCanvas(Composite parent, int style, ArrayList<ImageWraper> images) {
        super(parent, style);
        this.addPaintListener(new PaintListener() {


            @Override
            public void paintControl(PaintEvent e) {

                Log.d(TAG, "redraw canvas");

                GC gc = e.gc;

                for (ImageWraper wraper : getWrapers()){

                    gc.drawImage(wraper.getImage(), wraper.getX(), wraper.getY());
                    Log.d(TAG, "Draw: " + wraper.toString());
                }

            }
        });


        this.wrapers = images;

        this.addListener(SWT.MouseEnter, this);
        this.addListener(SWT.MouseDown, this);
        this.addListener(SWT.MouseMove, this);
        this.addListener(SWT.MouseExit, this);
    }

    public void redrawWithNewImage(ImageWraper wraper) {
        this.getWrapers().add(wraper);
        this.redraw();
    }

    public ArrayList<ImageWraper> getWrapers() {
        return wrapers;
    }

    public void setWrapers(ArrayList<ImageWraper> wrapers) {
        this.wrapers = wrapers;
    }


    public void removeImage() {

    }


    @Override
    public void handleEvent(Event event) {

        switch (event.type) {
            case SWT.MouseEnter:
				Log.d(TAG, "MouseEnter");
                break;

            case SWT.MouseDown:
				Log.d(TAG, "MouseDown");

                for (ImageWraper wraper : getWrapers()) {
                    final ImageData imageData = wraper.getImage().getImageData();
                    Rectangle rectangle = new Rectangle(wraper.getX(),wraper.getY(),imageData.width,imageData.height);
                    Log.d(TAG, rectangle.toString() + " : " + event.x + "/"+event.y);
                    if (rectangle.contains(event.x, event.y)) {

                        wraper.setSelected(true);
                        selected = true;
                    }
                }
                break;

            case SWT.MouseMove:
//				Log.d(TAG, "MouseMove");
                if (selected) {

                    for (ImageWraper wraper : getWrapers()) {
                        if (wraper.isSelected()) {
                            wraper.setX(event.x);
                            wraper.setY(event.y);
                            MyCanvas.this.redraw();
                        }
                    }

                }

                break;

            case SWT.MouseUp:
				Log.d(TAG, "MouseUp");
                selected = false;
                for (ImageWraper wraper : getWrapers()) {
                    if (wraper.isSelected()) {
                        wraper.setSelected(false);
                        wraper.setX(event.x);
                        wraper.setY(event.y);
                        MyCanvas.this.redraw();
                    }
                }
                break;
            case SWT.MouseExit:
				Log.d(TAG, "MouseExit");
                selected = false;
                for (ImageWraper wraper : getWrapers()) {
                    if (wraper.isSelected()) {
                        wraper.setSelected(false);
                        wraper.setX(event.x);
                        wraper.setY(event.y);
                        MyCanvas.this.redraw();
                    }
                }

                break;

        }
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
