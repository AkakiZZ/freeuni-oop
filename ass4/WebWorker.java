import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;
import javax.swing.*;

public class WebWorker extends Thread {
    private final String urlString;
    private final WebFrame frame;
    private final Semaphore semaphore;
    private final int row;

    public WebWorker(String urlString, WebFrame frame, Semaphore semaphore, int row) {
        this.urlString = urlString;
        this.row = row;
        this.semaphore = semaphore;
        this.frame = frame;
    }

 	public void download() {
		InputStream input = null;
		StringBuilder contents = null;
		try {

			long startTime = System.currentTimeMillis();
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();

			connection.setConnectTimeout(5000);

			connection.connect();
			input = connection.getInputStream();

			BufferedReader reader  = new BufferedReader(new InputStreamReader(input));

			char[] array = new char[1000];
			int len;
			contents = new StringBuilder(1000);
			while ((len = reader.read(array, 0, array.length)) > 0) {
				contents.append(array, 0, len);
				Thread.sleep(100);
			}
            long endTime = System.currentTimeMillis();

            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String status = dateFormat.format(now) + " " + (endTime - startTime) + "ms " + contents.length() + " bytes";
            frame.updateRow(row, status);
		}
		catch(IOException e) {
			frame.updateRow(row, "err");
		}
		catch(InterruptedException exception) {
			frame.updateRow(row, "interrupted");
		}

		finally {
			try{
				if (input != null) input.close();
			}
			catch(IOException ignored) {}
		}
 	}

 	@Override
 	public void run() {
        try {
            semaphore.acquire();
            frame.increaseRunning();
            download();
			frame.decreaseRunning();
            semaphore.release();

        } catch (InterruptedException e) {
            frame.updateRow(row, "Interrupted");
        }
    }
}
