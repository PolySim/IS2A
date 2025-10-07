package app.container;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

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
    if (this.cell.getNbBombe() > 0) {
      label.setBackground(Color.RED);
      label.setText("💣");
    } else {
      switch (this.cell.getNbBombeVoisins()) {
        case 1:
          label.setForeground(Color.BLUE);
          break;
        case 2:
          label.setForeground(Color.GREEN);
          break;
        case 3:
          label.setForeground(Color.YELLOW);
          break;
        case 4:
          label.setForeground(Color.ORANGE);
          break;
        default:
          label.setForeground(Color.RED);
          break;
      }
      if (this.cell.getNbBombeVoisins() > 0) {
        label.setText(String.valueOf(this.cell.getNbBombeVoisins()));
      } else {
        List<Cell> voisinsEmptys = this.cell.getVoisinsEmptys();
        voisinsEmptys.forEach(c -> {
          c.setStatus(Cell.Status.CLICK);
          GridContainer.cellContainers.get(c).label.setBackground(Color.WHITE);
        });
      }
      label.setBackground(Color.WHITE);
      this.cell.setStatus(Cell.Status.CLICK);
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
