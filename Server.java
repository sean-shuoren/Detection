import servers.MultiThreadedServer;

public class Server {
	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		System.out.println("Start Server on Port" + args[0]);
		MultiThreadedServer server = new MultiThreadedServer(port);
		new Thread(server).start();

		try {
		    Thread.sleep(60 * 1000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		System.out.println("Stopping Server");
		server.stop();
	}
}
