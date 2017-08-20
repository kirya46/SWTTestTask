package common.widgets;


import common.util.ImageUtil;
import common.util.ImageWrapper;
import common.util.Log;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

public class ImageCanvas extends Canvas implements Listener {

    private static final String TAG = ImageCanvas.class.getSimpleName();

    /**
     * Set true when any images on canvas is selected.
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
    private ArrayList<ImageWrapper> wrapers;


    private OnCanvasImageListener callback;

    public interface OnCanvasImageListener {
        void onCanvasSelected(ImageWrapper wraper);
        void onCanvasDeselect(ImageWrapper wraper);
    }

    public ImageCanvas(final Composite parent, int style) {
        super(parent, style);
        this.wrapers = new ArrayList<ImageWrapper>();

        //init paintListener
        this.addPaintListener(new PaintListener() {

            @Override
            public void paintControl(PaintEvent e) {

                final GC gc = e.gc;
                
                //draw each image
                for (ImageWrapper wraper : getWrapers()) {
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

    public void addCanvasImageListener(OnCanvasImageListener callback){
        this.callback = callback;
    }

    public void addImage(ImageWrapper wraper) {
        this.getWrapers().add(wraper);
        this.redraw();
    }

    public ArrayList<ImageWrapper> getWrapers() {
        return wrapers;
    }

    public void setWrapers(ArrayList<ImageWrapper> wrapers) {
        this.wrapers = wrapers;
    }


    public void removeImage(ImageWrapper ImageWrapper) {
        final Iterator<ImageWrapper> iterator = this.getWrapers().iterator();
        while (iterator.hasNext()) {
            final ImageWrapper next = iterator.next();
            if (next.getId() == ImageWrapper.getId()) {
                iterator.remove();
                this.redraw();
                return;
            }
        }
    }

    public void selectImage(final ImageWrapper wraper){
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                try{
                    Collections.swap(ImageCanvas.this.getWrapers(), ImageCanvas.this.getWrapers().indexOf(wraper),ImageCanvas.this.getWrapers().size()-1);

                }catch (ArrayIndexOutOfBoundsException e) {
                    //do nothing
                }
                ImageCanvas.this.redraw();
            }
        });
    }

    public void deselectImage(ImageWrapper imageWrapper) {
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                //do nothing
            }
        });

    }

    final int[] diffX = new int[1];
    final int[] diffY = new int[1];


    @Override
    public void handleEvent(Event event) {

        switch (event.type) {
            case SWT.MouseEnter:
                break;

            case SWT.MouseDown:

                diffX[0] = 0;
                diffY[0] = 0;

                final ListIterator<ImageWrapper> iterator = getWrapers().listIterator(getWrapers().size());
                while (iterator.hasPrevious()) {
                    final ImageWrapper wraper = iterator.previous();

                    final ImageData imageData = wraper.getImage().getImageData();
                    Rectangle rectangle = new Rectangle(wraper.getX(), wraper.getY(), imageData.width, imageData.height);

                    //choose only images in this coordinates
                    if (rectangle.contains(event.x, event.y)) {

                        if (!selected) {

                            //set image as selected
                            wraper.setSelected(true);
                            selected = true;

                            //swap select image to end of list
                            Collections.swap(wrapers, wrapers.indexOf(wraper),wrapers.size()-1);

                            //send callback about 'select' event
                            if (this.callback!=null)this.callback.onCanvasSelected(wraper);
                        }else {
                            wraper.setSelected(false);
                            selected = false;
                            if (this.callback!=null)this.callback.onCanvasDeselect(wraper);
                        }

                        diffX[0] = event.x - wraper.getX();
                        diffY[0] = event.y - wraper.getY();
                        return;
                    }


                }
                break;

            case SWT.MouseMove:

                if (selected) {
                    final ListIterator<ImageWrapper> listIterator = getWrapers().listIterator(getWrapers().size());

                    //choose selected image
                    while (listIterator.hasPrevious()) {
                        final ImageWrapper wraper = listIterator.previous();
                        if (wraper.isSelected()) {

                            //set new coordinates for selected image
                            wraper.setX(event.x - diffX[0]);
                            wraper.setY(event.y - diffY[0]);

                            //redraw canvas
                            ImageCanvas.this.redraw();
                            return;
                        }
                    }
                }
                break;

            case SWT.MouseUp:

                selected = false;
                for (ImageWrapper wraper : getWrapers()) {
                    if (wraper.isSelected()) {
                        wraper.setSelected(false);
                        wraper.setX(event.x);
                        wraper.setY(event.y);
                        ImageCanvas.this.redraw();
                        if (this.callback!=null)this.callback.onCanvasDeselect(wraper);
                    }
                }
                break;

            case SWT.MouseExit:

                selected = false;
                for (ImageWrapper wraper : getWrapers()) {
                    if (wraper.isSelected()) {
                        wraper.setSelected(false);
                        wraper.setX(event.x);
                        wraper.setY(event.y);
                        ImageCanvas.this.redraw();
                        if (this.callback!=null)this.callback.onCanvasDeselect(wraper);
                    }
                }
                break;

        }
    }
}