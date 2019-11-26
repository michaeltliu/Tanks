public class Game {

    private static int turn;
    public static int getTurn() {
        return turn;
    }
    public static void nextTurn() {
        turn ++;
    }
    public static void main(String[] args) {
        Window window = new Window();
    }

}