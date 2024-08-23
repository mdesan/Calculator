import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Calculator {
    JFrame frame;
    JTextArea textArea;
    Font font = new Font("Calculator", Font.PLAIN,30);
    JScrollPane scrollPane;
    JPanel topPanel;
    JPanel botPanel;
    JButton[] numberButtons = new JButton[10];
    JButton[] functionButtons = new JButton[13];
    JButton addButton, subButton, mulButton, divButton, negButton, eqlButton, rpButton, lpButton, decButton, clrButton, ansButton, exButton, delButton;
    Character negSign;
    String ex = "";
    ArrayList<String> tokens = new ArrayList<>();
    Stack<String> operatorStack = new Stack<>();
     Stack<Double> evStack = new Stack<>();
    Queue<String> outputQueue = new LinkedList<>();

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
        textArea.setFont(font);
        textArea.setBackground(frame.getContentPane().getBackground());
//        textArea.setPreferredSize(new Dimension(320,160));

        textArea.setCaretPosition(0);


        scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(10, 8, 330, 170);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

//        topPanel = new JPanel(new BorderLayout());
//        topPanel.setBounds(10, 8, 330, 170);
//        topPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//        topPanel.add(textArea);


//        topPanel.add(scrollPane, BorderLayout.CENTER);
        frame.add(scrollPane);

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

            int finalI = i;
            numberButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    textArea.append(numberButtons[finalI].getText());
                    ex += numberButtons[finalI].getText();
                }
            });
        }


        //function buttons----------------------------------------------------

        //addition button
        addButton = new JButton("+");
        functionButtons[0] = addButton;
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textArea.append(addButton.getText());
                ex += "+";
            }
        });

        //subtraction button
        subButton = new JButton("-");
        functionButtons[1] = subButton;
        subButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textArea.append(subButton.getText());
                ex += "-";
            }
        });

        //multiplication button
        mulButton = new JButton("*");
        functionButtons[2] = mulButton;
        mulButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textArea.append(mulButton.getText());
                ex += "*";
            }
        });

        //divide button
        divButton = new JButton("/");
        functionButtons[3] = divButton;
        divButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textArea.append(divButton.getText());
                ex += "/";
            }
        });

        negButton = new JButton("(-)");
        functionButtons[4] = negButton;

        //equals button
        eqlButton = new JButton("=");
        functionButtons[5] = eqlButton;
        eqlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                operatorStack.clear();
                result = 0;
                tokens.clear();
                outputQueue.clear();
                evStack.clear();

                tokens = toTokens(ex,tokens);
                result = evaluator(infixToPostfix(outputQueue,operatorStack,map,tokens), evStack);
                textArea.append("\n");
                textArea.append("                        " + result);
                textArea.append("\n");
                ex = "";

            }
        });

        lpButton = new JButton("(");
        functionButtons[6] = lpButton;
        lpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textArea.append(lpButton.getText());
                ex += "(";
            }
        });

        rpButton = new JButton(")");
        functionButtons[7] = rpButton;
        rpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textArea.append(rpButton.getText());
                ex += ")";
            }
        });

        clrButton = new JButton("C");
        functionButtons[8] = clrButton;
        clrButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ex = "";
                tokens.clear();
                evStack.clear();
                textArea.setText("");

            }
        });

        decButton = new JButton(".");
        functionButtons[9] = decButton;
        decButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textArea.append(decButton.getText());
                ex += ".";
            }
        });

        ansButton = new JButton("A");
        functionButtons[10] = ansButton;
        ansButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(Math.floor(result)==result){
                    ex += String.valueOf((int)Math.floor(result));
                    textArea.append(String.valueOf((int)Math.floor(result)));
                }
                else {
                    ex += String.valueOf(result);
                    textArea.append(String.valueOf(result));
                }
            }
        });

        exButton = new JButton("^");
        functionButtons[11] = exButton;
        exButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textArea.append(exButton.getText());
                ex += "^";
            }
        });

        delButton = new JButton("<-");
        functionButtons[12] = delButton;
        delButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(!ex.equals("")){
                    ex = ex.substring(0,ex.length()-1);
                    textArea.setText(textArea.getText().substring(0, textArea.getText().length()-1));
                }
            }
        });

        for(int i=0; i< functionButtons.length;i++){
            functionButtons[i].setFocusable(false);
        }

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

    //method for evaluating the postfix expression.
    public static double evaluator(Queue<String> outputQueue, Stack<Double> evStack){

        double num1;
        double num2;

        while(!outputQueue.isEmpty()) {
            if (Character.isDigit(outputQueue.peek().charAt(0))) {
                evStack.push(Double.parseDouble(outputQueue.poll()));

            }
            else {

                num2 = evStack.pop();
                num1 = evStack.pop();
                switch (outputQueue.peek()) {
                    case "+":

                        evStack.push(num1 + num2);
                        outputQueue.poll();
                        break;

                    case "-":

                        evStack.push(num1 - num2);
                        outputQueue.poll();
                        break;

                    case "*":

                        evStack.push(num1 * num2);
                        outputQueue.poll();
                        break;

                    case "/":

                        evStack.push(num1 / num2);
                        outputQueue.poll();
                        break;

                    case "^":

                        evStack.push(Math.pow(num1, num2));
                        outputQueue.poll();
                        break;
                }
            }
        }

        return evStack.pop();
    }

//    public void clearEverything(){
//        this.operatorStack.clear();
//        this.result = 0;
//        this.tokens.clear();
//        this.outputQueue.clear();
//        this.evStack.clear();
//        this.ex = "";
//    }

        public static void main (String[]args){

             HashMap<String, Integer> hmap = new HashMap<>();


                hmap.put("^", 3);
                hmap.put("/", 2);
                hmap.put("*", 2);
                hmap.put("+", 1);
                hmap.put("-", 1);

            Stack<String> Stack = new Stack<>();
            Queue<String> Queue = new LinkedList<>();

            String ex = "(8+2)^(2+0)-10";

            ArrayList<String> tokens = new ArrayList<>();
            tokens = toTokens(ex, tokens);



        }
    }


