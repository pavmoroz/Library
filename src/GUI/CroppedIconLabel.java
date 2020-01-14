package GUI;

import javax.swing.*;
import java.awt.*;

public class CroppedIconLabel extends JLabel {

    public CroppedIconLabel(ImageIcon imageIcon, int width, int height) {
        Image tmpImg = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        setIcon(new ImageIcon(tmpImg));
    }
}
