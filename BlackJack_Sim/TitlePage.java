package BlackJack_Sim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TitlePage {
    public static void main(String[] args) {
        // The main frame
        JFrame frame = new JFrame("Blackjack");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the background color
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setBackground(Color.BLACK);
        backgroundPanel.setBounds(0, 0, 800, 600);
        backgroundPanel.setLayout(null);
        frame.add(backgroundPanel);

        // Title image
        JLabel titleImage = new JLabel(new ImageIcon("BlackJack Sim\titleImage.png")); // Ensure the image path is correct
        titleImage.setBounds(145, 50, 550, 300);
        backgroundPanel.add(titleImage);

        // Created by
        JLabel subtitle = new JLabel("Created by Anthony Ortega, Edrich Silva, and David Aparicio");
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        subtitle.setBounds(150, 220, 500, 20);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(subtitle);

        // "How To Play" button
        JButton howToPlayButton = new JButton("How To Play");
        howToPlayButton.setBounds(300, 300, 200, 50);
        howToPlayButton.setBackground(Color.LIGHT_GRAY);
        howToPlayButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        backgroundPanel.add(howToPlayButton);

        howToPlayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        "To play Blackjack, try to get as close to 21 as possible without exceeding it.\n" +
                        "Click 'Hit' to get another card or 'Stay' to stand.\n" +
                        "If you run out of money, click the ATM button to add $100 to your balance.\n\n" +
                        "For more details, you can find many tutorials on YouTube!",
                        "How To Play", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // "Start" button
        JButton startButton = new JButton("Start");
        startButton.setBounds(300, 400, 200, 50);
        startButton.setBackground(Color.LIGHT_GRAY);
        startButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
        backgroundPanel.add(startButton);

        // Action listener for "Start" button to launch BlackjackUI
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new BlackjackUI();
                    }
                });
            }
        });

        frame.setVisible(true);
    }
}
