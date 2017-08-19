package common.widgets;

import common.util.ImageWraper;
import common.util.Log;
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
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.Iterator;

public class MyCanvas extends Canvas implements Listener {

    private static final String TAG = MyCanvas.class.getSimpleName();

    /**
     * Set true when any imagas on canvas is selected.
     */
    private static boolean selected = false;


    /**
     * Default width of images which draw on this canvas.
     */
    public static final int DEFAULT_IMAGE_WIDTH = 200;

    /**
     * Default height of images which draw on this canvas.
     */
    public static final int DEFAULT_IMAGE_HEIGHT = 150;

    /**
     * List of images for draw on canvas.
     */
    private ArrayList<ImageWraper> wrapers;

    public MyCanvas(Composite parent, int style) {
        super(parent, style);
        this.wrapers = new ArrayList<ImageWraper>();


        //init paintListener
        this.addPaintListener(new PaintListener() {


            @Override
            public void paintControl(PaintEvent e) {

                GC gc = e.gc;

                //draw each image
                for (ImageWraper wraper : getWrapers()) {
                    gc.drawImage(wraper.getImage(), wraper.getX(), wraper.getY());
                }

            }
        });

        //set listeners to mouse events
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


    public void removeImage(ImageWraper imageWraper) {
        final Iterator<ImageWraper> iterator = this.getWrapers().iterator();
        while (iterator.hasNext()) {
            final ImageWraper next = iterator.next();
            if (next.getId() == imageWraper.getId()) {
                iterator.remove();
                this.redraw();
                return;
            }
        }
    }


    @Override
    public void handleEvent(Event event) {

        switch (event.type) {
            case SWT.MouseEnter:
//				Log.d(TAG, "MouseEnter");
                break;

            case SWT.MouseDown:
//				Log.d(TAG, "MouseDown");

                for (ImageWraper wraper : getWrapers()) {

                    final ImageData imageData = wraper.getImage().getImageData();
                    Rectangle rectangle = new Rectangle(wraper.getX(), wraper.getY(), imageData.width, imageData.height);

                    //choose only images in this coordinates
                    if (rectangle.contains(event.x, event.y)) {

                        /*
                        NOTE: Select image if it not selected and unselect if it selected already.
                         */
                        wraper.setSelected(!wraper.isSelected());
                        selected = wraper.isSelected();
                        return;
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
                            return;
                        }
                    }

                }

                break;

            case SWT.MouseUp:
//				Log.d(TAG, "MouseUp");
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
//				Log.d(TAG, "MouseExit");
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