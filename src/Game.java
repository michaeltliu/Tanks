import java.util.Scanner;

public class Game {

    private static int turn;
    public static int getTurn() {
        return turn;
    }
    public static void nextTurn() {
        turn ++;
        turn %= Tank.getAllTanks().size();
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many players? ");
        String s = scanner.next();
        int players = Integer.parseInt(s);
        Window window = new Window(players);
    }

}