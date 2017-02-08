import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Client {

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


			while (true) {
				// only close when we type close
				String stringForServer = scan.nextLine();
				out = new DataOutputStream(outToServer);
				out.writeUTF(stringForServer);

				in = new DataInputStream(inFromServer);
				System.out.println("Server says: " + in.readUTF());

				if (stringForServer.compareToIgnoreCase("close") == 0) {
					// close client and return from method once close is typed
					out = new DataOutputStream(outToServer);
					out.writeUTF("Goodbye from " + client.getRemoteSocketAddress());
					client.close();
					scan.close();
					break;
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
