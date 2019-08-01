package conn;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

/**
 * Class to connect Scope and aquire Data
 * 
 * @author Andreas Butti
 *
 */
public class OwonTcpConnector {

	/**
	 * Buffer
	 */
	private byte[] buffer = new byte[50 * 1024];

	/**
	 * Socket to communicate
	 */
	private Socket socket;

	/**
	 * Format for screenshot name
	 */
	private DateTimeFormatter screenshotDate = DateTimeFormatter.ofPattern("dd.MM.yyyy-HH-mm-ss");

	/**
	 * Output Stream
	 */
	private OutputStream out;

	/**
	 * Input Stream
	 */
	private InputStream in;

	/**
	 * Constructor
	 * 
	 * @param socket
	 *            Socket to communicate
	 * @throws IOException
	 */
	public OwonTcpConnector(Socket socket) throws IOException {
		this.socket = socket;
		this.out = socket.getOutputStream();
		this.in = socket.getInputStream();
	}

	/**
	 * Connect Scope
	 * 
	 * @return true if connected
	 * @throws IOException
	 */
	public boolean connect() throws IOException {
		String result = queryString(":model?");
		// Something like: "306200103->"

		if (result.startsWith("30")) {
			System.out.println("Connected to scope: " + result);
			return true;
		}
		return false;
	}

	/**
	 * Take Screenshot
	 * 
	 * @throws IOException
	 */
	public void takeScreenshot() throws IOException {
		out.write(":DATA:WAVE:SCREen:BMP?".getBytes(StandardCharsets.ISO_8859_1));

		String name = "screenshot/" + LocalDateTime.now().format(screenshotDate);
		String nameBmp = name + ".bmp";

		try (OutputStream imgout = new FileOutputStream(nameBmp)) {
			int bmpSize = readInt();
			System.out.println("Bmp size: " + bmpSize);

			while (bmpSize > 0) {
				int size = in.read(buffer);
				bmpSize -= size;
				imgout.write(buffer, 0, size);

				System.out.println("Remaining bytes: " + bmpSize);
			}

			System.out.println("Screenshot received successfully");
		}

		try (InputStream bmpin = new FileInputStream(nameBmp)) {
			BufferedImage img = ImageIO.read(bmpin);
			ImageIO.write(img, "PNG", new File(name + ".png"));
			Files.delete(Paths.get(nameBmp));
		}
	}

	/**
	 * Take Screenshot
	 * 
	 * @throws IOException
	 */
	public void dumpMemory() throws IOException {
		out.write(":DATA:WAVE:DEPMem:ALL?".getBytes(StandardCharsets.ISO_8859_1));

		String name = "screenshot/dump-" + LocalDateTime.now().format(screenshotDate);
		String nameBmp = name + ".bin";

		try (OutputStream imgout = new FileOutputStream(nameBmp)) {
			int bmpSize = readInt();
			System.out.println("Dump size: " + bmpSize);

			while (bmpSize > 0) {
				int size = in.read(buffer);
				bmpSize -= size;
				imgout.write(buffer, 0, size);

				System.out.println("Remaining bytes: " + bmpSize);
			}

			System.out.println("Dump received successfully");
		}
	}

	/**
	 * Read an Int from Input
	 * 
	 * @return Int
	 * @throws IOException
	 */
	private int readInt() throws IOException {
		int i = in.read();
		i |= in.read() << 8;
		i |= in.read() << 16;
		i |= in.read() << 24;

		return i;
	}

	/**
	 * Query a String, return a String
	 * 
	 * @param query
	 *            Query
	 * @return Answer
	 * @throws IOException
	 */
	private String queryString(String query) throws IOException {
		OutputStream out = socket.getOutputStream();
		InputStream in = socket.getInputStream();
		out.write(query.getBytes(StandardCharsets.ISO_8859_1));
		int size = in.read(buffer);
		System.out.println("Received: " + size);
		return new String(buffer, 0, size, StandardCharsets.ISO_8859_1);
	}

	/**
	 * Main for Testing
	 * 
	 * @param args
	 *            Not used
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(":: start");
		try (Socket socket = new Socket("192.168.178.2", 3000)) {
			OwonTcpConnector con = new OwonTcpConnector(socket);
			if (!con.connect()) {
				System.out.println("Could not connect scope!");
				return;
			}

			// con.takeScreenshot();
			con.dumpMemory();
		}
		System.out.println(":: end");
	}
}
