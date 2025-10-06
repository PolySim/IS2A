package media;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import mediatheque.Bibliotheque;

public class AddLivre extends JFrame {
  private JTextField titre;
  private JTextField auteur;

  private JButton addButton;
  private JButton cancel;
  private JDialog dialog;

  public AddLivre(Bibliotheque bibliotheque, Map<Livre, JLabel> labels, Container container) {
    super();
    this.dialog = new JDialog(this, "Add Livre");
    this.dialog.setLayout(new GridLayout(4, 1));

    this.titre = new JTextField("Titre");
    this.dialog.add(this.titre);

    this.auteur = new JTextField("Auteur");
    this.dialog.add(this.auteur);

    this.addButton = new JButton("Add");
    this.addButton.addActionListener(_ -> this.addLivre(bibliotheque, labels, container));
    this.dialog.add(this.addButton);

    this.cancel = new JButton("Cancel");
    this.cancel.addActionListener(_ -> this.dialog.setVisible(false));
    this.dialog.add(this.cancel);

    this.dialog.setSize(400, 200);
    this.dialog.setVisible(false);
  }

  private void addLivre(Bibliotheque bibliotheque, Map<Livre, JLabel> labels, Container container) {
    Livre livre = new Livre(new Auteur(this.auteur.getText()), this.titre.getText(), 6);
    bibliotheque.add(livre);
    JLabel label = new JLabel(livre.show());
    container.setLayout(new GridLayout(bibliotheque.getLivres().size() + 2, 1));
    label.setBackground(Color.cyan);
    label.setOpaque(true);
    labels.put(livre, label);
    container.add(label);
    container.revalidate();
    container.repaint();
    this.dialog.setVisible(false);
  }

  public void makeDialogVisible() {
    this.dialog.setVisible(true);
  }
}
