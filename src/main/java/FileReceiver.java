import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileReceiver {
    private static final int PORT = 12345;
    private static final String SAVE_DIR = "D:\\Videa\\motionVideos\\";

    public void startReceiving() {

        new File(SAVE_DIR).mkdirs();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT);
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     InputStream is = clientSocket.getInputStream();
                     FileOutputStream fos = new FileOutputStream(SAVE_DIR + "motion_" + System.currentTimeMillis() + ".mp4")) {

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                    System.out.println("File received and saved.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}