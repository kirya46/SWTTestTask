package common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Display display = new Display();

        // Set shell layout
        Shell shell = new Shell(display);
        shell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        shell.setLayout(new GridLayout(2, false));
        shell.setText("SWT test task");

        //TODO: add composite for left side and test put Labels to this composite
        GridLayout leftLayout = new GridLayout(1,false);
        Composite leftComposite = new Composite(shell, SWT.NONE);
        leftComposite.setLayout(leftLayout);
        leftComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        //TODO: add composite for left side and test put Labels to this composite

        // Add canvas to shell
        MyCanvas canvas = new MyCanvas(leftComposite, SWT.NONE, new ArrayList<ImageWraper>());
        canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        canvas.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));


        // add right panel composite
        GridLayout rightLayout = new GridLayout();
        final Composite rightComp = new Composite(shell, SWT.NONE);
        rightComp.setLayout(rightLayout);
        GridData gridData = new GridData(SWT.WRAP, SWT.FILL, false, false);
        gridData.horizontalAlignment = SWT.RIGHT;
        rightComp.setLayoutData(gridData);
        rightComp.pack();

        // add ImageTable to right panel composite
        final ImageTable table = new ImageTable(rightComp, SWT.MULTI
                | SWT.BORDER | SWT.FULL_SELECTION, new ArrayList<ImageWraper>()); // TODO:change to destination array


        GridData tableData = new GridData(SWT.WRAP, SWT.FILL, true, true);
        tableData.horizontalAlignment = SWT.CENTER;
        table.setLayoutData(tableData);
        table.pack();

        initButton(rightComp, display, table, canvas);

        // start shell
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    private static FormAttachment FormAttachment(int i, int j) {
        // TODO Auto-generated method stub
        return null;
    }

    private static void initButton(Composite shell, final Display display,
                                   final Table table, final MyCanvas canvas) {

        // ClassLoader classLoader = Main.class.getClassLoader();
        // final File deleteIconFile = new File(classLoader.getResource(
        // "delete_icon.png").getFile());

        Button button = new Button(shell, SWT.PUSH);
        GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.CENTER;
        gridData.verticalAlignment = SWT.BOTTOM;
        button.setLayoutData(gridData);

        // register listener for the selection event
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                // Ask the user which files to upload
                FileDialog dialog = new FileDialog(Display.getCurrent()
                        .getActiveShell(), SWT.OPEN | SWT.MULTI);
                dialog.setText("Select the local files to upload");
                dialog.open();

                // loop all selected files
                ArrayList<File> files = new ArrayList<File>();
                for (String fname : dialog.getFileNames()) {

                    // create file from path
                    final File file = new File(dialog.getFilterPath()
                            + File.separator + fname);

                    // add file to list
                    files.add(file);

                    // check is file an image
                    String mimetype = new MimetypesFileTypeMap()
                            .getContentType(file);
                    String type = mimetype.split("/")[0];
                    if (type.equals("image")) {
                        System.out.println("It's an image");
                    } else {
                        System.out.println("It's NOT an image");
                        return;
                    }

                    // create TabItem for each selected file
                    TableItem item = new TableItem(table, SWT.NONE);

                    // set delete icon to TabItem
                    // Image deleteImage = new Image(display, deleteIconFile
                    // .getPath());
                    // deleteImage = ImageUtil.resize(deleteImage, 32, 32);
                    // item.setImage(0, deleteImage);
                    item.setText(0, "delete");

                    // set source image to TableItem
                    Image image = new Image(display, file.getPath());
                    image = ImageUtil.resize(image, 32, 32);
                    item.setImage(1, image);

                    // add to main image container
                    int x = new Random().nextInt();
                    int y = new Random().nextInt();
                    Rectangle rectangle = canvas.getBounds();

                    x = new Random().nextInt(rectangle.width);
                    y = new Random().nextInt(rectangle.height);

                    ImageWraper wraper = new ImageWraper(image, x, y);
                    canvas.redrawWithNewImage(wraper);

                }

            }
        });

        button.setText("Choose image");

        // set widgets size to their preferred size
        button.pack();
    }

    private static void initImageContainer(final Shell shell,
                                           final Composite rootComposite, Display display) {

        final Composite mainComposite = new Composite(rootComposite, SWT.NONE);
        mainComposite.setLayout(new GridLayout(2, false));
        mainComposite.setEnabled(true);
        mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                true, 1, 1));

        final Button label = new Button(mainComposite, SWT.WRAP);
        label.setText("Image");

        rootComposite.pack();
        rootComposite.setLocation(0, 0);

        final Point[] offset = new Point[1];
        Listener listener = new Listener() {
            public void handleEvent(Event event) {
                switch (event.type) {
                    case SWT.MouseDown:
                        Rectangle rect = rootComposite.getBounds();
                        if (rect.contains(event.x, event.y)) {
                            Point pt1 = rootComposite.toDisplay(0, 0);
                            Point pt2 = rootComposite.toDisplay(event.x, event.y);
                            offset[0] = new Point(pt2.x - pt1.x, pt2.y - pt1.y);
                        }
                        break;
                    case SWT.MouseMove:
                        if (offset[0] != null) {
                            Point pt = offset[0];
                            rootComposite.setLocation(event.x - pt.x, event.y
                                    - pt.y);
                        }
                        break;
                    case SWT.MouseUp:
                        offset[0] = null;
                        break;
                }
            }
        };

        shell.addListener(SWT.MouseDown, listener);
        shell.addListener(SWT.MouseUp, listener);
        shell.addListener(SWT.MouseMove, listener);

    }
}
