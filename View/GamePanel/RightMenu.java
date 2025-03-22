package View.GamePanel;

import java.awt.Graphics;

import Components.Front.CharacterCard;
import Model.GameManager;
import Model.Entity.Character;

public class RightMenu {
    // private final String TIMER = "TIMER: ";
    // private String timer = "";

    private CharacterCard playerCard;
    private CharacterCard botCard;

    public RightMenu() {
        Character[] characters = GameManager.getInstance().getAllCharacters();
        playerCard = new CharacterCard(characters[0], 835, 50);
        botCard = new CharacterCard(characters[1], 835, 175);
    }

    public void update() {
        // set timer text
        playerCard.update();
        botCard.update();
    }

    public void render(Graphics g) {
        playerCard.render(g);
        botCard.render(g);
        // g.setColor(Color.BLACK);
        // g.setFont(new Font("Arial", Font.BOLD, 24));
        // g.drawString(TIMER, 825, 600);
        // g.drawString(timer, 1200, 800);
    }
}
