package comms;
import gameLogistics.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import cardLibraries.Card;


public class Client {
	
	// player associated with the client
	static Player player;

	public static void main(String[] args) {
		String serverName = args[0];
		int port = Integer.parseInt(args[1]);
		try {
			System.out.println("Connecting to " + serverName + " on port " + port);
			Socket client = new Socket(serverName, port);

			System.out.println("Just connected to " + client.getRemoteSocketAddress());
			OutputStream outToServer = client.getOutputStream();
			DataOutputStream out = new DataOutputStream(outToServer);

			out.writeUTF("Hello from " + client.getLocalSocketAddress());
			InputStream inFromServer = client.getInputStream();
			DataInputStream in = new DataInputStream(inFromServer);

			System.out.println("Server says: " + in.readUTF());
			Scanner scan = new Scanner(System.in);
			// first scan has to be the server asking us the name
			String name = scan.nextLine();
			// send server the name and get a player id
			out.writeUTF(name);
			// get id from server
			int id = in.readInt();
			// initialize Player object for this client - by default 
			// initial money = $200
			player = new Player(name, id, 200);


			while (true) {

				if (in.available() <= 0) {
					//wait
					continue;
				}
				else if (in.available() > 0) {
					// get server msgs
					String serverMsg = in.readUTF();
					System.out.println("Server says: " + serverMsg);
					
					if (serverMsg.equalsIgnoreCase("TOKEN")) {
						System.out.println("Please type your decision:");
						String decision = scan.nextLine();
						out.writeUTF(decision);
					}
					else if (serverMsg.contains("hand")) {
						String[] handCards = serverMsg.split(" ");
						
						for (int i = 1; i < 3; i++) {
							String[] handDetails = handCards[i].split(":");
							player.addCardToHand(new Card(handDetails[0], handDetails[1]));
						}
					}

				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
