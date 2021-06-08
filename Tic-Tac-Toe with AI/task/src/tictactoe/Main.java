package tictactoe;

import java.util.*;

public class Main {
    static char[][] cells;
    static int xcells = 0;
    static int ocells = 0;
    static char currentPlayer = 'X';
    static boolean winner = false;
    static char[][] xWinCondition = new char[][]{
            {'X', 'X', 'X'},
            {'X', 'X', 'X'},
            {'X', 'X', 'X'},
    };
    static char[][] oWinCondition = new char[][]{
            {'O', 'O', 'O'},
            {'O', 'O', 'O'},
            {'O', 'O', 'O'},
    };

    static boolean checkSpots(char s, char[][] cells) {
        if (s == 'X') {
            return checkSequences(cells, xWinCondition);
        } else {

            return checkSequences(cells, oWinCondition);
        }
    }

    public static boolean checkSequences(char[][] cells, char[][] winCondition) {
        boolean isWinner = Arrays.equals(cells[0], winCondition[0])
                || Arrays.equals(cells[1], winCondition[1])
                || Arrays.equals(cells[2], winCondition[2]);
        if (!isWinner) {
            char[] diagonal1 = new char[]{cells[0][0], cells[1][1], cells[2][2]};
            char[] diagonal2 = new char[]{cells[0][2], cells[1][1], cells[2][0]};
            isWinner = Arrays.equals(diagonal1, winCondition[0])
                    || Arrays.equals(diagonal2, winCondition[0]);

        }
        return isWinner;
    }

