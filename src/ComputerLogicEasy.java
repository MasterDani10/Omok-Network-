import java.awt.*;

public class ComputerLogicEasy {
    Point p;
    int x, y;
    public ComputerLogicEasy(int x, int y){
        this.x = x;
        this.y = y;

    }

    public int getX(int i){
        if(i == 1) {
            if (x == 14) {
                return x-2;
            } else {
                return x+1;
            }
        }
        else if(i == 2){
            if (x == 0) {
                return x+2;

            } else {
                return x-1;
            }
        }
        else if(i == 3){
            return x;
        }
        else if(i == 4){
            return x;
        }
        else {
            ComputerLogicRandom random = new ComputerLogicRandom();
            return random.getRandom();
        }
    }
    public int getY(int i){
        if(i == 3) {
            if (x == 14) {
                return y-2;
            } else {
                return y+1;
            }
        }
        else if(i == 4){
            if (x == 0) {
                return y+2;

            } else {
                return y-1;
            }
        }
        else if(i == 1){
            return y;
        }
        else if(i == 2){
            return y;
        }
        else {
            ComputerLogicRandom random = new ComputerLogicRandom();
            return random.getRandom();
        }
    }
    public int getScaled(int x){
        return (x*30)+10;
    }

}
