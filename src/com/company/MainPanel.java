package com.company;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    private JTextArea mainArea=new JTextArea();
    private JScrollPane scrollTextArea=new JScrollPane (mainArea);
    private ToolsBar mainToolsBar =new ToolsBar();

    public MainPanel (){
        mainArea.setLineWrap(true);
        layoutConfig();
    }

    private void layoutConfig (){
        super.setLayout(new BorderLayout());
        add (mainToolsBar,BorderLayout.NORTH);
        add (scrollTextArea,BorderLayout.CENTER);
    }

    private class ToolsBar extends JPanel{
        private JButton saveButton =new JButton("Save to .txt");
        private JButton clearButton = new JButton("Clear All");
        ToolsBar (){
            add (saveButton);
            add(clearButton);
        }
    }
}

