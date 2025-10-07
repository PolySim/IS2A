package app.container;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.composent.Cell;

public class CellContainer extends JPanel {
  Cell cell;
  JLabel label;

  public CellContainer(Cell cell) {
    super();
    this.cell = cell;
    this.setLayout(new GridLayout(1, 1));

    this.label = new JLabel(cell.toString());
    label.setBackground(Color.LIGHT_GRAY);
    label.setOpaque(true);
    label.setHorizontalAlignment(JLabel.CENTER);
    label.setVerticalAlignment(JLabel.CENTER);
    label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    this.add(label);

    // Ajouter un listener pour gérer les clics
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        handleClick(e);
      }
    });
  }

  private void handleClick(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1) {
      onLeftClick();
    } else if (e.getButton() == MouseEvent.BUTTON3) {
      onRightClick();
    }
  }

  private void onLeftClick() {
    label.setText("Bombes voisines: " + cell.getNbBombeVoisins());
  }

  private void onRightClick() {
    label.setText("🚩");
  }
}
