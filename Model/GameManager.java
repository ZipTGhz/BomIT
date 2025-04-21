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
    private static final GameManager instance = new GameManager();

    public static GameManager getInstance() {
        return instance;
    }

    // Hệ thống
    // Lưu tất cả các thứ cần thiết
    private TileMap tileMap;
    private Player player;
    private ArrayList<Bot> bots;
    private ArrayList<Bomb> bombs;
    private ArrayList<Character> characters;

    private double remainingTime = 180;

    private GameManager() {
        setNewGame(3, 0);
    }

    public double getRemainingTime() {
        return remainingTime;
    }

    public void setNewGame(int botNum, int botDiff) {
        tileMap = new TileMap();
        bots = new ArrayList<>();
        bombs = new ArrayList<>();
        characters = new ArrayList<>();

        player = new Player(IGS.BLOCK_SIZE, IGS.BLOCK_SIZE, 2);
        Vector2[] botLocations = new Vector2[] { new Vector2(15 * IGS.BLOCK_SIZE, IGS.BLOCK_SIZE),
                new Vector2(IGS.BLOCK_SIZE, 13 * IGS.BLOCK_SIZE),
                new Vector2(15 * IGS.BLOCK_SIZE, 13 * IGS.BLOCK_SIZE) };

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
        if (remainingTime > 0)
            remainingTime = Math.max(0, remainingTime - IGS.DELTA_TIME);
        if (remainingTime <= 0)
            checkWin();
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

    private void checkWin() {
        // Nếu người chơi == null
        // Xử thua
        // Nếu chiều dài mảng bot = 0
        // Xử thắng
        if (remainingTime <= 0) {
            System.out.println("LOSE");
        }
        if (bots.size() == 0) {
            System.out.println("WIN");
        }
    }

    public void resetGameManager() {
        // Reset lại thời gian
//        remainingTime = 180;
//
//        // Lấy thông tin cài đặt mới
//        int botNum = GameSettings.getInstance().getNumEnemies();
//        int botDiff = GameSettings.getInstance().getDifficulty();
//
//        // Tạo lại toàn bộ game mới
//        setNewGame(botNum, botDiff);
    }

}
