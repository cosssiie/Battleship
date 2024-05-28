import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Smarter extends Battleship{

    private List<PositionXY> shipHits;

    private final boolean debugAI = false;

    private boolean preferMovesFormingLine;

    private boolean maximiseAdjacentRandomisation;

    public Smarter(Selection playerGrid, boolean preferMovesFormingLine, boolean maximiseAdjacentRandomisation) {
        super(playerGrid);
        shipHits = new ArrayList<>();
        this.preferMovesFormingLine = preferMovesFormingLine;
        this.maximiseAdjacentRandomisation = maximiseAdjacentRandomisation;
        Collections.shuffle(validMoves);
    }

    @Override
    public void reset() {
        super.reset();
        shipHits.clear();
        Collections.shuffle(validMoves);
    }

    @Override
    public PositionXY selectMove() {
        if(debugAI) System.out.println("\nBEGIN TURN===========");
        PositionXY selectedMove;
        // If a ship has been hit, but not destroyed
        if(shipHits.size() > 0) {
            if(preferMovesFormingLine) {
                selectedMove = getSmarterAttack();
            } else {
                selectedMove = getSmartAttack();
            }
        } else {
            if(maximiseAdjacentRandomisation) {
                selectedMove = findMostOpenPosition();
            } else {
                // Use a random move
                selectedMove = validMoves.get(0);
            }
        }
        updateShipHits(selectedMove);
        validMoves.remove(selectedMove);
        if(debugAI) {
            System.out.println("Selected Move: " + selectedMove);
            System.out.println("END TURN===========");
        }
        return selectedMove;
    }

    private PositionXY getSmartAttack() {
        List<PositionXY> suggestedMoves = getAdjacentSmartMoves();
        Collections.shuffle(suggestedMoves);
        return  suggestedMoves.get(0);
    }

    private PositionXY getSmarterAttack() {
        List<PositionXY> suggestedMoves = getAdjacentSmartMoves();
        for(PositionXY possibleOptimalMove : suggestedMoves) {
            if(atLeastTwoHitsInDirection(possibleOptimalMove,PositionXY.LEFT)) return possibleOptimalMove;
            if(atLeastTwoHitsInDirection(possibleOptimalMove,PositionXY.RIGHT)) return possibleOptimalMove;
            if(atLeastTwoHitsInDirection(possibleOptimalMove,PositionXY.DOWN)) return possibleOptimalMove;
            if(atLeastTwoHitsInDirection(possibleOptimalMove,PositionXY.UP)) return possibleOptimalMove;
        }
        // No optimal choice found, just randomise the move.
        Collections.shuffle(suggestedMoves);
        return  suggestedMoves.get(0);
    }

    private PositionXY findMostOpenPosition() {
        PositionXY position = validMoves.get(0);;
        int highestNotAttacked = -1;
        for(int i = 0; i < validMoves.size(); i++) {
            int testCount = getAdjacentNotAttackedCount(validMoves.get(i));
            if(testCount == 4) { // Maximum found, just return immediately
                return validMoves.get(i);
            } else if(testCount > highestNotAttacked) {
                highestNotAttacked = testCount;
                position = validMoves.get(i);
            }
        }
        return position;
    }

    private int getAdjacentNotAttackedCount(PositionXY position) {
        List<PositionXY> adjacentCells = getAdjacentCells(position);
        int notAttackedCount = 0;
        for(PositionXY adjacentCell : adjacentCells) {
            if(!playerGrid.getMarkerAtPosition(adjacentCell).isMarked()) {
                notAttackedCount++;
            }
        }
        return notAttackedCount;
    }

    private boolean atLeastTwoHitsInDirection(PositionXY start, PositionXY direction) {
        PositionXY testPosition = new PositionXY(start);
        testPosition.add(direction);
        if(!shipHits.contains(testPosition)) return false;
        testPosition.add(direction);
        if(!shipHits.contains(testPosition)) return false;
        if(debugAI) System.out.println("Smarter match found AT: " + start + " TO: " + testPosition);
        return true;
    }

    private List<PositionXY> getAdjacentSmartMoves() {
        List<PositionXY> result = new ArrayList<>();
        for(PositionXY shipHitPos : shipHits) {
            List<PositionXY> adjacentPositions = getAdjacentCells(shipHitPos);
            for(PositionXY adjacentPosition : adjacentPositions) {
                if(!result.contains(adjacentPosition) && validMoves.contains(adjacentPosition)) {
                    result.add(adjacentPosition);
                }
            }
        }
        if(debugAI) {
            printPositionList("Ship Hits: ", shipHits);
            printPositionList("Adjacent Smart Moves: ", result);
        }
        return result;
    }

    private void printPositionList(String messagePrefix, List<PositionXY> data) {
        String result = "[";
        for(int i = 0; i < data.size(); i++) {
            result += data.get(i);
            if(i != data.size()-1) {
                result += ", ";
            }
        }
        result += "]";
        System.out.println(messagePrefix + " " + result);
    }

    private List<PositionXY> getAdjacentCells(PositionXY position) {
        List<PositionXY> result = new ArrayList<>();
        if(position.x != 0) {
            PositionXY left = new PositionXY(position);
            left.add(PositionXY.LEFT);
            result.add(left);
        }
        if(position.x != Selection.GRID_WIDTH-1) {
            PositionXY right = new PositionXY(position);
            right.add(PositionXY.RIGHT);
            result.add(right);
        }
        if(position.y != 0) {
            PositionXY up = new PositionXY(position);
            up.add(PositionXY.UP);
            result.add(up);
        }
        if(position.y != Selection.GRID_HEIGHT-1) {
            PositionXY down = new PositionXY(position);
            down.add(PositionXY.DOWN);
            result.add(down);
        }
        return result;
    }

    private void updateShipHits(PositionXY testPosition) {
        Marker marker = playerGrid.getMarkerAtPosition(testPosition);
        if(marker.isShip()) {
            shipHits.add(testPosition);
            // Check to find if this was the last place to hit on the targeted ship
            List<PositionXY> allPositionsOfLastShip = marker.getAssociatedShip().getOccupiedCoordinates();
            if(debugAI) printPositionList("Last Ship", allPositionsOfLastShip);
            boolean hitAllOfShip = containsAllPositions(allPositionsOfLastShip, shipHits);
            // If it was remove the ship data from history to now ignore it
            if(hitAllOfShip) {
                for(PositionXY shipPosition : allPositionsOfLastShip) {
                    for(int i = 0; i < shipHits.size(); i++) {
                        if(shipHits.get(i).equals(shipPosition)) {
                            shipHits.remove(i);
                            if(debugAI) System.out.println("Removed " + shipPosition);
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean containsAllPositions(List<PositionXY> positionsToSearch, List<PositionXY> listToSearchIn) {
        for(PositionXY searchPosition : positionsToSearch) {
            boolean found = false;
            for(PositionXY searchInPosition : listToSearchIn) {
                if(searchInPosition.equals(searchPosition)) {
                    found = true;
                    break;
                }
            }
            if(!found) return false;
        }
        return true;
    }
}
