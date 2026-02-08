public class GameLogic {
    private char[][] board;
    private char currentPlayer;
    private char lastPlayer;
    private int[][] winningCells;

    public GameLogic(){
        board = new char[3][3];
        currentPlayer= 'X';
        lastPlayer = ' ';
        resetBoard();
    }

    public void resetBoard(){
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                board [i][j] = ' ';
            }
        }
    }
    // This defines a turn
    public boolean makeMove(int row,int column){
        if (board[row][column] != ' '){
            return false;
        }
        board [row][column] = currentPlayer;
        lastPlayer = currentPlayer;
        switchPlayer();
        return true;
    }
    public boolean wouldWin(int r, int c, char symbol) {
        if (!isCellEmpty(r, c)) return false;

        char temp = board[r][c];
        board[r][c] = symbol;

        boolean win = checkWin(symbol);

        board[r][c] = temp;

        return win;
    }
    public void switchPlayer() {
        if (currentPlayer == 'X') {
            currentPlayer = 'O';
        } else {
            currentPlayer = 'X';
        }
    }
    public int[][] getWinningCells(){
        return winningCells;
    }
    public char getCell(int r, int c){
        return board[r][c];
    }
    public boolean isCellEmpty(int r,int c){
        if (board[r][c] == ' '){
            return true;
        }
            return false;
    }
    public char getCurrentPlayer() {
        return currentPlayer;
    }
    public void setCurrentPLayer(char s){
        currentPlayer = s;
    }
    public char getLastPlayer() {
        return lastPlayer;
    }

    public boolean checkWin(){
        // checking columns
        for (int c = 0; c<3;c++){
            if ( (board[0][c] == board[1][c]) &&
                    (board[1][c] == board [2][c])&&
                    (board[0][c] != ' ')){
                winningCells = new int[][]{{0,c},{1,c},{2,c}};
                return true;
            }
        }
        // checking rows
        for (int r = 0; r<3;r++){
            if ( (board[r][0] == board[r][1]) &&
                    (board[r][1] == board [r][2])&&
                    (board[r][0] != ' ')){
                winningCells = new int[][]{{r,0},{r,1},{r,2}};
                return true;
            }
        }
        // checking Diagonals
        if (
                (board[0][0] == board[1][1])&&
                        (board[1][1] == board[2][2])&&
                        (board[0][0] != ' ')
        ){
            winningCells = new int[][]{{0,0},{1,1},{2,2}};
            return true;
        }
        if (
                (board[0][2] == board[1][1])&&
                        (board[1][1] == board[2][0])&&
                        (board[0][2] != ' ')
        ){
            winningCells = new int[][]{{0,2},{1,1},{2,0}};
            return true;
        }
        return false;
    }
    public boolean checkWin(char symbol){
        // checking columns
        for (int c = 0; c<3;c++){
            if ( (board[0][c] == board[1][c]) &&
                    (board[1][c] == board [2][c])&&
                    (board[0][c] == symbol)){
                return true;

            }
        }

        // checking rows
        for (int r = 0; r<3;r++){
            if ( (board[r][0] == board[r][1]) &&
                    (board[r][1] == board [r][2])&&
                    (board[r][0] == symbol)){
                return true;
            }
        }
        // checking Diagonals
            if (
                    (board[0][0] == board[1][1])&&
                    (board[1][1] == board[2][2])&&
                    (board[0][0] == symbol)
            ){
                return true;
            }
        if (
                (board[0][2] == board[1][1])&&
                        (board[1][1] == board[2][0])&&
                        (board[0][2] == symbol)
        ){
            return true;
        }
        return false;
    }

    public boolean checkTie() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }



}
