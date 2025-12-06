import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel {

    private Point dragOffset = new Point();

    List<Pile> piles;
    Card draggingCard;

    public Game(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(new Color(55, 148, 110));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                // reikia sukurt
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggingCard = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggingCard != null) {
                    Point p = e.getPoint();
                    // reikia sukurt
                    repaint();
                }
            }
        });
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Pile pile : piles) {
            pile.draw(g);
        }

        if (draggingCard != null) {
            draggingCard.draw(g);
        }
    }

    public void start() {
        //sukurti Deck ir Pilesus
    }
}
