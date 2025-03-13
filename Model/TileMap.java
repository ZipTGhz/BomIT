package Model;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import Util.UtilityTools;

public class TileMap {
    private ArrayList<Tile> tileSet;
    private ArrayList<ArrayList<Integer>> map;

    public TileMap() {
        tileSet = new ArrayList<>();
        map = new ArrayList<>();
        loadTileSet();

        String path = "/Resources/map.csv";
        loadMap(path);
    }

    private void loadTileSet() {
        int subImageSize = 16;
        boolean[][] tileConfig = {
                { false, false },
                { false, true },
                { true, true },
                { true, true },
                { true, true },
                { true, true },
                { true, true },
        };
        try {
            InputStream is = getClass().getResourceAsStream("/Resources/tileSet.png");
            BufferedImage tileSets = ImageIO.read(is);
            int tileIndex = 0;
            for (int j = 0; j < tileSets.getHeight(); j += subImageSize) {
                for (int i = 0; i < tileSets.getWidth(); i += subImageSize) {
                    BufferedImage tileImage = tileSets.getSubimage(i, j, subImageSize, subImageSize);
                    tileImage = UtilityTools.scaleImage(tileImage, GS.Config.BLOCK_SIZE, GS.Config.BLOCK_SIZE);
                    Tile tile = new Tile(tileConfig[tileIndex][0], tileConfig[tileIndex][1], tileImage);
                    tileIndex += 1;
                    tileSet.add(tile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadMap(String path) {
        try (InputStream is = getClass().getResourceAsStream(path);
                BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            if (is == null) { // Kiểm tra xem file có tồn tại không
                throw new FileNotFoundException("File không tồn tại: " + path);
            }

            map.clear(); // Xóa dữ liệu cũ trước khi nạp map mới

            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.split(",");

                ArrayList<Integer> row = new ArrayList<>();
                for (String w : words) {
                    row.add(Integer.parseInt(w.trim()));
                }

                map.add(row);
            }

        } catch (FileNotFoundException e) {
            System.err.println("Lỗi: Không tìm thấy file " + path);
        } catch (IOException e) {
            System.err.println("Lỗi khi đọc file " + path);
        } catch (NumberFormatException e) {
            System.err.println("Lỗi: Dữ liệu trong file không phải số nguyên.");
        }
    }

    public void update() {

    }

    public void render(Graphics g) {
        int width = GS.Config.BLOCK_SIZE;
        int m = map.size(), n = map.get(0).size();
        // Vẽ nền cỏ trước
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                g.drawImage(tileSet.get(0).getImage(), j * width, i * width, null);
            }
        }

        // Vẽ vật cản sau
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                int index = map.get(i).get(j);
                g.drawImage(tileSet.get(index).getImage(), j * width, i * width, null);
            }
        }
    }

    public int getTileIndex(int row, int col) {
        if (row >= 0 && row < map.size() && col >= 0 && col < map.get(0).size()) {
            return map.get(row).get(col);
        }
        return -1;
    }

    public Tile getTile(int index) {
        if (index >= 0 && index < tileSet.size()) {
            return tileSet.get(index);
        }
        return null;
    }

    public void deleteTile(int row, int col) {
        map.get(row).set(col, 0);
    }
}
