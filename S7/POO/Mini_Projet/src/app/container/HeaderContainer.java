package app.container;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.Timer;

import game.composent.BestTimeManager;
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
  private JLabel timerLabel;
  private JLabel bestTimeLabel;

  private Timer uiTimer;
  private game.composent.Timer gameTimer;
  private int gridSize;
  private int nbBombe;

  private static HeaderContainer instance;

  public HeaderContainer(int gridSize, int nbBombe, game.composent.Timer gameTimer) {
    super();
    HeaderContainer.modeFlag = ModeFlag.ADD;
    this.gridSize = gridSize;
    this.nbBombe = nbBombe;
    this.gameTimer = gameTimer;

    this.setLayout(new GridLayout(1, 5));
    this.setSize(1000, 100);

    this.nbBombeGlobalLabel = new JLabel("Nombre de bombes : " + Cell.getNbBombeGlobal());
    this.nbBombeRestantesLabel = new JLabel(
        "Bombes restantes : " + (Cell.getNbBombeGlobal() - Cell.getFlagGlobal()));

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

    this.timerLabel = new JLabel("Temps : 00:00");
    this.timerLabel.setHorizontalAlignment(JLabel.CENTER);
    this.timerLabel.setVerticalAlignment(JLabel.CENTER);
    this.add(this.timerLabel);

    this.bestTimeLabel = new JLabel("");
    this.bestTimeLabel.setHorizontalAlignment(JLabel.CENTER);
    this.bestTimeLabel.setVerticalAlignment(JLabel.CENTER);
    updateBestTimeLabel();
    this.add(this.bestTimeLabel);

    this.uiTimer = new Timer(1000, e -> updateTimerDisplay());

    // Enregistrer l'instance pour pouvoir la mettre à jour
    HeaderContainer.instance = this;
  }

  private void updateTimerDisplay() {
    if (this.gameTimer != null && this.gameTimer.isRunning()) {
      long elapsed = this.gameTimer.getElapsedSeconds();
      this.timerLabel.setText("Temps : " + BestTimeManager.formatTime(elapsed));
    }
  }

  private void updateBestTimeLabel() {
    Long bestTime = BestTimeManager.getBestTime(this.gridSize, this.nbBombe);
    if (bestTime != null) {
      this.bestTimeLabel.setText("Meilleur temps : " + BestTimeManager.formatTime(bestTime));
    } else {
      this.bestTimeLabel.setText("");
    }
  }

  public void startTimer() {
    if (this.gameTimer != null) {
      this.gameTimer.start();
      this.uiTimer.start();
    }
  }

  public void stopTimer() {
    if (this.gameTimer != null) {
      this.gameTimer.stop();
      this.uiTimer.stop();
      updateTimerDisplay();
    }
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

  public static void startTimerInstance() {
    if (HeaderContainer.instance != null) {
      HeaderContainer.instance.startTimer();
    }
  }

  public static void stopTimerInstance() {
    if (HeaderContainer.instance != null) {
      HeaderContainer.instance.stopTimer();
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
