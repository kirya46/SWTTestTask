package common;

public class ListObject {
	private String imageURI;
	
	public ListObject(String imageURI){
		this.imageURI = imageURI;
	}
	
	public String getImageUri(){
		return this.imageURI;
	}
	
	 public String toString() {
	      return "URI: " + this.imageURI;
	    }
}
