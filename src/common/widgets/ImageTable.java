package common.widgets;

import common.util.ImageWrapper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;
import java.util.Iterator;

public class ImageTable extends Table {

    private static final String TAG = ImageTable.class.getSimpleName();

    public static final int DEFAULT_PREVIEW_WIDTH = 32;
    public static final int DEFAULT_PREVIEW_HEIGHT = 32;

    private ArrayList<ImageWrapper> wrappers;

    private OnItemListener callback;

    /**
     * Callback interface for send events to outside.
     */
    public interface OnItemListener {

        /**
         * Will called when one of {@link TableItem} will removed.
         *
         * @param imageWrapper - instance of removed {@link ImageWrapper}
         */
        void onImageRemoved(ImageWrapper imageWrapper);

        /**
         * Will called when one of {@link TableItem} will selected.
         *
         * @param imageWrapper - instance of selected {@link ImageWrapper}
         */
        void onItemSelected(ImageWrapper imageWrapper);

        /**
         * Will called when one of {@link TableItem} will deselected.
         *
         * @param imageWrapper - instance of deselected {@link ImageWrapper}
         */
        void onItemDeselect(ImageWrapper imageWrapper);
    }

    @Override
    protected void checkSubclass() {
        //do nothing
    }

    /**
     * Default constructor.
     */
    public ImageTable(Composite parent, int style, OnItemListener itemListener) {
        super(parent, style);

        this.wrappers = new ArrayList<ImageWrapper>();
        this.callback = itemListener;

        this.setLinesVisible(true);
        this.setHeaderVisible(false);

        // create column for source image
        TableColumn imageColumn = new TableColumn(this, SWT.NONE);
        imageColumn.pack();
        imageColumn.setWidth(DEFAULT_PREVIEW_WIDTH);

        TableColumn nameColumn = new TableColumn(this, SWT.NONE);
        nameColumn.pack();
        nameColumn.setWidth(200 - DEFAULT_PREVIEW_WIDTH);


        //set TableItem selection listener (it is DoubleClick)
        this.addListener(SWT.DefaultSelection, new Listener() {
            public void handleEvent(Event e) {
                TableItem[] selection = ImageTable.this.getSelection();

                for (TableItem tableItem : selection) {

                    //get ImageWrapper id from TableItem
                    int id = (Integer) tableItem.getData("id");

                    final ImageWrapper imageById = ImageTable.this.getImageById(id);
                    if (ImageTable.this.callback != null && imageById != null) {
                        ImageTable.this.callback.onImageRemoved(imageById);
                    }

                    //remove ImageWrapper from table image list
                    ImageTable.this.removeImage(id);

                    //remove TableItem
                    tableItem.dispose();
                }
            }
        });

        //set listener for handle MouseDown event on TableItem (SingleClick)
        this.addListener(SWT.MouseDown, new Listener() {
            @Override
            public void handleEvent(Event event) {
                final TableItem[] selection = ImageTable.this.getSelection();

                for (TableItem tableItem : selection) {

                    //get ImageWrapper id from TableItem
                    int id = (Integer) tableItem.getData("id");

                    final ImageWrapper imageById = ImageTable.this.getImageById(id);
                    if (ImageTable.this.callback != null && imageById != null) {
                        ImageTable.this.callback.onItemSelected(imageById);
                    }
                }
            }
        });
    }


    /**
     * Will select the specific {@link TableItem} which represent {@param imageWrapper}.
     *
     * @param imageWrapper - destination {@link ImageWrapper}.
     */
    public void select(final ImageWrapper imageWrapper) {
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                ImageTable.this.deselectAll();
                ImageTable.this.select(ImageTable.this.wrappers.indexOf(imageWrapper));
            }
        });
    }

    /**
     * Will deselect the specific {@link TableItem} which represent {@param imageWrapper}.
     *
     * @param imageWrapper - destination {@link ImageWrapper}.
     */
    public void deselect(final ImageWrapper imageWrapper) {
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                ImageTable.this.deselectAll();
                //ImageTable.this.deselect(ImageTable.this.wrappers.indexOf(ImageWrapper));
            }
        });
    }

    /**
     * Add {@link TableItem} representation of {@param imageWrapper}
     * which contains small image preview and name of this image.
     *
     * @param imageWrapper - destination {@link ImageWrapper}.
     */
    public void addImage(ImageWrapper imageWrapper) {

        //add wrapper to table list
        this.wrappers.add(imageWrapper);

        // create TabItem for destination image
        TableItem item = new TableItem(this, SWT.NONE);

        //save id of ImageWrapper to this TableItem
        item.setData("id", imageWrapper.getId());

        //set preview image to TableItem
        item.setImage(0, imageWrapper.getPreview());

        //set file name to TableItem
        item.setText(1, " " + imageWrapper.getFileName());

        //set font for TableItem
        Font font = new Font(getDisplay(), "Arial", 10, SWT.NORMAL);
        item.setFont(font);
    }

    /**
     * Remove {@link ImageWrapper} with {@param id} from {@link ImageTable#wrappers} list.
     *
     * @param id - destination wrapper id.
     */
    private void removeImage(long id) {
        final Iterator<ImageWrapper> iterator = this.wrappers.iterator();
        while (iterator.hasNext()) {
            ImageWrapper wraper = iterator.next();
            if (wraper.getId() == id) {
                iterator.remove();
            }
        }

        this.wrappers.trimToSize();
    }

    /**
     * Find and return {@link ImageWrapper} with specific {@param id}.
     *
     * @param id - destination {@link ImageWrapper#id}.
     * @return - return instance of {@link ImageWrapper} if it exists, otherwise return null.
     */
    private ImageWrapper getImageById(long id) {
        for (ImageWrapper wraper : this.wrappers) {
            if (wraper.getId() == id) {
                return wraper;
            }
        }

        return null;
    }

}
