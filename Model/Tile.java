package Model;

import java.awt.image.BufferedImage;

public class Tile {
    private boolean canDestroy;
    private boolean isCollision;
    private BufferedImage image;

    public Tile(boolean canDestroy, boolean isCollision, BufferedImage image) {
        this.canDestroy = canDestroy;
        this.isCollision = isCollision;
        this.image = image;
    }

    public boolean getCanDestroy() { return canDestroy; }

    public boolean getIsCollision() { return isCollision; }

    public BufferedImage getImage() { return image; }
}
