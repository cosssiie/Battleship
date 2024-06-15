import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomMessage {

    private ImageIcon imageIcon;
    private JLabel imageLabel;

    private static JWindow window;

    private static int xFirst;
    private static int yFirst;
    private static int xSec;
    private static int ySec;

    private Runnable onClick;

    public CustomMessage(String path, int xFirst, int yFirst, int xSec, int ySec, Runnable onClick) {
        imageIcon = new ImageIcon(path);
        imageLabel = new JLabel(imageIcon);

        this.xFirst = xFirst;
        this.yFirst = yFirst;
        this.xSec = xSec;
        this.ySec = ySec;
        this.onClick = onClick;

        window = new JWindow();
        window.getContentPane().add(imageLabel);
        window.pack();

        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e, window);
            }
        });

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);

        window.setVisible(true);
    }

    public void showWindow() {
        window.setVisible(true);
    }

    public void closeWindow() {
        window.dispose();
    }

    private void handleMouseClick(MouseEvent e, JWindow window) {
        int x = e.getX();
        int y = e.getY();

        if (x >= xFirst && x <= xSec && y >= yFirst && y <= ySec) {
            window.dispose();
            onClick.run();
        }
    }
}
