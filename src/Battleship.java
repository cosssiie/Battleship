import java.util.ArrayList;
import java.util.List;


/**
 * Клас промальовки ходів
 *
 * */
public class Battleship {
   
    protected Selection playerGrid;
    protected List<PositionXY> validMoves;
    
    public Battleship(Selection playerGrid) {
        this.playerGrid = playerGrid;
        createValidMoveList();
    }
    
    public PositionXY selectMove() {
        return PositionXY.ZERO;
    }
    
    public void reset() {
        createValidMoveList();
    }
    
    private void createValidMoveList() {
        validMoves = new ArrayList<>();
        for(int x = 0; x < Selection.gridWidth; x++) {
            for(int y = 0; y < Selection.gridHeight; y++) {
                validMoves.add(new PositionXY(x,y));
            }
        }
    }
}
