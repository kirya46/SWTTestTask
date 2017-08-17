package common;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
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
		FillLayout layout = new FillLayout();
		shell.setLayout(layout);

		// Add some widgets to the Shell
		// Shell can be used as container
		// initLable(shell);
		// initText(shell, display);

		Table table = initListViewer(shell);
		initButton(shell, table);
		test(shell, display);

		// start shell
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

	private static void initButton(Shell shell, final Table table) {
		Button button = new Button(shell, SWT.PUSH);

		// register listener for the selection event
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// System.out.println("Button pressed");
				// TableItem item = new TableItem(table, SWT.NONE);
				// item.setText(0, "delete");
				// item.setText(1, System.currentTimeMillis() + "");

				// Ask the user which files to upload
				FileDialog dialog = new FileDialog(Display.getCurrent()
						.getActiveShell(), SWT.OPEN | SWT.MULTI);
				dialog.setText("Select the local files to upload");
				dialog.open();

				ArrayList<File> files = new ArrayList<File>();
				for (String fname : dialog.getFileNames())
					files.add(new File(dialog.getFilterPath() + File.separator
							+ fname));

				// // TODO enable upload command only when selection is exactly
				// one folder
				// List<DFSFolder> folders = filterSelection(DFSFolder.class,
				// selection);
				// if (folders.size() >= 1)
				// uploadToDFS(folders.get(0), files);
				// }
			}
		});

		button.setText("Choose image");

		// set widgets size to their preferred size
		button.pack();

	}

	private static Table initListViewer(Shell shell) {
		final Table table = new Table(shell, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);

		String[] titles = { "Delete", "Image" };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles[i]);
			table.getColumn(i).pack();
		}

		for (int i = 0; i <= 3; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(0, "Person " + i);
			item.setText(1, "LastName " + i);
			item.setText(2, String.valueOf(i));
		}

		for (int i = 0; i < titles.length; i++) {
			table.getColumn(i).pack();
		}

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

	private static void test(final Shell shell, Display display) {
		final Composite composite = new Composite(shell, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setEnabled(false);
		composite.setLayout(layout);
		composite.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

		// TODO: test different composites with Labels and onMouseListeners on some Labels
	
		final Composite mainComposite = new Composite(composite, SWT.NONE);
	    mainComposite.setLayout(new GridLayout(1, false));
	    mainComposite.setLayoutData(new GridData(SWT.WRAP, SWT.WRAP, true, true, 1, 1));
		
		final Button label = new Button(mainComposite, SWT.WRAP);
		label.setText("Image");
//		GridData fd = new GridData();
//		fd.widthHint = 100;
//		fd.heightHint =400;
//		label.setLayoutData(fd);
		
		composite.pack();
		composite.setLocation(1000, 550);

		final Point[] offset = new Point[1];
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.MouseDown:
					Rectangle rect = composite.getBounds();
					if (rect.contains(event.x, event.y)) {
						Point pt1 = mainComposite.toDisplay(0,0);
						Point pt2 = mainComposite.toDisplay(event.x, event.y);
						offset[0] = new Point(pt2.x - pt1.x, pt2.y - pt1.y);
					}
					break;
				case SWT.MouseMove:
					if (offset[0] != null) {
						Point pt = offset[0];
						mainComposite.setLocation(event.x - pt.x, event.y - pt.y);
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
