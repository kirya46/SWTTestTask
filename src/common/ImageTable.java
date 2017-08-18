package common;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class ImageTable extends Table {
	
	private static final String TAG = ImageTable.class.getCanonicalName();
	
	private ArrayList<ImageWraper> wrapers;
	
	@Override
	protected void checkSubclass () {
		//do nothing
	}

	public ImageTable(Composite parent, int style,ArrayList<ImageWraper> wrapers) {
		super(parent, style);
		
		//save list of images to 
		this.wrapers = wrapers;
		
		this.setLinesVisible(true);
		this.setHeaderVisible(false);
		
		
		// create column for 'delete icon'
		TableColumn iconColumn = new TableColumn(this, SWT.NONE);
		iconColumn.pack();
		iconColumn.setWidth(50);

		// create column for source image
		TableColumn imageColumn = new TableColumn(this, SWT.NONE);
		imageColumn.pack();
		imageColumn.setWidth(100);
		
		
		// Selection listener of all table
//		this.addListener(SWT.DefaultSelection, new Listener() {
//			public void handleEvent(Event e) {
//				String string = "";
//				TableItem[] selection = ImageTable.this.getSelection();
//
//				for (TableItem tableItem : selection) {
//					System.out.println("Remove table item: "
//							+ tableItem.getText(1));
//					tableItem.dispose();
//					
//					//TODO: remove from canvas
//				}
//			}
//		});
	}
	
	public void addImage(){
		//TODO: add image
	}
	
	public void removeImage(){
		//TODO: remove image by arg
	}

}
