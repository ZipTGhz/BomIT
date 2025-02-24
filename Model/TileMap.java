package Model;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
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

		String path = "/Resources/map.txt";
		loadMapFromTxt(path);
	}

	private void loadTileSet() {
		try {
			BufferedImage grassImage =
					ImageIO.read(getClass().getResourceAsStream("/Resources/Grass.png"));
			grassImage = UtilityTools.scaleImage(grassImage, GS.Config.BLOCK_SIZE,
					GS.Config.BLOCK_SIZE);
			Tile grass = new Tile(false, false, grassImage);
			tileSet.add(grass);

			BufferedImage wallImage =
					ImageIO.read(getClass().getResourceAsStream("/Resources/Wall.png"));
			wallImage = UtilityTools.scaleImage(wallImage, GS.Config.BLOCK_SIZE,
					GS.Config.BLOCK_SIZE);
			Tile wall = new Tile(false, true, wallImage);
			tileSet.add(wall);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void loadMapFromTxt(String path) {

		try {
			InputStream is = getClass().getResourceAsStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}

				String[] words = line.split(" ");

				ArrayList<Integer> row = new ArrayList<>();
				for (String w : words) {
					row.add(Integer.parseInt(w));
				}

				map.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void update() {

	}

	public void render(Graphics g) {
		int width = tileSet.get(0).getImage().getWidth();

		int m = map.size(), n = map.get(0).size();
		for (int i = 0; i < m; ++i) {
			for (int j = 0; j < n; ++j) {
				int index = map.get(i).get(j);
				g.drawImage(tileSet.get(index).getImage(), j * width, i * width, null);
			}
		}
	}

	public int getTileIndex(int row, int col) {
		return map.get(row).get(col);
	}

	public Tile getTile(int index) {
		return tileSet.get(index);
	}
}
