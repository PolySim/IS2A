package app.container;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

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

    this.label = new JLabel();
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
    if (!GridContainer.getIsStarted()) {
      GridContainer.setIsStarted(true);
      if (this.cell.getNbBombe() > 0) {
        GridContainer.moveBombe(this.cell);
      }
    }
    if (this.cell.getNbBombe() > 0) {
      label.setBackground(Color.RED);
      label.setText("💣");
      GameContainer.endGame(HomeContainer.Status.LOSE);
    } else {
      label.setForeground(getColorFromNbBombe(this.cell.getNbBombeVoisins()));
      if (this.cell.getNbBombeVoisins() > 0) {
        label.setText(String.valueOf(this.cell.getNbBombeVoisins()));
      } else {
        Set<Cell> voisinsEmptys = this.cell.getVoisinsEmptys();
        voisinsEmptys.forEach(c -> {
          c.setStatus(Cell.Status.CLICK);
          GridContainer.cellContainers.get(c).label.setBackground(Color.WHITE);
          if (c.getNbBombeVoisins() > 0) {
            GridContainer.cellContainers.get(c).label.setForeground(getColorFromNbBombe(c.getNbBombeVoisins()));
            GridContainer.cellContainers.get(c).label.setText(String.valueOf(c.getNbBombeVoisins()));
          }
        });
      }
      label.setBackground(Color.WHITE);
      this.cell.setStatus(Cell.Status.CLICK);
      if (GridContainer.isWin()) {
        GameContainer.endGame(HomeContainer.Status.WIN);
      }
    }
  }

  private Color getColorFromNbBombe(int nbBombe) {
    switch (nbBombe) {
      case 1:
        return Color.BLUE;
      case 2:
        return Color.GREEN;
      case 3:
        return Color.YELLOW;
      case 4:
        return Color.ORANGE;
      default:
        return Color.RED;
    }
  }

  private void onRightClick() {
    if (cell.getStatus() == Cell.Status.FLAG) {
      cell.setStatus(Cell.Status.VIERGE);
      label.setText("");
      HeaderContainer.updateInstance();
      return;
    }
    if (cell.getStatus() == Cell.Status.VIERGE) {
      cell.setStatus(Cell.Status.FLAG);
      label.setText("🚩");
      HeaderContainer.updateInstance();
    }
  }
}
