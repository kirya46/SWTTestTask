package common;

public class Log {
	public static void d(String TAG, String message){
		System.out.println(TAG + ": " + message);
	}
	public static void d(String message){
		System.out.println(message);
	}
}
