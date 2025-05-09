package Model.AI;

import Collections.Vector2;
import Interfaces.IGS;
import Model.Entity.Bomb;
import Model.GameManager;
import Model.Map.Tile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class AI {
    /**
     * Phương thức trả về danh sách đường đi đến đích với toạ độ của map
     * <p>
     * 1. Nếu AI gặp bom thì sẽ tìm đường né bom trước
     * <p>
     * 2. Nếu không tìm được đường đến đích thì trả về đường đi gần đích nhất
     * 
     * @param source toạ độ gốc
     * @param dist   toạ độ đích
     */
    public static ArrayList<Vector2> calculateMove(Vector2 source, Vector2 dist) {
        // Thuật toán BFS cơ bản
        source = source.clone();
        source.x /= IGS.BLOCK_SIZE;
        source.y /= IGS.BLOCK_SIZE;

        dist = dist.clone();
        dist.x /= IGS.BLOCK_SIZE;
        dist.y /= IGS.BLOCK_SIZE;

        // Mảng đánh dấu
        // Kiểm tra xem toạ độ đó đã được xét hay chưa
        Vector2 mapSize = GameManager.getInstance().getTileMap().getMapSize();
        boolean[][] seen = new boolean[mapSize.y][mapSize.x];

        // Trước hết ta nên kiểm tra xem AI có nằm trong vùng nổ bom không
        // Nếu có thì ta sẽ tìm đường đi tránh bom trước
        // Nếu không thì ta sẽ tìm đường đi bình thường

        boolean[][] dangerZone = GameManager.getInstance().getDangerZone();
        // boolean[][] dangerZone = new boolean[mapSize.y][mapSize.x];

        // ArrayList<Bomb> bombs = GameManager.getInstance().getBombs();
        // for (Bomb bomb : bombs) {
        // ArrayList<Vector2> explodeZone = bomb.getExplodeZone();
        // for (Vector2 zone : explodeZone) {
        // int zoneRow = zone.y / GS.Config.BLOCK_SIZE;
        // int zoneCol = zone.x / GS.Config.BLOCK_SIZE;
        // dangerZone[zoneRow][zoneCol] = true;
        // }
        // }

        boolean isSafeZone = true;
        if (dangerZone[source.y][source.x]) {
            dist = findClosestSafePosition(source, dangerZone, dist);
            isSafeZone = false;
        }

        // Hàng đợi
        Queue<Vector2> q = new LinkedList<>();
        q.add(source);
        seen[source.y][source.x] = true;

        // Bảng truy vết
        HashMap<Vector2, Vector2> mp = new HashMap<>();

        // 4 hướng di chuyển
        Vector2[] dir = new Vector2[] { Vector2.down(), Vector2.left(), Vector2.right(), Vector2.up() };

        Vector2 bestPoint = source;
        double minDist = Vector2.distance(source, dist);

        while (!q.isEmpty()) {
            Vector2 cur = q.remove();
            double curDist = Vector2.distance(cur, dist);
            if (curDist < minDist) {
                bestPoint = cur;
                minDist = curDist;
            }

            if (cur.equals(dist)) {
                break;
            }

            for (Vector2 d : dir) {
                Vector2 next = cur.add(d);
                if (next.x >= 0 && next.x < seen[0].length && next.y >= 0 && next.y < seen.length
                        && seen[next.y][next.x] == false
                        && (isSafeZone && dangerZone[next.y][next.x] == false || isSafeZone == false)) {
                    int index = GameManager.getInstance().getTileMap().getTileIndex(next.y, next.x);
                    Tile tile = GameManager.getInstance().getTileMap().getTile(index);
                    if (tile.getIsCollision() == false) {
                        mp.put(next, cur);
                        q.add(next);
                        seen[next.y][next.x] = true;
                    }
                }
            }

        }
        ArrayList<Vector2> tracePath = new ArrayList<>();
        while (mp.containsKey(bestPoint)) {
            tracePath.add(bestPoint.clone());
            bestPoint = mp.get(bestPoint);
        }
        if (tracePath.isEmpty())
            tracePath.add(source);

        Collections.reverse(tracePath);

        return tracePath;
    }

    private static Vector2 findClosestSafePosition(Vector2 source, boolean[][] dangerZone, Vector2 dist) {
        Vector2 mapSize = GameManager.getInstance().getTileMap().getMapSize();
        boolean[][] seen = new boolean[mapSize.y][mapSize.x];

        Queue<Vector2> q = new LinkedList<>();
        q.add(source);
        seen[source.y][source.x] = true;

        Vector2[] dir = new Vector2[] { Vector2.left(), Vector2.right(), Vector2.up(), Vector2.down() };

        Vector2 dirToPlayer = Vector2.getDirection(source, dist);
        if (dirToPlayer.equals(Vector2.zero()) == false) {
            // System.out.println(dirToPlayer.toString());
            Arrays.sort(dir, (a, b) -> {
                if (a.equals(dirToPlayer))
                    return 1;
                if (b.equals(dirToPlayer))
                    return -1;
                return 0;
            });
        }

        while (q.size() != 0) {
            Vector2 cur = q.remove();
            if (dangerZone[cur.y][cur.x] == false) {
                return cur;
            }

            for (Vector2 d : dir) {
                Vector2 next = cur.add(d);
                if (next.x >= 0 && next.x < seen[0].length && next.y >= 0 && next.y < seen.length
                        && seen[next.y][next.x] == false) {
                    int index = GameManager.getInstance().getTileMap().getTileIndex(next.y, next.x);
                    Tile tile = GameManager.getInstance().getTileMap().getTile(index);
                    if (tile.getIsCollision() == false) {
                        q.add(next);
                        seen[next.y][next.x] = true;
                    }
                }
            }
        }

        return source;
    }

    public static boolean isInDangerZone(Vector2 pos) {
        pos = pos.clone();
        pos.x /= IGS.BLOCK_SIZE;
        pos.y /= IGS.BLOCK_SIZE;

        Vector2 mapSize = GameManager.getInstance().getTileMap().getMapSize();
        boolean[][] dangerZone = new boolean[mapSize.y][mapSize.x];

        ArrayList<Bomb> bombs = GameManager.getInstance().getBombs();
        for (Bomb bomb : bombs) {
            ArrayList<Vector2> explodeZone = bomb.getExplodeZone();
            for (Vector2 zone : explodeZone) {
                int zoneRow = zone.y / IGS.BLOCK_SIZE;
                int zoneCol = zone.x / IGS.BLOCK_SIZE;
                dangerZone[zoneRow][zoneCol] = true;
            }
        }
        return dangerZone[pos.y][pos.x];
    }
}
