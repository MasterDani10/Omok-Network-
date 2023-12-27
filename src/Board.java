import java.awt.*;
import java.util.ArrayList;

public class Board {
    protected int[][] board;
    Player player1;
    Player player2;
    int size;

    public Board() {
        size = 15;
        this.board = new int[size][size];
    }

    public Board(int size) {
        this.size = size;
        this.board = new int[size][size];
    }

    public int size() {
        return size;
    }

    public boolean isFull() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void selectPlayerOne(Player player){
        player1 = new Player(player.name());
    }

    public void selectPlayerTwo(Player player){
        player2 = new Player(player.name());
    }

    public void placeStone(int x, int y, Player player) {
        if(player1.name().equals(player.name())){
            board[y][x] = 1;
        }
        else{
            board[y][x] = 2;
        }
    }

    public boolean isEmpty(int x, int y) {
        if(board[y][x] == 0){
            return true;
        }
        return false;
    }

    public boolean isOccupied(int x, int y) {
        if(board[y][x] == 0){
            return false;
        }
        return true;
    }

    public boolean isOccupiedBy(int x, int y, Player player) {
        if(player1.name().equals(player.name())){
            if(board[y][x] == 1){
                return true;
            }
        }
        if(player2.name().equals(player.name())){
            if(board[y][x] == 2){
                return true;
            }
        }
        return false;
    }

    public Player playerAt(int x, int y) {
        if(board[y][x] == 1){
            return player1;
        }
        else if(board[y][x] == 2){
            return player2;
        }
        else{
            return null;
        }
    }

    public boolean isWonBy(Player player) {
        int p;
        if(player.name().equals("Player1")) {
            p = 1;
        }
        else{
            p = 2;
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == p &&
                        (       checkDirection(i, j, 1, 0) ||
                                checkDirection(i, j, 0, 1) ||
                                checkDirection(i, j, 1, 1) ||
                                checkDirection(i, j, 1, -1))) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean checkDirection(int x, int y, int dx, int dy) {
        int n = board.length;
        int player = board[x][y];

        for (int i = 0; i < 5; i++){
            int newX = x + dx * i;
            int newY = y + dy * i;
            if (newX < 0 || newX >= n || newY < 0 || newY >= n || board[newX][newY] != player) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Point> winningRow(Player player) {
        int playerNum;
        if (player1.name().equals(player.name())) {//checking if player 1 or 2
            playerNum = 1;
        } else {
            playerNum = 2;
        }
        int[][] directions = {{1, 0}, {0, 1}, {1, 1}, {1, -1}};//initializing directions for winning row

        for (int i = 0; i < board.length; i++) {// iterating through rows and columns
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] != playerNum){//if position doesnt contain stone continues to check rest of positions
                    continue;
                }
                for(int dir = 0; dir < directions.length; dir++){
                    ArrayList<Point> row = new ArrayList<>();//stores position of stones
                    for(int count = 0; count < 5; count++){//checking for five in a row
                        int x = i + directions[dir][0] * count;
                        int y = j + directions[dir][1] * count;
                        if(x < 0 || x >= board.length || y >= board.length || board[x][y] != playerNum){//checking coordinates of the stones and if its the players stone if not clear row
                            row.clear();
                            break;
                        }
                        row.add(new Point(y, x));//if coordinates of stone are the players then coordinates are stored
                    }
                    if(row.size() == 5){// if 5 coordinates are stored in the row returns the winning row
                        return row;
                    }
                }
            }
        }
        return new ArrayList<>();// if not returns an empty list
    }

    public void clear(){
        for(int i = 0; i < size(); i++){
            for(int j = 0; j < size(); j++){
                board[i][j] = 0;
            }
        }
    }

    public int getRoundedX(int x){
        if(x < 25){
            x = 10;
        }
        else if (x < 55) {
            x = 40;
        }
        else if (x < 85) {
            x = 70;
        }
        else if (x < 115) {
            x = 100;
        }
        else if (x < 145) {
            x = 130;
        }
        else if (x < 175) {
            x = 160;
        }
        else if (x < 205) {
            x = 190;
        }
        else if (x < 235) {
            x = 220;
        }
        else if (x < 265) {
            x = 250;
        }
        else if (x < 295) {
            x = 280;
        }
        else if (x < 325) {
            x = 310;
        }
        else if (x < 355) {
            x = 340;
        }
        else if (x < 385) {
            x = 370;
        }
        else if (x < 415) {
            x = 400;
        }
        else if (x < 445) {
            x = 430;
        }
        else if (x < 475) {
            x = 460;
        }
        return x;
    }

    public int getRoundedY(int y){
        if(y < 25){
            y = 10;
        }
        else if (y < 55) {
            y = 40;
        }
        else if (y < 85) {
            y = 70;
        }
        else if (y < 115) {
            y = 100;
        }
        else if (y < 145) {
            y = 130;
        }
        else if (y < 175) {
            y = 160;
        }
        else if (y < 205) {
            y = 190;
        }
        else if (y < 235) {
            y = 220;
        }
        else if (y < 265) {
            y = 250;
        }
        else if (y < 295) {
            y = 280;
        }
        else if (y < 325) {
            y = 310;
        }
        else if (y < 355) {
            y = 340;
        }
        else if (y < 385) {
            y = 370;
        }
        else if (y < 415) {
            y = 400;
        }
        else if (y < 445) {
            y = 430;
        }
        else if (y < 475) {
            y = 460;
        }
        return  y;
    }
}
