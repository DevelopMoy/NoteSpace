package com.company;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class MainPanel extends JPanel {
    private int fontSize =16;
    private int fontType=Font.BOLD;
    private Font currentFont=new Font("arial",fontType,fontSize);
    private Component thisComp =this;
    private JTextPane mainArea=new JTextPane();
    private JScrollPane scrollTextArea=new JScrollPane (mainArea);
    private EditTextPanel mainEditPanel =new EditTextPanel();
    private JMenuBar mainMenuBar=new JMenuBar();
    private boolean saveState=false;
    private String thisFileName="";
    private String thisFilePath="";
    private boolean wasSavedForFirstTime=false; // TO SEE IF A FILE WAS SAVED AND THE PATH IS KNOWN IN ORDER TO USE THE SAVE FUNCTION
    private ToolsBar mainToolsBar =new ToolsBar();
    private Component superWindow;

    public boolean isSaveState() {
        return saveState;
    }

    public MainPanel (Component mainWindow){
        superWindow=mainWindow;
        mainArea.setFont(currentFont);
        mainArea.getDocument().addDocumentListener(new DocumentList());
        layoutConfig();
    }

    public void updateFont (){
        currentFont=new Font(mainArea.getFont().getName(),mainArea.getFont().getStyle(),fontSize);
        mainArea.setFont(currentFont);
    }

    private void layoutConfig (){
        super.setLayout(new BorderLayout());
        add (mainMenuBar,BorderLayout.NORTH);
        add (scrollTextArea,BorderLayout.CENTER);
        add (mainEditPanel,BorderLayout.SOUTH);
    }

    private class EditTextPanel extends JPanel {
        private String [] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        private JComboBox selectFont=new JComboBox(fonts);
        private SpinnerNumberModel numerModel = new SpinnerNumberModel(fontSize,4,72,4);
        private JSpinner fontSizeSpinner = new JSpinner(numerModel);

        public EditTextPanel (){
            selectFont.addActionListener(new EditFontAction());
            add(new JLabel("SELECT FONT: "));
            add(selectFont);
            add(new JLabel("SIZE: "));
            add(fontSizeSpinner);
            fontSizeSpinner.addChangeListener(new ChangeListener(){
                @Override
                public void stateChanged(ChangeEvent changeEvent) {
                    fontSize=(Integer) fontSizeSpinner.getValue();
                    updateFont();
                }
            });
        }

        private class EditFontAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                currentFont=new Font((String)selectFont.getSelectedItem(),fontType,fontSize);
                mainArea.setFont(currentFont);
            }
        }

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

    private void updateTittle (String t){
        JFrame wind=(JFrame)superWindow;
        wind.setTitle("NoteSpace - "+t);
    }

    private class ToolsBar {
        private JMenu fileMenu=new JMenu("File"); ;
        private JMenu editMenu=new JMenu("Edit");;
        private JMenu configMenu=new JMenu("Settings");
        private JMenuItem [] fileOptions = {new JMenuItem("New File"),new JMenuItem("Open"),new JMenuItem("Save"), new JMenuItem("Save as")};
        private JMenuItem [] editOptions = {new JMenuItem("Clear Text Area"),new JMenuItem("Copy"),new JMenuItem("Paste")};
        private JMenuItem credits = new JMenuItem("About");
        ToolsBar (){
            mainMenuBar.add(fileMenu);
            mainMenuBar.add(editMenu);
            mainMenuBar.add(configMenu);
            for (JMenuItem e:fileOptions){
                fileMenu.add(e);
            }
            for (JMenuItem e:editOptions){
                editMenu.add(e);
            }
            configMenu.add(credits);
            addActions ();
        }

        public void addActions (){
            fileOptions[0].addActionListener(new CreateNewAction());
            fileOptions[1].addActionListener(new OpenAction());
            fileOptions[2].addActionListener(new SaveAction());
            fileOptions[3].addActionListener(new SaveAsAction());
            editOptions[0].addActionListener(new ClearAllAction());
            editOptions[1].addActionListener(new DefaultEditorKit.CopyAction());
            editOptions[2].addActionListener(new DefaultEditorKit.PasteAction());
            credits.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    JOptionPane.showMessageDialog(thisComp,"DEVELOPED BY: MOISES ALVAREZ USING JAVA AND SWING LIBRARY","About",JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }

        private class CreateNewAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                boolean doIt=true;
                if ((!(mainArea.getText().compareTo("")==0))&&saveState==false) {
                    int op=JOptionPane.showConfirmDialog(thisComp,"ALL NON-SAVED CHANGES WILL BE LOST, DO YOU WANT TO CONTINUE?");
                    if (op==JOptionPane.NO_OPTION||op==JOptionPane.CANCEL_OPTION){
                        doIt=false;
                    }
                }
                if (doIt){
                    thisFilePath="";
                    thisFileName="";
                    mainArea.setText("");
                    saveState=true;
                    wasSavedForFirstTime=false;
                    updateTittle("");
                }
            }
        }

        private class SaveAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveFunction();
            }
        }

        private class SaveAsAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveAsFunction();
            }
        }

        private class OpenAction implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int op;
                boolean enter=true;
                if ((!(mainArea.getText().compareTo("")==0))&&saveState==false){
                    op=JOptionPane.showConfirmDialog(thisComp,"DO YOU WANT TO SAVE CHANGES FOR THIS DOCUMENT?");
                    if (op==JOptionPane.YES_OPTION){
                        if (wasSavedForFirstTime){
                            saveFunction();
                        }else {
                            saveAsFunction();
                        }
                    }else {
                        if (op==JOptionPane.NO_OPTION){
                            mainArea.setText("");
                        }else {
                            if (op==JOptionPane.CANCEL_OPTION){
                                enter=false;
                            }
                        }
                    }
                }
                if (enter){
                    openFunction();
                }
            }
        }

        private class ClearAllAction implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                mainArea.setText("");
            }
        }

        private void saveFunction (){
            FileWriter fw;
            File writteFile;
            BufferedWriter bw;
            if (wasSavedForFirstTime){
                try {
                    writteFile=new File(thisFilePath);
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
                }catch (HeadlessException e){
                    System.out.println("INPUT/OUTPUT ERROR: "+e.getMessage());
                }catch (NullPointerException e){
                    System.out.println("NULL POINTER ERROR"+e.getMessage());
                }catch (Exception e){
                    System.out.println("I/O ERROR "+e.getMessage());
                }
            }else {
                saveAsFunction();
            }
        }

        private void saveAsFunction (){
            JFileChooser select=new JFileChooser();
            FileWriter fw;
            File writteFile;
            BufferedWriter bw;
            try {
                int returnValue = select.showSaveDialog(thisComp);
                if (returnValue==JFileChooser.APPROVE_OPTION){
                    writteFile=new File(select.getCurrentDirectory()+"/"+select.getSelectedFile().getName());
                    thisFileName=select.getSelectedFile().getName();
                    thisFilePath=select.getCurrentDirectory()+"/"+select.getSelectedFile().getName();
                    wasSavedForFirstTime=true;
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
            updateTittle(thisFileName);
        }

        private void openFunction (){
            JFileChooser choose=new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("txt","txt");
            choose.setFileFilter(filter);
            int returnVal =choose.showOpenDialog(thisComp);
            if (returnVal==JFileChooser.APPROVE_OPTION){
                mainArea.setText("");
                String data;
                StringBuilder dataFinal= new StringBuilder();
                String path=choose.getCurrentDirectory()+"/"+choose.getSelectedFile().getName();
                thisFileName=choose.getSelectedFile().getName();
                thisFilePath=path;
                wasSavedForFirstTime=true;
                saveState=true;
                try  {
                    FileReader reader =new FileReader(new File(choose.getCurrentDirectory()+"/"+choose.getSelectedFile().getName()));
                    BufferedReader buffReader = new BufferedReader(reader);
                    do{
                        data=buffReader.readLine();
                        if (data!=null){
                            dataFinal.append(data);
                        }
                    }while (data!=null);
                    mainArea.setText(dataFinal.toString());
                }catch (NullPointerException e){
                    System.out.println(e.getMessage());
                }catch (FileNotFoundException e){
                    JOptionPane.showMessageDialog(thisComp,"ERROR, FILE NOT FOUND "+e.getMessage());
                }catch (IOException e){
                    JOptionPane.showMessageDialog(thisComp,"ERROR, INPUT / OUTPUT ERROR "+e.getMessage());
                }
            }
            updateTittle(thisFileName);
        }
    }
}
