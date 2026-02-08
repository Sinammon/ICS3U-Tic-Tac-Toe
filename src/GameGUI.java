import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class GameGUI extends JFrame implements ActionListener {

    // background image
    JLabel bg;
    ImageIcon bgTBS = new ImageIcon("gameGUI bg.jpg");
    Image bgScaled = bgTBS.getImage().getScaledInstance(1280, 750, Image.SCALE_SMOOTH);
    ImageIcon image = new ImageIcon(bgScaled);


    //x button
    ImageIcon xButtonTBS = new ImageIcon("x logo.png");
    Image xButtonScaled = xButtonTBS.getImage().getScaledInstance(300, 160, Image.SCALE_SMOOTH);
    ImageIcon xButtonImage = new ImageIcon(xButtonScaled);

    // o button
    ImageIcon oButtonTBS = new ImageIcon("o.png");
    Image oButtonScaled = oButtonTBS.getImage().getScaledInstance(270, 160, Image.SCALE_SMOOTH);
    ImageIcon oButtonImage = new ImageIcon(oButtonScaled);

    //button design
    Font neonFont = new Font("SansSerif", Font.BOLD, 20);
    Color neonC = new Color(255, 0, 255);

    // sound effect
    File clickFile = new File("game click.wav");
    AudioInputStream clickAudioStream = getAudioInputStream(clickFile);
    Clip clip = AudioSystem.getClip();

    // bass drop at the start of the game
    File bassFile = new File("bass drop.wav");
    AudioInputStream BassAudioStream = getAudioInputStream(bassFile);
    Clip BassClip = AudioSystem.getClip();

    GameLogic game;

    StatsManager statsManager;
    String currentPlayerName;

    JPanel top_Panel;
    JLabel playerTurn;

    // panels which act as buttons for the board
    AnimatedCell[][] boardCells = new AnimatedCell[3][3];

    // timer for AI to "think"
    Timer timer;

    JPanel boardPanel;
    JPanel centerAligner;

    JPanel bottom_Panel;
    JButton xButton;
    JButton oButton;

    //After win
    JLabel winner;
    JButton playAgain;
    JButton exit;

    // for AI
    boolean hard = false;
    boolean medium = false;
    boolean vsAI = false;
    char aiSymbol = 'O';
    char humanSymbol = 'X';
    boolean gameStarted = false;


    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == xButton) {
            humanSymbol = 'X';
            aiSymbol = 'O';
            gameStarted = true;
            game.setCurrentPLayer('X');
            startGame();
            return;
        }
        if (e.getSource() == oButton) {

            gameStarted = true;
            startGame();

            // AI goes first
            if (vsAI && medium) {
                humanSymbol = 'O'; // later create a method for o button cuz this is repetitive.
                aiSymbol = 'X';
                game.setCurrentPLayer('X');
                mediumMove();
            }else if(vsAI && hard){
                humanSymbol = 'O';
                aiSymbol = 'X';
                game.setCurrentPLayer('X');
                hardMove();
            }else if (vsAI) {
                humanSymbol = 'O';
                aiSymbol = 'X';
                game.setCurrentPLayer('X');
                aiMove();
            } else {
                game.setCurrentPLayer('O');
            }

            return;
        }
        if (e.getSource() == playAgain) {
            reset();
        }
        if (e.getSource() == exit) {
            dispose();
            try {
                new MainMenuGUI();
            } catch (UnsupportedAudioFileException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            } catch (FontFormatException ex) {
                throw new RuntimeException(ex);
            }
        }


    }
    private void hardMove() {
        if (!gameStarted || game.getCurrentPlayer() != aiSymbol) return;

        // checks number of AI moves
        int aiMoves = 0;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (game.getCell(r, c) == aiSymbol) aiMoves++;
            }
        }

        // 1 Win
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (game.wouldWin(r, c, aiSymbol)) {
                    makeAIMove(r, c);
                    return;
                }
            }
        }


        // 2️ Block Win
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (game.wouldWin(r, c, humanSymbol)) {
                    makeAIMove(r, c);
                    return;
                }
            }
        }

        // 3️ Center if it's available
        if (game.isCellEmpty(1, 1)) {
            makeAIMove(1, 1);
            return;
        }

        // 4️ Corners if its available
        if (aiMoves == 0 || aiMoves == 1) {
            int[][] corners = {{0, 0}, {0, 2}, {2, 0}, {2, 2}};
            for (int[] corner : corners) {
                if (game.isCellEmpty(corner[0], corner[1])) {
                    makeAIMove(corner[0], corner[1]);
                    return;
                }
            }
        }

        // 5️ Random
        aiMove();
    }
    private void mediumMove() {
        if (!gameStarted || game.getCurrentPlayer() != aiSymbol) return;

        // 1️ Try to win
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                // if putting an AI symbol over here lets you win then make the move
                if (game.wouldWin(r, c, aiSymbol)) {
                    makeAIMove(r, c);
                    return;
                }
            }
        }

        // 2️ Block human
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                // if putting a human symbol makes the human win then replace it with an AI symbol
                if (game.wouldWin(r, c, humanSymbol)) {
                    makeAIMove(r, c);
                    return;
                }
            }
        }

        // 3 Random
        aiMove();
    }

    private void aiMove() {

        if (!gameStarted) return;
        if (game.getCurrentPlayer() != aiSymbol) return;

        for (int i = 0; i < 100; i++) {
            // random move on the board
            int r = (int) (Math.random() * 3);
            int c = (int) (Math.random() * 3);

            if (game.makeMove(r, c)) {

                boardCells[r][c].setSymbol(aiSymbol);

                if (game.checkWin(aiSymbol)) {
                        endGame("AI WINS");
                        statsManager.recordLosses(currentPlayerName);
                        statsManager.saveToFile();
                        return;
                }
                if (game.checkTie()) {
                    endGame("IT'S A TIE");
                    statsManager.recordTies(currentPlayerName);
                    statsManager.saveToFile();
                    return;
                }

                updateTurnLabel();
                return;
            }
        }
    }
    private void makeAIMove(int r, int c) {

        game.makeMove(r, c);
        boardCells[r][c].setSymbol(aiSymbol);

        if (game.checkWin(aiSymbol)) {
            endGame("AI WINS");
            statsManager.recordLosses(currentPlayerName);
            statsManager.saveToFile();
            return;
        }

        if (game.checkTie()) {
            endGame("IT'S A TIE");
            statsManager.recordTies(currentPlayerName);
            statsManager.saveToFile();
            return;
        }

        updateTurnLabel();
    }

    private void reset() {
        game.resetBoard();
        gameStarted = false;

        // Clear board buttons
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                boardCells[r][c].reset();
            }
        }

        // Reset bottom panel
        bottom_Panel.removeAll();
        bottom_Panel.add(xButton);
        bottom_Panel.add(oButton);

        xButton.setEnabled(true);
        oButton.setEnabled(true);

        playerTurn.setText("Choose X or O");

        bottom_Panel.revalidate();
        bottom_Panel.repaint();
    }

    private void updateTurnLabel() {
        playerTurn.setText(game.getCurrentPlayer() + "'s turn");
    }

    private void startGame() {
        gameStarted = true;

        xButton.setEnabled(false);
        oButton.setEnabled(false);

        updateTurnLabel();
    }

    private void endGame(String message) {

        int[][] winningCells = game.getWinningCells();
        if (winningCells != null) {
            for (int[] cell : winningCells){
                boardCells[cell[0]][cell[1]].setWinning(true);
            }

        }
        disableBoard();

        // Remove X/O selection buttons
        bottom_Panel.removeAll();

        // Show result
        winner = new JLabel(message);
        winner.setFont(new Font("Arial", Font.BOLD, 36));
        winner.setForeground(neonC);

        playAgain = new JButton("Play Again");
        btnDesign(playAgain);

        exit = new JButton("Exit to Main Menu");
        btnDesign(exit);

        bottom_Panel.add(winner);
        bottom_Panel.add(playAgain);
        bottom_Panel.add(exit);

        bottom_Panel.revalidate();
        bottom_Panel.repaint();
    }

    private void disableBoard() {
       gameStarted = false;
    }
    public void btnDesign(JButton button){
        button.setFont(neonFont);
        button.setForeground(neonC);
        button.setBackground(Color.black);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(neonC, 3));
        button.addActionListener(this);

    }


    public GameGUI(boolean vsA,boolean m,boolean h, String playerName, StatsManager sm) throws LineUnavailableException, UnsupportedAudioFileException, IOException {

        statsManager = sm;
        currentPlayerName = playerName;
        vsAI = vsA;
        medium = m;
        hard = h;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // audio when game begins
        BassClip.open(BassAudioStream);
        BassClip.start();
        clip.open(clickAudioStream);

        // create game object where logic runs
        game = new GameLogic();
        game.resetBoard();

        bg = new JLabel(image);
        setContentPane(bg);
        bg.setLayout(new BorderLayout());

        top_Panel = new JPanel(new FlowLayout());
        top_Panel.setOpaque(false);
        bg.add(top_Panel, BorderLayout.NORTH);

        playerTurn = new JLabel("X's turn");
        playerTurn.setFont(new Font("SanSerif", Font.BOLD, 46));
        playerTurn.setForeground(new Color(255, 0, 255));
        top_Panel.add(playerTurn);

        centerAligner = new JPanel(new GridBagLayout());

        boardPanel = new JPanel(new GridLayout(3, 3));
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                // create panels inside the 3x3 board
                AnimatedCell cell = new AnimatedCell();
                int row = r;
                int col = c;
                cell.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mousePressed(java.awt.event.MouseEvent e) {
                        if (vsAI && game.getCurrentPlayer() == aiSymbol) return;

                        if (clip.isRunning()) {
                            clip.stop();
                        }
                        clip.setFramePosition(0);
                        clip.start();

                        if (game.makeMove(row, col)) {
                            cell.setSymbol(game.getLastPlayer());

                            if (game.checkWin()) {
                                endGame(game.getLastPlayer() + " WINS");
                                if (game.getLastPlayer() == aiSymbol){
                                    statsManager.recordLosses(playerName);
                                }
                                else{
                                    statsManager.recordWin(playerName);
                                }
                                statsManager.saveToFile();
                                return;
                            }
                            if (game.checkTie()) {
                                endGame("IT'S A TIE");
                                statsManager.recordTies(playerName);
                                statsManager.saveToFile();
                                return;
                            }

                            updateTurnLabel();

                            if (vsAI && game.getCurrentPlayer() == aiSymbol) {
                                if (timer != null && timer.isRunning()) return;
                                timer = new Timer(1000, actionEvent ->{
                                    if (hard) hardMove();
                                    else if (medium) mediumMove();
                                    else aiMove();
                                });
                                timer.setRepeats(false);
                                timer.start();

                            }
                        }
                    }
                });

                boardCells[r][c] = cell;
                boardPanel.add(cell);
            }
        }

        boardPanel.setPreferredSize(new Dimension(360, 360));
        boardPanel.setOpaque(false);
        centerAligner.setOpaque(false);
        bg.add(centerAligner, BorderLayout.CENTER);
        centerAligner.add(boardPanel);

        bottom_Panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30,20));

        xButton = new JButton();
        xButton.setIcon(xButtonImage);
        xButton.setPreferredSize(new Dimension(160, 160));
        xButton.setBackground(Color.black);
        xButton.setBorder(BorderFactory.createLineBorder(neonC, 3));
        xButton.addActionListener(this);

        oButton = new JButton();
        oButton.setIcon(oButtonImage);
        oButton.setBackground(Color.black);
        oButton.setBorder(BorderFactory.createLineBorder(neonC, 3));
        oButton.setPreferredSize(new Dimension(160, 160));
        oButton.addActionListener(this);

        bottom_Panel.setOpaque(false);

        bg.add(bottom_Panel, BorderLayout.SOUTH);
        bottom_Panel.add(xButton);
        bottom_Panel.add(oButton);

        setVisible(true);
    }

}