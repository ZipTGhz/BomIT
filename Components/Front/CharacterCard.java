package Components.Front;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Interfaces.IHash;
import Model.GameManager;
import Model.SpriteFactory;
import Model.Entity.Character;
import Util.UtilityTools;

public class CharacterCard {
    private static final Color coverColor = new Color(0, 0, 0, 150);

    private Character character;
    private int offset_x = 0, offset_y = 0;
    private BufferedImage avatar;
    private Color bgColor;

    private BufferedImage healthImage;
    private String healthText;

    private BufferedImage bombImage;
    private String bombText;

    private BufferedImage speedImage;
    private String speedText;

    public CharacterCard(Character instance, int offset_x, int offset_y) {
        character = instance;
        this.offset_x = offset_x;
        this.offset_y = offset_y;
        init();
    }

    public void update() {
        if (character.getHealth() != 0) {
            healthText = String.valueOf(character.getHealth());
            bombText = String.valueOf(GameManager.getInstance().isCharacterPlaceBomb(character) ? 0 : 1);
            speedText = String.valueOf(character.getSpeed());
        } else {
            healthText = String.valueOf(0);
        }
    }

    public void render(Graphics g) {
        // Vẽ background
        UtilityTools.drawRoundedRect(g, offset_x, offset_y, 200, 100, 20, 20, bgColor);

        // Vẽ avatar
        UtilityTools.drawRoundedRect(g, offset_x + 10, offset_y + 10, avatar.getWidth(), avatar.getHeight(), 10, 10, Color.WHITE);
        g.drawImage(avatar, offset_x + 10, offset_y + 5, null);

        // Vẽ nền cho thuộc tính
        UtilityTools.drawRoundedRect(g, offset_x + 70, offset_y + 10, 100, 20, 10, 10, Color.WHITE);
        UtilityTools.drawRoundedRect(g, offset_x + 70, offset_y + 40, 100, 20, 10, 10, Color.WHITE);
        UtilityTools.drawRoundedRect(g, offset_x + 70, offset_y + 70, 100, 20, 10, 10, Color.WHITE);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        // Vẽ thuộc tính máu
        g.drawImage(healthImage, offset_x + 73, offset_y + 12, null);
        g.drawString(healthText, offset_x + 155, offset_y + 26);
        // Vẽ thuộc tính bom
        g.drawImage(bombImage, offset_x + 65, offset_y + 34, null);
        g.drawString(bombText, offset_x + 155, offset_y + 56);
        // Vẽ thuộc tính tốc chạy
        g.drawImage(speedImage, offset_x + 73, offset_y + 72, null);
        g.drawString(speedText, offset_x + 155, offset_y + 86);

        // Nếu nhân vật chết thì thêm lớp phủ mờ
        if (character.getHealth() == 0) {
            UtilityTools.drawRoundedRect(g, offset_x, offset_y, 200, 100, 20, 20, coverColor);
        }
    }

    private void init() {
        avatar = character.getThumnail();
        bgColor = UtilityTools.getAverageColor(avatar);

        healthImage = SpriteFactory.getInstance().getSprite(IHash.SpriteName.HEART);
        healthText = String.valueOf(character.getHealth());

        bombImage = SpriteFactory.getInstance().getSprite(IHash.SpriteName.BOMB);
        bombText = String.valueOf(GameManager.getInstance().isCharacterPlaceBomb(character) ? 0 : 1);

        speedImage = SpriteFactory.getInstance().getSprite(IHash.SpriteName.SPEED);
        speedText = String.valueOf(character.getSpeed());

    }
}
