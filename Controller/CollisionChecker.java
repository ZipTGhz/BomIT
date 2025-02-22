package Controller;

import Collections.Vector2;
import Model.Character;
import Model.IGameSettings;
import View.GamePanel;

public class CollisionChecker {
    private GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public boolean checkCollision(Character character, Vector2 direction) {
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
            int row1 = topLeftPos.y / IGameSettings.Config.BLOCK_SIZE;
            int row2 = bottomRight.y / IGameSettings.Config.BLOCK_SIZE;
            int col = topLeftPos.x / IGameSettings.Config.BLOCK_SIZE;

            int value1 = gp.getTileMap().getTileIndex(row1, col);
            int value2 = gp.getTileMap().getTileIndex(row2, col);

            return gp.getTileMap().getTile(value1).getIsCollision()
                    || gp.getTileMap().getTile(value2).getIsCollision();
        } else if (direction.equals(Vector2.right())) {
            int row1 = topLeftPos.y / IGameSettings.Config.BLOCK_SIZE;
            int row2 = bottomRight.y / IGameSettings.Config.BLOCK_SIZE;
            int col = bottomRight.x / IGameSettings.Config.BLOCK_SIZE;

            int value1 = gp.getTileMap().getTileIndex(row1, col);
            int value2 = gp.getTileMap().getTileIndex(row2, col);

            return gp.getTileMap().getTile(value1).getIsCollision()
                    || gp.getTileMap().getTile(value2).getIsCollision();
        } else if (direction.equals(Vector2.up())) {
            int row = topLeftPos.y / IGameSettings.Config.BLOCK_SIZE;
            int col1 = topLeftPos.x / IGameSettings.Config.BLOCK_SIZE;
            int col2 = bottomRight.x / IGameSettings.Config.BLOCK_SIZE;

            int value1 = gp.getTileMap().getTileIndex(row, col1);
            int value2 = gp.getTileMap().getTileIndex(row, col2);

            return gp.getTileMap().getTile(value1).getIsCollision()
                    || gp.getTileMap().getTile(value2).getIsCollision();
        } else if (direction.equals(Vector2.down())) {
            int row = bottomRight.y / IGameSettings.Config.BLOCK_SIZE;
            int col1 = topLeftPos.x / IGameSettings.Config.BLOCK_SIZE;
            int col2 = bottomRight.x / IGameSettings.Config.BLOCK_SIZE;

            int value1 = gp.getTileMap().getTileIndex(row, col1);
            int value2 = gp.getTileMap().getTileIndex(row, col2);

            return gp.getTileMap().getTile(value1).getIsCollision()
                    || gp.getTileMap().getTile(value2).getIsCollision();
        }


        return false;
    }
}
