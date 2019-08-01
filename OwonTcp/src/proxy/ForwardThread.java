package proxy;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Forward connection Thread
 * 
 * @author Andreas Butti
 */
public class ForwardThread extends Thread {

	/**
	 * Server
	 */
	private Socket server;

	/**
	 * Thread ID
	 */
	private long id;

	/**
	 * Buffer
	 */
	private byte[] buffer = new byte[50 * 1024];

	/**
	 * Constructor
	 * 
	 * @param server
	 *            Server Socket
	 */
	public ForwardThread(Socket server) {
		this.server = server;
		this.id = Thread.currentThread().getId();
	}

	@Override
	public void run() {
		try {
			// Never closed...
			Socket client = new Socket("192.168.178.2", 3000);
			InputStream sin = server.getInputStream();
			OutputStream sout = server.getOutputStream();
			InputStream cin = client.getInputStream();
			OutputStream cout = client.getOutputStream();

			new CopyThread(id + "W: ", sin, cout).start();
			new CopyThread(id + "R: ", cin, sout).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
