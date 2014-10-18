import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
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
    private DefaultListModel<String> surveyData;
    private JList<String> dataList;
    private ArrayList<AddSurveyWindow> previousRecords;
    private Container contentPane;
    private int recordNo;
    private String formattedData;

    public MainWindow() {
        super("Survey on Social Media");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 400);
        this.setResizable(false);
        this.recordNo = 1;
        this.previousRecords = new ArrayList<AddSurveyWindow>();
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
        label.setFont(new Font("Perpetua", Font.PLAIN, 15));
        labels.add(label);
    }

    private void createSurveyViewer() {
        this.surveyData = new DefaultListModel<String>();
        this.dataList = new JList<String>(this.surveyData);
        this.dataList.setFont(new Font("Consolas", Font.PLAIN, 12));
        JScrollPane surveyViewer = new JScrollPane(this.dataList);
        this.dataList.setLayout(new FlowLayout(FlowLayout.RIGHT));
        contentPane.add(surveyViewer);
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
        button.setFont(new Font("Perpetua", Font.PLAIN, 15));
        panel.add(button);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.addButton) {
            AddSurveyWindow surveyWindow = new AddSurveyWindow(this, this.recordNo++);
            surveyWindow.setVisible(true);
            if (surveyWindow.isSubmitPressed()) {
                this.formattedData = surveyWindow.getDataEntered();
                this.surveyData.addElement(this.formattedData);
                this.previousRecords.add(surveyWindow);
            }
        }

        else if (e.getSource() == this.modifyButton) {
            String selectedSurvey = this.dataList.getSelectedValue();
            AddSurveyWindow surveyWindow = this.getSelectedSurvey(selectedSurvey);
            if (surveyWindow == null) {
                return;
            }
            surveyWindow.setVisible(true);
            if (surveyWindow.isSubmitPressed()) {
                this.formattedData = surveyWindow.getDataEntered();
                this.surveyData.setElementAt(this.formattedData, this.surveyData.size()-1);
            }
        }

        else if (e.getSource() == this.deleteButton) {
            String selectedSurvey = this.dataList.getSelectedValue();
            AddSurveyWindow surveyWindow = this.getSelectedSurvey(selectedSurvey);
            if (surveyWindow == null) {
                return;
            }
            this.surveyData.removeElement(selectedSurvey);
            this.previousRecords.remove(surveyWindow);
        }

        else {
            this.surveyData.removeAllElements();
            this.previousRecords.clear();
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

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
        window.setVisible(true);
    }
}

