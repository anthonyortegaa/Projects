package BlackJack_Sim;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class BlackjackUI {
    BlackjackGame game;
    private boolean gameStarted = false;
    private int gameCount = 0;

    JFrame frame = new JFrame("Black Jack");
    JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            try {
                if (gameStarted) {
                    Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/Back.png")).getImage();
                    if (!stayButton.isEnabled()) {
                        hiddenCardImg = new ImageIcon(getClass().getResource(game.hiddenCard.getImagePath())).getImage();
                    }
                    g.drawImage(hiddenCardImg, 20, 20, 110, 154, null);
                }

                // Draw dealer's hand
                for (int i = 0; i < game.dealerHand.size(); i++) {
                    Card card = game.dealerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, 135 + (115 * i), 20, 110, 154, null);
                }

                // Draw player's hand
                for (int i = 0; i < game.playerHand.size(); i++) {
                    Card card = game.playerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, 20 + (115 * i), 320, 110, 154, null);
                }

                if (!stayButton.isEnabled()) {
                    game.reduceDealerAce();
                    game.reducePlayerAce();
                    int winnings = 0;

                    String message = "";
                    if (game.playerSum > 21) {
                        message = "Bust, You Lose!";
                    } else if (game.dealerSum > 21) {
                        message = "Dealer Bust, You Win!";
                        winnings = currentBet * 2;
                    } else if (game.playerSum == game.dealerSum && gameStarted) {
                        message = "Draw!";
                        winnings = currentBet;
                    } else if (game.playerSum > game.dealerSum) {
                        message = "You Win!";
                        winnings = currentBet * 2;
                    }
                    else if (game.playerSum < game.dealerSum) {
                        message = "You Lose!";
                        winnings = currentBet * 2;
                    }
                    
                    gameCount++;
                    gambling.addWinnings(winnings);
                    currentBet = 0;
                    updateBettingFields();

                    g.setFont(new Font("Arial", Font.BOLD, 30));
                    g.setColor(Color.WHITE);
                    FontMetrics fm = g.getFontMetrics();
                    int textWidth = fm.stringWidth(message);
                    int messageX = (getWidth() - textWidth) / 2;
                    int messageY = getHeight() / 2 - 20;
                    g.drawString(message, messageX, messageY);

                }
                else if(!gameStarted){
                    g.setFont(new Font("Arial", Font.BOLD, 30));
                    g.setColor(Color.WHITE);
                    FontMetrics fm = g.getFontMetrics();
                    int textWidth = fm.stringWidth("PLACE BETS");
                    int messageX = (getWidth() - textWidth) / 2;
                    int messageY = getHeight() / 2 - 20;
                    g.drawString("PLACE BETS", messageX, messageY);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stand");
    JButton playAgainButton = new JButton("Play Again");
    JLabel totalsLabel = new JLabel("Dealer: ? | Player: ?");
    JButton dealButton = new JButton("Deal");
    JPanel bettingPanel = new JPanel();
    JTextField balanceField = new JTextField();
    JTextField betField = new JTextField();
    JButton bet50Button = new JButton("$50");
    JButton bet10Button = new JButton("$10");
    JButton bet1Button = new JButton("$1");
    JButton finalizeBetButton = new JButton("Finalize Bet");
    JButton clearBetButton = new JButton("Clear Bet");
    JButton ATMButton = new JButton("ATM");

    int currentBet = 0;
    Gamble gambling = new Gamble();

    private void updateTotals() {
        String dealerTotal = stayButton.isEnabled()
                ? String.valueOf(game.dealerSum - game.hiddenCard.getValue())
                : String.valueOf(game.dealerSum);
        totalsLabel.setText("Dealer: " + dealerTotal + " | Player: " + game.playerSum);
    }


    public BlackjackUI() {
        game = new BlackjackGame();

        setupBettingPanel();

        // Frame setup
        frame.setVisible(true);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Game panel for card display
        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);

        JPanel totalsPanel = new JPanel();
        totalsPanel.setOpaque(false);
        totalsPanel.setLayout(new BorderLayout());
        totalsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        totalsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalsLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalsLabel.setForeground(Color.WHITE);
        totalsPanel.add(totalsLabel, BorderLayout.CENTER);
        gamePanel.add(totalsPanel, BorderLayout.CENTER);

        // Button panel setup
        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        playAgainButton.setFocusable(false);
        playAgainButton.setVisible(false);
        buttonPanel.add(playAgainButton);
        totalsLabel.setVisible(false);
        hitButton.setVisible(false);
        stayButton.setVisible(false);
        ATMButton.setFocusable(false);
        ATMButton.setVisible(false);
        buttonPanel.add(ATMButton);

        dealButton.setFocusable(false);
        buttonPanel.add(dealButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // "Hit" button logic
        hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Card card = game.deck.drawCard();
                game.playerSum += card.getValue();
                game.playerAceCount += card.isAce() ? 1 : 0;
                game.playerHand.add(card);

                if (game.reducePlayerAce() > 21) {
                    hitButton.setEnabled(false);
                    stayButton.setEnabled(false);
                    playAgainButton.setVisible(true);
                }
                updateTotals();
                gamePanel.repaint();
            }
        });

        // "Stay" button logic
        stayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                while (game.dealerSum < 17) {
                    Card card = game.deck.drawCard();
                    game.dealerSum += card.getValue();
                    game.dealerAceCount += card.isAce() ? 1 : 0;
                    game.dealerHand.add(card);
                }

                playAgainButton.setVisible(true);
                updateTotals();
                gamePanel.repaint();
            }
        });

        // "Deal" button
        dealButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBettingFields();

                if (currentBet > 0) {
                    gameStarted = true;
                    game.dealInitialCards();
                    if(gameCount > 0){
                        game.dealerSum -= game.hiddenCard.getValue();
                    }
                    updateTotals();

                    // Disable betting buttons
                    bet50Button.setEnabled(false);
                    bet10Button.setEnabled(false);
                    bet1Button.setEnabled(false);
                    finalizeBetButton.setEnabled(false);
                    clearBetButton.setEnabled(false);

                    // Show and enable relevant buttons
                    totalsLabel.setVisible(true);
                    hitButton.setVisible(true);
                    stayButton.setVisible(true);
                    hitButton.setEnabled(true);
                    stayButton.setEnabled(true);
                    dealButton.setVisible(false);

                    gameStarted = true;
                    gamePanel.repaint();
                    if(gameCount > 0){
                        game.dealerSum += game.hiddenCard.getValue();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Place a bet to start!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // ATM Button
        ATMButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gambling.resetBalance();

                ATMButton.setVisible(false);
                JOptionPane.showMessageDialog(frame, "Bank Account Reset!\n New Balance: $100", "ATM Refill", JOptionPane.INFORMATION_MESSAGE);

                // Reset the game state
                playAgainButton.setVisible(false);
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                hitButton.setVisible(false);
                stayButton.setVisible(false);
                totalsLabel.setText("Dealer: ? | Player: ?");
                totalsLabel.setVisible(false);
                dealButton.setVisible(true);

                // Re-enable betting buttons
                bet50Button.setEnabled(true);
                bet10Button.setEnabled(true);
                bet1Button.setEnabled(true);
                finalizeBetButton.setEnabled(true);
                clearBetButton.setEnabled(true);

                game = new BlackjackGame();
                gameStarted = false;

                gamePanel.repaint();

            }
        });

        // "Play Again" button
        playAgainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Reset the game state
                playAgainButton.setVisible(false);
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                hitButton.setVisible(false);
                stayButton.setVisible(false);

                totalsLabel.setText("Dealer: ? | Player: ?");
                totalsLabel.setVisible(false);
                dealButton.setVisible(true);

                // Re-enable betting buttons
                bet50Button.setEnabled(true);
                bet10Button.setEnabled(true);
                bet1Button.setEnabled(true);
                finalizeBetButton.setEnabled(true);
                clearBetButton.setEnabled(true);

                currentBet = 0;
                updateBettingFields();

                // Ensure all game variables are reset
                game = new BlackjackGame();
                game.dealerSum = 0;
                game.hiddenCard = null;
                game.playerSum = 0;
                gameStarted = false;

                // Ensure the dealer hand is reset
                game.dealerHand.clear();
                game.playerHand.clear();

                if (gambling.getPlayerBalance() == 0) {
                    bet50Button.setEnabled(false);
                    bet10Button.setEnabled(false);
                    bet1Button.setEnabled(false);
                    dealButton.setVisible(false);
                    ATMButton.setVisible(true);
                }

                // Update UI
                gamePanel.repaint();
            }
        });


        frame.repaint();
    }


    private void setupBettingPanel() {
        bettingPanel.setLayout(new GridBagLayout());
        bettingPanel.setBackground(new Color(53, 101, 77));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Balance display
        balanceField.setEditable(false);
        balanceField.setFont(new Font("Arial", Font.BOLD, 14));
        balanceField.setHorizontalAlignment(JTextField.CENTER);
        gbc.gridy = 0;
        bettingPanel.add(balanceField, gbc);

        // Bet display
        betField.setEditable(false);
        betField.setFont(new Font("Arial", Font.BOLD, 14));
        betField.setHorizontalAlignment(JTextField.CENTER);
        gbc.gridy = 1;
        bettingPanel.add(betField, gbc);

        balanceField.setText("Balance: $" + gambling.getPlayerBalance());
        betField.setText("Bet: $0");

        // Betting buttons
        addBettingButton(bet50Button, 50);
        gbc.gridy = 2;
        bettingPanel.add(bet50Button, gbc);

        addBettingButton(bet10Button, 10);
        gbc.gridy = 3;
        bettingPanel.add(bet10Button, gbc);

        addBettingButton(bet1Button, 1);
        gbc.gridy = 4;
        bettingPanel.add(bet1Button, gbc);

        // Finalize and Clear buttons
        finalizeBetButton.addActionListener(e -> finalizeBet());
        gbc.gridy = 5;
        bettingPanel.add(finalizeBetButton, gbc);
        clearBetButton.addActionListener(e -> clearBet());
        gbc.gridy = 6;
        bettingPanel.add(clearBetButton, gbc);

        frame.add(bettingPanel, BorderLayout.EAST);
    }

    private void addBettingButton(JButton button, int amount) {
        button.setFocusable(false);
        button.addActionListener(e -> {
            if (gambling.addToBet(amount)) {
                currentBet += amount;
                updateBettingFields();
            } else {
                JOptionPane.showMessageDialog(frame, "Insufficient balance!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void finalizeBet() {
        if (currentBet > 0) {
            gambling.finalizeBet(currentBet);
            JOptionPane.showMessageDialog(frame, "Bet finalized: $" + currentBet, "Bet Placed", JOptionPane.INFORMATION_MESSAGE);
            updateBettingFields();
        } else {
            JOptionPane.showMessageDialog(frame, "Place a bet before finalizing.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearBet() {
        gambling.clearBet(currentBet);
        currentBet = 0;
        updateBettingFields();
    }

    private void updateBettingFields() {
        balanceField.setText("Balance: $" + gambling.getPlayerBalance());
        betField.setText("Bet: $" + currentBet);
    }
}
