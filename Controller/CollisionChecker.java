package Controller;

import java.awt.Rectangle;

import Collections.Vector2;
import Interfaces.IGS;
import Model.GameManager;
import Model.Entity.Bomb;
import Model.Entity.Character;
import Model.Map.Tile;

public class CollisionChecker {

    public static boolean checkCharacterCollision(Character character, Vector2 direction) {
        // Toạ độ gốc của người chơi
        Vector2 topLeftPos = character.getWorldPosition().add(character.getColliderOffset());
        Vector2 bottomRight = topLeftPos.add(character.getColliderSize());

        // Khoảng cách di chuyển
        Vector2 space = direction.scale(character.getSpeed());

        // Toạ độ của người chơi sau khi di chuyển
        topLeftPos = topLeftPos.add(space);
        bottomRight = bottomRight.add(space);

        // Kiểm tra 4 hướng xem có va chạm với khối chặn đường nào không.
        if (direction.equals(Vector2.left())) {
            int row1 = topLeftPos.y / IGS.BLOCK_SIZE;
            int row2 = bottomRight.y / IGS.BLOCK_SIZE;
            int col = topLeftPos.x / IGS.BLOCK_SIZE;

            int value1 = GameManager.getInstance().getTileMap().getTileIndex(row1, col);
            int value2 = GameManager.getInstance().getTileMap().getTileIndex(row2, col);

            return GameManager.getInstance().getTileMap().getTile(value1).getIsCollision()
                    || GameManager.getInstance().getTileMap().getTile(value2).getIsCollision();
        } else if (direction.equals(Vector2.right())) {
            int row1 = topLeftPos.y / IGS.BLOCK_SIZE;
            int row2 = bottomRight.y / IGS.BLOCK_SIZE;
            int col = bottomRight.x / IGS.BLOCK_SIZE;

            int value1 = GameManager.getInstance().getTileMap().getTileIndex(row1, col);
            int value2 = GameManager.getInstance().getTileMap().getTileIndex(row2, col);

            return GameManager.getInstance().getTileMap().getTile(value1).getIsCollision()
                    || GameManager.getInstance().getTileMap().getTile(value2).getIsCollision();
        } else if (direction.equals(Vector2.up())) {
            int row = topLeftPos.y / IGS.BLOCK_SIZE;
            int col1 = topLeftPos.x / IGS.BLOCK_SIZE;
            int col2 = bottomRight.x / IGS.BLOCK_SIZE;

            int value1 = GameManager.getInstance().getTileMap().getTileIndex(row, col1);
            int value2 = GameManager.getInstance().getTileMap().getTileIndex(row, col2);

            return GameManager.getInstance().getTileMap().getTile(value1).getIsCollision()
                    || GameManager.getInstance().getTileMap().getTile(value2).getIsCollision();
        } else if (direction.equals(Vector2.down())) {
            int row = bottomRight.y / IGS.BLOCK_SIZE;
            int col1 = topLeftPos.x / IGS.BLOCK_SIZE;
            int col2 = bottomRight.x / IGS.BLOCK_SIZE;

            int value1 = GameManager.getInstance().getTileMap().getTileIndex(row, col1);
            int value2 = GameManager.getInstance().getTileMap().getTileIndex(row, col2);

            return GameManager.getInstance().getTileMap().getTile(value1).getIsCollision()
                    || GameManager.getInstance().getTileMap().getTile(value2).getIsCollision();
        }

        return false;
    }

    /**
     * Kiểm tra xem khối tại vị trí đó có khả năng va chạm hay không
     * 
     * @param position
     * @return
     */
    public static boolean checkCollision(Vector2 position) {
        int tileRow = position.y / IGS.BLOCK_SIZE;
        int tileCol = position.x / IGS.BLOCK_SIZE;

        int tileIndex = GameManager.getInstance().getTileMap().getTileIndex(tileRow, tileCol);
        Tile tile = GameManager.getInstance().getTileMap().getTile(tileIndex);
        return tile != null && tile.getIsCollision();

    }

    /**
     * Kiểm tra xem khối tại vị trí đó có thể bị phá huỷ hay không
     * 
     * @param position
     * @return
     */
    public static boolean checkDestroy(Vector2 position) {
        int tileRow = position.y / IGS.BLOCK_SIZE;
        int tileCol = position.x / IGS.BLOCK_SIZE;

        int tileIndex = GameManager.getInstance().getTileMap().getTileIndex(tileRow, tileCol);
        Tile tile = GameManager.getInstance().getTileMap().getTile(tileIndex);
        return tile != null && tile.getCanDestroy();

    }

    public static boolean checkDestroy(Vector2 position, Character[] characters) { return false; }

    public static boolean checkBombCollision(Character c, Vector2 direction, Bomb bomb) {
        // Kiểm tra xem đối tượng e1 có nằm trong đối tượng e2 không
        // Nếu có thì trả về false
        // Sẽ cho phép đối tượng di chuyển ra ngoài như bom trước khi kiểm tra va chạm
        Vector2 topLeftE1 = c.getWorldPosition().add(c.getColliderOffset());
        Rectangle e1Rect = new Rectangle(topLeftE1.x, topLeftE1.y, c.getColliderSize().x, c.getColliderSize().y);
        Vector2 topLeftBomb = bomb.getWorldPosition();
        Rectangle bombRect = new Rectangle(topLeftBomb.x, topLeftBomb.y, IGS.BLOCK_SIZE, IGS.BLOCK_SIZE);
        if (e1Rect.intersects(bombRect)) {
            return false;
        }

        e1Rect.translate(direction.x, direction.y);
        if (e1Rect.intersects(bombRect)) {
            return true;
        }
        return false;
    }
}
