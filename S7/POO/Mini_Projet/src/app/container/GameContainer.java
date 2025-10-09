package app.container;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;

import game.composent.BestTimeManager;

public class GameContainer extends JFrame {
  static GameContainer instance;
  private static int currentGridSize;
  private static int currentNbBombe;

  public GameContainer() {
    super();

    Container cp = this.getContentPane();
    cp.setLayout(new GridLayout(1, 1));
    cp.add(new HomeContainer(HomeContainer.Status.NEW));

    GameContainer.instance = this;
  }

  public static void runGame(int gridSize, int nbBombe) {
    Container cp = GameContainer.instance.getContentPane();
    cp.removeAll();

    GameContainer.currentGridSize = gridSize;
    GameContainer.currentNbBombe = nbBombe;

    game.composent.Timer gameTimer = new game.composent.Timer();

    GridContainer gridContainer = new GridContainer(gridSize, nbBombe, gameTimer);
    HeaderContainer headerContainer = new HeaderContainer(gridSize, nbBombe, gameTimer);

    cp.setLayout(new GridLayout(2, 1));
    cp.add(headerContainer);
    cp.add(gridContainer);

    cp.revalidate();
    cp.repaint();
  }

  public static void endGame(HomeContainer.Status status) {
    HeaderContainer.stopTimerInstance();

    if (status == HomeContainer.Status.WIN) {
      game.composent.Timer gameTimer = GridContainer.getGameTimer();
      if (gameTimer != null) {
        long elapsedTime = gameTimer.getElapsedSeconds();
        BestTimeManager.saveBestTime(
            GameContainer.currentGridSize,
            GameContainer.currentNbBombe,
            elapsedTime);
      }
    }

    Container cp = GameContainer.instance.getContentPane();
    cp.removeAll();
    cp.setLayout(new GridLayout(1, 1));
    cp.add(new HomeContainer(status));

    cp.revalidate();
    cp.repaint();
  }
}
