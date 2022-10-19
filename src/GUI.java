import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI {
    public JPanel getPanel() {
        return panel;
    }

    private JPanel panel;

    public Point getThisLocation() {
        return thisLocation;
    }

    private Point thisLocation;

    public GUI() {
    }
}
