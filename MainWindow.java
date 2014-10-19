import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class MainWindow extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JButton deleteAllButton;
    private JScrollPane surveyViewer;
    private Font textFont;
    private Font titleFont;
    private int recordNo;
    private DefaultListModel<String> surveyData;
    private JList<String> dataList;
    private ArrayList<AddSurveyWindow> previousRecords;
    private Container contentPane;
    private String formattedData;

    public MainWindow() {
        super("Survey on Social Media");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(450, 350);
        this.setResizable(false);
        this.recordNo = 1;
        this.previousRecords = new ArrayList<>();
        this.textFont = new Font("Perpetua", Font.PLAIN, 15);
        this.titleFont = new Font("Perpetua", Font.BOLD, 15);
        this.contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        this.createLabels();

        this.createSurveyViewer();
    }

    private void createLabels() {
        JPanel labels = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        this.addLabel(labels, "Record No.");
        this.addLabel(labels, "Zip Code");
        this.addLabel(labels, "Social Media");
        this.addLabel(labels, "Age Group");
        this.addLabel(labels, "Avg Time");
        labels.setMaximumSize(labels.getPreferredSize());
        contentPane.add(labels);
    }

    private void addLabel(JPanel labels, String title) {
        JLabel label = new JLabel(title);
        label.setForeground(Color.BLUE);
        label.setFont(this.titleFont);
        labels.add(label);
    }

    private void createSurveyViewer() {
        this.surveyData = new DefaultListModel<>();
        this.dataList = new JList<>(this.surveyData);
        this.dataList.setFont(new Font("Consolas", Font.PLAIN, 12));
        this.surveyViewer = new JScrollPane(this.dataList);
        this.contentPane.add(surveyViewer);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        this.addButton = new JButton("Add");
        this.createButton(this.addButton, buttons);

        this.modifyButton = new JButton("Modify");
        this.createButton(this.modifyButton, buttons);

        this.deleteButton = new JButton("Delete");
        this.createButton(this.deleteButton, buttons);

        this.deleteAllButton = new JButton("Delete All");
        this.createButton(this.deleteAllButton, buttons);

        contentPane.add(buttons);
    }

    private void createButton(JButton button, JPanel panel) {
        button.addActionListener(this);
        button.setFont(this.textFont);
        panel.add(button);
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

        else {
            this.deleteAllButtonClicked();
        }
    }

    private void addButtonClicked() {
        AddSurveyWindow surveyWindow = new AddSurveyWindow(this, this.recordNo);
        surveyWindow.setVisible(true);
        if (surveyWindow.isSubmitPressed()) {
            this.formattedData = surveyWindow.getDataEntered();
            this.surveyData.addElement(this.formattedData);
            this.previousRecords.add(surveyWindow);
            this.recordNo++;
        }
    }

    private AddSurveyWindow getSelectedSurvey(String selectedSurvey) {
        //System.out.println(selectedSurvey);
        for (AddSurveyWindow survey : this.previousRecords) {
            //System.out.println("current " + survey.getDataEntered());
            if (survey.getDataEntered().equals(selectedSurvey)) {
                //System.out.println(survey.getDataEntered());
                return survey;
            }
        }
        return null;
    }

    private void modifyButtonClicked() {
        String selectedSurvey = this.dataList.getSelectedValue();
        AddSurveyWindow surveyWindow = this.getSelectedSurvey(selectedSurvey);
        //System.out.println(surveyWindow);
        if (surveyWindow == null) {
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
            return;
        }
        int removeIndex = this.dataList.getSelectedIndex();
        this.surveyData.removeElement(selectedSurvey);
        this.previousRecords.remove(selectedSurveyWindow);
        this.recordNo--;
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

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
        window.setVisible(true);
    }
}

