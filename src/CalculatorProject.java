import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CalculatorProject extends JFrame implements ActionListener, KeyListener {

/*   I used 2 JPanel container instead of directly putting it on the frame for better management.
     1 panel for the JTextField clear button and backspace, and 1 panel for the buttons.*/
   JPanel upperCorner, bottomCorner;

   // Text field for displaying the input and results
   JTextField resultField;

   // Buttons for clear and delete functions
   JButton clear, del;
   JButton[] buttons;

   // I initialized array of button labels for the calculator, I did this purposely so that I could easy modify it later on using grid layout
   private static final String[] CALCULATOR_BUTTONS = {
           "7", "8", "9", "+",
           "4", "5", "6", "-",
           "1", "2", "3", "x",
           ".", "0", "=", "/"
   };
/*   since strings are immutable, it means once written you cannot change it, and you have to make a new one.
     by using StringBuilder which is mutable, I can now handle the String on the text field freely. */
   private final StringBuilder input;

   // Variables to store operands and operator
   private double num1, num2;
   private char operator;

   //this is the constructor to set up the calculator's GUI
   CalculatorProject() {
      input = new StringBuilder(); //initialize string builder

      upperCorner = new JPanel(); //set up upper panel. below is the attributes
      //I used null layout for this Panel because I want to manually position the clear button and backspace
      upperCorner.setLayout(null);
      upperCorner.setPreferredSize(new Dimension(0, 170));
      upperCorner.setBackground(new Color(32, 32, 32));

      bottomCorner = new JPanel();
      //I used grid layout for this Panel for button placement efficiency by just using rows and cols.
      bottomCorner.setLayout(new GridLayout(4,4, 5, 5));
      bottomCorner.setPreferredSize(new Dimension(0,370));
      bottomCorner.setBackground(new Color(32, 32, 32));
      /*this method is for implementing margin, as you can see there are spaces outside the buttons
      when you run the program.*/
      bottomCorner.setBorder(BorderFactory.createEmptyBorder(10,3,3,3));

      resultField = new JTextField();
      resultField.setEditable(false);
      resultField.setFocusable(false);
      resultField.setBounds(17, 35, 290, 80);
      resultField.setBorder(new LineBorder(Color.white, 0, true));
      resultField.setBackground(new Color(50, 50, 50));
      resultField.setForeground(Color.white);
      resultField.setFont(new Font("Arial", Font.BOLD, 30));
      resultField.setHorizontalAlignment(JTextField.RIGHT);
      upperCorner.add(resultField);


      clear = new JButton("C");
      clear.setBounds(18, 120, 40, 40);
      clear.setFont(new Font("Arial", Font.PLAIN, 20));
      clear.setBackground(new Color(50,50,50));
      clear.setForeground(Color.red);
      clear.setFocusable(false); //removes the blinking cursor
      clear.setBorder(new LineBorder(null, 0, true)); //this line of code removes the borders that I don't want
      clear.addActionListener(this);
      upperCorner.add(clear);

      del = new JButton("←");
      del.setBounds(266, 120, 40, 40);
      del.setFont(new Font("Arial", Font.PLAIN, 20));
      del.setBackground(new Color(50,50,50));
      del.setForeground(Color.red);
      del.setFocusable(false); //removes the blinking cursor
      del.setBorder(new LineBorder(null, 0, true)); //this line of code removes the borders that i don't want
      del.addActionListener(this);
      upperCorner.add(del); //adds to my upperCorner panel which adds to the frame

      //creation of calculator buttons
      buttons = new JButton[CALCULATOR_BUTTONS.length];
      for(int i = 0; i < buttons.length; i++) {
         buttons[i] = new JButton(CALCULATOR_BUTTONS[i]);
         buttons[i].setFont(new Font("Arial", Font.PLAIN, 20));
         buttons[i].addActionListener(this); //adds action listener.
         buttons[i].addKeyListener(this);
         buttons[i].setBackground(new Color(50,50,50));
         buttons[i].setForeground(Color.white);
         buttons[i].setFocusable(false);
         buttons[i].setBorder(new LineBorder(null, 0, true));
         bottomCorner.add(buttons[i]);
      }
      //frame properties
      this.setLayout(new BorderLayout());
      this.add(upperCorner, BorderLayout.NORTH);
      this.add(bottomCorner, BorderLayout.CENTER);
      this.setTitle("Calculator Project");
      this.setSize(340, 540);
      this.setResizable(false);
      this.setVisible(true);
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
      this.addKeyListener(this);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      // Get the button that triggered the action event
      JButton source = (JButton)e.getSource();
      String buttonText = source.getText();

      switch (buttonText) {
         case "+":
         case "-":
         case "x":
         case "/":
            // Save the operator and first number, clear input for the next number
            operator = buttonText.charAt(0);
            num1 = Double.parseDouble(resultField.getText());
            input.setLength(0); // Clears the input StringBuilder
            break;
         case "=":
            // Parses second number and perform calculation
            num2 = Double.parseDouble(resultField.getText());
            calculate();
            break;
         case "←":
            // Handle backspace: remove last character from input, took me decades to figure this out.
            if (input.isEmpty()) {
               input.deleteCharAt(input.length() - 1);
               updateResultField(); // Updates the result field with new input
            }
            break;
         case "C":
            // Clears the input and result field
            input.setLength(0);
            resultField.setText("");
            break;
         default:
            // this appends the digit or decimal point to input
            input.append(buttonText);
            updateResultField(); // Update the result field with new input
            break;
      }
   }

   @Override
   public void keyTyped(KeyEvent e) {
      char c = e.getKeyChar();
      if (Character.isDigit(c) || c == '.') {
         input.append(c);
         updateResultField();
      }
   }

   @Override
   public void keyPressed(KeyEvent e) {

      switch (e.getKeyCode()){
         case 8:
            if (input.isEmpty()) {
               input.deleteCharAt(input.length() - 1);
               updateResultField(); // Updates the result field with new input
            }
            break;
         case 10:
            num2 = Double.parseDouble(resultField.getText());
            calculate();
            break;
         case 67:
            input.setLength(0);
            resultField.setText("");
            break;
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {
      //System.out.println(e.getKeyChar());
      System.out.println(e.getKeyCode());
   }

   //now this is the method that performs the calculation based on the stored operator and numbers.
   private void calculate() {
      double result = 0;
      switch (operator) {
         case '+':
            result = num1 + num2;
            break;
         case '-':
            result = num1 - num2;
            break;
         case 'x':
            result = num1 * num2;
            break;
         case '/':
            // Check to prevent division by zero. because *ehem* YOU CAN'T DIVIDE BY ZERO AND COMPUTERS ARE BAD AT DIVISION
            if (num2 != 0)
               result = num1 / num2;
            break;
      }
      resultField.setText(String.valueOf(result));
   }

   //method that updates the result text field with the current input.
   private void updateResultField() {
      resultField.setText(input.toString());
   }

   //main method to run the program
   public static void main(String[] args) {
      new CalculatorProject();
   }
}