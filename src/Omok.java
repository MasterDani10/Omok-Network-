import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Omok {
    Board board = new Board();
    HumanLogic h;
    Player player1 = new Player("Player1");
    Player player2 = new Player("Player2");
    ArrayList<Point> player1Stones = new ArrayList<>();
    ArrayList<Point> player2Stones = new ArrayList<>();
    //Boolean win = false;
    BoardPanel d = new BoardPanel(new Board(), player1Stones,player2Stones);
    Boolean p1 = true;
    Boolean pCom = false;
    Boolean restart = false;
    Boolean repeat = true;
    Boolean repeatCom = true;
    JComboBox comboBox;

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
    int mode = 0;
    String playerX;


    public Omok(int mode){
        this.mode = mode;
        Image imagePlay = Toolkit.getDefaultToolkit().getImage("Resources/play.png").
                getScaledInstance(20, 20, 20);
        Image imageAbout = Toolkit.getDefaultToolkit().getImage("Resources/about.png").
                getScaledInstance(20, 20, 20);
        ImageIcon iconPlay = new ImageIcon(imagePlay);
        ImageIcon iconAbout = new ImageIcon(imageAbout);

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
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        panel.setLayout(new BorderLayout());
        JButton p = new JButton("Play");
        p.addActionListener(e -> {
            playButtonClicked(frame,(String)comboBox.getSelectedItem());
        });

        panel2.add(p);
        panel2.add(new JLabel("            Opponent:"));

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
        JLabel player = new JLabel("Player 1 Turn");
        panel3.add(player);
        panel.add(panel2, BorderLayout.NORTH);
        panel.add(panel3);
        center.add(panel, BorderLayout.NORTH);

        d.setSize(d.getPreferredSize());
        d.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(mode == 1){
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
                        BoardPanel winning = new BoardPanel(board,player1Stones,player2Stones,board.winningRow(playerX));
                        center.add(winning, BorderLayout.CENTER);
                        d = new BoardPanel(board,player1Stones,player2Stones,board.winningRow(playerX));
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
                else {
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
                            BoardPanel winning = new BoardPanel(board,player1Stones,player2Stones,board.winningRow(playerCom));
                            center.add(winning, BorderLayout.CENTER);
                            d = new BoardPanel(board,player1Stones,player2Stones,board.winningRow(playerCom));
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
        });

        center.add(d, BorderLayout.CENTER);
        frame.add(center);
        frame.setVisible(true);
    }

    private void clean(){
        board = null;
        player1 = null;
        player2 = null;
        player1Stones = null;
        player2Stones = null;
        d = null;
        p1 = null;
        point = null;
    }

    private void playButtonClicked(JFrame frame, String mode){
        int result = JOptionPane.showConfirmDialog(frame,"Do you want to start a new "
                        + mode + " game?",
                "Omok",JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION){
            frame.setVisible(false);
            frame.dispose();
            clean();
            if(mode.equals("Human")){
                new Omok(1);
            }
            else if(mode.equals("ComputerRandom")){
                new Omok(2);
            }
            else if(mode.equals("ComputerEasy")){
                new Omok(3);
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

}
