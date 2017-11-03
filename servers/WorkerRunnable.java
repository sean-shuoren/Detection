package servers;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 */
public class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;
    protected String serverText   = null;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }

    public void run() {
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            long time = System.currentTimeMillis();

            int c;
            String res = "";
            InputStream in = socket.getInputStream();
            while ((c = in.read()) != -1) {
                res += c;
            }

            output.write(("HTTP/1.1 200 OK\n\nWorkerRunnable: " +
                this.serverText + " - " +
                time +
                "").getBytes());
            output.flush();

            // in.close();
            // output.close();
            System.out.println("Request processed: " + time + res);
            this.clientSocket.close();
            
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}