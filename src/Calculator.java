import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Calculator {
    JFrame frame;
    JTextArea textArea;
    JPanel topPanel;
    JPanel botPanel;
    JButton[] numberButtons = new JButton[10];
    JButton[] functionButtons = new JButton[13];
    JButton addButton, subButton, mulButton, divButton, negButton, eqlButton, rpButton, lpButton, decButton, clrButton, ansButton, exButton, delButton;
    Character negSign;
    String ex = "";
    ArrayList<String> tokens = new ArrayList<>();
    Stack<String> operatorStack = new Stack<>();
    static Queue<String> outputQueue = new LinkedList<>();

    static HashMap<String, Integer> map = new HashMap<>();

    static {
        map.put("^", 3);
        map.put("/", 2);
        map.put("*", 2);
        map.put("+", 1);
        map.put("-", 1);
    }

    double result;


    Calculator() {

        //creating the frame
        frame = new JFrame("Calculator");
        frame.setLayout(null);

        //top panel and text area
        textArea = new JTextArea();
        textArea.setBackground(frame.getContentPane().getBackground());
        topPanel = new JPanel();
        topPanel.setBounds(10, 8, 330, 170);
        topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        topPanel.add(textArea);

        frame.add(topPanel);

        //bottom panel and gridlayout

        botPanel = new JPanel();
        botPanel.setLayout(new GridLayout(6, 4, 20, 20));
        botPanel.setBounds(10, 185, 330, 370);
//        botPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(botPanel);

        //creating the number buttons
        for (int i = 0; i < numberButtons.length; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].setBackground(Color.WHITE);
            numberButtons[i].setFocusable(false);

        }

        //function buttons
        addButton = new JButton("+");
        functionButtons[0] = addButton;

        subButton = new JButton("-");
        functionButtons[1] = subButton;

        mulButton = new JButton("*");

        functionButtons[2] = mulButton;

        divButton = new JButton("/");
        functionButtons[3] = divButton;

        negButton = new JButton("(-)");
        functionButtons[4] = negButton;

        eqlButton = new JButton("=");
        functionButtons[5] = eqlButton;

        lpButton = new JButton("(");
        functionButtons[6] = lpButton;

        rpButton = new JButton(")");
        functionButtons[7] = rpButton;

        clrButton = new JButton("C");
        functionButtons[8] = clrButton;

        decButton = new JButton(".");
        functionButtons[9] = decButton;

        ansButton = new JButton("A");
        functionButtons[10] = ansButton;

        exButton = new JButton("^");
        functionButtons[11] = exButton;

        delButton = new JButton("<-");
        functionButtons[12] = delButton;

        //adding the buttons to the panel
        botPanel.add(numberButtons[7]);
        botPanel.add(numberButtons[8]);
        botPanel.add(numberButtons[9]);
        botPanel.add(divButton);

        botPanel.add(numberButtons[4]);
        botPanel.add(numberButtons[5]);
        botPanel.add(numberButtons[6]);
        botPanel.add(mulButton);

        botPanel.add(numberButtons[1]);
        botPanel.add(numberButtons[2]);
        botPanel.add(numberButtons[3]);
        botPanel.add(addButton);

        botPanel.add(numberButtons[0]);
        botPanel.add(decButton);
        botPanel.add(negButton);
        botPanel.add(subButton);

        botPanel.add(lpButton);
        botPanel.add(rpButton);
        botPanel.add(clrButton);
        botPanel.add(eqlButton);

        botPanel.add(exButton);
        botPanel.add(ansButton);
        botPanel.add(delButton);


        frame.setSize(350, 600);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    //methods for calculator logic
    public static ArrayList<String> toTokens(String ex, ArrayList<String> tokens) {
    //(1+3.022)-7^2/50
        String x = "";
        for (int i = 0; i < ex.length(); i++) {

            if (Character.isDigit(ex.charAt(i)) || ex.charAt(i) == '.') {
                x += ex.charAt(i);
            }
            else {
                if (!x.equals("") ) {
                    tokens.add(x);
                    x = "";
                }
                tokens.add(String.valueOf(ex.charAt(i)));
            }
        }
        if(!x.equals("")) {
            tokens.add(x);
        }
        return tokens;
    }

    //this method does the shunting yard algorithm.
    public static Queue<String> infixToPostfix(Queue<String> outputQueue, Stack<String> operatorStack, HashMap<String, Integer> map, ArrayList<String> tokens) {
        int i;
        for (i = 0; i < tokens.size(); i++) {

            String x = tokens.get(i);
            char c = x.charAt(0);

            if (Character.isDigit(c)) {
                outputQueue.add(x);
            } else if (c == '(') {
                operatorStack.push(x);
            } else if (c == ')') {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(") ) {
                    outputQueue.add(operatorStack.pop());
                }
                operatorStack.pop();
            } else {
                if (operatorStack.isEmpty()) {
                    operatorStack.push(x);
                }
                else {
                    while (!operatorStack.isEmpty() && map.get(operatorStack.peek()) != null && map.get(x) <= map.get(operatorStack.peek())) {
                        outputQueue.add(operatorStack.pop());
                    }
                    operatorStack.push(x);
                }

            }

        }
        if(!operatorStack.isEmpty()){
            while (!operatorStack.isEmpty()) {
                outputQueue.add(operatorStack.pop());
            }
        }

        return outputQueue;
    }

        public static void main (String[]args){

             HashMap<String, Integer> hmap = new HashMap<>();


                hmap.put("^", 3);
                hmap.put("/", 2);
                hmap.put("*", 2);
                hmap.put("+", 1);
                hmap.put("-", 1);

            Stack<String> Stack = new Stack<>();
            Queue<String> Queue = new LinkedList<>();



            String ex = "(1-1)*3-1.0";

            ArrayList<String> tokens = new ArrayList<>();
            tokens = toTokens(ex, tokens);

            System.out.println(tokens);



            Queue = infixToPostfix(Queue, Stack, hmap, tokens);

            System.out.println(Queue);

        }
    }


