package Controller;

import Collections.Vector2;
import Model.Character;
import Model.Entity;
import Model.GameManager;
import Model.Tile;
import Model.GS;

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
            int row1 = topLeftPos.y / GS.Config.BLOCK_SIZE;
            int row2 = bottomRight.y / GS.Config.BLOCK_SIZE;
            int col = topLeftPos.x / GS.Config.BLOCK_SIZE;

            int value1 = GameManager.getInstance().getTileMap().getTileIndex(row1, col);
            int value2 = GameManager.getInstance().getTileMap().getTileIndex(row2, col);

            return GameManager.getInstance().getTileMap().getTile(value1).getIsCollision()
                    || GameManager.getInstance().getTileMap().getTile(value2).getIsCollision();
        } else if (direction.equals(Vector2.right())) {
            int row1 = topLeftPos.y / GS.Config.BLOCK_SIZE;
            int row2 = bottomRight.y / GS.Config.BLOCK_SIZE;
            int col = bottomRight.x / GS.Config.BLOCK_SIZE;

            int value1 = GameManager.getInstance().getTileMap().getTileIndex(row1, col);
            int value2 = GameManager.getInstance().getTileMap().getTileIndex(row2, col);

            return GameManager.getInstance().getTileMap().getTile(value1).getIsCollision()
                    || GameManager.getInstance().getTileMap().getTile(value2).getIsCollision();
        } else if (direction.equals(Vector2.up())) {
            int row = topLeftPos.y / GS.Config.BLOCK_SIZE;
            int col1 = topLeftPos.x / GS.Config.BLOCK_SIZE;
            int col2 = bottomRight.x / GS.Config.BLOCK_SIZE;

            int value1 = GameManager.getInstance().getTileMap().getTileIndex(row, col1);
            int value2 = GameManager.getInstance().getTileMap().getTileIndex(row, col2);

            return GameManager.getInstance().getTileMap().getTile(value1).getIsCollision()
                    || GameManager.getInstance().getTileMap().getTile(value2).getIsCollision();
        } else if (direction.equals(Vector2.down())) {
            int row = bottomRight.y / GS.Config.BLOCK_SIZE;
            int col1 = topLeftPos.x / GS.Config.BLOCK_SIZE;
            int col2 = bottomRight.x / GS.Config.BLOCK_SIZE;

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
        int tileRow = position.y / GS.Config.BLOCK_SIZE;
        int tileCol = position.x / GS.Config.BLOCK_SIZE;

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
        int tileRow = position.y / GS.Config.BLOCK_SIZE;
        int tileCol = position.x / GS.Config.BLOCK_SIZE;

        int tileIndex = GameManager.getInstance().getTileMap().getTileIndex(tileRow, tileCol);
        Tile tile = GameManager.getInstance().getTileMap().getTile(tileIndex);
        return tile != null && tile.getCanDestroy();

    }

    public static boolean checkDestroy(Vector2 position, Character[] characters) { return false; }

    public static boolean checkEntityCollision(Entity e1, Vector2 direction, Entity e2) {
        return false;
    }
}
