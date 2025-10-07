package app.container;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import game.composent.Cell;

public class HeaderContainer extends JPanel {
  private JLabel nbBombeGlobalLabel;
  private JLabel nbBombeRestantesLabel;
  private static HeaderContainer instance;

  public HeaderContainer() {
    super();
    this.setLayout(new GridLayout(1, 2));
    this.setSize(1000, 100);

    this.nbBombeGlobalLabel = new JLabel("Nombre de bombes : " + Cell.getNbBombeGlobal());
    this.nbBombeRestantesLabel = new JLabel(
        "Nombre de bombes restantes : " + (Cell.getNbBombeGlobal() - Cell.getFlagGlobal()));

    this.nbBombeGlobalLabel.setHorizontalAlignment(JLabel.CENTER);
    this.nbBombeRestantesLabel.setHorizontalAlignment(JLabel.CENTER);
    this.nbBombeGlobalLabel.setVerticalAlignment(JLabel.CENTER);
    this.nbBombeRestantesLabel.setVerticalAlignment(JLabel.CENTER);

    this.add(this.nbBombeGlobalLabel);
    this.add(this.nbBombeRestantesLabel);

    // Enregistrer l'instance pour pouvoir la mettre à jour
    HeaderContainer.instance = this;
  }

  public void updateLabels() {
    this.nbBombeGlobalLabel.setText("Nombre de bombes : " + Cell.getNbBombeGlobal());
    this.nbBombeRestantesLabel.setText(
        "Nombre de bombes restantes : " + (Cell.getNbBombeGlobal() - Cell.getFlagGlobal()));
  }

  public static void updateInstance() {
    if (HeaderContainer.instance != null) {
      HeaderContainer.instance.updateLabels();
    }
  }
}
