// Command line Boggle game.
// Players try to find all words formed by horizontally, vertically, or diagonally adjacent letters.

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Boggle {
    static HashSet<String> dictionary = new HashSet<>();
    static HashSet<String> correctWords = new HashSet<>();

    public static void main(String args[]) {
        try {
            File myObj = new File("./src/dictionary.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                dictionary.add(myReader.nextLine().toUpperCase(Locale.ROOT));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[][] board = new char[4][4];
        Random r = new Random();
        Scanner scan = new Scanner(System.in);
        while (true) {
            for (int i = 0; i < 4; i++) {
                System.out.println();
                for (int j = 0; j < 4; j++) {
                    board[i][j] = alphabet.charAt(r.nextInt(alphabet.length()));
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
            System.out.println("Enter all words: ");
            String input = scan.nextLine().toUpperCase();
            String[] answer = input.split(" ");
            solve(board);
            int totalCorrect = 0;
            for (String ans : answer) {
                if (correctWords.contains(ans)) {
                    totalCorrect++;
                }
            }
            System.out.println();
            System.out.printf("You found %d out of %d !", totalCorrect, correctWords.size());
            System.out.println();
            for(String ans : correctWords) {
                System.out.println(ans);
            }
        }
    }

    // Searches for all words on the board, starting from each position on the board
    static public void solve(char[][] board){
        correctWords.clear();
        for(int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                // availabilityGrid indicates whether an position on the board is allowed to be added to the string being built
                Boolean[][] availabilityGrid = new Boolean[4][4];
                Arrays.stream(availabilityGrid).forEach(row -> Arrays.fill(row, true));
                explore(board, x, y, "", availabilityGrid);
            }
        }
    }

    // Searches all strings starting from a position on the board as long as string is a prefix of some english word
    static private void explore(char[][] board, int i, int j, String word, Boolean[][] available) {
        if (dictionary.contains(word)) {
            correctWords.add(word);
        } if (word.length() < 16 && prefix(word)) {

            //remove current block from available
            available[i][j] = false;

            //North
            if (i > 0 && available[i - 1][j]) {
                String w = word + board[i - 1][j];
                explore(board, i - 1, j, w, available);
            }
            //Northeast
            if (i > 0 && j < 3 && available[i - 1][j + 1]) {
                String w = word + board[i - 1][j + 1];
                explore(board, i - 1, j + 1, w, available);
            }
            //South
            if (j < 3 && available[i][j + 1]) {
                String w = word + board[i][j + 1];
                explore(board, i, j + 1, w, available);
            }
            //Southeast
            if (i < 3 && j < 3 && available[i + 1][j + 1]) {
                String w = word + board[i + 1][j + 1];
                explore(board, i + 1, j + 1, w, available);
            }
            //South
            if (i < 3 && available[i + 1][j]) {
                String w = word + board[i + 1][j];
                explore(board, i + 1, j, w, available);
            }
            //Southwest
            if (i < 3 && j > 0 && available[i + 1][j - 1]) {
                String w = word + board[i + 1][j - 1];
                explore(board, i + 1, j - 1, w, available);
            }
            //West
            if (j > 0 && available[i][j - 1]) {
                String w = word + board[i][j - 1];
                explore(board, i, j - 1, w, available);
            }
            //Northwest
            if (i > 0 && j > 0 && available[i - 1][j - 1]) {
                String w = word + board[i - 1][j - 1];
                explore(board, i - 1, j - 1, w, available);
            }

            //replace current block to available
            available[i][j] = true;
        }

    }

    // Returns true if passed string is a prefix of some word in the dictionary
    static boolean prefix(String pre) {
        for (String word : dictionary) {
            if (word.startsWith(pre)) {
                return true;
            }
        }
        return false;
    }
}
