import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;


public class Server extends Thread {
	private final ServerSocket serverSocket;

	public Server(int port) throws IOException {
		// port number specified when running on cmd as argument
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(100000);
	}

	public void clientActions (final Socket serveClient) {
		// create a new thread for each connected client
		// define run functionality for client
		Thread c = new Thread() {
			@Override
			public void run() {
				try {
					System.out.println("Just connected to " + serveClient.getRemoteSocketAddress());

					// client-server comm. 
					// get data from client and print it
					DataInputStream in = new DataInputStream(serveClient.getInputStream());
					System.out.println(in.readUTF());
					// write output data as string to display on client
					DataOutputStream out = new DataOutputStream(serveClient.getOutputStream());
					out.writeUTF("Please enter your name");
					// get data again
					in = new DataInputStream(serveClient.getInputStream());

					while (in.available() <= 0) {
						// wait for client to type
					}

					String clientName = in.readUTF();
					System.out.println("client " + clientName + " is connected on port " + serveClient.getLocalPort());
					// write data to specify mirroring action
					out = new DataOutputStream(serveClient.getOutputStream());
					out.writeUTF("This server acts as mirror for your messages. Type close to disconnect from server");

					while(true) {

						in = new DataInputStream(serveClient.getInputStream());

						while (in.available() <= 0) {
							// wait for client to type
						}
						String clientMsg = in.readUTF();
						System.out.println("Client " + clientName + " says: " + clientMsg);

						if (clientMsg.compareToIgnoreCase("close") == 0) {
							// close server when client types close
							out.writeUTF("Thank you for connecting to " + serveClient.getLocalSocketAddress()
									+ "\nGoodbye!");
							serveClient.close();
						} 
						else {
							// mirror with client until client types close
							out = new DataOutputStream(serveClient.getOutputStream());
							out.writeUTF(clientMsg);
							in = new DataInputStream(serveClient.getInputStream());
						}
					}


				}

				catch(SocketTimeoutException s) {
					System.out.println("Socket timed out!");
					return;
				}catch(IOException e) {
					e.printStackTrace();
					return;
				}
			}
		};

		// start thread for each client - functionality defined above
		c.start();
	}

	@Override
	public void run() {
		while(true) {
			try {
				// initialize server
				System.out.println("Welcome to poker server");
				System.out.println("Waiting for client on port " + 
						serverSocket.getLocalPort() + "...");
				// accept client
				Socket server = serverSocket.accept();
				this.clientActions(server);

			}catch(SocketTimeoutException s) {
				System.out.println("Socket timed out!");
				break;
			}catch(IOException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	public static void main(String [] args) {
		// start server on new thread on port# specified as cmd input argument
		int port = Integer.parseInt(args[0]);
		try {
			Thread t = new Server(port);
			t.start();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
