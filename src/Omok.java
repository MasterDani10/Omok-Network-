import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class Omok {
    Board board = new Board();
    Player player1 = new Player("Player1");
    Player player2 = new Player("Player2");
    ArrayList<Point> player1Stones = new ArrayList<>();
    ArrayList<Point> player2Stones = new ArrayList<>();
    ArrayList<Point> winningRow = new ArrayList<>();
    BoardPanel d = new BoardPanel(new Board(), player1Stones,player2Stones);
    Boolean repeat = true;
    JComboBox comboBox;
    JLabel player;

    int x = 0;
    int y = 0;
    Point point = new Point(0,0);
    Point pointCom = new Point(0,0);
    Boolean mouseListener = false;
    JTextArea serverTextArea;
    BufferedReader in;
    JPanel panel2;
    JLabel opponentText;
    HumanLogic h;
    Boolean isConnected = false;
    int size;
    JButton connect;
    JSONObject computerMove;
    BoardPanel winning;
    JPanel center;
    Boolean winningPanel = false;
    String pid;
    String response;
    String urlGetInfo = "http://omok.atwebpages.com/info/";
    String newGame = "http://omok.atwebpages.com/new/?strategy=%s";
    String move = "http://omok.atwebpages.com/play/?pid=%s&x=%d&y=%d";



    public Omok(){
        board.selectPlayerOne(player1);
        Image imagePlay = Toolkit.getDefaultToolkit().getImage("Resources/play.png").
                getScaledInstance(20, 20, 20);
        Image imageAbout = Toolkit.getDefaultToolkit().getImage("Resources/about.png").
                getScaledInstance(20, 20, 20);
        ImageIcon iconPlay = new ImageIcon(imagePlay);
        ImageIcon iconAbout = new ImageIcon(imageAbout);
        serverTextArea = new JTextArea(14,32);

        // Frame
        JFrame frame = new JFrame("Omok");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(470,615);

        // Menu Bar
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        menu.setMnemonic(KeyEvent.VK_G);
        menu.getAccessibleContext().setAccessibleDescription("Game menu");
        menuBar.add(menu);

        // Menu Item
        JMenuItem menuItemPlay = new JMenuItem("Play", KeyEvent.VK_P);
        menuItemPlay.setIcon(iconPlay);
        menuItemPlay.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_P, InputEvent.ALT_DOWN_MASK));
        menuItemPlay.getAccessibleContext().setAccessibleDescription(
                "Play game");
        menuItemPlay.addActionListener(e -> {
            playButtonClicked(frame,(String)comboBox.getSelectedItem());
        });
        menu.add(menuItemPlay);


        JMenuItem menuItemAbout = new JMenuItem("About", KeyEvent.VK_A);
        menuItemAbout.setIcon(iconAbout);
        menuItemAbout.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK));
        menuItemAbout.getAccessibleContext().setAccessibleDescription(
                "Omok Game Info");
        menuItemAbout.addActionListener(e -> {
            aboutButtonClicked(frame);
        });
        menu.add(menuItemAbout);


        frame.setJMenuBar(menuBar);

        JToolBar toolBar = new JToolBar("Omok");
        JButton playTool = new JButton();
        playTool.setIcon(iconPlay);
        playTool.addActionListener(e -> {
            playButtonClicked(frame, (String)comboBox.getSelectedItem());
        });
        playTool.setToolTipText("Play a new game");
        playTool.setFocusPainted(false);
        toolBar.add(playTool);

        JButton aboutTool = new JButton();
        aboutTool.setIcon(iconAbout);
        aboutTool.addActionListener(e -> {
            aboutButtonClicked(frame);
        });
        aboutTool.setToolTipText("Omok Game Info");
        aboutTool.setFocusPainted(false);
        toolBar.add(aboutTool);

        //playTool.
        frame.add(toolBar, BorderLayout.NORTH);

        center = new JPanel();
        center.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        panel.setLayout(new BorderLayout());
        JButton p = new JButton("Play");
        p.addActionListener(e -> {
            playButtonClicked(frame,(String)comboBox.getSelectedItem());
        });

        panel2.add(p);
        connect = new JButton("Connect");
        connect.addActionListener(e -> {
            connectButtonClicked(frame,urlGetInfo);
        });
        panel2.add(connect);
        opponentText = new JLabel("       Opponent:");
        panel2.add(opponentText);
        String[] placeholder = {"             "};
        comboBox = new JComboBox(placeholder);

        panel2.add(comboBox);
        player = new JLabel("Welcome to Omok");
        panel3.add(player);
        panel.add(panel2, BorderLayout.NORTH);
        panel.add(panel3);
        center.add(panel, BorderLayout.NORTH);

        d.setSize(d.getPreferredSize());

        d.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mouseListener){
                    repeat = true;
                    point = d.getMousePosition();
                    while (repeat){
                        h = new HumanLogic(point);
                        point = h.getPoint();
                        x = h.getUpdatedX();
                        y = h.getUpdatedY();
                        System.out.println(x+y);
                        if(board.isOccupied(x,y)){
                            System.out.println("occupied");
                            player.setText("Your Turn (Please Select an Empty Space!!!)");
                            warn(frame,"Please Select an Empty Space!!!");
                            point = d.getMousePosition();
                        }
                        else {
                            repeat = false;
                        }
                    }
                    player1Stones.add(point);
                    board.placeStone(x,y,player1);
                    player.setText("Computer Thinking");
                    center.add(d, BorderLayout.CENTER);
                    frame.add(center);
                    frame.setVisible(true);
                    String url = String.format(move,pid,x,y);

                    Thread workerThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // Call your sendGet method from within run
                            response = sendGet(url);
                        }
                    });
                    workerThread.start();
                    try {
                        workerThread.join();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    System.out.println(response);
                    JSONObject obj = new JSONObject(response);
                    Boolean accepted = obj.getBoolean("response");
                    if(accepted){
                        JSONObject ack_move = obj.getJSONObject("ack_move");
                        Boolean isWinAck = ack_move.getBoolean("isWin");
                        Boolean isWin = false;
                        Boolean isWinMove = false;
                        if(!isWinAck){
                            computerMove = obj.getJSONObject("move");
                            isWinMove = computerMove.getBoolean("isWin");

                            if(isWinMove){
                                isWin = true;
                            }
                            System.out.println("Win move " + isWinMove);
                            int x = computerMove.getInt("x");
                            int y = computerMove.getInt("y");
                            board.placeStone(x,y,player2);
                            x = getScaled(x);
                            y = getScaled(y);
                            pointCom = new Point(x,y);
                            player2Stones.add(pointCom);
                        }
                        System.out.println("Hello");
                        System.out.println(isWinAck);
                        player.setText("Your Turn");
                        if(isWinAck){
                            isWin = true;
                        }
                        if(isWin){
                            mouseListener = false;
                            String winnerString;
                            JSONArray winningRowArray;
                            int playerWin;
                            if(isWinAck){
                                playerWin = 1;
                                winnerString = "You Won, Congratulations!!!";
                                winningRowArray = ack_move.getJSONArray("row");
                                winningRow = readJsonRowArray(winningRowArray);

                            }
                            else {
                                playerWin = 2;
                                winnerString = "Computer Won, Better Luck Next Time.";
                                winningRowArray = computerMove.getJSONArray("row");
                                winningRow = readJsonRowArray(winningRowArray);
                            }
                            player.setText(winnerString);
                            winning = new BoardPanel(board,player1Stones,player2Stones,winningRow,playerWin);
                            center.add(winning, BorderLayout.CENTER);
                            winningPanel = true;
                            warn(frame,winnerString);
                            d.repaint();
                        }

                    }
                    else {

                    }
                }

            }
        });
        center.add(d, BorderLayout.CENTER);
        frame.add(center);
        frame.setVisible(true);
    }

    private void playButtonClicked(JFrame frame, String modeText){
        if(!isConnected){
            warn(frame,"Please Connect to the Server");
        }
        else {
            int result = JOptionPane.showConfirmDialog(frame,"Do you want to start a new "
                            + modeText + " game?",
                    "Omok",JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION){
                resetGame();
                String url = String.format(newGame, modeText);
                String game = sendGet(url);
                System.out.println(game);
                JSONObject obj = new JSONObject(game);
                Boolean startGame = obj.getBoolean("response");
                if(startGame){
                    pid = obj.getString("pid");
                    player.setText("Your Turn");
                }
                else{
                    String reason = obj.getString("reason");
                    System.out.println(reason);
                    warn(frame,"Couldn't Connect to Server, Try Disconnecting and Connecting Again.");
                }
            }
        }
    }
    private void aboutButtonClicked(JFrame frame){
        JPanel about = new JPanel(new GridLayout(0,1,5,5));
        about.add(new JLabel("Authors:"));
        about.add(new JLabel("Luis Daniel Estrada Aguirre"));
        about.add(new JLabel(""));
        about.add(new JLabel("Version: 1.0"));
        JOptionPane.showMessageDialog(frame,about,"About",JOptionPane.INFORMATION_MESSAGE);
    }

    private void connectButtonClicked(Frame frame, String urlGetInfo){
        if(!isConnected){
            try {
                String info = sendGet(urlGetInfo);
                JSONObject obj = new JSONObject(info);
                size = obj.getInt("size");
                JSONArray strategies = (JSONArray) obj.get("strategies");
                String[] opponents = jsonArraytoStringArray(strategies);
                System.out.println(size);
                System.out.println(opponents[0]);
                panel2.remove(comboBox);
                comboBox = new JComboBox(opponents);
                panel2.add(comboBox);
                connect.setText("Disconnect");
                panel2.revalidate();
                panel2.repaint();
                player.setText("Connected to Server");
                isConnected = true;
            } catch (Exception ex) {
                warn(frame, "Couldn't Connect to Server, Try Again.");
                throw new RuntimeException(ex);
            }
        }
        else {
            int result = JOptionPane.showConfirmDialog(frame,"Do you want to disconnect from the server?\n(This will end the game)",
                    "Omok",JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION){
                panel2.remove(comboBox);
                String[] placeholder = {"             "};
                comboBox = new JComboBox(placeholder);
                panel2.add(comboBox);
                connect.setText("Connect");
                panel2.revalidate();
                panel2.repaint();
                isConnected = false;
                resetGame();
                player.setText("Disconnected From Server");
                mouseListener = false;

            }

        }

    }

    private void resetGame(){
        board.clear();
        player1Stones.clear();
        player2Stones.clear();
        winningRow.clear();
        if (winningPanel){
            center.remove(winning);
        }
        winningPanel = false;
        d.repaint();
        mouseListener = true;
        player.setText("Your Turn");

    }
    private void warn(Frame frame,String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Omok",
                JOptionPane.PLAIN_MESSAGE);
    }

    public String sendGet(String urlString) {
        HttpURLConnection con = null;
        try {
            URL url = new URL(urlString);
            con = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return null;
    }

    public String[] jsonArraytoStringArray(JSONArray array) {
        if(array == null)
            return new String[0];

        String[] arr = new String[array.length()];
        for(int i=0; i<arr.length; i++) {
            arr[i]=array.optString(i);
        }
        return arr;
    }
    public int getScaled(int x){
        return (x*30)+10;
    }
    private ArrayList<Point> readJsonRowArray(JSONArray array){
        ArrayList<Point> arrayList = new ArrayList<Point>();
        Point p;
        for (int i = 0; i < array.length(); i = i+2) {
            int x = array.getInt(i);
            int y = array.getInt(i+1);
            p = new Point(x,y);
            arrayList.add(p);
        }
        return arrayList;
    }

    public static void main(String[] args){
        Omok omok = new Omok();
    }
}