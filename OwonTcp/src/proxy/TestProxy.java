package proxy;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Proxy for Logging Data, connect with the App to this Proxy, and forward it to
 * the scope...
 * 
 * @author Andreas Butti
 */
public class TestProxy {

	/**
	 * Constructor
	 */
	public TestProxy() {
	}

	/**
	 * Forward Proxy
	 * 
	 * @throws IOException
	 */
	private void forward() throws IOException {
		try (ServerSocket server = new ServerSocket(3000)) {
			while (true) {
				new ForwardThread(server.accept()).start();
				System.out.println("Accepted socket!");
			}
		}
	}

	/**
	 * Command Main
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		TestProxy tp = new TestProxy();
		tp.forward();
	}
}
