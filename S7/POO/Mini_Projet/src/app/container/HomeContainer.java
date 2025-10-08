package app.container;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import game.exceptions.TropDeBombeException;
import game.exceptions.TropPetiteGridException;
import game.exceptions.TropPeuDeBombeException;

public class HomeContainer extends JPanel {
  enum Status {
    NEW,
    WIN,
    LOSE,
  }

  private JButton startButton;
  private JLabel resultLabel;
  private JLabel gridSizeLabel;
  private JTextField gridSizeField;
  private JLabel nbBombeLabel;
  private JTextField nbBombeField;

  public HomeContainer(Status status) {
    super();
    this.setLayout(new GridLayout(status == Status.NEW ? 5 : 6, 1));

    if (status != Status.NEW) {
      resultLabel = new JLabel(status == Status.WIN ? "Gagné !" : "Perdu !");
      resultLabel.setHorizontalAlignment(JLabel.CENTER);
      resultLabel.setVerticalAlignment(JLabel.CENTER);
      resultLabel.setBackground(status == Status.WIN ? Color.GREEN : Color.RED);
      resultLabel.setOpaque(true);
      this.add(resultLabel);
    }

    this.startButton = new JButton("Start");
    this.add(this.startButton);

    this.gridSizeField = new JTextField("10");
    this.gridSizeLabel = new JLabel("Taille de la grille :");
    this.gridSizeLabel.setHorizontalAlignment(JLabel.CENTER);
    this.gridSizeLabel.setVerticalAlignment(JLabel.CENTER);
    this.add(this.gridSizeLabel);
    this.add(this.gridSizeField);

    this.nbBombeField = new JTextField("10");
    this.nbBombeLabel = new JLabel("Nombre de bombes :");
    this.nbBombeLabel.setHorizontalAlignment(JLabel.CENTER);
    this.nbBombeLabel.setVerticalAlignment(JLabel.CENTER);
    this.add(this.nbBombeLabel);
    this.add(this.nbBombeField);

    this.startButton.addActionListener(e -> onStartButtonClick());
  }

  private void controle(int nbBombe, int gridSize)
      throws TropDeBombeException, TropPeuDeBombeException, TropPetiteGridException {
    if (gridSize < 3) {
      throw new TropPetiteGridException();
    }
    if (nbBombe > gridSize * gridSize) {
      System.out.println("Trop de bombes" + nbBombe + " " + gridSize + " " + gridSize * gridSize);
      throw new TropDeBombeException();
    }
    if (nbBombe < 1) {
      throw new TropPeuDeBombeException();
    }
  }

  private void onStartButtonClick() {
    try {
      int gridSize = Integer.parseInt(this.gridSizeField.getText());
      int nbBombe = Integer.parseInt(this.nbBombeField.getText());

      this.controle(nbBombe, gridSize);
      GameContainer.runGame(gridSize, nbBombe);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this, "Veuillez entrer des nombres valides", "Erreur", JOptionPane.ERROR_MESSAGE);
      return;
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
      return;
    }
  }
}
