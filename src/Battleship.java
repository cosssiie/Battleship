import java.util.ArrayList;
import java.util.List;

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
        for(int x = 0; x < Selection.GRID_WIDTH; x++) {
            for(int y = 0; y < Selection.GRID_HEIGHT; y++) {
                validMoves.add(new PositionXY(x,y));
            }
        }
    }
}
