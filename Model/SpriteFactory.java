package Model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import Interfaces.IGS;
import Interfaces.IHash;
import Model.Entity.Bomb;
import Util.UtilityTools;

public class SpriteFactory {
    private static SpriteFactory instance = new SpriteFactory();

    public static SpriteFactory getInstance() {
        return instance;
    }

    private HashMap<String, ImageCategory> mp = new HashMap<>();
    private ArrayList<ImageCategory> factory;
    private Random random;

    private SpriteFactory() {
        loadSprites();
    }

    public ImageCategory getImageCategory(String key) {
        return mp.getOrDefault(key, null);
    }

    public ImageCategory getRandCharImg() {
        if (random == null)
            random = new Random();
        int index = random.nextInt(factory.size());
        ImageCategory target = factory.remove(index);
        return target;
    }

    public void reset() {
        if (factory == null)
            factory = new ArrayList<>();
        factory.clear();
        for (int i = 1; i <= 6; ++i) {
            String key = "Char" + String.valueOf(i);
            factory.add(mp.get(key));
        }
    }

    private void loadSprites() {
        loadBomb();
        loadChars();
        reset();
    }

    private void loadBomb() {
        ImageCategory imageCategory = new ImageCategory();
        try {
            URL url = Bomb.class.getResource("/Resources/Bomb");
            File folder = new File(url.toURI());
            File[] files = folder.listFiles((FilenameFilter) (_, name) -> name.endsWith(".png"));
            for (File file : files) {
                BufferedImage image = UtilityTools.scaleImage(ImageIO.read(file), IGS.BLOCK_SIZE, IGS.BLOCK_SIZE);
                if (file.getName().startsWith("bomb"))
                    imageCategory.addImage(IHash.BombState.IDLE, image);
                else
                    imageCategory.addImage(IHash.BombState.EXPLOSION, image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mp.put(IHash.SpriteName.BOMB, imageCategory);
    }

    private void loadChars() {
        String[] charState = new String[] { IHash.CharacterState.IDLE_DOWN, IHash.CharacterState.IDLE_LEFT,
                IHash.CharacterState.IDLE_RIGHT, IHash.CharacterState.IDLE_UP, IHash.CharacterState.MOVE_DOWN,
                IHash.CharacterState.MOVE_LEFT, IHash.CharacterState.MOVE_RIGHT, IHash.CharacterState.MOVE_UP };

        // B1: Đọc thư mục của character i
        for (int i = 1; i <= 6; ++i) {
            try {
                URL url = getClass().getResource("/Resources/Characters/Char " + String.valueOf(i));
                File folder = new File(url.toURI());
                File[] files = folder.listFiles((_, name) -> name.endsWith(".png"));

                // B2: Cắt ảnh
                BufferedImage[] moveImages = UtilityTools.splitImage(ImageIO.read(files[0]), 48);
                BufferedImage[] idleImages = UtilityTools.splitImage(ImageIO.read(files[1]), 48);

                // B3: Nhập vào image category
                ImageCategory imageCategory = new ImageCategory();
                for (int j = 0; j < moveImages.length; ++j)
                    imageCategory.addImage(charState[j / 4 + 4], moveImages[j]);
                for (int j = 0; j < idleImages.length; ++j)
                    imageCategory.addImage(charState[j / 4], idleImages[j]);

                // B4: Chuyển image category vào hash map
                String key = "Char" + String.valueOf(i);
                mp.put(key, imageCategory);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
