package com.project_1_2.group_16.titlescreen;

import javax.swing.*;

import com.project_1_2.group_16.App;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TitleScreen {

    JFrame frame;
    JLabel picLabel;
    JButton buttonY;
    JButton buttonQ;
    JLabel labelA;
    JLayeredPane layeredPane;
    public InputScreen inputScreen;

    static int width = 960;
    static int height = 640;

    /**
     * Set up the main menu's GUI
     */
    public void setUpGUI() {

        //defining fonts
        Font large = new Font("Serif",Font.BOLD,26);
        Font medium = new Font("Serif",Font.PLAIN,15);

        Color backGroundColor = new Color(59,87,72,255);
        //Color fontColor = new Color(255,255,255,255);

        picLabel = new JLabel(new ImageIcon(App.os_is_windows ? "./assets/golfTheme.png" : "../assets/golfTheme.png"));
        picLabel.setBounds(0, 0, width, height);

        buttonY = new JButton("Start");
        buttonY.setFont(medium);
        buttonY.setBackground(Color.decode("#676d88"));
        buttonY.setForeground(Color.decode("#03d8f6"));
        buttonY.setBounds(100, 270, 80, 30);

        buttonQ = new JButton("Quit");
        buttonQ.setFont(medium);
        buttonQ.setBackground(Color.decode("#676d88"));
        buttonQ.setForeground(Color.decode("#03d8f6"));
        buttonQ.setBounds(210, 270, 90, 30);
        buttonQ.setFocusable(false);

        labelA = new JLabel("Welcome");
        labelA.setFont(large);
        labelA.setBackground(Color.decode("#676d88"));
        labelA.setForeground(Color.decode("#03d8f6"));
        labelA.setBounds(100, 100, 240, 40);

        layeredPane = new JLayeredPane();
        layeredPane.setBackground(backGroundColor);
        layeredPane.setBounds(0, 0, width, height);
        layeredPane.add(picLabel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(buttonY, JLayeredPane.DRAG_LAYER);
        layeredPane.add(buttonQ, JLayeredPane.DRAG_LAYER);
        layeredPane.add(labelA, JLayeredPane.DRAG_LAYER);

        frame = new JFrame();
        frame.setSize(width, height);
        frame.setTitle("The G-Boys");
        frame.setResizable(false);
        frame.add(layeredPane);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void setUpButtonListeners() {
        ActionListener buttonListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                Object checkButton = ae.getSource();

                if (checkButton == buttonY) {
                    inputScreen = new InputScreen();
                    inputScreen.setUpGUI();
                    frame.dispose();
                }

                else if (checkButton == buttonQ )
                {
                   frame.dispose();
                   System.exit(0);
                }
            }
        };

        buttonY.addActionListener(buttonListener);
        buttonY.setFocusable(false);
        buttonQ.addActionListener(buttonListener);
        buttonQ.setFocusable(false);
    }
}
