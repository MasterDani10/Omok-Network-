import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Omok {
    Board board = new Board();
    HumanLogic h;
    Player player1 = new Player("Player1");
    Player player2 = new Player("Player2");
    ArrayList<Point> player1Stones = new ArrayList<>();
    ArrayList<Point> player2Stones = new ArrayList<>();
    ArrayList<Point> winningRow = new ArrayList<>();
    //Boolean win = false;
    BoardPanel d = new BoardPanel(new Board(), player1Stones,player2Stones);
    Boolean p1 = true;
    Boolean pCom = false;
    Boolean restart = false;
    Boolean repeat = true;
    Boolean repeatCom = true;
    JComboBox comboBox;
    JLabel player;

    int x = 0;
    int y = 0;
    int xCom = 0;
    int yCom = 0;
    int xComScaled = 0;
    int yComScaled = 0;
    int n = 0;
    Point point = new Point(0,0);
    Point pointCom = new Point(0,0);
    //Boolean human;
    int mode = 1;
    String playerX;
    Boolean mouseListener = true;
    Boolean p1Win = false;
    //NetworkGUI networkUI;
    Boolean ownServerConnected = false;
    Socket s;
    JTextArea serverTextArea;
    BufferedReader in;
    int port;
    JPanel panel2;
    JLabel opponentText;
    JLabel connectedText;

    String hostOpponentName;
    int hostOpponentPort;
    Boolean isConnected = false;


    public Omok(){
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

        JPanel center = new JPanel();
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
        JButton pair = new JButton("Pair");
        pair.addActionListener(e -> {
            port = 8001;
            if(!ownServerConnected){
                new Thread(() ->{
                    OmokServer server = new OmokServer(port);
                    server.start();
                }).start();
                serverTextArea.append("Server created on port " + port +"!\n");
                ownServerConnected = true;
            }
            //networkUI = new NetworkGUI(frame,port);
            pairButtonClicked(frame);


        });
        panel2.add(pair);
        opponentText = new JLabel("            Opponent:");
        panel2.add(opponentText);

        if(mode == 1){
            String[] opponents = {"Human", "ComputerRandom", "ComputerEasy"};
            comboBox = new JComboBox(opponents);
        }
        else if(mode == 2) {
            String[] opponents = {"ComputerRandom", "Human", "ComputerEasy"};
            comboBox = new JComboBox(opponents);
        }
        else if(mode == 3) {
            String[] opponents = {"ComputerEasy", "Human", "ComputerRandom"};
            comboBox = new JComboBox(opponents);
        }

        panel2.add(comboBox);
        player = new JLabel("Player 1 Turn");
        panel3.add(player);
        panel.add(panel2, BorderLayout.NORTH);
        panel.add(panel3);
        center.add(panel, BorderLayout.NORTH);

        d.setSize(d.getPreferredSize());


        d.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(mode == 1){
                    if(mouseListener){
                        repeat = true;
                        point = d.getMousePosition();
                        while(repeat){

                            h = new HumanLogic(point);
                            point = h.getPoint();
                            x = h.getUpdatedX();
                            y = h.getUpdatedY();

                            if (board.isOccupied(x,y)){
                                if(p1){
                                    playerX = "Player 1";
                                }
                                else {
                                    playerX = "Player 2";
                                }
                                player.setText(playerX + " Turn (Please Select an Empty Space!!!)");
                                JOptionPane.showMessageDialog(frame,
                                        playerX + ", Please Select an Empty Space!!!!",
                                        "Omok", JOptionPane.PLAIN_MESSAGE);
                                point = d.getMousePosition();
                            }
                            else {
                                repeat = false;
                            }
                        }

                        Player playerX;
                        String j1, j2;
                        if (p1) {
                            player1Stones.add(point);
                            playerX = player1;
                            j1 = " 1 ";
                            j2 = " 2 ";
                        }
                        else {
                            player2Stones.add(point);
                            playerX = player2;
                            j1 = " 2 ";
                            j2 = " 1 ";
                        }

                        board.selectPlayerOne(player1);
                        board.placeStone((int)x,(int)y,playerX);
                        if(board.isWonBy(playerX)){
                            if(playerX.equals(player1)){ p1Win = true; }
                            mouseListener = false;
                            winningRow = board.winningRow(playerX);
                            BoardPanel winning = new BoardPanel(board,player1Stones,player2Stones,winningRow);
                            center.add(winning, BorderLayout.CENTER);
                            JOptionPane.showMessageDialog(frame, "Player" + j1 + "Won!!!",
                                    "Omok", JOptionPane.PLAIN_MESSAGE);
                            player.setText("Player" + j1 + "Won!!!");
                        }
                        else {
                            player.setText("Player" + j2 + "Turn");
                        }

                        if (board.isFull()){
                            d = new BoardPanel(board,player1Stones,player2Stones);
                            JOptionPane.showMessageDialog(frame, "The Game is a Draw",
                                    "Omok", JOptionPane.PLAIN_MESSAGE);
                        }

                        p1 = !p1;
                    }

                }
                else {
                    if(mouseListener){
                        Player playerCom;
                        String msg1 = "", msg2 = "";
                        repeat = true;
                        System.out.println(d.getMousePosition());
                        point = d.getMousePosition();
                        while(repeat){

                            h = new HumanLogic(point);
                            point = h.getPoint();
                            x = h.getUpdatedX();
                            y = h.getUpdatedY();

                            if(board.isOccupied(x,y)){
                                player.setText("Player 1 Turn (Please Select an Empty Space!!!)");
                                JOptionPane.showMessageDialog(frame,
                                        "Player 1, Please Select an Empty Space!!!!",
                                        "Omok", JOptionPane.PLAIN_MESSAGE);
                                point = d.getMousePosition();
                            }
                            else {
                                repeat = false;
                            }
                        }

                        player1Stones.add(point);


                        if(mode == 2){
                            ComputerLogicRandom random = new ComputerLogicRandom();
                            repeatCom = true;
                            while(repeatCom){
                                xCom = random.getRandom();
                                yCom = random.getRandom();
                                if(!board.isOccupied(xCom,yCom)){
                                    repeatCom = false;
                                }
                            }
                            pointCom = new Point(random.getScaled(xCom),random.getScaled(yCom));
                        }
                        else if(mode == 3){
                            ComputerLogicEasy easy = new ComputerLogicEasy(x,y);
                            repeatCom = true;

                            while (repeatCom){
                                n++;
                                xCom = easy.getX(n);
                                yCom = easy.getY(n);
                                if(!board.isOccupied(xCom,yCom)){
                                    repeatCom = false;
                                }
                            }
                            pointCom = new Point(easy.getScaled(xCom), easy.getScaled(yCom));
                        }

                        player2Stones.add(pointCom);

                        Boolean repeatOneTime = true;
                        while(repeatOneTime){
                            if(!pCom){
                                playerCom = player1;
                                msg1 = "Player 1 Won!!!";
                                msg2 = "Computer Turn";
                            }
                            else {
                                if(n > 3){
                                    n = 0;
                                }
                                repeatOneTime = false;
                                playerCom = player2;
                                msg1 = "Computer Won, Better Luck Next Time!";
                                msg2 = "Player 1 Turn";
                                x = xCom;
                                y = yCom;

                            }

                            board.selectPlayerOne(player1);
                            board.placeStone((int)x,(int)y,playerCom);
                            if(board.isWonBy(playerCom)){
                                mouseListener = false;
                                winningRow = board.winningRow(playerCom);
                                player2Stones.remove(player2Stones.size()-1);
                                BoardPanel winning = new BoardPanel(board,player1Stones,player2Stones,winningRow);
                                center.add(winning, BorderLayout.CENTER);
                                player.setText(msg1);
                                JOptionPane.showMessageDialog(frame, msg1,
                                        "Omok", JOptionPane.PLAIN_MESSAGE);
                                d.repaint();
                                break;
                            }
                            else {
                                player.setText(msg2);
                            }
                            if (board.isFull()){
                                d = new BoardPanel(board,player1Stones,player2Stones);
                                JOptionPane.showMessageDialog(frame, "The Game is a Draw",
                                        "Omok", JOptionPane.PLAIN_MESSAGE);
                            }
                            pCom = !pCom;
                        }
                    }
                }
            }
        });

        center.add(d, BorderLayout.CENTER);
        frame.add(center);
        frame.setVisible(true);
    }

    private void playButtonClicked(JFrame frame, String modeText){
        int result = JOptionPane.showConfirmDialog(frame,"Do you want to start a new "
                        + modeText + " game?",
                "Omok",JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION){

            if(modeText.equals("Human")){
                mode = 1;
                resetGame();
            }
            else if(modeText.equals("ComputerRandom")){
                mode = 2;
                resetGame();
            }
            else if(modeText.equals("ComputerEasy")){
                mode = 3;
                resetGame();
            }

        }
    }
    private void aboutButtonClicked(JFrame frame){
        JPanel about = new JPanel(new GridLayout(0,1,5,5));
        about.add(new JLabel("Authors:"));
        about.add(new JLabel("Luis Daniel Estrada Aguirre"));
        about.add(new JLabel("Benjamin Laffita"));
        about.add(new JLabel(""));
        about.add(new JLabel("Version: 1.0"));
        JOptionPane.showMessageDialog(frame,about,"About",JOptionPane.INFORMATION_MESSAGE);
    }

    private void resetGame(){
        board.clear();
        player1Stones.clear();
        player2Stones.clear();
        winningRow.clear();
        d.repaint();
        mouseListener = true;
        if(p1Win){
            player.setText("Player 2 Turn");
        }
        else{
            player.setText("Player 1 Turn");
        }
        p1Win = false;
    }

    private void pairButtonClicked(JFrame frame){

        Color background = new Color(0,0,0,0);

        JFrame pairFrame = new JFrame("Omok");
        pairFrame.setSize(400,500);
        JPanel pairPanel1 = new JPanel(new BorderLayout());
        JPanel pairPanel2 = new JPanel(new BorderLayout());


        // Set up the title for different panels
        pairPanel1.setBorder(BorderFactory.createTitledBorder("Player"));
        pairPanel2.setBorder(BorderFactory.createTitledBorder("Opponent"));

        JPanel pairPlayerPanel1 = new JPanel();
        JPanel pairPlayerPanel2 = new JPanel();
        JPanel pairPlayerPanel3 = new JPanel();
        pairPlayerPanel1.add(new JLabel("Host name:    "));

        int columnSize = 20;
        JTextField namePlayer = null;
        JTextField ipPlayer = null;
        JTextField portPlayer = new JTextField(""+port, columnSize);
        Color defaultBackground = portPlayer.getBackground();

        try {
            namePlayer = new JTextField(InetAddress.getLocalHost().getHostName(),columnSize);
            ipPlayer = new JTextField(InetAddress.getLocalHost().getHostAddress().trim(),columnSize);
        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        }

        pairPlayerPanel1.add(namePlayer);
        pairPlayerPanel2.add(new JLabel("IP number:    "));
        pairPlayerPanel2.add(ipPlayer);
        pairPlayerPanel3.add(new JLabel("Port number: "));
        pairPlayerPanel3.add(portPlayer);

        namePlayer.setEditable(false);
        ipPlayer.setEditable(false);
        portPlayer.setEditable(false);
        namePlayer.setHorizontalAlignment(SwingConstants.CENTER);
        ipPlayer.setHorizontalAlignment(SwingConstants.CENTER);
        portPlayer.setHorizontalAlignment(SwingConstants.CENTER);
        namePlayer.setBackground(background);
        ipPlayer.setBackground(background);
        portPlayer.setBackground(background);

        LineBorder border = new LineBorder(Color.BLACK);
        namePlayer.setBorder(border);
        ipPlayer.setBorder(border);
        portPlayer.setBorder(border);

        pairPanel1.add(pairPlayerPanel1, BorderLayout.NORTH);
        pairPanel1.add(pairPlayerPanel2, BorderLayout.CENTER);
        pairPanel1.add(pairPlayerPanel3, BorderLayout.SOUTH);


        JPanel opponentPairPanel1 = new JPanel();
        JPanel opponentPairPanel2 = new JPanel();
        JPanel opponentPairPanel3 = new JPanel();
        opponentPairPanel1.add(new JLabel("Host name / IP: "));
        JTextField hostOpponent = new JTextField("", columnSize);
        opponentPairPanel1.add(hostOpponent);
        opponentPairPanel2.add(new JLabel("Port number:  "));
        JTextField portOpponent = new JTextField("", columnSize);
        opponentPairPanel2.add(portOpponent);
        JButton connect = new JButton("Connect");
        JButton disconnect = new JButton("Disconnect");
        disconnect.setEnabled(false);
        connect.addActionListener(e -> {
            try {
                hostOpponentName = hostOpponent.getText();
                hostOpponentPort = Integer.parseInt(portOpponent.getText());
                s = new Socket(hostOpponentName, hostOpponentPort);
                new ClientHandler(s).start();
                connect.setEnabled(false);
                disconnect.setEnabled(true);
                hostOpponent.setEditable(false);
                portOpponent.setEditable(false);
                hostOpponent.setBackground(background);
                portOpponent.setBackground(background);
                panel2.remove(opponentText);
                panel2.remove(comboBox);
                connectedText = new JLabel("                Connected");
                panel2.add(connectedText);
                panel2.revalidate();
                panel2.repaint();
                isConnected = true;
            }
            catch (UnknownHostException e1){
                warn(pairFrame, "Couldn't Connect To Server, Try Again.");
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }

        });
        disconnect.addActionListener(e -> {
            try {
                s.close();
                serverTextArea.append("Disconnected from server.\n");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            disconnect.setEnabled(false);
            connect.setEnabled(true);
            panel2.remove(connectedText);
            panel2.add(opponentText);
            panel2.add(comboBox);
            panel2.revalidate();
            panel2.repaint();
            hostOpponent.setEditable(true);
            portOpponent.setEditable(true);
            hostOpponent.setBackground(defaultBackground);
            portOpponent.setBackground(defaultBackground);
            isConnected = false;
        });
        if(isConnected){
            hostOpponent.setText(hostOpponentName);
            portOpponent.setText(String.valueOf(hostOpponentPort));
            connect.setEnabled(false);
            disconnect.setEnabled(true);
            hostOpponent.setEditable(false);
            portOpponent.setEditable(false);
            hostOpponent.setBackground(background);
            portOpponent.setBackground(background);
        }
        opponentPairPanel3.add(connect);
        opponentPairPanel3.add(disconnect);
        pairPanel2.add(opponentPairPanel1, BorderLayout.NORTH);
        pairPanel2.add(opponentPairPanel2, BorderLayout.CENTER);
        pairPanel2.add(opponentPairPanel3, BorderLayout.SOUTH);

        JPanel serverPanel = new JPanel(new BorderLayout());
        serverTextArea.setEditable(false);
        JPanel serverPanelEast = new JPanel(new BorderLayout());
        JButton close = new JButton("Close");
        close.addActionListener(e -> {
            pairFrame.dispose();
        });
        serverPanelEast.add(close, BorderLayout.EAST);
        JPanel serverPanelCenter = new JPanel();
        serverPanelCenter.add(serverTextArea);
        serverPanel.add(serverPanelCenter,BorderLayout.CENTER);
        serverPanel.add(serverPanelEast,BorderLayout.SOUTH);



        JPanel playerOpponentPanel = new JPanel(new BorderLayout());
        playerOpponentPanel.add(pairPanel1,BorderLayout.NORTH);
        playerOpponentPanel.add(pairPanel2,BorderLayout.SOUTH);
        pairFrame.setLayout(new BorderLayout());
        pairFrame.add(playerOpponentPanel, BorderLayout.NORTH);
        pairFrame.add(serverPanel, BorderLayout.CENTER);
        pairFrame.setVisible(true);
    }



    private class ClientHandler extends Thread {
        private Socket socket;
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                String str = null;
                while((str = in.readLine()) != null){
                    serverTextArea.append(str+ "\n");
                }
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void warn(Frame frame,String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Omok",
                JOptionPane.PLAIN_MESSAGE);
    }
}
