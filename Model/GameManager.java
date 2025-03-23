package Model;

import java.awt.Graphics;
import java.util.ArrayList;

import Collections.Vector2;
import Interfaces.IGS;
import Model.Entity.Bomb;
import Model.Entity.Bot;
import Model.Entity.Character;
import Model.Entity.Player;
import Model.Map.TileMap;

/**
 * Xử lý tất cả logic chơi game ở đây
 */
public class GameManager {
    private static GameManager instance = new GameManager();

    public static GameManager getInstance() {
        return instance;
    }

    // Hệ thống
    // Lưu tất cả các thứ cần thiết
    private TileMap tileMap;
    private Player player;
    private ArrayList<Bot> bots = new ArrayList<>();
    private ArrayList<Bomb> bombs = new ArrayList<>();
    private ArrayList<Character> characters = new ArrayList<>();

    private GameManager() {
        tileMap = new TileMap();

        setGameSetting(3, 0);

        // player = new Player(1 * IGS.BLOCK_SIZE, IGS.BLOCK_SIZE, 1);
        // bots.add(new Bot(15 * IGS.BLOCK_SIZE, 1 * IGS.BLOCK_SIZE, 1, 1));

        // player = new Player(4 * GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE, 1);
        // bots.add(new Bot(1 * GS.Config.BLOCK_SIZE,2 * GS.Config.BLOCK_SIZE, 1, 1));
    }

    public void setGameSetting(int botNum, int botDiff) {
        player = new Player(IGS.BLOCK_SIZE, IGS.BLOCK_SIZE, 2);
        Vector2[] botLocations = new Vector2[] { new Vector2(15 * IGS.BLOCK_SIZE, IGS.BLOCK_SIZE),
                new Vector2(IGS.BLOCK_SIZE, 13 * IGS.BLOCK_SIZE),
                new Vector2(15 * IGS.BLOCK_SIZE, 13 * IGS.BLOCK_SIZE) };

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
            System.arraycopy(bots.toArray(new Character[0]), 0, characters, 1, bots.size());
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
        if (player != null)
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
        if (player != null)
            player.render(g);
    }

    public boolean isCharacterPlaceBomb(Character character) {
        return characters.contains(character);
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public boolean deleteCharacter(Character character) {
        if (character instanceof Player) {
            System.out.println("LOSE");
            checkWin();
            return true;
        } else if (bots.contains(character)) {
            bots.remove(character);
            checkWin();
            return true;
        }
        return false;
    }

    private void checkWin() {
        // Nếu người chơi == null
        // Xử thua
        // Nếu chiều dài mảng bot = 0
        // Xử thắng
        if (bots.size() == 0) {
            System.out.println("WIN");
        }
    }

    public boolean[][] getDangerZone() {
        Vector2 mapSize = tileMap.getMapSize();
        boolean[][] dangerZone = new boolean[mapSize.y][mapSize.x];

        for (Bomb bomb : bombs) {
            ArrayList<Vector2> explodeZone = bomb.getExplodeZone();
            for (Vector2 zone : explodeZone) {
                int zoneRow = zone.y / IGS.BLOCK_SIZE;
                int zoneCol = zone.x / IGS.BLOCK_SIZE;
                dangerZone[zoneRow][zoneCol] = true;
            }
        }
        return dangerZone;
    }
}
