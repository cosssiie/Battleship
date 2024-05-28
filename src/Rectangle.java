public class Rectangle {

    protected PositionXY position;

    protected int width;

    protected int height;

    public Rectangle(PositionXY position, int width, int height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public Rectangle(int x, int y, int width, int height) {
        this(new PositionXY(x,y),width,height);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public PositionXY getPosition() {
        return position;
    }

    public boolean isPositionInside(PositionXY targetPosition) {
        return targetPosition.x >= position.x && targetPosition.y >= position.y
                && targetPosition.x < position.x + width && targetPosition.y < position.y + height;
    }
}
