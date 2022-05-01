package com.project_1_2.group_16.titlescreen;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.swing.*;
import com.badlogic.gdx.math.Vector2;
import com.project_1_2.group_16.Input;
import com.project_1_2.group_16.ai.FloodFill;
import com.project_1_2.group_16.misc.ANSI;
import com.project_1_2.group_16.misc.User;

import static com.project_1_2.group_16.ai.FloodFill.matrixParcour;

public class InputScreen extends JFrame {

    private JFrame frame;
    private JLayeredPane layeredPane;
    private JLabel backGround;
    private JButton buttonP;
    private JRadioButton user;
    private JRadioButton basicBot;
    private JRadioButton advancedBot;
    private ButtonGroup buttonGroup;
    private JLabel labelA = new JLabel("Who will play?");
    private Font large;

    private final int width = 370;
    private final int height = 650;

    boolean status;
    
    //Initiate text boxes for the inputs
    private JTextField x0T = new JTextField();
    private JTextField y0T = new JTextField();
    private JTextField xtT = new JTextField();
    private JTextField ytT = new JTextField();
    private JTextField rT = new JTextField();
    private JTextField mukT = new JTextField();
    private JTextField musT = new JTextField();
    private JTextField muksT = new JTextField();
    private JTextField mussT = new JTextField();
    private JTextField heightFT = new JTextField();
    private JTextField treesNumT = new JTextField();
    private JTextField sandPitsNumT = new JTextField();

    //Initiate labels for the inputs
    private JLabel x0L = new JLabel("x0: ");
    private JLabel y0L = new JLabel("y0: ");
    private JLabel xtL = new JLabel("xt: ");
    private JLabel ytL = new JLabel("yt: ");
    private JLabel rL = new JLabel("r: ");
    private JLabel mukL = new JLabel("muk: ");
    private JLabel musL = new JLabel("mus: ");
    private JLabel muksL = new JLabel("muks: ");
    private JLabel mussL = new JLabel("muss: ");
    private JLabel heightFL = new JLabel("heightProfile: ");
    private JLabel treesNumL = new JLabel("Number of trees:");
    private JLabel sandPitsNumL = new JLabel("Number of sandpits:");

