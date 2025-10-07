package app.container;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class HomeContainer extends JPanel {
  enum Status {
    NEW,
    WIN,
    LOSE,
  }

  private JButton startButton;
  private JLabel resultLabel;
  private JTextField gridSizeField;
  private JTextField nbBombeField;

  public HomeContainer(Status status) {
    super();
    this.setLayout(new GridLayout(status == Status.NEW ? 3 : 4, 1));

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
    this.add(this.gridSizeField);

    this.nbBombeField = new JTextField("10");
    this.add(this.nbBombeField);

    this.startButton.addActionListener(e -> onStartButtonClick());
  }

  private void onStartButtonClick() {
    int gridSize = Integer.parseInt(this.gridSizeField.getText()) > 0 ? Integer.parseInt(this.gridSizeField.getText())
        : 10;
    int nbBombe = Integer.parseInt(this.nbBombeField.getText()) > 0 ? Integer.parseInt(this.nbBombeField.getText())
        : 10;
    GameContainer.runGame(gridSize, nbBombe);
  }
}