    static char[][] generateBoard() {
        char[][] cells = new char[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j] = ' ';
            }
        }

        return cells;
    }

    static void displayBoard() {
        System.out.println("---------");
        System.out.print("| " + Arrays.toString(cells[0])
                .replaceAll(",", "")
                .replace("[", "").replace("]", "") + " |\n");
        System.out.print("| " + Arrays.toString(cells[1])
                .replaceAll(",", "")
                .replace("[", "").replace("]", "") + " |\n");
        System.out.print("| " + Arrays.toString(cells[2])
                .replaceAll(",", "")
                .replace("[", "").replace("]", "") + " |\n");
        System.out.println("---------");
    }

    static int[] getPlayerMove(Scanner sc) {
        boolean retryInput;
        System.out.println("Enter the coordinates: ");
        do {
            if (sc.hasNextInt()) {
                retryInput = true;
            } else {
                System.out.println("You should enter numbers!");
                retryInput = false;
                sc.next();
            }
        } while (!retryInput);
        int a = sc.nextInt();

        do {
            if (sc.hasNextInt()) {
                retryInput = true;
            } else {
                System.out.println("You should enter numbers!");
                retryInput = false;
                sc.next();
            }
        } while (!retryInput);
        int b = sc.nextInt();

        return new int[]{a, b};
    }
    static int evaluate() {
        int score = 0;
        // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
        score += evaluateLine(0, 0, 0, 1, 0, 2);  // row 0
        score += evaluateLine(1, 0, 1, 1, 1, 2);  // row 1
        score += evaluateLine(2, 0, 2, 1, 2, 2);  // row 2
        score += evaluateLine(0, 0, 1, 0, 2, 0);  // col 0
        score += evaluateLine(0, 1, 1, 1, 2, 1);  // col 1
        score += evaluateLine(0, 2, 1, 2, 2, 2);  // col 2
        score += evaluateLine(0, 0, 1, 1, 2, 2);  // diagonal
        score += evaluateLine(0, 2, 1, 1, 2, 0);  // alternate diagonal
        return score;
    }
    static int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
        int score = 0;
        char oppSeed = (currentPlayer == 'X') ? 'O' : 'X';
        // First cell
        if (cells[row1][col1] == currentPlayer) {
            score = 1;
        } else if (cells[row1][col1] == oppSeed) {
            score = -1;
        }

        // Second cell
        if (cells[row2][col2] == currentPlayer) {
            if (score == 1) {   // cell1 is mySeed
                score = 10;
            } else if (score == -1) {  // cell1 is oppSeed
                return 0;
            } else {  // cell1 is empty
                score = 1;
            }
        } else if (cells[row2][col2] == oppSeed) {
            if (score == -1) { // cell1 is oppSeed
                score = -10;
            } else if (score == 1) { // cell1 is mySeed
                return 0;
            } else {  // cell1 is empty
                score = -1;
            }
        }

        // Third cell
        if (cells[row3][col3] == currentPlayer) {
            if (score > 0) {  // cell1 and/or cell2 is mySeed
                score *= 10;
            } else if (score < 0) {  // cell1 and/or cell2 is oppSeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = 1;
            }
        } else if (cells[row3][col3] == oppSeed) {
            if (score < 0) {  // cell1 and/or cell2 is oppSeed
                score *= 10;
            } else if (score > 1) {  // cell1 and/or cell2 is mySeed
                return 0;
            } else {  // cell1 and cell2 are empty
                score = -1;
            }
        }
        return score;
    }
    static List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<int[]>();
        if (checkSpots('X', cells) || checkSpots('O', cells)) {
            return nextMoves;
        }
        for(int i = 0; i < 3; ++i){
            for(int j = 0; j < 3; ++j){
                if (cells[i][j] != 'X' && cells[i][j] != 'O'){
                    nextMoves.add(new int[] {i, j});
                }
            }
        }
        return nextMoves;
    }

    static int[] minMax( int depth, char seed){
        char oppSeed = (currentPlayer == 'X') ? 'O' : 'X';
        List<int[]> nextMoves = generateMoves();
        int bestScore = (currentPlayer == seed) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;

        if( nextMoves.isEmpty() || depth == 0){
            bestScore = evaluate();
        } else {
            for (int[] move : nextMoves){
                cells[move[0]][move[1]] = seed;
                if(seed == currentPlayer){
                    currentScore = minMax(depth-1, oppSeed)[0];
                    if(currentScore > bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                } else {
                    currentScore = minMax(depth - 1, currentPlayer)[0];
                    if(currentScore < bestScore) {
                        bestScore = currentScore;
                        bestRow = move[0];
                        bestCol = move[1];
                    }
                }
                cells[move[0]][move[1]] = ' ';
            }
        }
        return new int[] { bestScore, bestRow, bestCol };
    }
    static int[] hardMachineMove(){
        int[] move = minMax(2, currentPlayer);
        System.out.print(Arrays.toString(move));
        return new int[] {move[1] + 1, move[2] + 1};
    }
    static int[] mediumMachineMove(char friendlyPiece, char enemyPiece) {
        System.out.println("Making move level \"medium\"");
        int[] move = new int[]{};
        for (int i = 0; i < 3; i++) {
            int enemyCells = 0;
            int friendlyCells = 0;
            for (int j = 0; j < 3; j++) {
                if (cells[i][j] == enemyPiece) {
                    enemyCells++;
                } else if (cells[i][j] == friendlyPiece) {
                    friendlyCells++;
                } else {
                    move = new int[]{i + 1, j + 1};
                }
            }


            if (enemyCells == 0 && friendlyCells == 2) {
                return move;
            }
            if (enemyCells == 2 && friendlyCells == 0) {
                return move;
            }

        }
        for (int i = 0; i < 3; i++) {
            int enemyCells = 0;
            int friendlyCells = 0;
            for (int j = 2; j >= 0; j--) {
                if (cells[i][j] == enemyPiece) {
                    enemyCells++;
                } else if (cells[i][j] == friendlyPiece) {
                    friendlyCells++;
                } else {
                    move = new int[]{i + 1, j + 1};
                }
            }

            if (enemyCells == 0 && friendlyCells == 2) {
                return move;
            }
            if (enemyCells == 2 && friendlyCells == 0) {
                return move;
            }
        }
        int enemyDiag = 0;
        int friendlyDiag = 0;
        for (int i = 0; i < 3; i++) {
            if (cells[i][i] == enemyPiece) {
                enemyDiag++;
            } else if (cells[i][i] == friendlyPiece) {
                friendlyDiag++;
            } else {
                move = new int[]{i + 1, i + 1};
            }
            System.out.print(currentPlayer);

            if(i==2){

                if (enemyDiag == 0 && friendlyDiag == 2) {
                    return move;
                }
                if (enemyDiag == 2 && friendlyDiag == 0) {
                    return move;
                }
            }

        }

        int enemyDiag2 = 0;
        int friendlyDiag2 = 0;

        char[] diagonal2 = new char[]{cells[0][2], cells[1][1], cells[2][0]};

        for(int i = 0; i < 3; i++ ){
            if (diagonal2[i] == enemyPiece) {
                enemyDiag2++;
            } else if (diagonal2[i] == friendlyPiece) {
                friendlyDiag2++;
            } else {
                move = new int[]{i + 1, 2-i + 1};
            }
        }

        if (enemyDiag2 == 0 && friendlyDiag2 == 2) {
            return move;
        }
        if (enemyDiag2 == 2 && friendlyDiag2 == 0) {
            return move;
        }

        return machineMove();
    }

    static int[] machineMove() {
        Random random = new Random();

        int a = random.nextInt(3) + 1;
        int b = random.nextInt(3) + 1;
        while (cells[a - 1][b - 1] != ' ') {
            a = random.nextInt(3) + 1;
            b = random.nextInt(3) + 1;
        }
        return new int[]{a, b};
    }

    public static void match(String p1, String p2) {
        // write your code here

        boolean p1Computer = p1.equals("easy");
        boolean p2Computer = p2.equals("easy");
        boolean p1ComputerMedium = p1.equals("medium");
        boolean p2ComputerMedium = p2.equals("medium");
        boolean p1ComputerHard = p1.equals("hard");
        boolean p2ComputerHard = p2.equals("hard");
        Scanner sc = new Scanner(System.in);
        cells = generateBoard();
        displayBoard();
        while (!winner || (xcells + ocells) < 9) {
            int[] playerMove;
            if (xcells > ocells) {
                currentPlayer = 'O';
                if (p2Computer) {
                    System.out.println("Making move level \"easy\"");
                    playerMove = machineMove();
                } else if (p2ComputerMedium) {
                    playerMove = mediumMachineMove('O', 'X');
                } else if (p2ComputerHard) {
                    playerMove = hardMachineMove();
                }else {
                    playerMove = getPlayerMove(sc);
                }
            } else {
                currentPlayer = 'X';
                if (p1Computer) {
                    System.out.println("Making move level \"easy\"");
                    playerMove = machineMove();
                } else if (p1ComputerMedium) {
                    playerMove = mediumMachineMove('X', 'O');
                } else if (p1ComputerHard) {
                    playerMove = hardMachineMove();
                }else {
                    playerMove = getPlayerMove(sc);
                }
            }
            int a = playerMove[0];
            int b = playerMove[1];
            System.out.println(a);
            System.out.println(b);
            if (a > 3 || b > 3 || a < 1 || b < 1) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }
            if (cells[a - 1][b - 1] != ' ') {
                System.out.println("This cell is occupied! Choose another one!");
                continue;
            }
            if (currentPlayer == 'X') {
                cells[a - 1][b - 1] = 'X';
                xcells++;
            } else {
                cells[a - 1][b - 1] = 'O';
                ocells++;
            }
            displayBoard();
            System.out.println(currentPlayer);
            winner = checkSpots(currentPlayer, cells);
            if (winner) {
                System.out.println(currentPlayer + " wins");
                break;
            }
            if ((xcells + ocells) >= 9) {
                System.out.println("Draw");
                break;
            }

        }

    }

    static boolean validateParams(String[] params) {
        return ((params.length == 3) && params[0].equals("start"));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean exit;
        do {
            System.out.println("Enter the initial setup: \n");
            String input = sc.nextLine();
            String[] options = input.split(" ");
            exit = options[0].equals("exit");
            if (!exit) {
                boolean validParams = validateParams(options);
                if (!validParams) {
                    System.out.println("Bad parameters!");
                    continue;
                }
                String p1 = options[1];
                String p2 = options[2];
                match(p1, p2);
            }

        } while (!exit);


    }

}
