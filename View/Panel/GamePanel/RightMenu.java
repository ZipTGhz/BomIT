package View.Panel.GamePanel;

import java.awt.Graphics;

import Components.Front.CharacterCard;
import Model.GameManager;
import Model.Entity.Character;

public class RightMenu {
    // private final String TIMER = "TIMER: ";
    // private String timer = "";

    private CharacterCard[] charCards;

    public RightMenu() {
        Character[] characters = GameManager.getInstance().getAllCharacters();
        charCards = new CharacterCard[characters.length];
        int offset_x = 835, offset_y = 20;

        for (int i = 0; i < characters.length; ++i) {
            CharacterCard tmp = new CharacterCard(characters[i], offset_x, offset_y);
            charCards[i] = tmp;
            offset_y += 125;
        }
    }

    public void update() {
        // set timer text
        for (CharacterCard x : charCards) {
            x.update();
        }
    }

    public void render(Graphics g) {
        for (CharacterCard x : charCards) {
            x.render(g);
        }
        // g.setColor(Color.BLACK);
        // g.setFont(new Font("Arial", Font.BOLD, 24));
        // g.drawString(TIMER, 825, 600);
        // g.drawString(timer, 1200, 800);
    }
}
