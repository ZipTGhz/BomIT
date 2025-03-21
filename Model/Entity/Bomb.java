package Model.Entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import Collections.Vector2;
import Controller.CollisionChecker;
import Interfaces.IGS;
import Interfaces.IHash;
import Model.GameManager;

public class Bomb extends Entity {
    public static final double BOMB_TIME = 3;
    public static final double EXPLOSION_TIME = 0.25;

    private static String[] stateNames = new String[] { IHash.BombState.IDLE, IHash.BombState.EXPLOSION };

    public static ArrayList<Vector2> explodeZoneIfPlace(Vector2 position) {
        int blSz = IGS.BLOCK_SIZE;
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

    private ArrayList<Vector2> dmgZones;
    private double bombStartTime = 0;
    private boolean exploded = false;

    private boolean endAnimation = false;

    public Bomb(int x, int y) {
        super(x, y);
        initBomb();
    }

    public Bomb(Vector2 position) {
        super(position);
        initBomb();
    }

    public void update() {
        bombStartTime += IGS.DELTA_TIME;
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
            for (Vector2 pos : dmgZones)
                sr.render(g, pos);
        }

    }

    public void explode() {
        if (exploded)
            return;
        exploded = true;
        bombStartTime = 0;

        sr.changeState(IHash.BombState.EXPLOSION);

        Character[] characters = GameManager.getInstance().getAllCharacters();

        for (Vector2 pos : dmgZones) {
            int dmgRow = pos.y / IGS.BLOCK_SIZE, dmgCol = pos.x / IGS.BLOCK_SIZE;

            if (CollisionChecker.checkDestroy(pos)) {
                GameManager.getInstance().getTileMap().deleteTile(dmgRow, dmgCol);
            }

            Rectangle dmgZone = new Rectangle(pos.x, pos.y, IGS.BLOCK_SIZE, IGS.BLOCK_SIZE);
            for (Character character : characters) {
                Vector2 worldPos = character.position.add(character.collider.offset);
                Rectangle colliderRect = new Rectangle(worldPos.x, worldPos.y, character.collider.size.x,
                        character.collider.size.y);
                if (dmgZone.intersects(colliderRect)) {
                    character.damage();
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

    private void initBomb() {
        sr.setInterval(20);
        this.loadSprites(IHash.SpriteName.BOMB, stateNames);
        sr.changeState(IHash.BombState.IDLE);
        calculateExplodeZone();
    }

    private void calculateExplodeZone() {
        dmgZones = new ArrayList<>();
        int blSz = IGS.BLOCK_SIZE;
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