    /**
     * Set up the input screen's GUI
     */
    public void setUpGUI(){
        frame = new JFrame();
        buttonP = new JButton("Play");
        large = new Font("Arial",Font.BOLD,16);
        Color backGroundColor = new Color(59,87,72,255);
        Color fontColor = new Color(255,255,255,255);
        backGround = new JLabel();
        layeredPane = new JLayeredPane();
        user = new JRadioButton("User");
        basicBot = new JRadioButton("Basic bot");
        advancedBot = new JRadioButton("Advanced bot");
        buttonGroup = new ButtonGroup();

        labelA.setFont(large);
        labelA.setBackground(backGroundColor);
        labelA.setForeground(fontColor);
        labelA.setBounds(70, 460, 140, 40);

        user.setBounds(50, 500, 60, 30);
        user.setFocusable(false);
        user.setBackground(backGroundColor);
        user.setForeground(fontColor);
        user.setSelected(true);

        basicBot.setBounds(110, 500, 80, 30);
        basicBot.setFocusable(false);
        basicBot.setBackground(backGroundColor);
        basicBot.setForeground(fontColor);

        advancedBot.setBounds(190, 500, 120, 30);
        advancedBot.setFocusable(false);
        advancedBot.setBackground(backGroundColor);
        advancedBot.setForeground(fontColor);

        buttonGroup.add(user);
        buttonGroup.add(basicBot);
        buttonGroup.add(advancedBot);

        backGround.setBounds(0, 0, width, height);
        backGround.setBackground(backGroundColor);
        backGround.setOpaque(true);

        buttonP.setFont(large);
        buttonP.setBounds(130, 550, 80, 40);
        buttonP.setFocusable(false);
        buttonP.setBackground(Color.decode("#676d88"));
        buttonP.setForeground(fontColor);
        buttonP.setFocusable(false);
        buttonP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runTheGame();
            }
        });
        setUpButtonListeners();

        x0L.setFont(large);
        x0L.setBackground(Color.decode("#676d88"));
        x0L.setForeground(fontColor);
        x0L.setBounds(70, 100, 30, 25);
        x0T.setBounds(100, 100, 50, 25);

        y0L.setFont(large);
        y0L.setBackground(Color.decode("#676d88"));
        y0L.setForeground(fontColor);
        y0L.setBounds(205, 100, 30, 25);
        y0T.setBounds(235, 100, 50, 25);

        xtL.setFont(large);
        xtL.setBackground(Color.decode("#676d88"));
        xtL.setForeground(fontColor);
        xtL.setBounds(70, 145, 30, 25);
        xtT.setBounds(100, 145, 50, 25);

        ytL.setFont(large);
        ytL.setBackground(Color.decode("#676d88"));
        ytL.setForeground(fontColor);
        ytL.setBounds(205, 145, 30, 25);
        ytT.setBounds(235, 145, 50, 25);

        rL.setFont(large);
        rL.setBackground(Color.decode("#676d88"));
        rL.setForeground(fontColor);
        rL.setBounds(70, 190, 30, 25);
        rT.setBounds(100, 190, 50, 25);

        mukL.setFont(large);
        mukL.setBackground(Color.decode("#676d88"));
        mukL.setForeground(fontColor);
        mukL.setBounds(50, 235, 50, 25);
        mukT.setBounds(100, 235, 50, 25);

        musL.setFont(large);
        musL.setBackground(Color.decode("#676d88"));
        musL.setForeground(fontColor);
        musL.setBounds(185, 235, 50, 25);
        musT.setBounds(235, 235, 50, 25);

        muksL.setFont(large);
        muksL.setBackground(Color.decode("#676d88"));
        muksL.setForeground(fontColor);
        muksL.setBounds(45, 280, 55, 25);
        muksT.setBounds(100, 280, 50, 25);

        mussL.setFont(large);
        mussL.setBackground(Color.decode("#676d88"));
        mussL.setForeground(fontColor);
        mussL.setBounds(180, 280, 55, 25);
        mussT.setBounds(235, 280, 50, 25);

        heightFL.setFont(large);
        heightFL.setBackground(Color.decode("#676d88"));
        heightFL.setForeground(fontColor);
        heightFL.setBounds(45, 325, 120, 25);
        heightFT.setBounds(153, 325, 136, 25);

        sandPitsNumL.setFont(large);
        sandPitsNumL.setBackground(Color.decode("#676d88"));
        sandPitsNumL.setForeground(fontColor);
        sandPitsNumL.setBounds(45, 370, 170, 25);
        sandPitsNumT.setBounds(210, 370, 40, 25);

        treesNumL.setFont(large);
        treesNumL.setBackground(Color.decode("#676d88"));
        treesNumL.setForeground(fontColor);
        treesNumL.setBounds(45, 415, 140, 25);
        treesNumT.setBounds(185, 415, 40, 25);

        // default values
        x0T.setText(""+Input.V0.x);
        y0T.setText(""+Input.V0.y);
        xtT.setText(""+Input.VT.x);
        ytT.setText(""+Input.VT.y);
        rT.setText(""+Input.R);
        mukT.setText(""+Input.MUK);
        musT.setText(""+Input.MUS);
        muksT.setText(""+Input.MUKS);
        mussT.setText(""+Input.MUSS);
        heightFT.setText(""+Input.H);
        sandPitsNumT.setText(""+Input.SAND);
        treesNumT.setText(""+Input.TREES);

        // Adding components to the container
        layeredPane.setBounds(0, 0, width, height);
        layeredPane.add(backGround, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(buttonP , JLayeredPane.DRAG_LAYER);
        layeredPane.add(labelA, JLayeredPane.DRAG_LAYER);
        layeredPane.add(user, JLayeredPane.DRAG_LAYER);
        layeredPane.add(basicBot, JLayeredPane.DRAG_LAYER);
        layeredPane.add(advancedBot, JLayeredPane.DRAG_LAYER);
        layeredPane.add(x0T, JLayeredPane.DRAG_LAYER);
        layeredPane.add(x0L , JLayeredPane.DRAG_LAYER);
        layeredPane.add(y0T, JLayeredPane.DRAG_LAYER);
        layeredPane.add(y0L , JLayeredPane.DRAG_LAYER);
        layeredPane.add(xtT, JLayeredPane.DRAG_LAYER);
        layeredPane.add(xtL , JLayeredPane.DRAG_LAYER);
        layeredPane.add(ytT , JLayeredPane.DRAG_LAYER);
        layeredPane.add(ytL , JLayeredPane.DRAG_LAYER);
        layeredPane.add(rT, JLayeredPane.DRAG_LAYER);
        layeredPane.add(rL , JLayeredPane.DRAG_LAYER);
        layeredPane.add(mukT, JLayeredPane.DRAG_LAYER);
        layeredPane.add(mukL , JLayeredPane.DRAG_LAYER);
        layeredPane.add(musT, JLayeredPane.DRAG_LAYER);
        layeredPane.add(musL , JLayeredPane.DRAG_LAYER);
        layeredPane.add(muksT, JLayeredPane.DRAG_LAYER);
        layeredPane.add(muksL , JLayeredPane.DRAG_LAYER);
        layeredPane.add(mussT, JLayeredPane.DRAG_LAYER);
        layeredPane.add(mussL , JLayeredPane.DRAG_LAYER);
        layeredPane.add(heightFT, JLayeredPane.DRAG_LAYER);
        layeredPane.add(heightFL , JLayeredPane.DRAG_LAYER);
        layeredPane.add(sandPitsNumT, JLayeredPane.DRAG_LAYER);
        layeredPane.add(sandPitsNumL , JLayeredPane.DRAG_LAYER);
        layeredPane.add(treesNumT, JLayeredPane.DRAG_LAYER);
        layeredPane.add(treesNumL , JLayeredPane.DRAG_LAYER);

        //Setting up the frame
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Input screen");
        frame.setResizable(false);
        frame.setLayout(null);
        frame.add(layeredPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Set up action listeners for the radio buttons
     */
    public void setUpButtonListeners() {
        ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Object checkButton = ae.getSource();
                if (checkButton == user) {
                    Input.USER = User.USER;
                }
                else if (checkButton == basicBot) {
                    Input.USER = User.BASIC_BOT;
                }
                else if (checkButton == advancedBot) {
                    Input.USER = User.ADVANCED_BOT;
                }
            }
        };

        //Adding action listeners to the buttons.
        basicBot.addActionListener(buttonListener);
        basicBot.setFocusable(false);
        user.addActionListener(buttonListener);
        advancedBot.addActionListener(buttonListener);
        advancedBot.setFocusable(false);
    }

    @Override
    public void dispose() {
        this.frame.dispose();
        super.dispose();
    }

    /**
     * Run the game after the Play button is pressed and save the inputs for backEnd's use
     */
    private void runTheGame() {
        System.out.println(ANSI.GREEN+"Launching game..."+ANSI.RESET+" (this may take a few seconds)");

        // parse inputs
        Input.V0 = new Vector2(Float.parseFloat(x0T.getText()), Float.parseFloat(y0T.getText()));
        Input.VT = new Vector2(Float.parseFloat(xtT.getText()), Float.parseFloat(ytT.getText()));
        Input.R = Float.parseFloat(rT.getText());
        Input.MUK = Float.parseFloat(mukT.getText());
        Input.MUS = Float.parseFloat(musT.getText());
        Input.MUKS = Float.parseFloat(muksT.getText());
        Input.MUSS = Float.parseFloat(mussT.getText());
        Input.SAND = Integer.parseInt(sandPitsNumT.getText());
        Input.TREES = Integer.parseInt(treesNumT.getText());

        // parse height function
        String heightFunction = "";
        for (int i = 0; i < heightFT.getText().length(); i++) {
            if (heightFT.getText().charAt(i) != ' ') {
                heightFunction += heightFT.getText().charAt(i);
            }
        }
        Input.H = heightFunction;

        long start = System.currentTimeMillis();
		FloodFill.fillGraphTable();
		FloodFill.floodFill((int)FloodFill.flood_i,(int)FloodFill.flood_j);
		long end = System.currentTimeMillis();
		System.out.println("runtime: " + (end-start));
		System.out.println(Arrays.deepToString(matrixParcour));
        
        this.status = true;
        this.dispose();
    }
}