package app.container;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;

public class GameContainer extends JFrame {
  HeaderContainer headerContainer = new HeaderContainer();
  GridContainer gridContainer = new GridContainer();

  public GameContainer() {
    super();

    Container cp = this.getContentPane();
    cp.setLayout(new GridLayout(2, 1));
    cp.add(this.headerContainer);
    cp.add(this.gridContainer);
  }
}
