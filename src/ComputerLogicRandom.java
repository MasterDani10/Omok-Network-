import java.util.Random;

public class ComputerLogicRandom {
    Random random;
    public ComputerLogicRandom(){
        random = new Random();
    }
    public int getRandom(){
        return random.nextInt(14)+1;
    }

    public int getScaled(int x){
        return (x*30)+10;
    }
}
