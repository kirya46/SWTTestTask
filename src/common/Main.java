package common;

import common.util.ImageUtil;
import common.util.ImageWrapper;
import common.widgets.ImageCanvas;
import common.widgets.ImageTable;

import common.widgets.SelectButton;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static final String TAG = Main.class.getSimpleName();
    private static Widget mouseControl = null;


    public static void main(String[] args) {

        final Display display = new Display();

        /* Overall, keep track of the Widget the mouse is moving over */
        display.addFilter(SWT.MouseMove, new Listener() {
            @Override
            public void handleEvent(Event e) {
                mouseControl = e.widget;
            }
        });

        // Set shell layout
        final Shell shell = new Shell(display);
        shell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        shell.setLayout(new GridLayout(2, false));
        shell.setText("SWT test task");


        // Add canvas to shell
        final ImageCanvas canvas = new ImageCanvas(shell, SWT.NONE);
        canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        canvas.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));

        // add right panel composite
        GridLayout rightLayout = new GridLayout();
        final Composite rightComp = new Composite(shell, SWT.NONE);
        rightComp.setLayout(rightLayout);
        GridData gridData = new GridData(SWT.WRAP, SWT.FILL, false, false);
        gridData.horizontalAlignment = SWT.RIGHT;
        rightComp.setLayoutData(gridData);
        rightComp.pack();

        // add ImageTable to right panel composite
        final ImageTable table = new ImageTable(rightComp, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION,
                new ImageTable.OnItemListener() {
                    @Override
                    public void onImageRemoved(ImageWrapper imageWrapper) {
                        canvas.removeImage(imageWrapper);
                    }

                    @Override
                    public void onItemSelected(ImageWrapper imageWrapper) {
                        canvas.selectImage(imageWrapper);
                    }

                    @Override
                    public void onItemDeselect(ImageWrapper imageWrapper) {
                        canvas.deselectImage(imageWrapper);
                    }
                });

        //add image select listener to canvas
        canvas.addCanvasImageListener(
                new ImageCanvas.OnCanvasImageListener() {
                    @Override
                    public void onCanvasSelected(ImageWrapper wrapper) {
                        table.select(wrapper);
                    }

                    @Override
                    public void onCanvasDeselect(ImageWrapper wrapper) {
                        table.deselect(wrapper);
                    }
                });

        GridData tableData = new GridData(SWT.WRAP, SWT.FILL, true, true);
        tableData.horizontalAlignment = SWT.CENTER;
        tableData.minimumWidth = 150;
        table.setLayoutData(tableData);
        table.pack();


        //create button for call FileDialog for select images
        SelectButton button = new SelectButton(rightComp, SWT.PUSH,
                new SelectButton.OnImageSelectListener() {
                    @Override
                    public void onImagesSelect(ArrayList<File> imageFiles) {
                        for (File file : imageFiles) {

                            final String name = file.getName();

                            //get source image
                            Image sourceImage = new Image(display, file.getPath());

                            //resize source image for TableItem preview
                            Image preview = ImageUtil.resize(sourceImage, ImageTable.DEFAULT_PREVIEW_WIDTH, ImageTable.DEFAULT_PREVIEW_HEIGHT);

                            //resize source image for Canvas
                            sourceImage = new Image(display, file.getPath());
                            Image canvasImage = ImageUtil.scale(sourceImage, ImageCanvas.DEFAULT_IMAGE_WIDTH, ImageCanvas.DEFAULT_IMAGE_HEIGHT);

                            //generate random coordinates
                            Rectangle rectangle = canvas.getBounds();
                            int randomX = new Random().nextInt(rectangle.width);
                            int randomY = new Random().nextInt(rectangle.height);

                            ImageWrapper wraper = new ImageWrapper(canvasImage, preview, randomX, randomY, name, file.getPath());

                            table.addImage(wraper);
                            canvas.addImage(wraper);
                        }
                    }

                    @Override
                    public void onOtherFilesSelect(ArrayList<File> files) {

                        if (files == null || files.size() == 0) return;

                        MessageBox dialog =
                                new MessageBox(shell, SWT.ICON_WARNING | SWT.OK) /*SWT>CANCEL*/;
                        dialog.setText("Warning");
                        dialog.setMessage("Some files were not added.");

                        // open dialog and await user selection
                        int returnCode = dialog.open();
                    }
                });

        //set button layout params
        GridData buttonData = new GridData();
        buttonData.horizontalAlignment = SWT.CENTER;
        buttonData.verticalAlignment = SWT.BOTTOM;
        button.setLayoutData(buttonData);
        button.setText("Choose image");
        button.pack();


        // start shell
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }


}
