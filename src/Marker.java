import java.awt.*;

public class Marker extends Rectangle {

    private final Color HIT_COLOUR = new Color(252, 9, 9, 180);
    private final Color MISS_COLOUR = new Color(6, 58, 222, 180);
    private boolean showMarker;
    private Ship shipAtMarker;

    public Marker(int x, int y, int width, int height) {
        super(x, y, width, height);
        reset();
    }

    public void reset() {
        shipAtMarker = null;
        showMarker = false;
    }

    public void mark() {
        if (!showMarker && isShip()) {
            shipAtMarker.destroySection();
        }
        showMarker = true;
    }

    public boolean isMarked() {
        return showMarker;
    }

    public void setAsShip(Ship ship) {
        this.shipAtMarker = ship;
    }

    public boolean isShip() {
        return shipAtMarker != null;
    }

    public Ship getAssociatedShip() {
        return shipAtMarker;
    }

    public void paint(Graphics g) {
        if (!showMarker) return;

        Graphics2D g2 = (Graphics2D) g.create();

        if (isShip()) {
            // Хрестик для попадання
            g2.setColor(HIT_COLOUR);
            g2.setStroke(new BasicStroke(4));
            int padding = 3;
            g2.drawLine(position.x + padding, position.y + padding, position.x + width - padding, position.y + height - padding);
            g2.drawLine(position.x + width - padding, position.y + padding, position.x + padding, position.y + height - padding);
        } else {
            // Кружечок для промаха
            g2.setColor(MISS_COLOUR);
            int radius = 10;
            g2.fillOval(position.x + width / 2 - radius / 2, position.y + height / 2 - radius / 2, radius, radius);
        }

        g2.dispose();
    }
}
