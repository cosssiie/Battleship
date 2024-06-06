import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Panel extends JPanel implements MouseListener, MouseMotionListener {

    public enum GameState { PlacingShips, FiringShots, GameOver }
    public StatusOfPanel statusPanel;
    private Selection computer;
    private Selection player;
    private Battleship aiController;
    private Ship placingShip;
    private PositionXY tempPlacingPosition;
    private int placingShipIndex;
    private GameState gameState;
    public static boolean debugModeActive;
    private ArrayList<Ship> placedShips;
    private int difficultyChoice;

    private static final String SHOT_SOUND_PATH = "Battleship//shot.wav";

    public Panel(int aiChoice) {
        this.difficultyChoice = aiChoice;
        computer = new Selection(10, 30);
        player = new Selection(computer.getWidth() + 50, 30);
        placedShips = new ArrayList<>();
        setBackground(new Color(255, 255, 255));
        setPreferredSize(new Dimension(computer.getWidth() + player.getWidth() + 60, player.getHeight() + 150));
        setLayout(null);
        addMouseListener(this);
        addMouseMotionListener(this);

        if (aiChoice == 0) {
            aiController = new SimpleRandom(player);
        } else {
            aiController = new Smarter(player, aiChoice == 2, aiChoice == 2);
        }

        statusPanel = new StatusOfPanel(new PositionXY(0, computer.getHeight() + 1), computer.getWidth(), 80);
        statusPanel.setBounds(10, player.getHeight() + 50, 300, 120);
        add(statusPanel);

        restart();

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Z && gameState == GameState.PlacingShips) {
                    if (placingShip != null) {
                        placingShip.toggleSideways();
                        repaint();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    debugModeActive = !debugModeActive;
                    repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_R && gameState == GameState.GameOver) {
                    restart();
                }
            }
        };

        addKeyListener(keyAdapter);
        setFocusable(true);
        requestFocusInWindow();
    }

    public void nextLevel() {
        if (difficultyChoice == 0) {
            difficultyChoice = 1;
        } else if (difficultyChoice == 1) {
            difficultyChoice = 2;
        }
        Menu.nextLevelButton.setVisible(false);
        restart();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        computer.paint(g);
        player.paint(g);
        for (Ship ship : placedShips) {
            ship.paint(g);
        }
        if (gameState == GameState.PlacingShips) {
            placingShip.paint(g);
        }
        computer.paintMarkers(g);
        player.paintMarkers(g);

        statusPanel.repaint();
    }

    public void autoPlaceShips() {
        player.reset();
        placedShips.clear();

        for (int i = 0; i < Selection.BOAT_SIZES.length; i++) {
            boolean placed = false;
            while (!placed) {
                boolean sideways = Math.random() < 0.5;
                int x = (int) (Math.random() * (sideways ? Selection.GRID_WIDTH - Selection.BOAT_SIZES[i] : Selection.GRID_WIDTH));
                int y = (int) (Math.random() * (!sideways ? Selection.GRID_HEIGHT - Selection.BOAT_SIZES[i] : Selection.GRID_HEIGHT));

                if (player.canPlaceShipWithGap(x, y, Selection.BOAT_SIZES[i], sideways)) {
                    Ship ship = new Ship(new PositionXY(x, y),
                            new PositionXY(player.getPosition().x + x * Selection.CELL_SIZE,
                                    player.getPosition().y + y * Selection.CELL_SIZE),
                            Selection.BOAT_SIZES[i], sideways);
                    player.placeShip(ship, x, y);
                    placedShips.add(ship);
                    placed = true;
                }
                requestFocusInWindow();
            }
        }
        gameState = GameState.FiringShots;
        statusPanel.setTopLine("Attack the Computer!");
        statusPanel.setBottomLine("Destroy all Ships to win!");
        repaint();
    }

    public void restart() {
        computer.reset();
        player.reset();
        placedShips.clear();
        player.setShowShips(true);
        aiController.reset();
        tempPlacingPosition = new PositionXY(0, 0);
        placingShip = new Ship(new PositionXY(0, 0),
                new PositionXY(player.getPosition().x, player.getPosition().y),
                Selection.BOAT_SIZES[0], true);
        placingShipIndex = 0;
        updateShipPlacement(tempPlacingPosition);
        computer.populateShips();
        debugModeActive = false;
        statusPanel.reset();
        gameState = GameState.PlacingShips;

        Menu.autoPlaceButton.setEnabled(true);
        requestFocusInWindow();
        repaint();
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
        if(!computer.isPositionMarked(targetPosition)) {
//            Menu.playSounds(SHOT_SOUND_PATH); // Play sound when player fires
            doPlayerTurn(targetPosition);

            if (Menu.autoPlaceButton.isEnabled()) {
                Menu.autoPlaceButton.setEnabled(false);
            }

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
            gameState = GameState.GameOver;
            statusPanel.setTopLine("You won!");
            statusPanel.setBottomLine("Press R to restart or\n \"Next level\"");
            Menu.nextLevelButton.setVisible(true);
            repaint();
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
        if(placingShip.isSideways()) {
            targetPos.x = Math.min(targetPos.x, Selection.GRID_WIDTH - Selection.BOAT_SIZES[placingShipIndex]);
        } else {
            targetPos.y = Math.min(targetPos.y, Selection.GRID_HEIGHT - Selection.BOAT_SIZES[placingShipIndex]);
        }
        placingShip.setDrawPosition(new PositionXY(targetPos),
                new PositionXY(player.getPosition().x + targetPos.x * Selection.CELL_SIZE,
                        player.getPosition().y + targetPos.y * Selection.CELL_SIZE));
        tempPlacingPosition = targetPos;
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
