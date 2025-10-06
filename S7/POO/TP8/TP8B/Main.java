import javax.swing.JFrame;

import UI.ContainerGame;
import exceptions.LoseException;
import exceptions.WinException;

public class Main {
    public static void main(String[] args) throws WinException, LoseException {
        ContainerGame container = new ContainerGame();
        container.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Guess g = new Guess(10, 20, 3);
        // try {
        // g.test(12);
        // } catch (LoseException e) {
        // System.out.println(e.getMessage());
        // }
        // try {
        // g.test(13);
        // } catch (LoseException e) {
        // System.out.println(e.getMessage());
        // }
        // try {
        // g.test(14);
        // } catch (LoseException e) {
        // System.out.println(e.getMessage());
        // }
    }
}
