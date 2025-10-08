package app.container;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import game.composent.Cell;

public class HeaderContainer extends JPanel {
  enum ModeFlag {
    ADD,
    REMOVE,
  }

  private static ModeFlag modeFlag;

  private JLabel nbBombeGlobalLabel;
  private JLabel nbBombeRestantesLabel;
  private JToggleButton modeButton;

  private static HeaderContainer instance;

  public HeaderContainer() {
    super();
    HeaderContainer.modeFlag = ModeFlag.ADD;
    this.setLayout(new GridLayout(1, 3));
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

    this.modeButton = new JToggleButton("Mode : Ajouter drapeau");
    this.modeButton.setSelected(false); // Démarrer en mode ADD (non sélectionné)
    this.add(this.modeButton);
    this.modeButton.addActionListener(e -> onModeButtonClick());

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

  public static ModeFlag getModeFlag() {
    return HeaderContainer.modeFlag;
  }

  private void onModeButtonClick() {
    if (this.modeButton.isSelected()) {
      HeaderContainer.modeFlag = ModeFlag.REMOVE;
      this.modeButton.setText("Mode : Retirer drapeau");
      return;
    }
    HeaderContainer.modeFlag = ModeFlag.ADD;
    this.modeButton.setText("Mode : Ajouter drapeau");
  }
}
