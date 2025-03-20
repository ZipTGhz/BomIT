package Model.Entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import Collections.Vector2;
import Controller.CollisionChecker;
import Model.GS;
import Model.GameManager;
import Model.IHash;
import Util.UtilityTools;

public class Bomb extends Entity {
    public static final double BOMB_TIME = 3;
    public static final double EXPLOSION_TIME = 0.25;

    private static final Map<String, BufferedImage> bombSprites = new HashMap<>();
    private static final Map<String, BufferedImage> explosionSprites = new HashMap<>();

    private static boolean staticInitDone = false;

    static {
        loadSprites();
    }

    public static ArrayList<Vector2> explodeZoneIfPlace(Vector2 position) {
        int blSz = GS.Config.BLOCK_SIZE;
        Vector2[][] explodeZone = new Vector2[][] { new Vector2[] { position, },
                new Vector2[] { position.add(-blSz, 0), position.add(-2 * blSz, 0) },
                new Vector2[] { position.add(blSz, 0), position.add(2 * blSz, 0) },
                new Vector2[] { position.add(0, -blSz), position.add(0, -2 * blSz) },
                new Vector2[] { position.add(0, blSz), position.add(0, 2 * blSz) } };

        ArrayList<Vector2> res = new ArrayList<>();

        for (Vector2[] zone : explodeZone) {
            for (Vector2 z : zone) {
                if (!CollisionChecker.checkCollision(z)) {
                    res.add(z);
                } else {
                    if (CollisionChecker.checkDestroy(z)) {
                        res.add(z);
                    }
                    break;
                }
            }
        }
        return res;
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

    private ArrayList<Vector2> dmgZones = new ArrayList<>();
    private double bombStartTime = 0;
    private boolean exploded = false;

    private boolean endAnimation = false;

    public Bomb(int x, int y) {
        super(x, y);
        initBoom();
    }

    public Bomb(Vector2 position) {
        super(position);
        initBoom();
    }

    public void update() {
        bombStartTime += GS.Config.DELTA_TIME;
        if (!exploded && bombStartTime >= BOMB_TIME) {
            explode();
        }

        if (exploded && bombStartTime >= EXPLOSION_TIME) {
            endAnimation = true;
        }
    }

    public void render(Graphics g) {
        if (!exploded) {
            sr.render(g, position);
        } else {
            for (Vector2 pos : dmgZones) {
                sr.render(g, pos);
            }
        }

    }

    public void explode() {
        if (exploded)
            return;
        exploded = true;
        bombStartTime = 0;

        sr.changeState(IHash.BoomState.EXPLOSION);

        Character[] characters = GameManager.getInstance().getAllCharacters();

        for (Vector2 pos : dmgZones) {
            int dmgRow = pos.y / GS.Config.BLOCK_SIZE, dmgCol = pos.x / GS.Config.BLOCK_SIZE;

            if (CollisionChecker.checkDestroy(pos)) {
                GameManager.getInstance().getTileMap().deleteTile(dmgRow, dmgCol);
            }

            Rectangle dmgZone = new Rectangle(pos.x, pos.y, GS.Config.BLOCK_SIZE,
                    GS.Config.BLOCK_SIZE);
            for (Character character : characters) {
                Vector2 worldPos = character.position.add(character.collider.offset);
                Rectangle colliderRect = new Rectangle(worldPos.x, worldPos.y,
                        character.collider.size.x, character.collider.size.y);
                if (dmgZone.intersects(colliderRect)) {
                    System.out.println("Aww!");
                    character.die();
                }
            }
        }
    }

    public boolean isEndAnimation() {
        return endAnimation;
    }

    public ArrayList<Vector2> getExplodeZone() {
        return this.dmgZones;
    }

    public Vector2 getPosition() {
        return position;
    }

    private void initBoom() {
        sr.addState(IHash.BoomState.IDLE);
        sr.addState(IHash.BoomState.EXPLOSION);
        sr.setInterval(16);

        for (int i = 3; i <= 19; ++i) {
            sr.addSprite(IHash.BoomState.IDLE, bombSprites.get("bomb" + i));
        }

        for (int i = 2; i <= 18; ++i) {
            sr.addSprite(IHash.BoomState.EXPLOSION, explosionSprites.get("explosion" + i));
        }

        sr.changeState(IHash.BoomState.IDLE);

        if (staticInitDone)
            calculateExplodeZone();
        staticInitDone = true;
    }

    private void calculateExplodeZone() {
        int blSz = GS.Config.BLOCK_SIZE;
        Vector2[][] explodeZone = new Vector2[][] { new Vector2[] { this.position, },
                new Vector2[] { this.position.add(-blSz, 0), this.position.add(-2 * blSz, 0) },
                new Vector2[] { this.position.add(blSz, 0), this.position.add(2 * blSz, 0) },
                new Vector2[] { this.position.add(0, -blSz), this.position.add(0, -2 * blSz) },
                new Vector2[] { this.position.add(0, blSz), this.position.add(0, 2 * blSz) } };

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
    }
}
