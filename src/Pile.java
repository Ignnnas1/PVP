import java.awt.*;
import java.util.List;

public class Pile {
    List<Card> cards;
    int x, y;

    void add(Card c){

    }
    Card removeTop(){
        return null;
    }
    void draw(Graphics g){
        for (Card c : cards) {
            c.draw(g);
        }
    }

}
