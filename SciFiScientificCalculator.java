import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;

public class SciFiScientificCalculator extends JFrame {
    private JTextField display;
    private JPanel buttonPanel;
    private double firstNumber = 0;
    private String operation = "";
    private boolean startNewInput = true;
    private boolean secondFunction = false;

    public SciFiScientificCalculator() {
        setTitle("Science Fiction Scientific Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);  // Increased height to accommodate more buttons
        setLocationRelativeTo(null);
        
        // Set sci-fi theme colors
        Color bgColor = new Color(30, 30, 40);
        Color displayColor = new Color(0, 100, 0);
        Color buttonColor = new Color(70, 70, 90);
        Color textColor = Color.GREEN;
        Color secondFuncColor = new Color(100, 0, 0);
        
        getContentPane().setBackground(bgColor);
        
        // Display
        display = new JTextField("0", 20);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(displayColor);
        display.setForeground(textColor);
        display.setFont(new Font("Digital", Font.BOLD, 24));
        display.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        
        // Button panel - changed to 8 rows to match our button layout
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(8, 5, 5, 5));  // Changed to 8 rows
        buttonPanel.setBackground(bgColor);
        
        // Scientific calculator buttons - rearranged in logical order
        String[][] buttonLabels = {
            // First row: Second function and trigonometric functions
            {"2nd", "sin", "cos", "tan", "π"},
            // Second row: Powers and roots
            {"√x", "x²", "x³", "x^y", "e^x"},
            // Third row: Logarithms and constants
            {"log", "ln", "10^x", "e", "1/x"},
            // Fourth row: Parentheses, factorial, sign change, clear
            {"(", ")", "n!", "±", "C"},
            // Fifth row: Numbers and basic operations (7-9)
            {"7", "8", "9", "/", "%"},
            // Sixth row: Numbers and basic operations (4-6)
            {"4", "5", "6", "*", "EE"},
            // Seventh row: Numbers and basic operations (1-3)
            {"1", "2", "3", "-", "="},
            // Eighth row: Zero, decimal, delete, add, random
            {"0", ".", "DEL", "+", "Rand"}
        };
        
        for (String[] row : buttonLabels) {
            for (String label : row) {
                JButton button = new JButton(label);
                button.setBackground(buttonColor);
                button.setForeground(textColor);
                button.setFont(new Font("Arial", Font.BOLD, 14));
                button.setFocusPainted(false);
                button.setBorder(BorderFactory.createRaisedBevelBorder());
                
                // Highlight 2nd function button when active
                if (label.equals("2nd")) {
                    button.addActionListener(e -> {
                        secondFunction = !secondFunction;
                        button.setBackground(secondFunction ? secondFuncColor : buttonColor);
                    });
                }
                
                // Add glow effect on hover
                button.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent evt) {
                        button.setBackground(new Color(100, 100, 120));
                    }
                    public void mouseExited(MouseEvent evt) {
                        button.setBackground(label.equals("2nd") && secondFunction ? 
                                           secondFuncColor : buttonColor);
                    }
                });
                
                button.addActionListener(new ButtonClickListener());
                buttonPanel.add(button);
            }
        }
        
        // Layout
        setLayout(new BorderLayout(5, 5));
        add(display, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }
    
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            
            if (command.matches("[0-9]")) {
                if (startNewInput) {
                    display.setText("");
                    startNewInput = false;
                }
                display.setText(display.getText() + command);
            } 
            else if (command.equals(".")) {
                if (startNewInput) {
                    display.setText("0.");
                    startNewInput = false;
                } else if (!display.getText().contains(".")) {
                    display.setText(display.getText() + ".");
                }
            }
            else if (command.equals("C")) {
                display.setText("0");
                firstNumber = 0;
                operation = "";
                startNewInput = true;
            }
            else if (command.equals("DEL")) {
                String currentText = display.getText();
                if (currentText.length() > 1) {
                    display.setText(currentText.substring(0, currentText.length() - 1));
                } else {
                    display.setText("0");
                    startNewInput = true;
                }
            }
            else if (command.equals("±")) {
                double num = Double.parseDouble(display.getText());
                display.setText(String.valueOf(-num));
            }
            else if (command.equals("=")) {
                if (!operation.isEmpty()) {
                    double secondNumber = Double.parseDouble(display.getText());
                    double result = calculate(firstNumber, secondNumber, operation);
                    display.setText(String.valueOf(result));
                    operation = "";
                    startNewInput = true;
                }
            }
            else if (command.equals("π")) {
                display.setText(String.valueOf(Math.PI));
                startNewInput = true;
            }
            else if (command.equals("e")) {
                display.setText(String.valueOf(Math.E));
                startNewInput = true;
            }
            else if (command.equals("Rand")) {
                display.setText(String.valueOf(Math.random()));
                startNewInput = true;
            }
            else if (command.equals("2nd")) {
                // Handled in the button creation
            }
            else {
                // Handle scientific functions
                double num = display.getText().isEmpty() ? 0 : Double.parseDouble(display.getText());
                
                switch (command) {
                    // Basic operations
                    case "+": case "-": case "*": case "/": case "%":
                    case "x^y":
                        firstNumber = num;
                        operation = command;
                        startNewInput = true;
                        break;
                        
                    // Scientific functions
                    case "sin":
                        display.setText(String.valueOf(secondFunction ? 
                                       Math.asin(num) : Math.sin(Math.toRadians(num))));
                        break;
                    case "cos":
                        display.setText(String.valueOf(secondFunction ? 
                                       Math.acos(num) : Math.cos(Math.toRadians(num))));
                        break;
                    case "tan":
                        display.setText(String.valueOf(secondFunction ? 
                                       Math.atan(num) : Math.tan(Math.toRadians(num))));
                        break;
                    case "√x":
                        display.setText(String.valueOf(secondFunction ? 
                                       num * num : Math.sqrt(num)));
                        break;
                    case "x²":
                        display.setText(String.valueOf(num * num));
                        break;
                    case "x³":
                        display.setText(String.valueOf(num * num * num));
                        break;
                    case "log":
                        display.setText(String.valueOf(secondFunction ? 
                                       Math.pow(10, num) : Math.log10(num)));
                        break;
                    case "ln":
                        display.setText(String.valueOf(secondFunction ? 
                                       Math.exp(num) : Math.log(num)));
                        break;
                    case "10^x":
                        display.setText(String.valueOf(Math.pow(10, num)));
                        break;
                    case "e^x":
                        display.setText(String.valueOf(Math.exp(num)));
                        break;
                    case "1/x":
                        display.setText(String.valueOf(1 / num));
                        break;
                    case "n!":
                        display.setText(String.valueOf(factorial((int)num)));
                        break;
                    case "EE":
                        // Scientific notation
                        display.setText(String.format("%.4e", num));
                        break;
                }
                startNewInput = true;
            }
        }
    }
    
    private double calculate(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return a / b;
            case "%": return a % b;
            case "x^y": return Math.pow(a, b);
            default: return b;
        }
    }
    
    private long factorial(int n) {
        if (n < 0) return -1;
        if (n == 0) return 1;
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            SciFiScientificCalculator calculator = new SciFiScientificCalculator();
            calculator.setVisible(true);
        });
    }
}