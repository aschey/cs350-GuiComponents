import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class CSample {
    public String recordNo;
    public String zipCode;
    public String socialMedia;
    public String ageGroup;
    public String avgTime;

    public CSample(int recordNumber, JTextField zip, JCheckBox[] mediaCheckBoxes,
                   JRadioButton[] ages, JRadioButton[] time) {
        this.recordNo = String.format("%08d", recordNumber);
        this.zipCode = zip.getText();
        this.socialMedia = this.getSocialMedia(mediaCheckBoxes);
        this.ageGroup = this.getAgeGroup(ages);
        this.avgTime = this.getAvgTime(time);
    }

    private String getChosenButton(JRadioButton[] buttons) {
        for (JRadioButton button : buttons) {
            if(button.isSelected()) {
                return button.getText();
            }
        }
        return "";
    }

    private String getAgeGroup(JRadioButton[] ages) {
        String ageText = this.getChosenButton(ages);
        String ageToDisplay;
        switch(ageText) {
            case "19 or less":
                ageToDisplay = "  -19";
                break;

            case "50 and up":
                ageToDisplay = "50-  ";
                break;

            default:
                ageToDisplay = ageText.replace(" ", "");
        }
        return ageToDisplay;
    }

    private String getAvgTime(JRadioButton[] times) {
        String timeText = this.getChosenButton(times);
        int start = timeText.length() - 2;
        int end = start + 1;
        if (start >= 0) {
            return timeText.substring(start, end);
        }
        else {
            return "";
        }
    }

    private String getSocialMedia(JCheckBox[] mediaCheckBoxes) {
        String chosenMedia = "";
        for (JCheckBox checkBox : mediaCheckBoxes) {
            if (checkBox.isSelected()) {
                chosenMedia += (checkBox.getText().substring(0, 1));
            }
            else {
                chosenMedia += "-";
            }
        }
        return chosenMedia;
    }

    @Override
    public String toString() {
        return String.format("  %-13s %-11s %s %13s %9s", this.recordNo, this.zipCode,
                this.socialMedia, this.ageGroup, this.avgTime);
    }

    public void setRecordNo(int recordNo) {
        this.recordNo = String.format("%08d", recordNo);
    }
}
