import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;


import java.util.Random.*;

/**
 *
 * A complete Java class that demonstrates how to use the Socket
 * class, specifically how to open a socket, write to the socket,
 * and read from the socket.
 *
 * @author alvin alexander, alvinalexander.com.
 *
 */
public class Client implements Runnable {
	final static int NUM = 10;

	protected int port;

	// call our constructor to start the program
	public static void main(String[] args) {
		for (int i = 0; i < NUM; i++) {
			int port = Integer.parseInt(args[0]);
			Client c = new Client(port);

			try {
			    Thread.sleep(500);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}	

			new Thread(c).start();
		}
	}

	public Client(int port) {
		this.port = port;
	}

	public void run() {
		int reqNum = 3;
		while (reqNum-- != 0) {

			// delay 'time' milliseconds
			// int time = rand.nextInt(100) + 500;
			// int time = 500;
			// try {
			//     Thread.sleep(time);
			// } catch (InterruptedException e) {
			//     e.printStackTrace();
			// }

			try {
				// open a socket
				Socket socket = openSocket("localhost", this.port);

				// write-to, and read-from the socket.
				// in this case just write a simple command to a web server.
				String result = writeToAndReadFromSocket(socket, "hello\n\n"+"");

				// print out the result we got back from the server
				// System.out.println(result);
				// close the socket, and we're done
				socket.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String writeToAndReadFromSocket(Socket socket, String writeTo) throws Exception {
		int c;
		String res = "";

		try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));){
			// write text to the socket
            try {
                Thread.sleep(100); //milliseconds
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }

            out.println("sa wa di ka\nbye\n"+"");

			System.out.println("sending complete");
			// read text from the socket

            
            StringBuilder sb = new StringBuilder();
            String str;

            while ((str = bufferedReader.readLine()) != null) {
                System.out.println(str);
                // System.out.println("one line");
                // sb.append(str + "\n");
            }

            System.out.println("receiving complete");


			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "qi guai";
	}

	/**
	* Open a socket connection to the given server on the given port.
	* This method currently sets the socket timeout value to 10 seconds.
	* (A second version of this method could allow the user to specify this timeout.)
	*/
	private Socket openSocket(String server, int port) throws Exception
	{
		Socket socket;

		// create a socket with a timeout
		try	{
			InetAddress inteAddress = InetAddress.getByName(server);
			SocketAddress socketAddress = new InetSocketAddress(inteAddress, port);

			// create a socket
			socket = new Socket();

			// this method will block no more than timeout ms.
			int timeoutInMs = 10*1000;   // 10 seconds
			socket.connect(socketAddress, timeoutInMs);

			return socket;
		
		} catch (SocketTimeoutException ste) {
			System.err.println("Timed out waiting for the socket.");
			ste.printStackTrace();
			throw ste;
		}
	}

}