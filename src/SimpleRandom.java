import java.util.Collections;


/**
 * Логіка комп'ютера для легкого рівня
 *
 * */
public class SimpleRandom extends Battleship {

    public SimpleRandom(Selection playerGrid) {
        super(playerGrid);
        Collections.shuffle(validMoves);
    }

    @Override
    public void reset() {
        super.reset();
        Collections.shuffle(validMoves);
    }

    @Override
    public PositionXY selectMove() {
        PositionXY nextMove = validMoves.get(0);
        validMoves.remove(0);
        return nextMove;
    }
}
