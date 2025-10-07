package app.container;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;

public class GameContainer extends JFrame {
  GridContainer gridContainer = new GridContainer();
  HeaderContainer headerContainer = new HeaderContainer();

  public GameContainer() {
    super();

    Container cp = this.getContentPane();
    cp.setLayout(new GridLayout(2, 1));
    cp.add(this.headerContainer);
    cp.add(this.gridContainer);
  }
}
