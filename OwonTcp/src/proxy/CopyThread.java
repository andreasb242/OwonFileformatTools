package proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Thread to forward one input to one output stream
 * 
 * @author Andreas Butti
 */
public class CopyThread extends Thread {

	/**
	 * Dump ID
	 */
	private String id;

	/**
	 * Buffer
	 */
	private byte[] buffer = new byte[50 * 1024];

	/**
	 * Input to read
	 */
	private InputStream in;

	/**
	 * Output to write
	 */
	private OutputStream out;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            ID to print
	 * @param in
	 *            In
	 * @param out
	 *            out
	 */
	public CopyThread(String id, InputStream in, OutputStream out) {
		this.id = id;
		this.in = in;
		this.out = out;
	}

	@Override
	public void run() {
		try {
			while (true) {
				int size = in.read(buffer);
				out.write(buffer, 0, size);
				dumpData(buffer, size);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Dump Data to System.out
	 * 
	 * @param buffer
	 *            Buffer
	 * @param size
	 *            Size
	 */
	private void dumpData(byte[] buffer, int size) {
		System.out.print(id);

		for (int i = 0; i < size; i++) {
			String str = Integer.toHexString(buffer[i] & 0xff);
			if (str.length() < 2) {
				str = " " + str;
			}

			System.out.print(str);
		}

		System.out.println();
		System.out.print(id + "-> ");
		System.out.println(new String(buffer, 0, size, StandardCharsets.ISO_8859_1));
	}
}
