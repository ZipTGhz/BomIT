package Model;

import java.awt.Graphics;
import java.util.ArrayList;
import Controller.CollisionChecker;
import Controller.PlayerController;

/**
 * Xử lý tất cả logic chơi game ở đây
 */
public class GameManager {
    private static GameManager instance;

    // Hệ thống
    private PlayerController input;
    private CollisionChecker collisionChecker;

    // Lưu tất cả các thứ cần thiết
    private TileMap tileMap;
    private Player player;
    private Bot[] bots;
    private ArrayList<Bomb> bombs = new ArrayList<>();
    private ArrayList<Character> characters = new ArrayList<>();

    private GameManager() {
        input = new PlayerController();
        collisionChecker = new CollisionChecker();

        tileMap = new TileMap();
        player = new Player(240, 240, 2);

        // Không được xoá
        new Bomb(-999, -999);
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public PlayerController getInput() {
        return input;
    }

    public CollisionChecker getCollisionChecker() {
        return collisionChecker;
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public Character[] getAllCharacters() {
        if (bots != null) {
            Character[] characters = new Character[bots.length + 1];
            characters[0] = player;
            System.arraycopy(bots, 0, characters, 1, bots.length);
            return characters;
        }

        return new Character[] {player};
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
        player.update();
        tileMap.update();
    }

    public void render(Graphics g) {
        tileMap.render(g);
        for (int i = 0; i < bombs.size(); ++i) {
            bombs.get(i).render(g);
        }
        player.render(g);
    }

    public boolean isCharacterPlaceBomb(Character character) {
        return characters.contains(character);
    }


}
