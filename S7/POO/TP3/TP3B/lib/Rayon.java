package lib;

public class Rayon {
  private Article[] articles;
  private final int max_size;

  public Rayon(int max_size) {
    this.max_size = max_size;
    this.initArticles();
  }

  public void initArticles() {
    this.articles = new Article[this.max_size];
  }

  public Article[] getArticles() {
    return this.articles;
  }

  public void replace(int index, Article article) {
    this.articles[index] = article;
  }

  public void setArticles(Article[] articles) {
    this.articles = articles;
  }

  public int getAvailPos() {
    int i = 0;
    while (i < this.max_size && this.articles[i] != null) {
      i++;
    }
    return i;
  }

  public void add(Article article, int index) {
    try {
      this.replace(index, article);
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Index out of bounds");
    }
  }

  public void add(Article article) {
    this.add(article, this.getAvailPos());
  }

  public void remove(int index) {
    try {
      this.replace(index, null);
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Index out of bounds");
    }
  }

  public String toString() {
    String result = "";
    for (Article article : this.articles) {
      if (article != null) {
        result += article.toString() + "\n";
      }
    }
    return result;
  }
}
