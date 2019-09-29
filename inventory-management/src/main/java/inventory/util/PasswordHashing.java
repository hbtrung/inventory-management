package inventory.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordHashing {
	private static final String SALT = "inventory_management";
	public static String encrypt(String originalPassword) {
		String result = null;
		byte[] salt = SALT.getBytes();
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt);
			byte[] passHash = md.digest(originalPassword.getBytes(StandardCharsets.US_ASCII));
			result = Base64.getEncoder().encodeToString(passHash).substring(0, 64);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static void main(String[] args) {
		String rs = encrypt("12345");
		System.out.println(rs);
	}
}
