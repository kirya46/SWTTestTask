package common;

import java.io.File;
import java.util.ArrayList;

import javax.activation.MimetypesFileTypeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class Main {

	public static void main(String[] args) {

		Display display = new Display();

		Shell shell = new Shell(display);

		// the layout manager handle the layout
		// of the widgets in the container
		FillLayout mainLayout = new FillLayout();
		GridLayout leftLayout = new GridLayout();
		GridLayout rightLayout = new GridLayout();

		Composite mainComp = new Composite(shell, SWT.NONE);
		mainComp.setLayout(mainLayout);

		Composite leftComp = new Composite(mainComp, SWT.NONE);
		GridData gridData = new GridData(100, SWT.FILL);
		leftComp.setLayout(rightLayout);
		leftComp.setBackground(display.getSystemColor(SWT.COLOR_DARK_GREEN));

		Composite rightComp = new Composite(mainComp, SWT.NONE);
		rightComp.setLayout(rightLayout);
		gridData = new GridData(100, SWT.FILL);
		rightComp.setData(gridData);

		shell.setLayout(mainLayout);

		// Add some widgets to the Shell
		// Shell can be used as container
		// initLable(shell);
		// initText(shell, display);

		// initImageContainer(shell, leftComp, display);

		MyCanvas canvas = new MyCanvas(leftComp,SWT.NONE,new ArrayList());
		Table table = initListViewer(rightComp);
		initButton(rightComp, display, table, canvas);

		// start shell
		shell.setSize(600,400);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void initText(Shell shell, Display display) {
		Text text = new Text(shell, SWT.NONE);
		text.setText("This is the text in the text widget");
		text.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		text.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		// set widgets size to their preferred size
		text.pack();
	}

	private static void initLable(Shell shell) {

		Label label = new Label(shell, SWT.BORDER);
		label.setText("This is a label:");
		label.setToolTipText("This is the tooltip of this label");
		// set widgets size to their preferred size
		label.pack();
	}

	private static void initButton(Composite shell, final Display display,
			final Table table, final Canvas canvas) {

		ClassLoader classLoader = Main.class.getClassLoader();
		final File deleteIconFile = new File(classLoader.getResource(
				"delete_icon.png").getFile());

		Button button = new Button(shell, SWT.PUSH);
		GridData gridData = new GridData();
		gridData.heightHint = 100;
		gridData.widthHint = 1000;
		button.setData(gridData);

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
					Image deleteImage = new Image(display, deleteIconFile
							.getPath());
					deleteImage = ImageUtil.resize(deleteImage, 32, 32);
					item.setImage(0, deleteImage);

					// set source image to TableItem
					Image image = new Image(display, file.getPath());
					image = ImageUtil.resize(image, 32, 32);
					item.setImage(1, image);

					
					//add to main image container
					canvas.redraw();

				}

			}
		});

		button.setText("Choose image");

		// set widgets size to their preferred size
		button.pack();
	}
	
	private static class MyPaintListener implements PaintListener{
		
		private Display display;
		private File imageFile;
		public MyPaintListener (Display display, File imageFile){
			this.display = display;
			this.imageFile = imageFile;
		}
		
		
		@Override
		public void paintControl(PaintEvent e) {
			Image image = new Image(this.display, this.imageFile.getPath());

			ImageData data = image.getImageData();

			data.setPixel(5, 5, 5);

			Image anotherImage = new Image(this.display, data);
			e.gc.drawImage(anotherImage, 10, 10);
			image.dispose();
		}
		
	}

	private static Table initListViewer(Composite shell) {

		final Table table = new Table(shell, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(false);
		GridData data = new GridData(SWT.WRAP, SWT.FILL, true, true);
		data.heightHint = 200;
		// data.widthHint = 100;
		table.setLayoutData(data);

		// create column for 'delete icon'
		TableColumn iconColumn = new TableColumn(table, SWT.NONE);
		iconColumn.pack();
		iconColumn.setWidth(50);

		// create column for source image
		TableColumn imageColumn = new TableColumn(table, SWT.NONE);
		imageColumn.pack();
		imageColumn.setWidth(100);

		// Listener of all table
		table.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				TableItem[] selection = table.getSelection();

				for (TableItem tableItem : selection) {
					System.out.println("Remove table item: "
							+ tableItem.getText(1));
					tableItem.dispose();
				}
			}
		});

		table.pack();

		return table;
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
