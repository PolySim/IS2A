package mediatheque;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import media.AddLivre;
import media.Livre;

public class BibliothequeGraphique extends JFrame {
  Bibliotheque bibliotheque;
  Map<Livre, JLabel> labels = new HashMap<>();
  JButton addPrixFor;
  JButton toggleDialog;
  AddLivre addLivre;

  public BibliothequeGraphique(Bibliotheque n_bibliotheque) {
    super();
    this.bibliotheque = n_bibliotheque;
    Container cp = this.getContentPane();
    cp.setLayout(new GridLayout(this.bibliotheque.getLivres().size() + 2, 3));

    this.addPrixFor = new JButton("Add Prix For");
    cp.add(this.addPrixFor);

    this.toggleDialog = new JButton("Add Livre");
    this.addLivre = new AddLivre(this.bibliotheque, this.labels, cp);
    this.toggleDialog.addActionListener(_ -> this.addLivre.makeDialogVisible());
    cp.add(this.toggleDialog);

    this.bibliotheque.getLivres().stream()
        .forEach(l -> {
          JLabel label = new JLabel(l.show());
          if (l.getPrix() < 10) {
            label.setBackground(Color.green);
          } else {
            label.setBackground(Color.cyan);
          }
          label.setOpaque(true);
          this.labels.put(l, label);
          cp.add(label);
        });

    this.addPrixFor.addActionListener(_ -> {
      this.addPrixFor(4);
    });
  }

  private void addPrixFor(int prix) {
    this.bibliotheque.getLivres().stream()
        .forEach(l -> {
          l.incrementPrix(prix);
          this.labels.get(l).setText(l.show());
        });
  }
}
