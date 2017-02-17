package room.toom.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class Solution {
	
	public static void main(String[] a) throws Exception{
		
		String x4 = "111111";
		
		BigInteger l = new BigInteger(MessageDigest.getInstance("MD5").digest("".getBytes())).
				// x4 = 111 111
				mod(new BigInteger(x4));
		
		String password = "";
		
		for(int i = 0; i < Integer.MAX_VALUE; i++){
			
			password = "" + i;
			
			l = new BigInteger(MessageDigest.getInstance("MD5").digest(password.getBytes())).
					// x4 = 111 111
					mod(new BigInteger(x4));
			
			if(l.equals(BigInteger.ZERO)){
				System.out.println("Solution, password is: " + i);
				Thread.sleep(3000);
			}
			
		}
		
	}
}
