import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkClient extends NetworkAdapter {


    private PrintWriter writer;
    private BufferedReader reader;
    private static NetworkAdapter network;

    public NetworkClient(Socket socket) {
        super(socket);
        try{
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            network = new NetworkAdapter(socket);

            network.setMessageListener(new NetworkAdapter.MessageListener(){
                @Override
                public void messageReceived(NetworkAdapter.MessageType type, int x, int y) {
                    switch (type) {
                        case PLAY:
                            network.writePlay();
                            break;
                        case PLAY_ACK:
                            network.writePlayAck(true, true);
                            break;
                        case MOVE:
                            //Player.Opponent.pickPlace(x,y);
                            break;
                        case MOVE_ACK:
                            network.writeMoveAck(x, y);
                            break;
                        case QUIT:
                            network.writeQuit();
                            break;
                        case CLOSE:
                            network.close();
                            break;
                        case UNKNOWN:
                            System.err.println("Unknown message");
                            break;
                    }
                }
            });
            network.receiveMessagesAsync();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void startClient() {
        try {
            String address = "localhost";
            int serverPort = 8001;
            Socket socket = new Socket(address, serverPort);
            NetworkClient client = new NetworkClient(socket);

            client.writePlay();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] agrs){
        SwingUtilities.invokeLater(() -> {
            Omok omokClient = new Omok(false, 8001);

        });
    }

}

