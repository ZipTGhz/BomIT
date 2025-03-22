package Model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class ImageCategory {
    private Map<String, ArrayList<BufferedImage>> categoryMap = new LinkedHashMap<>();

    public void addImage(String key, BufferedImage image) {
        categoryMap.computeIfAbsent(key, _ -> new ArrayList<>()).add(image);
    }

    public ArrayList<BufferedImage> getImages(String key) {
        return categoryMap.getOrDefault(key, new ArrayList<>());
    }

    public BufferedImage getThumnail() {
        for (ArrayList<BufferedImage> images : categoryMap.values())
            return images.get(0);
        return null;
    }
}
