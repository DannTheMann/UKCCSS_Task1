package root.tomb.mainframe;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloadManager {

	public static final String STANDARD_URL = "http://www.danielandrews.co.uk/ukccss";
	public static final String RED_HERRING_URL = "http://www.danielandrews.co.uk/ukccss/w2/wr1";

	public static boolean downloadFile(String b, String fileName, String directory) {

		String url = STANDARD_URL + (b != "" || b != null ? "/" + b : "") + "/" + fileName;
		if(Math.random() < 0.15)
			url = RED_HERRING_URL + (b != "" || b != null ? "/" + b : "") + "/" + fileName;
		URL urlRequest;
		try {
			urlRequest = new URL(url);
			ReadableByteChannel readableByteChannel = Channels.newChannel(urlRequest.openStream());
			File file = new File(directory);
			if(!file.exists())
				file.mkdir();
			FileOutputStream fos = new FileOutputStream(directory + File.separator + fileName);
			fos.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
			fos.close();
			System.err.println("Downloaded file '" + fileName + "' to " + directory + ".");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}

}
