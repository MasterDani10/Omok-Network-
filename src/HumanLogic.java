import java.awt.*;

public class HumanLogic {
    Board board = new Board();
    int x, y;
    Point p;
    public HumanLogic(Point p){
        this.p = p;
        this.x = (int)p.getX();
        this.y = (int)p.getY();
    }

    public Point getPoint(){
        x = board.getRoundedX(x);
        y = board.getRoundedY(y);

        return new Point(x,y);
    }

    public int getUpdatedX(){
        x = (x-10) / 30;
        return x;
    }
    public int getUpdatedY(){
        y = (y-10) / 30;
        return y;
    }
}
