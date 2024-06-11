import javax.swing.*;
import java.awt.*;

public class AnimationPanel extends JPanel {
    private ImageIcon animationIcon;

    public AnimationPanel(String gifPath) {
        animationIcon = new ImageIcon(gifPath);
        setPreferredSize(new Dimension(animationIcon.getIconWidth(), animationIcon.getIconHeight()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        animationIcon.paintIcon(this, g, 0, 0);
    }
}
