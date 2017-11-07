import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 */
public class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;
    protected String serverText   = null;
    private Random rand;
    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
        this.rand = new Random(0);
    }

    public void run() {
        try (OutputStream output = clientSocket.getOutputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));){

            long time = System.currentTimeMillis();
            // System.out.println("here1");


            String str;
            int cnt = 1;

            while ( (str = bufferedReader.readLine()) != null && cnt > 0 ){
                System.out.println(str);
                cnt -= 1;
                // System.out.println("here2");
            }

            // System.out.println("here3");
            
            try {
                Thread.sleep(100); //milliseconds
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }

            int sum = 0;
            for(int i = 0; i < 10000000; i++){
                sum += rand.nextInt(20) - 10;
            }


            output.write(("OK\nWorkerRunnable: " +
                this.serverText + " - " + Integer.toString(sum)).getBytes());

            // try {
            //     Thread.sleep(6000); //milliseconds
            // } catch (InterruptedException e) {
            //     // e.printStackTrace();
            // }

            System.out.println("writing complete");

            try {
                Thread.sleep(100); //milliseconds
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
            clientSocket.close();
            
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}