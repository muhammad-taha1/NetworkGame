package comms;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import cardLibraries.Card;
import cardLibraries.Deck;
import gameLogistics.Player;
import gameLogistics.Rules;
import gameLogistics.Rules.handType;


public class Server extends Thread {
	private final ServerSocket serverSocket;
	/// the clientsList is used to keep a list of all clients connected to server. Used for broadcast
	private final ArrayList<PlayerClientMap> playerClientMapList;
	private int turnTracker = 0;
	private final int numberOfPlayers = 2;
	private int lastBet;
	private int pot;

	public Server(int port) throws IOException {
		// port number specified when running on cmd as argument
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(100000);
		playerClientMapList = new ArrayList<PlayerClientMap>();
	}

	public synchronized String clientActions (final PlayerClientMap playerClient) {

		DataInputStream in = null;
		DataOutputStream out = null;
		System.out.println("Client action");

		try {
			out = new DataOutputStream(playerClient.client.getOutputStream());
			in = new DataInputStream(playerClient.client.getInputStream());

			// tell client of its turn by sending token
			out.writeUTF("TOKEN");
			out.writeInt(playerClient.player.getMoney());
			out.writeInt(lastBet);

			// get client's decision
			String clientMsg = in.readUTF();

			// TODO: actions according to client's decision
			if (clientMsg.contains("bet")) {
				String[] bet = clientMsg.split(" ");
				playerClient.player.bet(Integer.valueOf(bet[1]));
				lastBet = Integer.valueOf(bet[1]);

				System.out.println(lastBet);
				pot += Integer.valueOf(bet[1]);
			}

			if (clientMsg.contains("fold")) {
				playerClient.player.fold();
			}
			System.out.println("client says:" + clientMsg);

			return clientMsg;

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "";
	}

	public void sendDataToAllClients(String clientDecision) {
		for (PlayerClientMap pc : playerClientMapList) {
			try {
				DataOutputStream out = new DataOutputStream(pc.client.getOutputStream());
				out.writeUTF(clientDecision);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


	public synchronized void initializeClients(final PlayerClientMap playerClient, final CountDownLatch latch) {
		Thread c = new Thread() {
			@Override
			public void run() {
				System.out.println("Initializing a new client");

				try {
					// client-server comm.
					// get data from client and print it
					DataInputStream in = new DataInputStream(playerClient.client.getInputStream());
					String clientName = in.readUTF();
					// write output data as string to display on client
					DataOutputStream out = new DataOutputStream(playerClient.client.getOutputStream());

					int id = playerClientMapList.size();

					playerClient.player = new Player(clientName, id, 200);

					System.out.println("Player " + clientName +" successfully initialized, port " + playerClient.client.getPort());

					// tell the client about total number of participants
					out.writeUTF("numberOfPlayers " + numberOfPlayers);

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
				if (playerClientMapList.size() != numberOfPlayers) {
					// initialize server
					System.out.println("Welcome to poker server");
					System.out.println("Waiting for client on port " +
							serverSocket.getLocalPort() + "...");

					// accept client
					Socket server = serverSocket.accept();

					// the arrayList clientsList keeps a list of clients in server
					playerClientMapList.add(new PlayerClientMap(server, new Player("", 0, 200)));
					System.out.println("wait for all clients to connect....");
				}

				// do not proceed with system, until all clients are connected
				else {

					sendDataToAllClients("Start game");

					// initialize all clients
					CountDownLatch latch = new CountDownLatch(numberOfPlayers);
					for (PlayerClientMap pc : playerClientMapList) {
						this.initializeClients(pc, latch);
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

						pot = 0;

						turnTracker = 0;
						Deck deck = new Deck();
						ArrayList<Card> potCards = new ArrayList<Card>();

						// deal cards to all clients
						for (PlayerClientMap playerClient : playerClientMapList) {
							try {
								DataOutputStream out = new DataOutputStream(playerClient.client.getOutputStream());
								// deal 2 hand cards
								playerClient.player.addCardToHand(deck.deal());
								playerClient.player.addCardToHand(deck.deal());

								// tell client its hand cards
								String handCards = "hand ";
								handCards += playerClient.player.getHand().get(0).toString();
								handCards += ":";
								handCards +=  playerClient.player.getHand().get(1).toString();
								out.writeUTF(handCards);

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						// add 3 initial cards to pot
						for (int i = 0; i < 3; i++) {
							potCards.add(deck.deal());
						}

						// send pot cards to all clients
						String potCardsString = "";
						for (Card c : potCards) {
							potCardsString += c.toString() + ":";
						}
						potCardsString = potCardsString.substring(0, potCardsString.length() - 1);
						sendDataToAllClients("potCards " + potCardsString);

						// start first round
						boolean firstRoundOver = false;
						lastBet = 0;
						// go to make decision for each client
						while (!firstRoundOver) {
							System.out.println("turn: " + turnTracker);
							if (!playerClientMapList.get(turnTracker).player.isHasFolded()) {
								String clientDecision = this.clientActions(playerClientMapList.get(turnTracker));

								// now tell all clients about the above client's decision
								sendDataToAllClients(clientDecision);
								sendDataToAllClients("POT " + pot);
							}

							turnTracker++;
							if (turnTracker > (numberOfPlayers-1)) {
								turnTracker = 0;
								firstRoundOver = true;
							}
						}

						// start second round
						boolean secondRoundOver = false;
						turnTracker = 0;
						lastBet = 0;
						// go to make decision for each client
						System.out.println("starting second round");
							potCards.add(deck.deal());

							// send pot cards to all clients
							potCardsString = "";
							for (Card c : potCards) {
								potCardsString += c.toString() + ":";
							}
							potCardsString = potCardsString.substring(0, potCardsString.length() - 1);
							sendDataToAllClients("potCards " + potCardsString);

							while (!secondRoundOver) {
							System.out.println("turn: " + turnTracker);
							if (!playerClientMapList.get(turnTracker).player.isHasFolded()) {
								String clientDecision = this.clientActions(playerClientMapList.get(turnTracker));

								// now tell all clients about the above client's decision
								sendDataToAllClients(clientDecision);
								sendDataToAllClients("POT " + pot);
							}
							turnTracker++;
							if (turnTracker > (numberOfPlayers-1)) {
								turnTracker = 0;
								secondRoundOver = true;
							}
						}

						// start third round
						System.out.println("startign 3rd round");
						boolean thirdRoundOver = false;
						lastBet = 0;
						turnTracker = 0;
						// go to make decision for each client
							potCards.add(deck.deal());

							// send pot cards to all clients
							potCardsString = "";
							for (Card c : potCards) {
								potCardsString += c.toString() + ":";
							}

							potCardsString = potCardsString.substring(0, potCardsString.length() - 1);
							sendDataToAllClients("potCards " + potCardsString);

							while (!thirdRoundOver) {
							System.out.println("turn: " + turnTracker);
							if (!playerClientMapList.get(turnTracker).player.isHasFolded()) {
								String clientDecision = this.clientActions(playerClientMapList.get(turnTracker));

								System.out.println("turn: " + turnTracker);
								// now tell all clients about the above client's decision
								sendDataToAllClients(clientDecision);
								sendDataToAllClients("POT " + pot);
							}

							turnTracker++;
							if (turnTracker > (numberOfPlayers-1)) {
								turnTracker = 0;
								thirdRoundOver = true;
							}
						}

						// TODO: tell all clients the winner and update the winning players
						int max = 10;
						int maxIdx = 0;
						handType playerCombo = Rules.handType.pair;
						for (int i = 0; i < playerClientMapList.size(); i++) {
							handType CurrentPlayerCombo = Rules.winHierarchy(playerClientMapList.get(i).player.getHand().toArray(new Card[2]), potCards.toArray(new Card[5]));
							if (CurrentPlayerCombo.ordinal() < max) {
								max = CurrentPlayerCombo.ordinal();
								maxIdx = i;
								playerCombo = CurrentPlayerCombo;
							}
							System.out.println("player " + playerClientMapList.get(i).player.getName() + " has " + playerCombo);
						}

						sendDataToAllClients(playerClientMapList.get(maxIdx).player.getName() + " won with " + playerCombo.toString());
						playerClientMapList.get(maxIdx).player.addMoney(pot);

						// tell clients to update their funds and set pot money on display as 0
						for (PlayerClientMap pc : playerClientMapList) {
							DataOutputStream out = new DataOutputStream(pc.client.getOutputStream());
							out.writeUTF("updateWinnings");
							out.writeInt(pc.player.getMoney());
						}


						// round over tell all clients about it
						sendDataToAllClients("round over");
						// clear the hand cards and fold status for all players
						for (PlayerClientMap pc : playerClientMapList) {
							pc.player.clearHand();
							pc.player.resetFold();
						}
						// clear pot cards
						potCards.clear();
						pot = 0;
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
