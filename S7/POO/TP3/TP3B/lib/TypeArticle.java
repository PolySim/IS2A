package lib;

import exceptions.ProblemeCategorieException;

public class TypeArticle {
    private String categorie;
    private static String[] CATEGORIES = {"alimentaire", "textile", "electromenager", "cosmetique"};

    public TypeArticle(String newCategorie) throws ProblemeCategorieException {
        this.setCategorie(newCategorie);
    }

    private void controleCategorie(String categorie) throws ProblemeCategorieException {
        boolean found = false;
        int i = 0;
        while (!found && i < CATEGORIES.length) {
            if (CATEGORIES[i].equals(categorie)) {
                found = true;
            }
            i++;
        }
        if (!found) {
            throw new ProblemeCategorieException();
        }
    }

    public String getCategorie() {
        return this.categorie;
    }

    public void setCategorie(String newCategorie) throws ProblemeCategorieException {
        this.controleCategorie(newCategorie);
        this.categorie = newCategorie;
    }

    public String toString() {
        return "|Catérogie : " + this.getCategorie() + "|";
    }
}
