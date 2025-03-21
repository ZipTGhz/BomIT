package Model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class ImageCategory {
    private HashMap<String, ArrayList<BufferedImage>> categoryMap = new HashMap<>();

    public void addImage(String key, BufferedImage image) {
        categoryMap.computeIfAbsent(key, _ -> new ArrayList<>()).add(image);
    }

    public ArrayList<BufferedImage> getImages(String key) {
        return categoryMap.getOrDefault(key, new ArrayList<>());
    }
}
