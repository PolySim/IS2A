package UI;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import exceptions.LoseException;
import exceptions.WinException;
import lib.Guess;

public class ContainerGame extends JFrame {
  Guess g;
  JLabel globalTitle;
  JTextField numberField;
  JSlider slider;
  JButton validButton;
  JLabel resJLabel;

  int value;

  public ContainerGame() {
    super();
    Container cp = this.getContentPane();
    cp.setLayout(new GridLayout(5, 1));

    this.g = new Guess(10, 20, 3);
    this.value = (int) (this.g.getBoundMin() + this.g.getBoundMax()) / 2;

    this.globalTitle = new JLabel(g.toString());
    this.globalTitle.setHorizontalAlignment(JLabel.CENTER);
    this.add(this.globalTitle);

    this.numberField = new JTextField(String.valueOf(this.value));
    this.numberField.addActionListener(_ -> onTextFieldChange(this.numberField.getText()));
    cp.add(this.numberField);

    this.slider = new JSlider();
    this.slider.setMinimum(this.g.getBoundMin());
    this.slider.setMaximum(this.g.getBoundMax());
    this.slider.setValue(this.value);
    this.slider.addChangeListener(_ -> onSliderChange(this.slider.getValue()));
    cp.add(this.slider);

    this.validButton = new JButton("Nouvel essai");
    this.validButton.addActionListener(_ -> onValidButtonClick());
    cp.add(this.validButton);

    this.resJLabel = new JLabel("Il te reste " + this.g.getNbGuess() + " essai(s)");
    this.resJLabel.setHorizontalAlignment(JLabel.CENTER);
    cp.add(this.resJLabel);

    this.setSize(1000, 400);
    this.setVisible(true);
  }

  private void onSliderChange(int value) {
    this.value = value;
    this.numberField.setText(String.valueOf(this.value));
    this.slider.setValue(this.value);
  }

  private void onTextFieldChange(String value) {
    try {
      if (Integer.parseInt(value) < this.g.getBoundMin() || Integer.parseInt(value) > this.g.getBoundMax()) {
        throw new NumberFormatException();
      }
      this.value = Integer.parseInt(value);
      this.numberField.setText(String.valueOf(this.value));
      this.slider.setValue(this.value);
    } catch (NumberFormatException e) {
      this.numberField.setText(String.valueOf(this.value));
    }
  }

  private void onValidButtonClick() {
    try {
      this.g.test(this.value);
      this.globalTitle.setText(this.g.toString());
      this.resJLabel.setText("Il te reste " + this.g.getNbGuess() + " essai(s)");
      this.slider.setMaximum(this.g.getBoundMax());
      this.slider.setMinimum(this.g.getBoundMin());
    } catch (WinException e) {
      this.resJLabel.setText("Gagné");
      this.resJLabel.setBackground(Color.green);
      this.resJLabel.setOpaque(true);
      this.globalTitle.setText(this.g.completeString());
    } catch (LoseException e) {
      this.resJLabel.setText("Perdu");
      this.resJLabel.setBackground(Color.red);
      this.resJLabel.setOpaque(true);
      this.globalTitle.setText(this.g.completeString());
    }
  }
}
