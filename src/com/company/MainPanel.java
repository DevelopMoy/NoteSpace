package com.company;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MainPanel extends JPanel {
    private Component thisComp =this;
    private JTextArea mainArea=new JTextArea();
    private JScrollPane scrollTextArea=new JScrollPane (mainArea);
    private ToolsBar mainToolsBar =new ToolsBar();
    private boolean saveState=false;

    public MainPanel (){
        mainArea.setLineWrap(true);
        mainArea.getDocument().addDocumentListener(new DocumentList());
        layoutConfig();
    }

    private void layoutConfig (){
        super.setLayout(new BorderLayout());
        add (mainToolsBar,BorderLayout.NORTH);
        add (scrollTextArea,BorderLayout.CENTER);
    }

    private class DocumentList implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent documentEvent) {
            saveState=false;
        }

        @Override
        public void removeUpdate(DocumentEvent documentEvent) {
            saveState=false;
        }

        @Override
        public void changedUpdate(DocumentEvent documentEvent) {
            saveState=false;
        }
    }

    private class ToolsBar extends JPanel{
        private JButton saveButton =new JButton("Save to .txt");
        private JButton clearButton = new JButton("Clear All");
        private JButton openButton = new JButton("Open");

        ToolsBar (){
            add (openButton);
            add (saveButton);
            add(clearButton);
            addFunctionToButtons();
        }

        private void saveFunction (){
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
                    saveState=true;
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

        private void openFunction (){

            JFileChooser choose=new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT FILE","txt");
            choose.setFileFilter(filter);
            int returnVal =choose.showOpenDialog(thisComp);
            if (returnVal==JFileChooser.APPROVE_OPTION){
                String data;
                String path=choose.getCurrentDirectory()+"/"+choose.getSelectedFile().getName();
                try  {
                    FileReader reader =new FileReader(new File(choose.getCurrentDirectory()+"/"+choose.getSelectedFile().getName()));
                    BufferedReader buffReader = new BufferedReader(reader);
                    do{
                        data=buffReader.readLine();
                        if (data!=null){
                            mainArea.append(data);
                        }
                    }while (data!=null);
                }catch (NullPointerException e){
                    System.out.println(e.getMessage());
                }catch (FileNotFoundException e){
                    JOptionPane.showMessageDialog(thisComp,"ERROR, FILE NOT FOUND "+e.getMessage());
                }catch (IOException e){
                    JOptionPane.showMessageDialog(thisComp,"ERROR, INPUT / OUTPUT ERROR "+e.getMessage());
                }regat
            }
        }

        private void addFunctionToButtons(){
            clearButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    mainArea.setText("");
                }
            });

            openButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    int op;
                    boolean enter=true;
                    if ((!(mainArea.getText().compareTo("")==0))&&saveState==false){
                        op=JOptionPane.showConfirmDialog(thisComp,"DO YOU WANT TO SAVE CHANGES FOR THIS DOCUMENT?");
                        if (op==JOptionPane.YES_OPTION){
                            System.out.println("yes");
                            saveFunction();
                        }else {
                            if (op==JOptionPane.NO_OPTION){
                                System.out.println("NO");
                                mainArea.setText("");
                            }else {
                                if (op==JOptionPane.CANCEL_OPTION){
                                    System.out.println("CANCEL");
                                    enter=false;
                                }
                            }
                        }
                    }
                    if (enter){
                        openFunction();
                    }
                }
            });


            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    saveFunction();
                }
            });
        }
    }
}

