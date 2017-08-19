package common.widgets;

import common.util.ImageWraper;
import common.util.Log;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.*;

import java.util.ArrayList;
import java.util.Iterator;

public class ImageTable extends Table {
	
	private static final String TAG = ImageTable.class.getCanonicalName();

	public static final int DEFAULT_PREVIEW_WIDTH = 32;
	public static final int DEFAULT_PREVIEW_HEIGHT =32;

	private ArrayList<ImageWraper> wrappers;

	private OnItemListener callback;

	public interface OnItemListener{
		void onImageRemoved(ImageWraper imageWraper);
	}

	@Override
	protected void checkSubclass () {
		//do nothing
	}

	public ImageTable(Composite parent, int style,OnItemListener itemListener) {
		super(parent, style);

		this.wrappers = new ArrayList<ImageWraper>();
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
				Log.d(TAG,wrappers.size()+"");
				for (TableItem tableItem : selection) {

					//get ImageWrapper id from TableItem
					int id = (Integer) tableItem.getData("id");

					final ImageWraper imageById = ImageTable.this.getImageById(id);
					if (ImageTable.this.callback != null && imageById != null){
						ImageTable.this.callback.onImageRemoved(imageById);
					}

					//remove ImageWraper from table image list
					ImageTable.this.removeImage(id);

					//remove TableItem
					tableItem.dispose();
				}
			}
		});
	}

	public void addImage(ImageWraper imageWraper){
		
		//add wrapper to table list
		this.wrappers.add(imageWraper);

		// create TabItem for each selected file
		TableItem item = new TableItem(this, SWT.NONE);
		item.setData("id",imageWraper.getId());
		item.setImage(0, imageWraper.getPreview());
		item.setText(1," "+imageWraper.getFileName());
		Font font = new Font(getDisplay(),"Arial", 10, SWT.NORMAL );



		item.setFont(font);
	}

	public void removeImage(long id){
		final Iterator<ImageWraper> iterator = this.wrappers.iterator();
		while (iterator.hasNext()){
			ImageWraper wraper = iterator.next();
			Log.d(TAG, "Compare: " + wraper.getId() +  " / "+ id);
			if (wraper.getId() == id){
				iterator.remove();
				Log.d(TAG,"Size agter remove: " +wrappers.size());
			}
		}

	}

	public ImageWraper getImageById(long id){
		for (ImageWraper wraper : this.wrappers){
			if (wraper.getId() == id){
				return wraper;
			}
		}

		return null;
	}

}
