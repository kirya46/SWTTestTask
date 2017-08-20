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
	public static final int DEFAULT_PREVIEW_HEIGHT =32;

	private ArrayList<ImageWrapper> wrappers;

	private OnItemListener callback;

	public interface OnItemListener{
		void onImageRemoved(ImageWrapper ImageWrapper);
		void onItemSelected(ImageWrapper ImageWrapper);
		void onItemDeselect(ImageWrapper ImageWrapper);
	}

	@Override
	protected void checkSubclass () {
		//do nothing
	}

	public ImageTable(Composite parent, int style,OnItemListener itemListener) {
		super(parent, style);

		this.wrappers = new ArrayList<ImageWrapper>();
		this.callback = itemListener;

		this.setLinesVisible(true);
		this.setHeaderVisible(false);

		// create column for source image
		TableColumn imageColumn = new TableColumn(this, SWT.NONE);
		imageColumn.pack();
		imageColumn.setWidth(DEFAULT_PREVIEW_WIDTH);

		TableColumn nameColumn = new TableColumn(this,SWT.NONE);
		nameColumn.pack();
		nameColumn.setWidth(200-DEFAULT_PREVIEW_WIDTH);


		// Selection callback of all table
		this.addListener(SWT.DefaultSelection, new Listener() {
			public void handleEvent(Event e) {
				TableItem[] selection = ImageTable.this.getSelection();

				for (TableItem tableItem : selection) {

					//get ImageWrapper id from TableItem
					int id = (Integer) tableItem.getData("id");

					final ImageWrapper imageById = ImageTable.this.getImageById(id);
					if (ImageTable.this.callback != null && imageById != null){
						ImageTable.this.callback.onImageRemoved(imageById);
					}

					//remove ImageWrapper from table image list
					ImageTable.this.removeImage(id);

					//remove TableItem
					tableItem.dispose();
				}
			}
		});

		this.addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				final TableItem[] selection = ImageTable.this.getSelection();


				// TODO: 20/08/17 call callback.deselect()

				for (TableItem tableItem : selection) {

					//get ImageWrapper id from TableItem
					int id = (Integer) tableItem.getData("id");

					final ImageWrapper imageById = ImageTable.this.getImageById(id);
					if (ImageTable.this.callback != null && imageById != null){
						ImageTable.this.callback.onItemSelected(imageById);
					}
				}
			}
		});
	}


    public void select(final ImageWrapper ImageWrapper){
		Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				ImageTable.this.deselectAll();
				ImageTable.this.select(ImageTable.this.wrappers.indexOf(ImageWrapper));
			}
		});
	}


	public void deselect(final ImageWrapper ImageWrapper){
    	Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				ImageTable.this.deselectAll();
				ImageTable.this.deselect(ImageTable.this.wrappers.indexOf(ImageWrapper));

			}
		});
	}




	public void addImage(ImageWrapper ImageWrapper){
		
		//add wrapper to table list
		this.wrappers.add(ImageWrapper);

		// create TabItem for each selected file
		TableItem item = new TableItem(this, SWT.NONE);
		item.setData("id",ImageWrapper.getId());
		item.setImage(0, ImageWrapper.getPreview());
		item.setText(1," "+ImageWrapper.getFileName());
		Font font = new Font(getDisplay(),"Arial", 10, SWT.NORMAL );



		item.setFont(font);
	}

	public void removeImage(long id){
		final Iterator<ImageWrapper> iterator = this.wrappers.iterator();
		while (iterator.hasNext()){
			ImageWrapper wraper = iterator.next();
			if (wraper.getId() == id){
				iterator.remove();
			}
		}
		
		this.wrappers.trimToSize();
	}

	public ImageWrapper getImageById(long id){
		for (ImageWrapper wraper : this.wrappers){
			if (wraper.getId() == id){
				return wraper;
			}
		}

		return null;
	}

}
