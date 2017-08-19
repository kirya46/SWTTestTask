package common.util;

public class Log {
	public static void d(String TAG, String message){
		System.out.println(TAG + ": " + message);
	}
	public static void d(String message){
		System.out.println(message);
	}
	
	public static void err(String error){
		System.err.println(error);
	}
	
}
