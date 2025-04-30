import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class sortVisualizer{
    public static void main(String[] args){
        final int size = 200; // Change number of elements <<<<< 
        final int initialSpeed = 30;
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("Sorting Algo. Visualizer");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setSize(1000, 700);
            window.setLayout(new BorderLayout());

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new GridBagLayout());
            controlPanel.setBackground(Color.LIGHT_GRAY);

            JPanel visualizerPanel = new JPanel(new BorderLayout());

            sortPanel panel = new sortPanel(size, initialSpeed);
            panel.setPreferredSize(new Dimension(1000, 500));
            visualizerPanel.add(panel, BorderLayout.CENTER);

            JButton selectionSortButton = new JButton("Selection Sort");
            JButton bubbleSortButton = new JButton("Bubble Sort");
            JButton mergeSortButton = new JButton("Merge Sort");
            JButton quickSortButton = new JButton("Quick Sort");
            JButton runButton = new JButton("Run");
            runButton.setEnabled(false);

            JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, initialSpeed);
            speedSlider.setMajorTickSpacing(20);
            speedSlider.setMinorTickSpacing(5);
            speedSlider.setPaintTicks(true);
            speedSlider.setPaintLabels(false);
            speedSlider.setSnapToTicks(true);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            gbc.gridx = 0;
            gbc.gridy = 0;
            controlPanel.add(selectionSortButton, gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            controlPanel.add(bubbleSortButton, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            controlPanel.add(mergeSortButton, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            controlPanel.add(quickSortButton, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            controlPanel.add(runButton, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 2;
            controlPanel.add(speedSlider, gbc);

            window.add(controlPanel, BorderLayout.NORTH);
            window.add(visualizerPanel, BorderLayout.CENTER);

            window.setLocationRelativeTo(null);
            window.setVisible(true);

            final String[] selectedSort = {null};

            ActionListener selectSortAction = e -> {
                selectionSortButton.setBackground(null);
                bubbleSortButton.setBackground(null);
                mergeSortButton.setBackground(null);
                quickSortButton.setBackground(null);

                JButton source = (JButton) e.getSource();
                source.setBackground(Color.GREEN);
                selectedSort[0] = source.getText();
                runButton.setEnabled(true);
            };

            selectionSortButton.addActionListener(selectSortAction);
            bubbleSortButton.addActionListener(selectSortAction);
            mergeSortButton.addActionListener(selectSortAction);
            quickSortButton.addActionListener(selectSortAction);

            runButton.addActionListener(e -> {
                panel.generateRandomArray(size);
                panel.repaint();
                switch(selectedSort[0]){
                    case "Selection Sort":
                        panel.selectionSort();
                        break;
                    case "Bubble Sort":
                        panel.bubbleSort();
                        break;
                    case "Merge Sort":
                        panel.mergeSort();
                        break;
                    case "Quick Sort":
                        panel.quickSort();
                        break;
                    default:
                        JOptionPane.showMessageDialog(window, "Please select a sorting algorithm.");
                }
            });

            speedSlider.addChangeListener(new ChangeListener(){
                public void stateChanged(ChangeEvent e){
                    int newSpeed = speedSlider.getValue();
                    panel.setSleepTime(newSpeed);
                }
            });
        });
    }
}
