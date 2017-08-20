package common.widgets;


import common.util.ImageWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
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
    private ArrayList<ImageWrapper> wrappers;

    /**
     * List of {@link ImageCanvas} events listeners.
     */
    private ArrayList<OnCanvasImageListener> listeners;

    /**
     * Different between image coordinates and position of mouse cursor on destination image by oX.
     * when user produce {@link SWT#MouseDown} event
     */
    final int[] diffX = new int[1];

    /**
     * Different between image coordinates and position of mouse cursor on destination image by oY.
     * when user produce {@link SWT#MouseDown} event
     */
    final int[] diffY = new int[1];

    /**
     * Callback interface for send events to outside.
     */
    public interface OnCanvasImageListener {
        void onCanvasSelected(ImageWrapper wraper);

        void onCanvasDeselect(ImageWrapper wraper);
    }

    /**
     * Default constructor.
     */
    public ImageCanvas(final Composite parent, int style) {
        super(parent, style);
        this.wrappers = new ArrayList<ImageWrapper>();
        this.listeners = new ArrayList<OnCanvasImageListener>();

        //init paintListener
        this.addPaintListener(new PaintListener() {

            @Override
            public void paintControl(PaintEvent e) {

                final GC gc = e.gc;

                //draw each image
                for (ImageWrapper wraper : getWrappers()) {
                    gc.drawImage(wraper.getImage(), wraper.getX(), wraper.getY());
                }
            }
        });

        //set listeners to mouse events
        this.addListener(SWT.MouseEnter, this);
        this.addListener(SWT.MouseDown, this);
        this.addListener(SWT.MouseMove, this);
        this.addListener(SWT.MouseUp, this);
        this.addListener(SWT.MouseExit, this);
    }

    /**
     * Add canvas event listener.
     *
     * @param callback - destination listener.
     */
    public void addCanvasImageListener(OnCanvasImageListener callback) {
        if (!this.listeners.contains(callback)) this.listeners.add(callback);
    }

    /**
     * Add new {@link ImageWrapper} to {@link ImageCanvas#wrappers}
     * and redraw canvas with new image.
     *
     * @param wraper - new wrapper.
     */
    public void addImage(ImageWrapper wraper) {
        this.getWrappers().add(wraper);
        this.redraw();
    }

    /**
     * Get all {@link ImageWrapper}.
     *
     * @return - list of all {@link ImageWrapper} of this canvas.
     */
    public ArrayList<ImageWrapper> getWrappers() {
        return wrappers;
    }

    /**
     * Remove {@param imageWrapper} from {@link ImageCanvas#wrappers}
     * and redraw canvas without removed image.
     *
     * @param imageWrapper
     */
    public void removeImage(ImageWrapper imageWrapper) {
        final Iterator<ImageWrapper> iterator = this.getWrappers().iterator();
        while (iterator.hasNext()) {
            final ImageWrapper next = iterator.next();
            if (next.getId() == imageWrapper.getId()) {
                iterator.remove();
                this.redraw();
                return;
            }
        }
    }


    /**
     * Move selected image to foreground.
     *
     * @param wraper - selected image.
     */
    public void selectImage(final ImageWrapper wraper) {
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                try {
                    Collections.swap(ImageCanvas.this.getWrappers(), ImageCanvas.this.getWrappers().indexOf(wraper), ImageCanvas.this.getWrappers().size() - 1);
                } catch (ArrayIndexOutOfBoundsException e) {
                    //do nothing
                }
                ImageCanvas.this.redraw();
            }
        });
    }

    /**
     * Not implemented.
     */
    public void deselectImage(ImageWrapper imageWrapper) {
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                //do nothing
            }
        });
    }

    /**
     * Handle mouse events for select and move images.
     *
     * @param event - mouse event.
     */
    @Override
    public void handleEvent(Event event) {

        switch (event.type) {
            case SWT.MouseEnter:
                break;

            case SWT.MouseDown:

                diffX[0] = 0;
                diffY[0] = 0;

                final ListIterator<ImageWrapper> iterator = getWrappers().listIterator(getWrappers().size());
                while (iterator.hasPrevious()) {
                    final ImageWrapper wraper = iterator.previous();

                    final ImageData imageData = wraper.getImage().getImageData();
                    Rectangle rectangle = new Rectangle(wraper.getX(), wraper.getY(), imageData.width, imageData.height);

                    //choose only images in this coordinates
                    if (rectangle.contains(event.x, event.y)) {

                        if (!selected) {

                            //calculete difference between image coordinates
                            //and position of mouse cursor (event) on destination image
                            diffX[0] = event.x - wraper.getX();
                            diffY[0] = event.y - wraper.getY();

                            //set image as selected
                            wraper.setSelected(true);
                            selected = true;

                            //swap select image to end of list
                            Collections.swap(wrappers, wrappers.indexOf(wraper), wrappers.size() - 1);

                            //notify listeners about 'select' event
                            for (OnCanvasImageListener callback : this.listeners) {
                                if (callback != null) callback.onCanvasSelected(wraper);
                            }
                        } else {

                            //select image of not selected
                            wraper.setSelected(false);
                            selected = false;

                            //notify listeners about 'deselect' event
                            for (OnCanvasImageListener callback : this.listeners) {
                                if (callback != null) callback.onCanvasDeselect(wraper);
                            }
                        }

                        return;
                    }


                }
                break;

            case SWT.MouseMove:

                if (selected) {
                    final ListIterator<ImageWrapper> listIterator = getWrappers().listIterator(getWrappers().size());

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
                for (ImageWrapper wraper : getWrappers()) {
                    if (wraper.isSelected()) {
                        wraper.setSelected(false);
                        wraper.setX(event.x - diffX[0]);
                        wraper.setY(event.y - diffY[0]);
                        ImageCanvas.this.redraw();

                        //notify listeners about 'deselect' event
                        for (OnCanvasImageListener callback : this.listeners) {
                            if (callback != null) callback.onCanvasDeselect(wraper);
                        }
                    }
                }
                break;

            case SWT.MouseExit:

                selected = false;
                for (ImageWrapper wraper : getWrappers()) {
                    if (wraper.isSelected()) {
                        wraper.setSelected(false);
                        wraper.setX(event.x - diffX[0]);
                        wraper.setY(event.y - diffY[0]);
                        ImageCanvas.this.redraw();

                        //notify listeners about 'deselect' event
                        for (OnCanvasImageListener callback : this.listeners) {
                            if (callback != null) callback.onCanvasDeselect(wraper);
                        }
                    }
                }
                break;

        }
    }
}