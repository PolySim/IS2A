import java.util.ArrayList;

import exceptions.PasMajeurException;
import lib.ChatEtudiant;
import lib.ChienEtudiant;
import lib.Etudiant;
import lib.Promo;
import lib.PromoChat;

public class Application {
  public static void main(String[] args) {
    // Question 1 - 2 - 3 - 5
    // Constructeur
    try {
      Etudiant etudiant_simon = new Etudiant("Simon", 22);
      System.out.println(etudiant_simon); // {Simon, 22}
      System.out.println(etudiant_simon.getAge()); // 22
      System.out.println(etudiant_simon.getId()); // 0

      // Constructeur par copie
      Etudiant etudiant_simon_copy = new Etudiant(etudiant_simon);
      System.out.println(etudiant_simon_copy); // {Simon, 22}
      System.out.println(etudiant_simon_copy.getId()); // 1
    } catch (PasMajeurException e) {
      System.out.println(e.getMessage());
    }

    try {
      Etudiant etudiant_vide = new Etudiant();
      System.out.println(etudiant_vide); // {Jean, 18}
      System.out.println(etudiant_vide.getAge()); // 18
      System.out.println(etudiant_vide.getId()); // 2
    } catch (PasMajeurException e) {
      System.out.println(e.getMessage());
    }

    // Question 4
    try {
      Etudiant etudiant_invalide = new Etudiant("Tom", 17);
    } catch (PasMajeurException e) {
      System.out.println(e.getMessage()); // L'étudiant n'est pas majeur
    }

    // Question 6
    try {
      Etudiant etudiant_chien = new ChienEtudiant("Waf", 10);
      System.out.println(etudiant_chien.getAge()); // 70
    } catch (PasMajeurException e) {
      System.out.println(e.getMessage());

    }

    // Question 7
    try {
      Etudiant etudiant_chat = new ChatEtudiant("Miaou", 10, 3);
      System.out.println(etudiant_chat.getAge()); // 80
    } catch (PasMajeurException e) {
      System.out.println(e.getMessage());
    }

    // Question 10

    try {
      Etudiant etudiant_chat = new ChatEtudiant("Miaou", 20, 3);
      Etudiant etudiant_simon = new Etudiant("Simon", 22);
      Etudiant etudiant_chien = new ChienEtudiant("Waf", 20);
      ArrayList<Etudiant> etudiants = new ArrayList<Etudiant>();
      etudiants.add(etudiant_chien);
      etudiants.add(etudiant_simon);
      etudiants.add(etudiant_chat);

      Promo promo = new Promo("Promo 1", etudiants);
      System.out.println(promo); // Nom : Promo 1, Etudiants : [{Waf, 140}, {Simon, 22}, {Miaou, 130}]

      // Question 11
      promo.sortEByNom();
      System.out.println(promo); // Nom : Promo 1, Etudiants : [{Miaou, 130}, {Simon, 22}, {Waf, 140}]

      // Question 12
      System.out.println(promo.getCEByAge()); // [{Miaou, 130}]

      // Question 13
      Etudiant etudiant_zoe = new Etudiant("Zoe", 22);
      promo.add(etudiant_zoe);
      System.out.println(promo.getMapAgeToEs()); // {130=[{Miaou, 130}], 22=[{Simon, 22}, {Zoe, 22}], 140=[{Waf, 140}]}
    } catch (PasMajeurException e) {
      System.out.println(e.getMessage());
    }

    // Question 14
    try {
      ChatEtudiant etudiant_chat = new ChatEtudiant("Miaou", 20, 3);
      ChatEtudiant etudiant_chat2 = new ChatEtudiant("Miaou 2", 20, 3);
      ChatEtudiant etudiant_chat3 = new ChatEtudiant("Miaou 3", 20, 3);
      ArrayList<ChatEtudiant> chats = new ArrayList<ChatEtudiant>();

      chats.add(etudiant_chat);
      chats.add(etudiant_chat2);
      chats.add(etudiant_chat3);

      PromoChat promoChat = new PromoChat("Promo chat", chats);
      System.out.println(promoChat); // Nom : Promo chat, Etudiants : [{Miaou, 130}, {Miaou 2, 130}, {Miaou 3, 130}]

    } catch (PasMajeurException e) {
      System.out.println(e.getMessage());
    }

  }
}
