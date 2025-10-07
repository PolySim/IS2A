import javax.swing.JFrame;

import UI.ContainerGame;
import exceptions.LoseException;
import exceptions.WinException;

public class Main {
    public static void main(String[] args) throws WinException, LoseException {
        ContainerGame container = new ContainerGame();
        container.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
