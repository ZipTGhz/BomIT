package Model;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import Collections.Vector2;
import Components.SpriteRenderer;
import Controller.CollisionChecker;
import Util.UtilityTools;

public class Bomb {
    public static final int BOMB_TIME = 3000;
    public static final int EXPLOSION_TIME = 250;

    private static final Map<String, BufferedImage> bombSprites = new HashMap<>();
    private static final Map<String, BufferedImage> explosionSprites = new HashMap<>();

    private ArrayList<Vector2> dmgZones = new ArrayList<>();

    static {
        loadSprites();
    }

    private static void loadSprites() {
        try {
            for (int i = 3; i <= 19; ++i) {
                String path = "/Resources/Bombs_etc/Bomb/bomb" + i + ".png";
                BufferedImage image = ImageIO.read(Bomb.class.getResourceAsStream(path));
                image = UtilityTools.scaleImage(image, GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE);
                bombSprites.put("bomb" + i, image);
            }

            for (int i = 2; i <= 18; ++i) {
                String path = "/Resources/Bombs_etc/Explosion/Explosion" + i + ".png";
                BufferedImage image = ImageIO.read(Bomb.class.getResourceAsStream(path));
                image = UtilityTools.scaleImage(image, GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE);
                explosionSprites.put("explosion" + i, image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Vector2 position;
    private SpriteRenderer spriteRenderer;

    private long bombStartTime;
    private boolean exploded = false;
    private boolean endAnimation = false;

    public Bomb(int x, int y) {
        this.position = new Vector2(x, y);
        spriteRenderer = new SpriteRenderer();
        this.bombStartTime = System.currentTimeMillis();
        initBoom();
    }

    public Bomb(Vector2 position) {
        this.position = position.clone();
        spriteRenderer = new SpriteRenderer();
        this.bombStartTime = System.currentTimeMillis();
        initBoom();
    }

    private void initBoom() {
        spriteRenderer.addState(IHash.BoomState.IDLE);
        spriteRenderer.addState(IHash.BoomState.EXPLOSION);
        spriteRenderer.setInterval(16);

        for (int i = 3; i <= 19; ++i) {
            spriteRenderer.addSprite(IHash.BoomState.IDLE, bombSprites.get("bomb" + i));
        }

        for (int i = 2; i <= 18; ++i) {
            spriteRenderer.addSprite(IHash.BoomState.EXPLOSION,
                    explosionSprites.get("explosion" + i));
        }

        spriteRenderer.changeState(IHash.BoomState.IDLE);
    }

    public void update() {
        long curTime = System.currentTimeMillis();
        if (!exploded && curTime - bombStartTime >= BOMB_TIME) {
            explode();
        }

        if (exploded && curTime - bombStartTime >= EXPLOSION_TIME) {
            endAnimation = true;
        }
    }

    public void render(Graphics g) {
        if (!exploded) {
            spriteRenderer.render(g, position);
        } else {
            for (Vector2 pos : dmgZones) {
                spriteRenderer.render(g, pos);
            }
        }

    }

    public void explode() {
        if (exploded)
            return;
        exploded = true;
        bombStartTime = System.currentTimeMillis();

        spriteRenderer.changeState(IHash.BoomState.EXPLOSION);

        int blSz = GS.Config.BLOCK_SIZE;
        Vector2[][] explodeZone = new Vector2[][] {
                new Vector2[] { this.position, },
                new Vector2[] { this.position.add(-blSz, 0), this.position.add(-2 * blSz, 0) },
                new Vector2[] { this.position.add(blSz, 0), this.position.add(2 * blSz, 0) },
                new Vector2[] { this.position.add(0, -blSz), this.position.add(0, -2 * blSz) },
                new Vector2[] { this.position.add(0, blSz), this.position.add(0, 2 * blSz) }
        };
        Character[] characters = GameManager.getInstance().getAllCharacters();

        dmgZones.clear();

        for (Vector2[] zone : explodeZone) {
            for (Vector2 z : zone) {
                if (!CollisionChecker.checkCollision(z)) {
                    dmgZones.add(z);
                } else {
                    if (CollisionChecker.checkDestroy(z)) {
                        dmgZones.add(z);
                    }
                    break;
                }
            }
        }

        for (Vector2 pos : dmgZones) {
            int dmgRow = pos.y / GS.Config.BLOCK_SIZE, dmgCol = pos.x / GS.Config.BLOCK_SIZE;

            if (CollisionChecker.checkDestroy(pos)) {
                GameManager.getInstance().getTileMap().deleteTile(dmgRow, dmgCol);
            }

            Rectangle dmgZone = new Rectangle(pos.x, pos.y, GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE);
            for (Character character : characters) {
                Vector2 worldPos = character.position.add(character.collider.offset);
                Rectangle colliderRect = new Rectangle(worldPos.x, worldPos.y,
                        character.collider.size.x, character.collider.size.y);
                if (dmgZone.intersects(colliderRect)) {
                    System.out.println("Aww!");
                }
            }
        }
    }

    public boolean isEndAnimation() {
        return endAnimation;
    }

}
