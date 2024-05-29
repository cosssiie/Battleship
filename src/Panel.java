import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Panel extends JPanel implements MouseListener, MouseMotionListener {

    public enum GameState { PlacingShips, FiringShots, GameOver }
    private StatusOfPanel statusPanel;
    private Selection computer;
    private Selection player;
    private Battleship aiController;
    private Ship placingShip;
    private PositionXY tempPlacingPosition;
    private int placingShipIndex;
    private GameState gameState;
    public static boolean debugModeActive;

    public Panel(int aiChoice) {
        computer = new Selection(10, 30);
        player = new Selection(computer.getWidth() + 50, 30);
        setBackground(new Color(255, 255, 255));
        setPreferredSize(new Dimension(computer.getWidth() + player.getWidth() + 60, player.getHeight() + 120));
        addMouseListener(this);
        addMouseMotionListener(this);

        if (aiChoice == 0) {
            aiController = new SimpleRandom(player);
        } else {
            aiController = new Smarter(player, aiChoice == 2, aiChoice == 2);
        }

        statusPanel = new StatusOfPanel(new PositionXY(0, computer.getHeight() + 1), computer.getWidth(), 49);
        restart();
    }
    public void paint(Graphics g) {
        super.paint(g);
        computer.paint(g);
        player.paint(g);
        if (gameState == GameState.PlacingShips) {
            placingShip.paint(g);
        }
        statusPanel.paint(g);
    }

    public void handleInput(int keyCode) {
        if(keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(1);
        } else if(keyCode == KeyEvent.VK_R) {
            restart();
        } else if(gameState == GameState.PlacingShips && keyCode == KeyEvent.VK_Z) {
            placingShip.toggleSideways();
            updateShipPlacement(tempPlacingPosition);
        } else if(keyCode == KeyEvent.VK_D) {
            debugModeActive = !debugModeActive;
        }
        repaint();
    }
    public void restart() {
        computer.reset();
        player.reset();
        // Player can see their own ships by default
        player.setShowShips(true);
        aiController.reset();
        tempPlacingPosition = new PositionXY(0,0);
        placingShip = new Ship(new PositionXY(0,0),
                new PositionXY(player.getPosition().x,player.getPosition().y),
                Selection.BOAT_SIZES[0], true);
        placingShipIndex = 0;
        updateShipPlacement(tempPlacingPosition);
        computer.populateShips();
        debugModeActive = false;
        statusPanel.reset();
        gameState = GameState.PlacingShips;
    }
    private void tryPlaceShip(PositionXY mousePosition) {
        PositionXY targetPosition = player.getPositionInGrid(mousePosition.x, mousePosition.y);
        updateShipPlacement(targetPosition);
        if(player.canPlaceShipWithGap(targetPosition.x, targetPosition.y,
                Selection.BOAT_SIZES[placingShipIndex], placingShip.isSideways())) {
            placeShip(targetPosition);
        }
    }


    private void placeShip(PositionXY targetPosition) {
        placingShip.setShipPlacementColour(Ship.ShipPlacementColour.Placed);
        player.placeShip(placingShip,tempPlacingPosition.x,tempPlacingPosition.y);
        placingShipIndex++;
        // If there are still ships to place
        if(placingShipIndex < Selection.BOAT_SIZES.length) {
            placingShip = new Ship(new PositionXY(targetPosition.x, targetPosition.y),
                    new PositionXY(player.getPosition().x + targetPosition.x * Selection.CELL_SIZE,
                            player.getPosition().y + targetPosition.y * Selection.CELL_SIZE),
                    Selection.BOAT_SIZES[placingShipIndex], true);
            updateShipPlacement(tempPlacingPosition);
        } else {
            gameState = GameState.FiringShots;
            statusPanel.setTopLine("Attack the Computer!");
            statusPanel.setBottomLine("Destroy all Ships to win!");
        }
    }
    private void tryFireAtComputer(PositionXY mousePosition) {
        PositionXY targetPosition = computer.getPositionInGrid(mousePosition.x,mousePosition.y);
        // Ignore if position was already clicked
        if(!computer.isPositionMarked(targetPosition)) {
            doPlayerTurn(targetPosition);
            // Only do the AI turn if the game didn't end from the player's turn.
            if(!computer.areAllShipsDestroyed()) {
                doAITurn();
            }
        }
    }
    private void doPlayerTurn(PositionXY targetPosition) {
        boolean hit = computer.markPosition(targetPosition);
        String hitMiss = hit ? "Hit" : "Missed";
        String destroyed = "";
        if(hit && computer.getMarkerAtPosition(targetPosition).getAssociatedShip().isDestroyed()) {
            destroyed = "(Destroyed)";
        }
        statusPanel.setTopLine("Player " + hitMiss + " " + targetPosition + destroyed);
        if(computer.areAllShipsDestroyed()) {
            // Player wins!
            gameState = GameState.GameOver;
            statusPanel.showGameOver(true);
        }
    }
    private void doAITurn() {
        PositionXY aiMove = aiController.selectMove();
        boolean hit = player.markPosition(aiMove);
        String hitMiss = hit ? "Hit" : "Missed";
        String destroyed = "";
        if(hit && player.getMarkerAtPosition(aiMove).getAssociatedShip().isDestroyed()) {
            destroyed = "(Destroyed)";
        }
        statusPanel.setBottomLine("Computer " + hitMiss + " " + aiMove + destroyed);
        if(player.areAllShipsDestroyed()) {
            // Computer wins!
            gameState = GameState.GameOver;
            statusPanel.showGameOver(false);
        }
    }
    private void tryMovePlacingShip(PositionXY mousePosition) {
        if(player.isPositionInside(mousePosition)) {
            PositionXY targetPos = player.getPositionInGrid(mousePosition.x, mousePosition.y);
            updateShipPlacement(targetPos);
        }
    }
    private void updateShipPlacement(PositionXY targetPos) {
        // Constrain to fit inside the grid
        if(placingShip.isSideways()) {
            targetPos.x = Math.min(targetPos.x, Selection.GRID_WIDTH - Selection.BOAT_SIZES[placingShipIndex]);
        } else {
            targetPos.y = Math.min(targetPos.y, Selection.GRID_HEIGHT - Selection.BOAT_SIZES[placingShipIndex]);
        }
        // Update drawing position to use the new target position
        placingShip.setDrawPosition(new PositionXY(targetPos),
                new PositionXY(player.getPosition().x + targetPos.x * Selection.CELL_SIZE,
                        player.getPosition().y + targetPos.y * Selection.CELL_SIZE));
        // Store the grid position for other testing cases
        tempPlacingPosition = targetPos;
        // Change the colour of the ship based on whether it could be placed at the current location.
        if(player.canPlaceShipAt(tempPlacingPosition.x, tempPlacingPosition.y,
                Selection.BOAT_SIZES[placingShipIndex],placingShip.isSideways())) {
            placingShip.setShipPlacementColour(Ship.ShipPlacementColour.Valid);
        } else {
            placingShip.setShipPlacementColour(Ship.ShipPlacementColour.Invalid);
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        PositionXY mousePosition = new PositionXY(e.getX(), e.getY());
        if(gameState == GameState.PlacingShips && player.isPositionInside(mousePosition)) {
            tryPlaceShip(mousePosition);
        } else if(gameState == GameState.FiringShots && computer.isPositionInside(mousePosition)) {
            tryFireAtComputer(mousePosition);
        }
        repaint();
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        if(gameState != GameState.PlacingShips) return;
        tryMovePlacingShip(new PositionXY(e.getX(), e.getY()));
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}

}
