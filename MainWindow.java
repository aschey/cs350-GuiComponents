/**
 * CS 350
 * Project 5
 * Austin Schey
 * MainWindow.java- creates a JFrame to display the initial window
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class MainWindow extends JFrame implements ActionListener {

    private static final long serialVersionUID = 6906069523004353622L;
    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JButton saveButton;
    private JButton openButton;
    private JScrollPane surveyViewer;
    private Font textFont;
    private Font titleFont;
    private int recordNo;
    private DefaultListModel<String> surveyData;
    private JList<String> dataList;
    private ArrayList<AddSurveyWindow> previousRecords;
    private Container contentPane;
    private String formattedData;
    private JFileChooser chooseFile;


    public MainWindow(String filename) {
        super("Survey on Social Media");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(450, 350);
        this.setLocation(400, 250);
        this.setResizable(false);
        this.recordNo = 1;
        this.chooseFile = new JFileChooser();
        this.chooseFile.setVisible(false);
        FileNameExtensionFilter saveFilter = new FileNameExtensionFilter("DAT files", "dat");
        this.chooseFile.setFileFilter(saveFilter);
        this.previousRecords = new ArrayList<>();
        this.surveyData = new DefaultListModel<>();
        this.textFont = new Font("Perpetua", Font.PLAIN, 15);
        this.titleFont = new Font("Perpetua", Font.BOLD, 15);
        this.contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        this.createLabels();

        this.createSurveyViewer();

        this.createButtons();

        this.createFileButtons();

        if (filename != null) {
            this.openData(filename);
        }

        // prompt the user to save data on close
        final MainWindow frame = this;
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                int choice = JOptionPane.showConfirmDialog(frame, "Save data before closing?");
                if (choice == JOptionPane.YES_OPTION) {
                    frame.saveButtonClicked();
                }
                if (choice != JOptionPane.CANCEL_OPTION) {
                    frame.dispose();
                }
            }
        });
    }

    private void createLabels() {
        JPanel labels = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        this.addLabel(labels, "Record No.");
        this.addLabel(labels, "Zip Code");
        this.addLabel(labels, "Social Media");
        this.addLabel(labels, "Age Group");
        this.addLabel(labels, "Avg Time");
        // prevent panel from expanding vertically
        labels.setMaximumSize(labels.getPreferredSize());
        this.contentPane.add(labels);
    }

    private void addLabel(JPanel labels, String title) {
        JLabel label = new JLabel(title);
        label.setForeground(Color.BLUE);
        label.setFont(this.titleFont);
        labels.add(label);
    }

    private void createSurveyViewer() {
        this.dataList = new JList<>(this.surveyData);
        this.dataList.setFont(new Font("Consolas", Font.PLAIN, 12));
        this.surveyViewer = new JScrollPane(this.dataList);
        this.contentPane.add(this.surveyViewer);
    }

    private void createButtons() {
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        this.addButton = new JButton("Add");
        this.createButton(this.addButton, buttons);

        this.modifyButton = new JButton("Modify");
        this.createButton(this.modifyButton, buttons);

        this.deleteButton = new JButton("Delete");
        this.createButton(this.deleteButton, buttons);

        JButton deleteAllButton = new JButton("Delete All");
        this.createButton(deleteAllButton, buttons);

        this.contentPane.add(buttons);
    }

    private void createFileButtons() {
        JPanel fileButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        this.openButton = new JButton("Open");
        this.createButton(this.openButton, fileButtons);

        this.saveButton = new JButton("Save");
        this.createButton(this.saveButton, fileButtons);

        this.contentPane.add(fileButtons);
    }

    private void createButton(JButton button, JPanel panel) {
        button.addActionListener(this);
        button.setFont(this.textFont);
        panel.add(button);
    }

    private void openData(String filePath) {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(filePath))) {
            DefaultListModel<String> tempSurveyData = (DefaultListModel)input.readObject();
            ArrayList<AddSurveyWindow> tempPrevRecords = (ArrayList<AddSurveyWindow>)input.readObject();
            // Clear old data and add the new
            this.surveyData.clear();
            this.previousRecords.clear();
            int i;
            for (i = 0; i < tempSurveyData.size(); i++) {
                this.surveyData.addElement(tempSurveyData.get(i));
                this.previousRecords.add(tempPrevRecords.get(i));
            }
            // update the record number to reflect the added records, if there are any
            if (i > 0) {
                this.recordNo = this.previousRecords.get(i - 1).getRecordNo() + 1;
            }
            else {
                this.recordNo = 1;
            }
        }
        catch (IOException | ClassNotFoundException | ClassCastException ex) {
            JOptionPane.showMessageDialog(this, "Error: Invalid or corrupt file");
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.addButton) {
            this.addButtonClicked();
        }

        else if (e.getSource() == this.modifyButton) {
            this.modifyButtonClicked();
        }

        else if (e.getSource() == this.deleteButton) {
            this.deleteButtonClicked();
        }

        else if (e.getSource() == this.saveButton) {
            this.saveButtonClicked();
        }

        else if (e.getSource() == this.openButton) {
            this.openButtonClicked();
        }

        else {
            this.deleteAllButtonClicked();
        }
    }

    private void addButtonClicked() {
        AddSurveyWindow surveyWindow = new AddSurveyWindow(this, this.recordNo);
        surveyWindow.setVisible(true);
        if (surveyWindow.isSubmitPressed()) {
            this.formattedData = surveyWindow.getDataEntered();
            // save the window in case the modify button is pressed
            this.surveyData.addElement(this.formattedData);
            this.previousRecords.add(surveyWindow);
            this.recordNo++;
        }
    }

    private AddSurveyWindow getSelectedSurvey(String selectedSurvey) {
        for (AddSurveyWindow survey : this.previousRecords) {
            if (survey.getDataEntered().equals(selectedSurvey)) {
                return survey;
            }
        }
        return null;
    }

    private void modifyButtonClicked() {
        String selectedSurvey = this.dataList.getSelectedValue();
        AddSurveyWindow surveyWindow = this.getSelectedSurvey(selectedSurvey);
        if (surveyWindow == null) {
            JOptionPane.showMessageDialog(this, "Nothing selected");
            return;
        }
        surveyWindow.setVisible(true);
        if (surveyWindow.isSubmitPressed()) {
            this.formattedData = surveyWindow.getDataEntered();
            this.surveyData.setElementAt(this.formattedData, this.dataList.getSelectedIndex());
        }
    }

    private void deleteButtonClicked() {
        String selectedSurvey = this.dataList.getSelectedValue();
        AddSurveyWindow selectedSurveyWindow = this.getSelectedSurvey(selectedSurvey);
        if (selectedSurveyWindow == null) {
            JOptionPane.showMessageDialog(this, "Nothing selected");
            return;
        }
        int removeIndex = this.dataList.getSelectedIndex();
        this.surveyData.removeElement(selectedSurvey);
        this.previousRecords.remove(selectedSurveyWindow);
        this.recordNo--;
        // adjust the record numbers of all the previous records
        for (int i = removeIndex; i < this.previousRecords.size(); i++) {
            AddSurveyWindow previousWindow = this.previousRecords.get(i);
            previousWindow.setRecordNo(i+1);
            this.surveyData.set(i, previousWindow.getDataEntered());
        }
        this.surveyViewer.repaint();
    }

    private void deleteAllButtonClicked() {
        this.surveyData.removeAllElements();
        this.previousRecords.clear();
        this.recordNo = 1;
    }

    private void saveButtonClicked() {
        this.chooseFile.setVisible(true);
        int choice = this.chooseFile.showSaveDialog(this);
        if (choice == JFileChooser.CANCEL_OPTION) {
            return;
        }
        String path = this.chooseFile.getSelectedFile().getAbsolutePath();
        String filename = this.chooseFile.getSelectedFile().getName();
        if (!filename.endsWith(".dat")) {
            path = path.concat(".dat");
        }
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(path))) {
            output.writeObject(this.surveyData);
            output.writeObject(this.previousRecords);
        }
        catch (IOException ex) {
            // Some error occurred when writing the data
            JOptionPane.showMessageDialog(this, "Error writing data to file");
            ex.printStackTrace();
        }
    }

    private void openButtonClicked() {
        this.chooseFile.setVisible(true);
        int choice = this.chooseFile.showOpenDialog(this);
        if (choice == JFileChooser.CANCEL_OPTION) {
            return;
        }
        this.openData(this.chooseFile.getSelectedFile().getAbsolutePath());
    }

    public static void main(String[] args) {
        String filename = args.length == 1 ? args[0] : null;
        MainWindow window = new MainWindow(filename);
        window.setVisible(true);
    }
}

