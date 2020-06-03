package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame {
    private MainPanel mPanel = new MainPanel(this);
    private JFrame thisFrame=this;

    public MainWindow (){
        super.setSize(1024,768);
        super.setLocationRelativeTo(null);
        super.setTitle("NoteSpace");
        super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        add(mPanel);
        addWindowListener(new WindowActions());

        super.setVisible(true);
    }

    private class WindowActions extends WindowAdapter {
        int option;
        @Override
        public void windowClosing(WindowEvent e) {
            System.out.println("entro");
            if (!mPanel.isSaveState()){
                option=JOptionPane.showConfirmDialog(thisFrame,"All non-saved changes will not be saved, exit?");
                if (option==JOptionPane.YES_OPTION){
                    dispose();
                }
            }else {
                dispose();
            }
        }
    }

}
