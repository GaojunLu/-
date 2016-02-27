package com.example.mobilesafe.utiles;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Ω¯––MD5º”√‹
 * @author Administrator
 *
 */
public class MD5Utiles {
	public static String encode(String input){
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] bs = digest.digest(input.getBytes());
			StringBuilder builder = new StringBuilder();
			for (byte b : bs) {
				String s = Integer.toHexString(b&0xff);
				if(s.length()==1){
					builder.append("0");
				}
				builder.append(s);
			}
			return builder.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
