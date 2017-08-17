package common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Main {

	public static void main(String[] args) {

		Display display = new Display();

		Shell shell = new Shell(display);

		// the layout manager handle the layout
		// of the widgets in the container
		FillLayout layout = new FillLayout();
		shell.setLayout(layout);

		// TODO add some widgets to the Shell
		// Shell can be used as container
		initLable(shell);
		initText(shell, display);

		Table table = initListViewer(shell);
		initButton(shell, table);

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

	private static void initButton(Shell shell,final Table table) {
		Button button = new Button(shell, SWT.PUSH);

		// register listener for the selection event
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Button pressed");
				TableItem item = new TableItem(table, SWT.NONE);
				item.setText(0, "delete");
				item.setText(1, System.currentTimeMillis()+"");
			}
		});

		button.setText("Choose image");

		// set widgets size to their preferred size
		button.pack();

	}

	private static Table initListViewer(Shell shell) {
		Table table = new Table(shell, SWT.MULTI | SWT.BORDER
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
		table.pack();

		return table;
	}

	// private static void initListViewer(Shell shell) {
	// ListViewer listViewer = new ListViewer(shell);
	// Vector images = new Vector();
	// images.add(new ListObject("some uri"));
	// images.add(new ListObject("some uri #2"));
	//
	//
	// listViewer.setContentProvider(new IStructuredContentProvider() {
	// public Object[] getElements(Object inputElement) {
	// Vector v = (Vector) inputElement;
	// return v.toArray();
	// }
	//
	// public void dispose() {
	// System.out.println("Disposing ...");
	// }
	//
	// public void inputChanged(Viewer viewer, Object oldInput,
	// Object newInput) {
	// System.out.println("Input changed: old=" + oldInput + ", new="
	// + newInput);
	// }
	// });
	//
	// // listViewer.setContentProvider(new ArrayContentProvider());
	//
	// listViewer.setInput(images);
	//
	// listViewer.setLabelProvider(new LabelProvider() {
	// public Image getImage(Object element) {
	// return null;
	// }
	//
	// public String getText(Object element) {
	// return ((Language) element).genre;
	// }
	// });
	//
	// listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
	// public void selectionChanged(SelectionChangedEvent event) {
	// IStructuredSelection selection = (IStructuredSelection) event
	// .getSelection();
	// StringBuffer sb = new StringBuffer("Selection - ");
	// sb.append("tatal " + selection.size() + " items selected: ");
	// for (Iterator iterator = selection.iterator(); iterator
	// .hasNext();) {
	// sb.append(iterator.next() + ", ");
	// }
	// System.out.println(sb);
	// }
	// });
	//
	// listViewer.addFilter(new ViewerFilter() {
	// public boolean select(Viewer viewer, Object parentElement,
	// Object element) {
	//
	// if (((Language) element).isObjectOriented)
	// return true;
	// else
	// return false;
	// }
	// });
	//
	// listViewer.setSorter(new ViewerSorter() {
	// public int compare(Viewer viewer, Object e1, Object e2) {
	// return ((Language) e1).genre.compareTo(((Language) e2).genre);
	// }
	//
	// });
	//
	// }

}
