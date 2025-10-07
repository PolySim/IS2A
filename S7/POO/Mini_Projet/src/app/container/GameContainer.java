package app.container;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;

public class GameContainer extends JFrame {
  static GameContainer instance;

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
    GridContainer gridContainer = new GridContainer(gridSize, nbBombe);
    HeaderContainer headerContainer = new HeaderContainer();

    cp.setLayout(new GridLayout(2, 1));
    cp.add(headerContainer);
    cp.add(gridContainer);

    cp.revalidate();
    cp.repaint();
  }

  public static void endGame(HomeContainer.Status status) {
    Container cp = GameContainer.instance.getContentPane();
    cp.removeAll();
    cp.add(new HomeContainer(status));

    cp.revalidate();
    cp.repaint();
  }
}
