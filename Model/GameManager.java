package Model;

import java.awt.Graphics;
import java.util.ArrayList;

import Collections.Vector2;
import Model.Entity.Bomb;
import Model.Entity.Bot;
import Model.Entity.Character;
import Model.Entity.Player;
import Model.Map.TileMap;

/**
 * Xử lý tất cả logic chơi game ở đây
 */
public class GameManager {
    private static GameManager instance;

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    // Hệ thống
    // private PlayerController input;
    // Lưu tất cả các thứ cần thiết
    private TileMap tileMap;
    private Player player;
    private ArrayList<Bot> bots = new ArrayList<>();
    private ArrayList<Bomb> bombs = new ArrayList<>();

    private ArrayList<Character> characters = new ArrayList<>();

    private GameManager() {
        tileMap = new TileMap();

        setGameSetting(3, 1);
        // player = new Player(4 * GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE, 1);
        // bots.add(new Bot(1 * GS.Config.BLOCK_SIZE,2 * GS.Config.BLOCK_SIZE, 1, 1));

        // Không được xoá
        new Bomb(-999, -999);
    }

    public void setGameSetting(int botNum, int botDiff) {
        player = new Player(GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE, 1);
        Vector2[] botLocations = new Vector2[] { new Vector2(15 * GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE),
                new Vector2(GS.Config.BLOCK_SIZE, 13 * GS.Config.BLOCK_SIZE),
                new Vector2(15 * GS.Config.BLOCK_SIZE, 13 * GS.Config.BLOCK_SIZE) };

        bots.clear();
        for (int i = 0; i < botNum; ++i)
            bots.add(new Bot(botLocations[i].x, botLocations[i].y, 1, botDiff));
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public Character[] getAllCharacters() {
        if (bots != null) {
            Character[] characters = new Character[bots.size() + 1];
            characters[0] = player;
            for (int i = 0; i < bots.size(); ++i) {
                characters[i + 1] = bots.get(i);
            }
            return characters;
        }

        return new Character[] { player };
    }

    public void addBomb(Bomb bomb, Character character) {
        bombs.add(bomb);
        characters.add(character);
    }

    public void update() {
        for (int i = 0; i < bombs.size(); ++i) {
            Bomb bomb = bombs.get(i);
            bomb.update();
            if (bomb.isEndAnimation()) {
                bombs.remove(i);
                characters.remove(i);
                i -= 1;
            }
        }
        for (Bot bot : bots) {
            bot.update();
        }
        player.update();
        tileMap.update();
    }

    public void render(Graphics g) {
        tileMap.render(g);
        for (int i = 0; i < bombs.size(); ++i) {
            bombs.get(i).render(g);
        }
        for (Bot bot : bots) {
            bot.render(g);
        }
        player.render(g);
    }

    public boolean isCharacterPlaceBomb(Character character) {
        return characters.contains(character);
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public boolean deleteCharacter(Character character) {
        if (player.equals(character)) {
            checkWin();
            return true;
        } else if (bots.contains(character)) {
            checkWin();
            bots.remove(character);
            return true;
        }
        return false;
    }

    private void checkWin() {}
}
