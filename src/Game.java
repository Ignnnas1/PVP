import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel {

    private Point dragOffset = new Point();

    private final List<Pile> piles = new ArrayList<>(); // tableau + foundations + stock + waste
    private List<Card> draggingCards = new ArrayList<>();
    private Pile sourcePile;
    private Deck deck;

    private Pile stockPile;
    private Pile wastePile;

    public Game(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setBackground(new Color(55, 148, 110));
        setOpaque(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();


                if (stockPile != null && stockPile.contains(p)) {
                    handleStockClick();
                    return;
                }


                draggingCards.clear();
                sourcePile = null;

                for (Pile pile : piles) {
                    if (pile.getType() == PileType.TABLEAU) {

                        Card clicked = pile.getCardAt(p);
                        if (clicked != null && clicked.isFaceUp()) {
                            int index = pile.indexOf(clicked);
                            if (index >= 0) {
                                sourcePile = pile;
                                draggingCards = pile.removeFromIndex(index);


                                Card top = draggingCards.get(0);
                                dragOffset.x = p.x - top.x;
                                dragOffset.y = p.y - top.y;
                                repaint();
                                break;
                            }
                        }
                    } else {

                        Card top = pile.peekTop();
                        if (top != null && top.isFaceUp() && top.contains(p)) {
                            sourcePile = pile;
                            draggingCards.add(top);
                            pile.remove(top);

                            dragOffset.x = p.x - top.x;
                            dragOffset.y = p.y - top.y;
                            repaint();
                            break;
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggingCards.isEmpty()) return;

                Point p = e.getPoint();
                Pile target = null;

                for (Pile pile : piles) {
                    if (pile.contains(p)) {
                        target = pile;
                        break;
                    }
                }

                Card movingTop = draggingCards.get(0);

                if (target != null && target != sourcePile && canDropOnPile(movingTop, target)) {

                    target.addStack(draggingCards);

                    if (sourcePile != null) {
                        sourcePile.flipTopIfNeeded();
                    }
                } else {

                    if (sourcePile != null) {
                        sourcePile.addStack(draggingCards);
                    }
                }

                draggingCards.clear();
                sourcePile = null;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggingCards.isEmpty()) return;

                Point p = e.getPoint();
                int offset = (sourcePile != null) ? sourcePile.getVerticalOffset() : 0;

                for (int i = 0; i < draggingCards.size(); i++) {
                    Card c = draggingCards.get(i);
                    c.x = p.x - dragOffset.x;
                    c.y = p.y - dragOffset.y + i * offset;
                }
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Pile pile : piles) {
            pile.draw(g);
        }

        for (Card c : draggingCards) {
            c.draw(g);
        }
    }

    public void start() {
        deck = new Deck();
        piles.clear();
        draggingCards.clear();
        sourcePile = null;


        int stockX = 60;
        int stockY = 40;
        int wasteX = 200;
        int wasteY = 40;

        stockPile = new Pile(stockX, stockY, PileType.STOCK);
        wastePile = new Pile(wasteX, wasteY, PileType.WASTE);

        piles.add(stockPile);
        piles.add(wastePile);


        int foundationStartX = 500;
        int foundationGap = 120;
        int foundationY = 40;

        for (int i = 0; i < 4; i++) {
            Pile foundation = new Pile(foundationStartX + i * foundationGap, foundationY, PileType.FOUNDATION);
            piles.add(foundation);
        }


        int tableauStartX = 60;
        int tableauGap = 120;
        int tableauY = 200;

        for (int i = 0; i < 7; i++) {
            Pile pile = new Pile(tableauStartX + i * tableauGap, tableauY, PileType.TABLEAU);
            piles.add(pile);

            for (int j = 0; j <= i; j++) {
                Card c = deck.draw();
                if (c == null) continue;
                c.setFaceUp(j == i);
                pile.add(c);
            }
        }


        Card c;
        while ((c = deck.draw()) != null) {
            c.setFaceUp(false);
            stockPile.add(c);
        }

        repaint();
    }


    private void handleStockClick() {
        if (!stockPile.isEmpty()) {

            Card c = stockPile.removeTop();
            c.setFaceUp(true);
            wastePile.add(c);
        } else {

            if (!wastePile.isEmpty()) {
                while (!wastePile.isEmpty()) {
                    Card c = wastePile.removeTop();
                    c.setFaceUp(false);
                    stockPile.add(c);
                }
            }
        }
        repaint();
    }

    private boolean canDropOnPile(Card moving, Pile target) {
        if (target.getType() == PileType.TABLEAU) {
            return canDropOnTableau(moving, target);
        } else if (target.getType() == PileType.FOUNDATION) {
            return canDropOnFoundation(moving, target);
        }

        return false;
    }

    // Tableau rules:
    // - Empty pile: only King (rank 13)
    // - Non-empty: opposite color, rank one less than top
    private boolean canDropOnTableau(Card moving, Pile target) {
        Card top = target.peekTop();

        if (top == null) {
            return moving.getRank() == 13; // King
        } else {
            boolean differentColor = moving.isRed() != top.isRed();
            boolean rankOneLess = moving.getRank() == top.getRank() - 1;
            return differentColor && rankOneLess;
        }
    }

    // Foundation rules:
    // - Empty: only Ace (rank 1)
    // - Non-empty: same suit, rank one higher than top
    private boolean canDropOnFoundation(Card moving, Pile target) {
        Card top = target.peekTop();

        if (top == null) {
            return moving.getRank() == 1; // Ace
        } else {
            boolean sameSuit = moving.getSuit() == top.getSuit();
            boolean rankOneHigher = moving.getRank() == top.getRank() + 1;
            return sameSuit && rankOneHigher;
        }
    }
}
