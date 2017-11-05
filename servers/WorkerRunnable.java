package servers;

import java.io.*;
import java.net.Socket;

import java.util.concurrent.TimeUnit;

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

            long time = System.currentTimeMillis();
            System.out.println("here1");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String str;

            while ((str = bufferedReader.readLine()) != null) {
                System.out.println(str);
                // sb.append(str + "\n");
            }
            bufferedReader.close();
            System.out.println("here2");
            
            try {
                Thread.sleep(1000); //milliseconds
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }

            OutputStream output = clientSocket.getOutputStream();
            output.write(("HTTP/1.1 200 OK\n\nWorkerRunnable: " +
                this.serverText + " - " +
                time +
                "").getBytes());

            // try {
            //     Thread.sleep(6000); //milliseconds
            // } catch (InterruptedException e) {
            //     // e.printStackTrace();
            // }
            output.flush();
            output.close();
            System.out.println("writing complete");
            // in.close();
            // output.close();
            clientSocket.close();

        } catch (IOException e) {
            //report exception somewhere.
            // e.printStackTrace();
        }
    }
}