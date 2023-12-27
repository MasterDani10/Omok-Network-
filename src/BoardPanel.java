import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoardPanel extends JPanel {
    private Board board;
    int size;
    int width = 450+10;
    int height = 450+10;
    int playerWin;
    ArrayList<Point> player1Stones;
    ArrayList<Point> player2Stones;
    ArrayList<Point> winningRow;
    Player player1 = new Player("Player1");
    Player player2 = new Player("Player2");

    public BoardPanel(Board board, ArrayList<Point> p1, ArrayList<Point> p2){
        this.board = board;
        this.player1Stones = p1;
        this.player2Stones = p2;

    }
    public BoardPanel(Board board, ArrayList<Point> p1, ArrayList<Point> p2, ArrayList<Point> winner, int playerWin){
        this.board = board;
        this.player1Stones = p1;
        this.player2Stones = p2;
        this.winningRow = winner;
        this.playerWin = playerWin;
    }
    @Override
    protected void paintComponent(Graphics g){
        size = board.size();
        g.setColor(Color.ORANGE);
        g.fillRect(10, 10, width-10, height-10);
        g.setColor(Color.BLACK);

        for(int i = 10; i <= width; i+= width/size){
            g.drawLine(i,10,i,width);
        }

        for(int i = 10; i <= height; i+= height/size){
            g.drawLine(10,i,height,i);
        }

        if(!player1Stones.isEmpty()){
            for (int i = 0; i < player1Stones.size(); i++){
                g.setColor(Color.WHITE);
                g.fillOval((int)player1Stones.get(i).getX()-10, (int)player1Stones.get(i).getY()-10, 20,20);
            }
        }
        if(!player2Stones.isEmpty()){
            for (int i = 0; i < player2Stones.size(); i++){
                g.setColor(Color.BLACK);
                g.fillOval((int)player2Stones.get(i).getX()-10, (int)player2Stones.get(i).getY()-10, 20,20);
            }
        }

        if(board.isWonBy(player1) || board.isWonBy(player2)){
            if(board.isWonBy(player1)){
                g.setColor(Color.RED);
            }
            else {
                g.setColor(Color.GREEN);
            }
            for (int i = 0; i < winningRow.size(); i++){
                for (int j = 1; j < 3; j++) {
                    g.drawOval((int) winningRow.get(i).getX() * 30, (int) winningRow.get(i).getY() * 30, 20-j, 20-j);
                }
            }
        }
        repaint();
    }
    @Override
    public Dimension getPreferredSize(){
        return new Dimension(width,size);
    }
}
