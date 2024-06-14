import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Selection extends Rectangle {

    public static final int CELL_SIZE = 30;
    public static int gridWidth;
    public static int gridHeight;
    public static int[] BOAT_SIZES = {5, 4, 3, 3, 2, 2};

    private Marker[][] markers;
    private List<Ship> ships;
    private Random rand;
    private boolean showShips;
    private boolean allShipsDestroyed;
    public static int playerMovesCount;
    public static int AIMovesCount;

    public Selection(int x, int y, int gridWidth, int gridHeight) {
        super(x, y, CELL_SIZE * gridWidth, CELL_SIZE * gridHeight);
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        markers = new Marker[gridWidth][gridHeight];
        createMarkerGrid();
        ships = new ArrayList<>();
        rand = new Random();
        showShips = false;
        playerMovesCount = 0;
        AIMovesCount = 0;
    }


    public void resetMovesCount() {
        playerMovesCount = 0;
        AIMovesCount = 0;
    }

    public static int incrementPlayerMovesCount() {
        return playerMovesCount++;
    }
    public static int incrementAIMovesCount() {
        return AIMovesCount++;
    }

    public void paint(Graphics g) {
        for (Ship ship : ships) {
            if (showShips || Panel.debugModeActive || ship.isDestroyed()) {
                ship.paint(g);
            }
        }
        drawMarkers(g);
        drawGrid(g);
    }

    public void setShowShips(boolean showShips) {
        this.showShips = showShips;
    }

    public void reset() {
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                markers[x][y].reset();
            }
        }
        ships.clear();
        showShips = false;
        allShipsDestroyed = false;
    }

    public boolean markPosition(PositionXY posToMark) {
        markers[posToMark.x][posToMark.y].mark();
        allShipsDestroyed = true;
        for (Ship ship : ships) {
            if (!ship.isDestroyed()) {
                allShipsDestroyed = false;
                break;
            }
        }
        return markers[posToMark.x][posToMark.y].isShip();
    }

    public boolean areAllShipsDestroyed() {
        return allShipsDestroyed;
    }

    public boolean isPositionMarked(PositionXY posToTest) {
        return markers[posToTest.x][posToTest.y].isMarked();
    }

    public Marker getMarkerAtPosition(PositionXY posToSelect) {
        return markers[posToSelect.x][posToSelect.y];
    }

    public PositionXY getPositionInGrid(int mouseX, int mouseY) {
        if (!isPositionInside(new PositionXY(mouseX, mouseY))) return new PositionXY(-1, -1);

        return new PositionXY((mouseX - position.x) / CELL_SIZE, (mouseY - position.y) / CELL_SIZE);
    }

    public boolean canPlaceShipAt(int gridX, int gridY, int segments, boolean sideways) {
        if (gridX < 0 || gridY < 0) return false;

        if (sideways) {
            if (gridY > gridHeight || gridX + segments > gridWidth) return false;
            for (int x = 0; x < segments; x++) {
                if (markers[gridX + x][gridY].isShip()) return false;
            }
        } else {
            if (gridY + segments > gridHeight || gridX > gridWidth) return false;
            for (int y = 0; y < segments; y++) {
                if (markers[gridX][gridY + y].isShip()) return false;
            }
        }
        return true;
    }

    private void drawGrid(Graphics g) {
        g.setColor(new Color(52, 109, 241, 123));
        // Draw vertical lines
        int y2 = position.y;
        int y1 = position.y + height;
        for (int x = 0; x <= gridWidth; x++)
            g.drawLine(position.x + x * CELL_SIZE, y1, position.x + x * CELL_SIZE, y2);

        // Draw horizontal lines
        int x2 = position.x;
        int x1 = position.x + width;
        for (int y = 0; y <= gridHeight; y++)
            g.drawLine(x1, position.y + y * CELL_SIZE, x2, position.y + y * CELL_SIZE);
    }

    private void drawMarkers(Graphics g) {
        for (int x = 0; x < markers.length; x++) {
            for (int y = 0; y < markers[0].length; y++) {
                markers[x][y].paint(g);
            }
        }
    }

    private void createMarkerGrid() {
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                markers[x][y] = new Marker(position.x + x * CELL_SIZE, position.y + y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    public void populateShips() {
        ships.clear();
        for (int i = 0; i < BOAT_SIZES.length; i++) {
            boolean sideways = rand.nextBoolean();
            int gridX, gridY;
            while (true) {
                boolean isHorizontal = rand.nextBoolean();
                gridX = rand.nextInt(isHorizontal ? gridWidth - BOAT_SIZES[i] + 1 : gridWidth);
                gridY = rand.nextInt(isHorizontal ? gridHeight : gridHeight - BOAT_SIZES[i] + 1);
                if (canPlaceShipWithGap(gridX, gridY, BOAT_SIZES[i], isHorizontal)) {
                    placeShip(gridX, gridY, BOAT_SIZES[i], isHorizontal);
                    break;
                }
            }
        }
    }

    public void placeShip(int gridX, int gridY, int segments, boolean sideways) {
        placeShip(new Ship(new PositionXY(gridX, gridY),
                new PositionXY(position.x + gridX * CELL_SIZE, position.y + gridY * CELL_SIZE),
                segments, sideways), gridX, gridY);
    }

    public void placeShip(Ship ship, int gridX, int gridY) {
        ships.add(ship);
        if (ship.isSideways()) {
            for (int x = 0; x < ship.getSegments(); x++) {
                markers[gridX + x][gridY].setAsShip(ships.get(ships.size() - 1));
            }
        } else {
            for (int y = 0; y < ship.getSegments(); y++) {
                markers[gridX][gridY + y].setAsShip(ships.get(ships.size() - 1));
            }
        }
    }

    public boolean canPlaceShipWithGap(int x, int y, int length, boolean isHorizontal) {
        int startX = Math.max(0, x - 1);
        int endX = isHorizontal ? Math.min(gridWidth - 1, x + length) : Math.min(gridWidth - 1, x + 1);
        int startY = Math.max(0, y - 1);
        int endY = isHorizontal ? Math.min(gridHeight - 1, y + 1) : Math.min(gridHeight - 1, y + length);

        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                if (markers[i][j].isShip()) {
                    return false;
                }
            }
        }
        return true;
    }
    public void paintMarkers(Graphics g) {
        for (int y = 0; y < markers[0].length; y++) {
            for (int x = 0; x < markers.length; x++) {
                if (markers[y][x] != null) {
                    markers[y][x].paint(g);
                }
            }
        }
    }
}
