import javax.swing.*;
import java.awt.*;
import java.net.Socket;

public class NetworkServer extends NetworkAdapter{

    private static NetworkAdapter network;
    private static NetworkAdapter.MessageWriter message;

    public NetworkServer(Socket socket) {
        super(socket);

        try {
            network = new NetworkAdapter(socket);

            network.setMessageListener(new NetworkAdapter.MessageListener() {
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
            startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void startServer(){
        try{
            int response = JOptionPane.showConfirmDialog(null, "Do you want to play a game?", "Game invitation", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                message.write("Accepted");
            }
            else{
                message.write("Declined");

            }
        } catch (HeadlessException e) {
            e.printStackTrace();
        }
    }
    public static NetworkAdapter getNetwork() {
        return network;
    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(() ->{
            Omok omokServer = new Omok(true, 8000);
        });
    }
}


