	import javax.swing.*;
import java.awt.*;
import java.util.Random;
 class GameSelector extends JFrame {
    private static final long serialVersionUID = 1L;

    public GameSelector() {
        setTitle("Game Selector");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1, 10, 10));
        setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton snakeWaterGunButton = new JButton("Snake-Water-Gun");
        JButton ticTacToeButton = new JButton("Tic-Tac-Toe");

        snakeWaterGunButton.addActionListener(e -> new SnakeWaterGun());
        ticTacToeButton.addActionListener(e -> new TicTacToe());

        styleButton(snakeWaterGunButton);
        styleButton(ticTacToeButton);

        buttonPanel.add(snakeWaterGunButton);
        buttonPanel.add(ticTacToeButton);

        JLabel titleLabel = new JLabel("Choose a Game", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        add(titleLabel);
        add(buttonPanel);

        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new GameSelector());
    }
}

class SnakeWaterGun extends JFrame {
    
    private static final String[] CHOICES = { "Snake", "Water", "Gun" };

    private final JLabel scoreLabel, choiceCountLabel;
    private final JLabel resultLabel, computerChoiceLabel, userChoiceLabel;
    private int wins = 0, losses = 0, ties = 0;
    private int snakeCount = 0, waterCount = 0, gunCount = 0;

