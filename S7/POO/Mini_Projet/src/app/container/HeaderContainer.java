package app.container;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class HeaderContainer extends JPanel {
  public HeaderContainer() {
    super();
    this.setLayout(new GridLayout(1, 1));
    this.add(new JLabel("Header"));
  }
}
