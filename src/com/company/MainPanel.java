package com.company;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainPanel extends JPanel {
    private Component thisComp =this;
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
            addFunctionToButtons();
        }

        private void addFunctionToButtons(){
            clearButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    mainArea.setText("");
                }
            });

            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    JFileChooser select=new JFileChooser();
                    FileWriter fw;
                    File writteFile;
                    BufferedWriter bw;
                    try {
                        int returnValue = select.showSaveDialog(thisComp);
                        if (returnValue==JFileChooser.APPROVE_OPTION){
                            System.out.println("YOU SELECTED: "+select.getCurrentDirectory().toString()+" FILE NAME: "+select.getSelectedFile().getName());
                            writteFile=new File(select.getCurrentDirectory()+"/"+select.getSelectedFile().getName());
                            if (!writteFile.exists()){
                                writteFile.createNewFile();
                            }
                            fw=new FileWriter(writteFile);
                            bw=new BufferedWriter(fw);
                            bw.write(mainArea.getText());
                            if (bw!=null){
                                bw.close();
                            }
                        }
                    }catch (HeadlessException e){
                        System.out.println("INPUT/OUTPUT ERROR: "+e.getMessage());
                    }catch (NullPointerException e){
                        System.out.println("NULL POINTER ERROR"+e.getMessage());
                    }catch (Exception e){
                        System.out.println("I/O ERROR "+e.getMessage());
                    }

                }
            });
        }
    }
}

