package common.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Kirill Stoianov on 19/08/17.
 */
public class SelectButton extends Button  {

    private static final String TAG = SelectButton.class.getCanonicalName();


    private OnImageSelectListener selectListener;

    /**
     * Callback for return selected images.
     */
    public interface OnImageSelectListener {

        /**
         * Call with images.
         * @param imageFiles - files with type 'image'.
         */
        void onImagesSelect(ArrayList<File> imageFiles);

        /**
         * Call non-image files which was selected in {@link FileDialog}.
         * @param files - with different types.
         */
        void onOtherFilesSelect(ArrayList<File> files);
    }

    /**
     * Default constructor.
     * @param parent
     * @param style
     * @param selectListener
     */
    public SelectButton(Composite parent, int style,OnImageSelectListener selectListener ) {
        super(parent, style);

        this.selectListener = selectListener;

        //create selection listener
        MySelectionAdapter selectionAdapter = new MySelectionAdapter(this.selectListener);

        //add listener to this button
        this.addSelectionListener(selectionAdapter);
    }
    
    @Override
	protected void checkSubclass () {
		//do nothing
	}

    /**
     * Selection listener for call {@link FileDialog} and return
     * image selection result.
     */
    public class MySelectionAdapter extends SelectionAdapter {

        private OnImageSelectListener callback;

        public MySelectionAdapter(OnImageSelectListener callback) {
            this.callback = callback;
        }

        @Override
        public void widgetSelected(SelectionEvent e) {

            // Ask the user which files to upload
            FileDialog dialog = new FileDialog(Display.getCurrent()
                    .getActiveShell(), SWT.OPEN | SWT.MULTI);
            dialog.setText("Select the local files to upload");
            dialog.open();

            //lists of image-type files and not image-type files
            ArrayList<File> imageFiles = new ArrayList<File>();
            ArrayList<File> notImageFiles = new ArrayList<File>();

            for (String fname : dialog.getFileNames()) {

                // create file from path
                final File file = new File(dialog.getFilterPath()
                        + File.separator + fname);


                // check is file an image
                String mimetype = new MimetypesFileTypeMap()
                        .getContentType(file);
                String type = mimetype.split("/")[0];
                if (type.equals("image")) {
                    imageFiles.add(file);
                } else {
                    notImageFiles.add(file);
                }
            }

            //send callBack
            if (this.callback!=null)callback.onImagesSelect(imageFiles);
            if (this.callback!=null)callback.onOtherFilesSelect(notImageFiles);
        }
    }
}
