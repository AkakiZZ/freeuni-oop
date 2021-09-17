// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import java.security.*;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	private final int maxLength;
	private final int numThreads;
	private final byte[] hash;
	private final CountDownLatch latch;
	private String crackedPassword;
	private final Lock passLock;

	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int aByte : bytes) {
			int val = aByte;
			val = val & 0xff;  // remove higher bits, sign
			if (val < 16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}

	public Cracker(String hashedPassword, int maxLength, int numThreads) {
		latch = new CountDownLatch(numThreads);
		hash = hexToArray(hashedPassword);
		passLock = new ReentrantLock();
		this.maxLength = maxLength;
		this.numThreads = numThreads;
	}

	public static String generateHash(String s) throws NoSuchAlgorithmException {
		MessageDigest digest = null;
		digest = MessageDigest.getInstance("SHA");

		return hexToString(digest.digest(s.getBytes()));
	}

	public String crackPassword() throws InterruptedException, NoSuchAlgorithmException{
		int offset = CHARS.length / numThreads - 1;
		int start = 0;
		for (int i = 0; i < numThreads - 1; i++) {
			start = offset * i + i;
			Thread worker = new Thread(new Worker(start, start + offset));
			worker.start();
		}
		Thread worker = new Thread(new Worker(start + offset + 1, CHARS.length - 1));
		worker.start();

		latch.await();
		System.out.println("All done");
		return crackedPassword;
	}
	
	
	public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {
		if (args.length == 0) {
			throw new IllegalArgumentException("Args: target length [workers]");
		}
		if (args.length == 1) {
			System.out.println(generateHash(args[0]));
			return;
		}
		// args: targ len [num]
		String targ = args[0];
		int len = Integer.parseInt(args[1]);
		int num = 1;
		if (args.length>2) {
			num = Integer.parseInt(args[2]);
		}
		Cracker cracker = new Cracker(targ, len, num);
		System.out.println(cracker.crackPassword());
		// a! 34800e15707fae815d7c90d49de44aca97e2d759
		// xyz 66b27417d37e024c46526c2f6d358a754fc552f3

		// YOUR CODE HERE
	}

	public class Worker implements Runnable {
		private final int start;
		private final int end;
		private MessageDigest md;

		public Worker(int start, int end) throws NoSuchAlgorithmException{
			this.start = start;
			this.end = end;
			md = MessageDigest.getInstance("SHA");
		}

		@Override
		public void run() {
			generateAll();
			latch.countDown();
		}

		private void generateAll() {
			for (int i = start; i <= end; i++) {
				for (int j = 1; j <= maxLength; j++) {
					byte[] pass = new byte[j];
					pass[0] = (byte)CHARS[i];
					permute(pass, 1, j);
				}
			}
		}

		private void permute(byte[] pass, int left, int right) {
			if (left == right) {
				if (Arrays.equals(md.digest(pass), hash)) {
					passLock.lock();
					crackedPassword = new String(pass);
					passLock.unlock();
				}
			} else {
				for (char ch : CHARS) {
					pass[left] = (byte) ch;
					permute(pass, left + 1, right);
				}
			}
		}
	}
}
