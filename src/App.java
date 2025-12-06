import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int boardWith = 1440;
            int boardHeight = 800;

            JFrame frame = new JFrame("Solitare");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            Game game = new Game(boardWith, boardHeight);
            frame.setContentPane(game);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setSize(boardWith, boardHeight);
            game.start();
        });
    }
}
