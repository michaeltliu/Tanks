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
        System.out.println("How many players? (between 2 and 10 inc.) ");
        String s = scanner.next();
        int players = Integer.parseInt(s);
        while (players > 10 || players < 2) {
            System.out.println("Must have between 2 and 10 players inclusive.");
            s = scanner.next();
            players = Integer.parseInt(s);
        }
        System.out.println("Running...");
        Window window = new Window(players);
    }

}