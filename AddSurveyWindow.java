import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;


public class AddSurveyWindow extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;
    private int recordNo;
    private boolean submitPressed;
    private JButton submit;
    private JTextField zipCodeText;
    private Font textFont;
    private Font titleFont;

    private JCheckBox[] socialMedia = {new JCheckBox("Facebook"), new JCheckBox("Twitter"),
            new JCheckBox("LinkedIn"), new JCheckBox("Pinterest"), new JCheckBox("Others")};

    private JRadioButton[] ages = {new JRadioButton("19 or less"), new JRadioButton("20 - 35"),
            new JRadioButton("36 - 49"), new JRadioButton("50 and up")};

    private JRadioButton[] times = {new JRadioButton("less than 0.5 hours (L)"),
            new JRadioButton("between 0.5 and 1 hour (M)"), new JRadioButton("between 1 and 2 hours (H)"),
            new JRadioButton("longer than 2 hours (X)")};

    private Container contentPane;
    private JPanel socialMediaPanel;
    private JPanel agePanel;
    private JPanel recordNoPanel;
    private CSample dataEntered;

    public AddSurveyWindow(JFrame owner, int recordNo) {
        super(owner, "Add a Survey", true);
        this.setSize(800, 400);
        this.recordNo = recordNo;
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.submitPressed = false;
        this.textFont = new Font("Perpetua", Font.PLAIN, 15);
        this.titleFont = new Font("Perpetua", Font.BOLD, 15);

        this.contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        this.createRecordNoPanel();
        this.createZipCodePanel();
        this.createSocialMediaPanel();
        this.createAgePanel();
        this.createTimePanel();
        this.createButtonPanel();
    }

    private void createZipCodePanel() {
        JPanel zipCodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel zipCode = new JLabel("Zip Code");
        this.createTitle(zipCode, zipCodePanel);

        this.zipCodeText = new JTextField();
        this.zipCodeText.setFont(new Font("Consolas", Font.PLAIN, 12));
        this.zipCodeText.setPreferredSize(new Dimension(100, 25));
        zipCodePanel.add(this.zipCodeText);

        this.contentPane.add(zipCodePanel);
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        this.submit = new JButton("Submit");
        this.createButton(this.submit, buttonPanel);

        JButton cancel = new JButton("Cancel");
        this.createButton(cancel, buttonPanel);

        this.contentPane.add(buttonPanel);
    }

    private void createTimePanel() {
        JPanel timeTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        this.createTitle(new JLabel("How much time do you spend on social media on an average day?"), timeTitlePanel);
        contentPane.add(timeTitlePanel);
        ButtonGroup timeGroup = new ButtonGroup();

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        timePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        for (JRadioButton timeButton : this.times) {
            this.createRadioButton(timeButton, timePanel, timeGroup);
        }
        contentPane.add(timePanel);
    }

    private void createSocialMediaPanel() {
        JPanel mediaTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        this.createTitle(new JLabel("What social media do you use?"), mediaTitlePanel);
        contentPane.add(mediaTitlePanel);

        this.socialMediaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        this.socialMediaPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        for (JCheckBox mediaCheckBox : this.socialMedia) {
            this.createCheckBox(mediaCheckBox, this.socialMediaPanel);
        }
        this.contentPane.add(this.socialMediaPanel);
    }

    private void createAgePanel() {
        ButtonGroup ageButtonGroup = new ButtonGroup();
        JPanel ageTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        this.createTitle(new JLabel("What is your age?"), ageTitlePanel);
        contentPane.add(ageTitlePanel);

        this.agePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        this.agePanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));

        for (JRadioButton ageButton : this.ages) {
            this.createRadioButton(ageButton, this.agePanel, ageButtonGroup);
        }
        this.contentPane.add(agePanel);
    }

    private void createRecordNoPanel() {
        this.recordNoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        this.createTitle(new JLabel("Record No."), this.recordNoPanel);
        this.createTitle(new JLabel(String.format("%08d", this.recordNo)), this.recordNoPanel, Color.RED);
        this.contentPane.add(this.recordNoPanel, 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.submit) {
            this.dataEntered = new CSample(this.recordNo, this.zipCodeText, this.socialMedia,
                    this.ages, this.times);
            this.submitPressed = true;
        }

        this.setVisible(false);
    }

    private void createButton(JButton button, JPanel panel) {
        button.setFont(this.textFont);
        button.addActionListener(this);
        panel.add(button);
    }

    private void createRadioButton(JRadioButton button, JPanel panel, ButtonGroup group) {
        button.setFont(this.textFont);
        group.add(button);
        panel.add(button);
    }

    private void createCheckBox( JCheckBox checkBox, JPanel panel) {
        checkBox.setFont(this.textFont);
        panel.add(checkBox);
    }

    private void createTitle(JLabel label, JPanel panel) {
        label.setFont(this.titleFont);
        label.setForeground(Color.blue);
        panel.add(label);
    }

    private void createTitle(JLabel label, JPanel panel, Color color) {
        label.setFont(this.titleFont);
        label.setForeground(color);
        panel.add(label);
    }

    private void reDrawRecordNoPanel() {
        this.contentPane.remove(this.recordNoPanel);
        this.createRecordNoPanel();
        this.dataEntered.setRecordNo(this.recordNo);
    }

    public void setRecordNo(int recordNo) {
        this.recordNo = recordNo;
        this.reDrawRecordNoPanel();
    }
    public String getDataEntered() {
        return this.dataEntered.toString();
    }

    public boolean isSubmitPressed() {
        return this.submitPressed;
    }
}

