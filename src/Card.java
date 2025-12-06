import javax.swing.*;
import java.awt.*;

public class Card {

    private int rank;        // 1 = Ace, 13 = King
    private Suit suit;
    private boolean faceUp;

    private Image frontImage;
    private static Image backImage;

    public int x, y;
    public int width = 80;
    public int height = 120;

    public Card(int rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.faceUp = false;

        String filename = "/cards/" + rankToString() + suitToChar() + ".jpg";
        frontImage = new ImageIcon(getClass().getResource(filename)).getImage();

        if (backImage == null) {
            backImage = new ImageIcon(getClass().getResource("/cards/back.jpg")).getImage();
        }
    }

    public void draw(Graphics g) {
        Image img = faceUp ? frontImage : backImage;
        g.drawImage(img, x, y, width, height, null);
    }

    public boolean contains(Point p) {
        return (p.x >= x && p.x <= x + width &&
                p.y >= y && p.y <= y + height);
    }

    private String rankToString() {
        return switch (rank) {
            case 1 -> "A";
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            default -> String.valueOf(rank);
        };
    }

    private String suitToChar() {
        return switch (suit) {
            case CLUBS -> "C";
            case DIAMONDS -> "D";
            case HEARTS -> "H";
            case SPADES -> "S";
        };
    }
}
