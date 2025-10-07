package app;

import javax.swing.JFrame;

import app.container.GameContainer;

public class Main {
  public static void main(String[] args) {
    GameContainer gameContainer = new GameContainer();
    gameContainer.setSize(1000, 1000);
    gameContainer.setVisible(true);
    gameContainer.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }
}
