package comms;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


public class Server extends Thread {
	private final ServerSocket serverSocket;
	/// the clientsList is used to keep a list of all clients connected to server. Used for broadcast
	private final ArrayList<Socket> clientsList;
	private String potCards;
	private int turnTracker = 0;
	private final int numberOfPlayers = 2;

	public Server(int port) throws IOException {
		// port number specified when running on cmd as argument
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(100000);
		clientsList = new ArrayList<Socket>();
		// initialize the deck here, along with pot cards
		// assume the following pot cards
		potCards = "AceH, AceC, AceD";		
	}

	public synchronized String clientActions (final Socket serveClient) {

		DataInputStream in = null;
		DataOutputStream out = null;
		System.out.println("Client action");

		try {
			out = new DataOutputStream(serveClient.getOutputStream());
			in = new DataInputStream(serveClient.getInputStream());

			// tell client of its turn by sending token
			out.writeUTF("TOKEN");
			while (in.available() <= 0) {
				// wait for client to make decision
			}

			// get client's decision
			String clientMsg = in.readUTF();
			System.out.println("client says:" + clientMsg);

			return clientMsg;

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "";
	}

	public void sendDataToAllClients(String clientDecision) {
		for (Socket client : clientsList) {
			try {
				DataOutputStream out = new DataOutputStream(client.getOutputStream());
				out.writeUTF("last Client's decision: " + clientDecision);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


	public synchronized void initializeClients(final Socket serveClient, final CountDownLatch latch) {
		Thread c = new Thread() {
			@Override
			public void run() {
				System.out.println("Initializing a new client");

				try {
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
					System.out.println("client " + clientName + " is connected on port " + serveClient.getPort());
					// tell client about player object
					// send client its id
					System.out.println("Send id to client");
					int id = clientsList.size();
					out.writeInt(id);

					System.out.println("sending card stuff to cleint");
					// tell client the pot cards - should be a global variable
					out.writeUTF(potCards);

					// send client its hand cards
					out.writeUTF("10D, 9D");

					// tell client the pot money. TODO
					out.writeUTF("potMoney: 0");
					out.writeUTF("type bet, fold or pass to make a decision");

					latch.countDown();
					while (true) {
						// keep this background thread alive, just to keep each  
						// client from disconnecting
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

		c.start();

	}

	@Override
	public void run() {
		while(true) {
			try {
				if (clientsList.size() != numberOfPlayers) {
					// initialize server
					System.out.println("Welcome to poker server");
					System.out.println("Waiting for client on port " + 
							serverSocket.getLocalPort() + "...");

					// accept client
					Socket server = serverSocket.accept();

					// the arrayList clientsList keeps a list of clients in server
					clientsList.add(server);
					System.out.println("wait for all clients to connect....");
				}

				// do not proceed with system, until all clients are connected
				else {

					// initialize all clients
					CountDownLatch latch = new CountDownLatch(numberOfPlayers);
					for (Socket client : clientsList) {
						this.initializeClients(client, latch);
					}

					try {
						latch.await();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("All clients initialized, go to client actions");

					// allow each client to make decisions, based on turnTracker
					while (true) {
						// go to make decision for each client
						String clientDecision = this.clientActions(clientsList.get(turnTracker));
						// now tell all clients about the above client's decision

						turnTracker++;
						if (turnTracker > (numberOfPlayers-1)) turnTracker = 0;

						System.out.println(turnTracker);
						sendDataToAllClients(clientDecision);


					}
				}

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
