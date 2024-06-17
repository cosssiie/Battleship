import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Створення кораблів та надання їм властивостей
 *
 * */
public class Ship {
    public enum ShipPlacementColour {Valid, Invalid, Placed}

    private PositionXY gridPosition;

    private PositionXY drawPosition;

    private int segments;

    private boolean isSideways;

    private int destroyedSections;

    private ShipPlacementColour shipPlacementColour;


    public Ship(PositionXY gridPosition, PositionXY drawPosition, int segments, boolean isSideways) {
        this.gridPosition = gridPosition;
        this.drawPosition = drawPosition;
        this.segments = segments;
        this.isSideways = isSideways;
        destroyedSections = 0;
        shipPlacementColour = ShipPlacementColour.Placed;
    }


    public void paint(Graphics g) {
        if(shipPlacementColour == ShipPlacementColour.Placed) {
            g.setColor(new Color(157, 154, 154, 250));
        } else {
            g.setColor(shipPlacementColour == ShipPlacementColour.Valid ? new Color(127, 215, 91, 250): new Color(239, 53, 53, 250));
        }
        if(isSideways) paintHorizontal(g);
        else paintVertical(g);
    }


    public void setShipPlacementColour(ShipPlacementColour shipPlacementColour) {
        this.shipPlacementColour = shipPlacementColour;
    }


    public void toggleSideways() {
        isSideways = !isSideways;
    }


    public void destroySection() {
        destroyedSections++;
    }


    public boolean isDestroyed() { return destroyedSections >= segments; }


    public void setDrawPosition(PositionXY gridPosition, PositionXY drawPosition) {
        this.drawPosition = drawPosition;
        this.gridPosition = gridPosition;
    }


    public boolean isSideways() {
        return isSideways;
    }

    public int getSegments() {
        return segments;
    }


    public List<PositionXY> getOccupiedCoordinates() {
        List<PositionXY> result = new ArrayList<>();
        if(isSideways) { // handle the case when horizontal
            for(int x = 0; x < segments; x++) {
                result.add(new PositionXY(gridPosition.x+x, gridPosition.y));
            }
        } else { // handle the case when vertical
            for(int y = 0; y < segments; y++) {
                result.add(new PositionXY(gridPosition.x, gridPosition.y+y));
            }
        }
        return result;
    }


    public void paintVertical(Graphics g) {
        int boatWidth = (int)(Selection.CELL_SIZE * 0.8);
        int boatLeftX = drawPosition.x + Selection.CELL_SIZE / 2 - boatWidth / 2;
        g.fillPolygon(new int[]{drawPosition.x+Selection.CELL_SIZE/2,boatLeftX,boatLeftX+boatWidth},
                new int[]{drawPosition.y+Selection.CELL_SIZE/4,drawPosition.y+Selection.CELL_SIZE,drawPosition.y+Selection.CELL_SIZE},3);
        g.fillRect(boatLeftX,drawPosition.y+Selection.CELL_SIZE, boatWidth,
                (int)(Selection.CELL_SIZE * (segments-1.2)));
    }

    public void paintHorizontal(Graphics g) {
        int boatWidth = (int)(Selection.CELL_SIZE * 0.8);
        int boatTopY = drawPosition.y + Selection.CELL_SIZE / 2 - boatWidth / 2;
        g.fillPolygon(new int[]{drawPosition.x+Selection.CELL_SIZE/4,drawPosition.x+Selection.CELL_SIZE,drawPosition.x+Selection.CELL_SIZE},
                new int[]{drawPosition.y+Selection.CELL_SIZE/2,boatTopY,boatTopY+boatWidth},3);
        g.fillRect(drawPosition.x+Selection.CELL_SIZE,boatTopY,
                (int)(Selection.CELL_SIZE * (segments-1.2)), boatWidth);
    }
}
