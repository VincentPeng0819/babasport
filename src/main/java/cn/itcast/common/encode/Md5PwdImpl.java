package cn.itcast.common.encode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class Md5PwdImpl implements Md5Pwd {
	public String encode(String password){
		MessageDigest messageDigest = null;
		try {
			 messageDigest = MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] digest = messageDigest.digest(password.getBytes());
		String s = Hex.encodeHexString(digest);
		return s;
	}
}
