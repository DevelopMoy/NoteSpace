package com.company;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private MainPanel mPanel = new MainPanel();

    public MainWindow (){
        super.setSize(1024,768);
        super.setLocationRelativeTo(null);
        super.setTitle("NoteSpace");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(mPanel);

        super.setVisible(true);
    }



}
