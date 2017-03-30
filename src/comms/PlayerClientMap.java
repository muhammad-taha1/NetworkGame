package comms;

import gameLogistics.Player;

import java.net.Socket;

public class PlayerClientMap {
	public Socket client;
	public Player player;
	
	public PlayerClientMap(Socket a, Player b) {
		client = a;
		player = b;
	}

}