    public SnakeWaterGun() {
        setTitle("Snake Water Gun");
        setSize(500, 550); // Increased height to accommodate new panels
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // Instruction Panel
        JPanel instructionPanel = new JPanel();
        instructionPanel.setLayout(new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
        instructionPanel.setBorder(BorderFactory.createTitledBorder("Game Rules"));
        
        JTextArea rulesText = new JTextArea(
            "• Snake drinks Water (Snake wins against Water)\n" +
            "• Water extinguishes Gun (Water wins against Gun)\n" +
            "• Gun kills Snake (Gun wins against Snake)\n" +
            "• Same choices result in a tie."
        );
        rulesText.setEditable(false);
        rulesText.setBackground(instructionPanel.getBackground());
        rulesText.setFont(new Font("Arial", Font.PLAIN, 14));
        rulesText.setLineWrap(true);
        rulesText.setWrapStyleWord(true);
        instructionPanel.add(rulesText);

        JPanel topPanel = new JPanel();
        JLabel instructionLabel = new JLabel("Choose Snake, Water, or Gun:");
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(instructionLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        for (String choice : CHOICES) {
            JButton button = new JButton(choice);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setFocusPainted(false);
            button.addActionListener(e -> handleChoice(e.getActionCommand()));
            buttonPanel.add(button);
        }

        // Result Display Panel
        JPanel resultPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        resultPanel.setBorder(BorderFactory.createTitledBorder("Round Result"));
        
        userChoiceLabel = new JLabel("Your choice: ", JLabel.CENTER);
        userChoiceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        computerChoiceLabel = new JLabel("Computer's choice: ", JLabel.CENTER);
        computerChoiceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        resultLabel = new JLabel("Make your choice to start", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        resultPanel.add(userChoiceLabel);
        resultPanel.add(computerChoiceLabel);
        resultPanel.add(resultLabel);

        JPanel scorePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        scorePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        scoreLabel = new JLabel("Wins: 0 | Losses: 0 | Ties: 0", JLabel.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));

        choiceCountLabel = new JLabel("Snake: 0 | Water: 0 | Gun: 0", JLabel.CENTER);
        choiceCountLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        scorePanel.add(scoreLabel);
        scorePanel.add(choiceCountLabel);

        JButton resetButton = new JButton("Reset Stats");
        resetButton.addActionListener(e -> resetStats());
        scorePanel.add(resetButton);

        // Combine top sections in a panel
        JPanel topSectionPanel = new JPanel(new BorderLayout());
        topSectionPanel.add(instructionPanel, BorderLayout.NORTH);
        topSectionPanel.add(topPanel, BorderLayout.SOUTH);

        // Add all panels to the frame
        add(topSectionPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.AFTER_LINE_ENDS);
        add(scorePanel, BorderLayout.SOUTH);

        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void handleChoice(String userChoice) {
        updateChoiceCount(userChoice);
        String computerChoice = getSmartCounterChoice();
        String result = determineWinner(userChoice, computerChoice);

        // Update the choice and result labels
        userChoiceLabel.setText("Your choice: " + userChoice);
        computerChoiceLabel.setText("Computer's choice: " + computerChoice);
        resultLabel.setText(result);

        if (result.equals("You win!")) {
            wins++;
            resultLabel.setForeground(new Color(0, 150, 0)); // Green for win
        } else if (result.equals("You lose!")) {
            losses++;
            resultLabel.setForeground(Color.RED); // Red for loss
        } else {
            ties++;
            resultLabel.setForeground(Color.BLUE); // Blue for tie
        }

        updateScore();
    }

    private void updateChoiceCount(String userChoice) {
        switch (userChoice) {
            case "Snake": snakeCount++; break;
            case "Water": waterCount++; break;
            case "Gun": gunCount++; break;
        }
    }

    private String getSmartCounterChoice() {
        Random random = new Random();

        if (random.nextInt(10) < 2) {
            return CHOICES[random.nextInt(CHOICES.length)];
        }

        if (snakeCount == waterCount && waterCount == gunCount) {
            return CHOICES[random.nextInt(CHOICES.length)];
        }

        if (snakeCount == waterCount && snakeCount > gunCount) {
            return random.nextBoolean() ? "Gun" : "Snake";
        }
        if (snakeCount == gunCount && snakeCount > waterCount) {
            return random.nextBoolean() ? "Gun" : "Water";
        }
        if (waterCount == gunCount && waterCount > snakeCount) {
            return random.nextBoolean() ? "Snake" : "Water";
        }

        if (snakeCount > waterCount && snakeCount > gunCount) {
            return "Gun";
        } else if (waterCount > snakeCount && waterCount > gunCount) {
            return "Snake";
        } else {
            return "Water";
        }
    }

    private String determineWinner(String userChoice, String computerChoice) {
        if (userChoice.equals(computerChoice)) {
            return "It's a tie!";
        } else if ((userChoice.equals("Snake") && computerChoice.equals("Water")) ||
                (userChoice.equals("Water") && computerChoice.equals("Gun")) ||
                (userChoice.equals("Gun") && computerChoice.equals("Snake"))) {
            return "You win!";
        } else {
            return "You lose!";
        }
    }

    private void updateScore() {
        scoreLabel.setText("Wins: " + wins + " | Losses: " + losses + " | Ties: " + ties);
        choiceCountLabel.setText("Snake: " + snakeCount + " | Water: " + waterCount + " | Gun: " + gunCount);
    }

    private void resetStats() {
        wins = losses = ties = 0;
        snakeCount = waterCount = gunCount = 0;
        userChoiceLabel.setText("Your choice: ");
        computerChoiceLabel.setText("Computer's choice: ");
        resultLabel.setText("Make your choice to start");
        resultLabel.setForeground(Color.BLACK);
        updateScore();
    }
}
class TicTacToe extends JFrame {
    private static final long serialVersionUID = 1L;

    private final JButton[][] buttons = new JButton[3][3];
    private char currentPlayer = 'X';
    private boolean gameActive = true;
    private final JLabel statusLabel;
    private final JLabel scoreLabel; // Add this field to reference the score label
    private int xWins = 0;
    private int oWins = 0;
    private int draws = 0;
    
    // Add fields to track winning cells
    private int[] winningRow = null;
    private int[] winningCol = null;
    private boolean winningDiagonal1 = false;
    private boolean winningDiagonal2 = false;

    public TicTacToe() {
        setTitle("Tic Tac Toe");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Tic Tac Toe");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titlePanel.add(titleLabel);

        JPanel statusPanel = new JPanel();
        statusLabel = new JLabel("Player X's turn");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusPanel.add(statusLabel);

        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initializeBoard(gridPanel);

        JPanel controlPanel = new JPanel(new GridLayout(1, 3, 10, 0)); // Changed to 3 columns for 3 components
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton resetButton = new JButton("Reset Game");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.addActionListener(e -> resetBoard());

        scoreLabel = new JLabel("X: 0 | O: 0 | Draws: 0", JLabel.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton resetStatsButton = new JButton("Reset Stats");
        resetStatsButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetStatsButton.addActionListener(e -> resetStats());

        controlPanel.add(resetButton);
        controlPanel.add(scoreLabel);
        controlPanel.add(resetStatsButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(statusPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void initializeBoard(JPanel gridPanel) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new JButton("");
                buttons[row][col].setFont(new Font("Arial", Font.BOLD, 60));
                buttons[row][col].setFocusPainted(false);
                buttons[row][col].setPreferredSize(new Dimension(100, 100));

                final int finalRow = row;
                final int finalCol = col;
                buttons[row][col].addActionListener(e -> handleButtonClick(finalRow, finalCol));

                gridPanel.add(buttons[row][col]);
            }
        }
    }

    private void handleButtonClick(int row, int col) {
        if (!gameActive || !buttons[row][col].getText().equals("")) {
            return;
        }

        buttons[row][col].setText(String.valueOf(currentPlayer));
        buttons[row][col].setForeground(currentPlayer == 'X' ? Color.BLUE : Color.RED);

        if (checkForWin()) {
            gameActive = false;
            statusLabel.setText("Player " + currentPlayer + " wins!");
            highlightWinningCells();
            updateScore();
        } else if (isBoardFull()) {
            gameActive = false;
            statusLabel.setText("It's a draw!");
            draws++;
            updateScoreLabel(); // Update score label when there's a draw
        } else {
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            statusLabel.setText("Player " + currentPlayer + "'s turn");
        }
    }

    private void updateScore() {
        if (currentPlayer == 'X') {
            xWins++;
        } else {
            oWins++;
        }
        updateScoreLabel();
    }
    
    // Add this new method to update the score label
    private void updateScoreLabel() {
        scoreLabel.setText("X: " + xWins + " | O: " + oWins + " | Draws: " + draws);
    }
    
    // Add this new method to reset statistics
    private void resetStats() {
        xWins = 0;
        oWins = 0;
        draws = 0;
        updateScoreLabel();
    }

    private boolean checkForWin() {
        String playerMark = String.valueOf(currentPlayer);
        
        // Reset winning cell trackers
        winningRow = null;
        winningCol = null;
        winningDiagonal1 = false;
        winningDiagonal2 = false;

        // Check rows
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(playerMark) &&
                    buttons[i][1].getText().equals(playerMark) &&
                    buttons[i][2].getText().equals(playerMark)) {
                winningRow = new int[]{i, 0, i, 1, i, 2};
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (buttons[0][i].getText().equals(playerMark) &&
                    buttons[1][i].getText().equals(playerMark) &&
                    buttons[2][i].getText().equals(playerMark)) {
                winningCol = new int[]{0, i, 1, i, 2, i};
                return true;
            }
        }

        // Check diagonal 1
        if (buttons[0][0].getText().equals(playerMark) &&
                buttons[1][1].getText().equals(playerMark) &&
                buttons[2][2].getText().equals(playerMark)) {
            winningDiagonal1 = true;
            return true;
        }

        // Check diagonal 2
        if (buttons[0][2].getText().equals(playerMark) &&
                buttons[1][1].getText().equals(playerMark) &&
                buttons[2][0].getText().equals(playerMark)) {
            winningDiagonal2 = true;
            return true;
        }

        return false;
    }

    private void highlightWinningCells() {
        Color winColor = new Color(20, 250, 20); // Green color for winning cells
        
        if (winningRow != null) {
            buttons[winningRow[0]][winningRow[1]].setBackground(winColor);
            buttons[winningRow[2]][winningRow[3]].setBackground(winColor);
            buttons[winningRow[4]][winningRow[5]].setBackground(winColor);
        }
        
        if (winningCol != null) {
            buttons[winningCol[0]][winningCol[1]].setBackground(winColor);
            buttons[winningCol[2]][winningCol[3]].setBackground(winColor);
            buttons[winningCol[4]][winningCol[5]].setBackground(winColor);
        }
        
        if (winningDiagonal1) {
            buttons[0][0].setBackground(winColor);
            buttons[1][1].setBackground(winColor);
            buttons[2][2].setBackground(winColor);
        }   
        if (winningDiagonal2) {
            buttons[0][2].setBackground(winColor);
            buttons[1][1].setBackground(winColor);
            buttons[2][0].setBackground(winColor);
        }
    }
    
    private boolean isBoardFull() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void resetBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setText("");
                buttons[row][col].setBackground(null); // Reset background color
                buttons[row][col].setBackground(UIManager.getColor("Button.background")); // Reset to default button color
            }
        }
        gameActive = true;
        currentPlayer = 'X';
        statusLabel.setText("Player X's turn"); 
        // Reset winning tracking variables
        winningRow = null;
        winningCol = null;
        winningDiagonal1 = false;
        winningDiagonal2 = false;
    }
}
