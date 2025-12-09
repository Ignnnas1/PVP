import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Pile {
    private final List<Card> cards = new ArrayList<>();
    int x, y;
    int verticalOffset; // spacing between cards

    public static final int CARD_WIDTH = 80;
    public static final int CARD_HEIGHT = 120;

    private final PileType type;

    public Pile(int x, int y, PileType type) {
        this.x = x;
        this.y = y;
        this.type = type;


        if (type == PileType.TABLEAU) {
            this.verticalOffset = 25;
        } else {
            this.verticalOffset = 0;
        }
    }

    public PileType getType() {
        return type;
    }

    public int getVerticalOffset() {
        return verticalOffset;
    }

    public void add(Card c) {
        cards.add(c);
        layoutCards();
    }

    public void addStack(List<Card> stack) {
        cards.addAll(stack);
        layoutCards();
    }

    public void remove(Card c) {
        cards.remove(c);
        layoutCards();
    }

    public Card removeTop() {
        if (cards.isEmpty()) return null;
        Card c = cards.remove(cards.size() - 1);
        layoutCards();
        return c;
    }

    public Card peekTop() {
        if (cards.isEmpty()) return null;
        return cards.get(cards.size() - 1);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int indexOf(Card c) {
        return cards.indexOf(c);
    }


    public List<Card> removeFromIndex(int index) {
        List<Card> stack = new ArrayList<>(cards.subList(index, cards.size()));
        cards.subList(index, cards.size()).clear();
        layoutCards();
        return stack;
    }

    public void draw(Graphics g) {

        g.setColor(new Color(0, 80, 0));
        g.fillRoundRect(x, y, CARD_WIDTH, CARD_HEIGHT, 10, 10);
        g.setColor(Color.WHITE);
        g.drawRoundRect(x, y, CARD_WIDTH, CARD_HEIGHT, 10, 10);

        for (Card c : cards) {
            c.draw(g);
        }
    }

    public Card getCardAt(Point p) {
        for (int i = cards.size() - 1; i >= 0; i--) {
            Card c = cards.get(i);
            if (c.contains(p)) {
                return c;
            }
        }
        return null;
    }

    public boolean contains(Point p) {
        int h;
        if (type == PileType.TABLEAU) {
            h = CARD_HEIGHT + Math.max(0, (cards.size() - 1) * verticalOffset);
        } else {
            h = CARD_HEIGHT;
        }
        Rectangle r = new Rectangle(x, y, CARD_WIDTH, h);
        return r.contains(p);
    }

    public void flipTopIfNeeded() {
        Card top = peekTop();
        if (top != null && !top.isFaceUp()) {
            top.setFaceUp(true);
        }
    }

    private void layoutCards() {
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            c.x = x;
            c.y = y + i * verticalOffset;
        }
    }
}
