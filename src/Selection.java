import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Selection extends Rectangle {

    /**
     * Розмір клітинки в пікселях.
     */
    public static final int CELL_SIZE = 35;

    /**
     * Кількість клітинок в рядку.
     */
    public static final int GRID_WIDTH = 10;
    /**
     * Кількість клітинок в стовпчику.
     */
    public static final int GRID_HEIGHT = 10;
    /**
     * Кількість кораблів та їх розмір
     */
    public static final int[] BOAT_SIZES = {5,4,3,3,2};

    /**
     * Грід маркерів для візуального відображення влучання/промаху під час атак.
     */
    private Marker[][] markers = new Marker[GRID_WIDTH][GRID_HEIGHT];

    /**
     * Список кораблів на полі.
     */
    private List<Ship> ships;

    /**
     * Загальний об'єкт Random для використання у випадковому розміщенні кораблів.
     */
    private Random rand;
    /**
     * Кораблі малюються, коли це значення true. Це використовується переважно для того, щоб кораблі гравця завжди були видимі.
     */
    private boolean showShips;
    /**
     * True, коли всі кораблі у списку ships знищені.
     */
    private boolean allShipsDestroyed;
    public Selection(int x, int y) {
        super(x, y, CELL_SIZE*GRID_WIDTH, CELL_SIZE*GRID_HEIGHT);
        createMarkerGrid();
        ships = new ArrayList<>();
        rand = new Random();
        showShips = false;
    }


    public void paint(Graphics g) {
        for(Ship ship : ships) {
            if(showShips || Panel.debugModeActive || ship.isDestroyed()) {
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
        for(int x = 0; x < GRID_WIDTH; x++) {
            for(int y = 0; y < GRID_HEIGHT; y++) {
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
        for(Ship ship : ships) {
            if(!ship.isDestroyed()) {
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
        if(!isPositionInside(new PositionXY(mouseX,mouseY))) return new PositionXY(-1,-1);

        return new PositionXY((mouseX - position.x)/CELL_SIZE, (mouseY - position.y)/CELL_SIZE);
    }


    public boolean canPlaceShipAt(int gridX, int gridY, int segments, boolean sideways) {
        if(gridX < 0 || gridY < 0) return false;

        if(sideways) {
            if(gridY > GRID_HEIGHT || gridX + segments > GRID_WIDTH) return false;
            for(int x = 0; x < segments; x++) {
                if(markers[gridX+x][gridY].isShip()) return false;
            }
        } else {
            if(gridY + segments > GRID_HEIGHT || gridX > GRID_WIDTH) return false;
            for(int y = 0; y < segments; y++) {
                if(markers[gridX][gridY+y].isShip()) return false;
            }
        }
        return true;
    }



    private void drawGrid(Graphics g) {
        g.setColor(Color.BLACK);
        // Draw vertical lines
        int y2 = position.y;
        int y1 = position.y+height;
        for(int x = 0; x <= GRID_WIDTH; x++)
            g.drawLine(position.x+x * CELL_SIZE, y1, position.x+x * CELL_SIZE, y2);

        // Draw horizontal lines
        int x2 = position.x;
        int x1 = position.x+width;
        for(int y = 0; y <= GRID_HEIGHT; y++)
            g.drawLine(x1, position.y+y * CELL_SIZE, x2, position.y+y * CELL_SIZE);
    }



    private void drawMarkers(Graphics g) {
        for(int x = 0; x < GRID_WIDTH; x++) {
            for(int y = 0; y < GRID_HEIGHT; y++) {
                markers[x][y].paint(g);
            }
        }
    }



    private void createMarkerGrid() {
        for(int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                markers[x][y] = new Marker(position.x+x*CELL_SIZE, position.y + y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }


    public void populateShips() {
        ships.clear();
        for(int i = 0; i < BOAT_SIZES.length; i++) {
            boolean sideways = rand.nextBoolean();
            int gridX,gridY;
            do {
                gridX = rand.nextInt(sideways?GRID_WIDTH-BOAT_SIZES[i]:GRID_WIDTH);
                gridY = rand.nextInt(sideways?GRID_HEIGHT:GRID_HEIGHT-BOAT_SIZES[i]);
            } while(!canPlaceShipAt(gridX,gridY,BOAT_SIZES[i],sideways));
            placeShip(gridX, gridY, BOAT_SIZES[i], sideways);
        }
    }


    public void placeShip(int gridX, int gridY, int segments, boolean sideways) {
        placeShip(new Ship(new PositionXY(gridX, gridY),
                new PositionXY(position.x+gridX*CELL_SIZE, position.y+gridY*CELL_SIZE),
                segments, sideways), gridX, gridY);
    }


    public void placeShip(Ship ship, int gridX, int gridY) {
        ships.add(ship);
        if(ship.isSideways()) {
            for(int x = 0; x < ship.getSegments(); x++) {
                markers[gridX+x][gridY].setAsShip(ships.get(ships.size()-1));
            }
        } else {
            for(int y = 0; y < ship.getSegments(); y++) {
                markers[gridX][gridY+y].setAsShip(ships.get(ships.size()-1));
            }
        }
    }
}