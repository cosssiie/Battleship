
public class PositionXY {

    public static final PositionXY DOWN = new PositionXY(0,1);

    public static final PositionXY UP = new PositionXY(0,-1);

    public static final PositionXY LEFT = new PositionXY(-1,0);

    public static final PositionXY RIGHT = new PositionXY(1,0);
    public static final PositionXY ZERO = new PositionXY(0,0);


    public int x;

    public int y;


    public PositionXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PositionXY(PositionXY positionToCopy) {
        this.x = positionToCopy.x;
        this.y = positionToCopy.y;
    }


    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void add(PositionXY otherPosition) {
        this.x += otherPosition.x;
        this.y += otherPosition.y;
    }

    public double distanceTo(PositionXY otherPosition) {
        return Math.sqrt(Math.pow(x-otherPosition.x,2)+Math.pow(y-otherPosition.y,2));
    }

    public void multiply(int amount) {
        x *= amount;
        y *= amount;
    }
    public void subtract(PositionXY otherPosition) {
        this.x -= otherPosition.x;
        this.y -= otherPosition.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionXY position = (PositionXY) o;
        return x == position.x && y == position.y;
    }

    @Override
    public String toString() {
        return "(" + (x+1) + ", " + (y+1) + ")";
    }
}
