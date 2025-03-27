package Components.Front;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Collections.Vector2;
import Util.UtilityTools;

public class GameDialog {

    private int x = 0, y = 0, w = 50, h = 50;

    private GameButton btnClose;

    private ArrayList<BufferedImage> images = new ArrayList<>();
    private ArrayList<Vector2> imageOffsets = new ArrayList<>();

    private boolean enabled = false;

    public GameDialog(JPanel parent, int x, int y, int w, int h) {
        this.setBound(x, y, w, h);
        try {
            File[] files = UtilityTools.getFiles("/Resources/Common", ".png");
            BufferedImage closeButton = ImageIO.read(files[0]);

            btnClose = new GameButton(closeButton, null, null, this.x + this.w - closeButton.getWidth(), this.y);
            btnClose.registerAdapter(parent);
            btnClose.setOnClickAction(new Runnable() {
                @Override
                public void run() {
                    enabled = !enabled;
                    parent.repaint();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void pack() {
        int w = 0, h = 0;
        for (Vector2 v2 : imageOffsets) {
            w = Math.max(w, v2.x);
            h = Math.max(h, v2.y);
        }
        setSize(w, h);
    }

    public void addImage(BufferedImage img, int x, int y) {
        images.add(img);
        imageOffsets.add(new Vector2(this.x + x, this.y + y));
    }

    public void addImage(BufferedImage img, int x, int y, int w, int h) {
        images.add(UtilityTools.scaleImage(img, w, h));
        imageOffsets.add(new Vector2(this.x + x, this.y + y));
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int w, int h) {
        this.w = Math.max(50, w);
        this.h = Math.max(50, h);
        if (btnClose != null)
            btnClose.setLocation(this.x + this.w - btnClose.getWidth(), this.y);
    }

    public void setBound(int x, int y, int w, int h) {
        this.setLocation(x, y);
        this.setSize(w, h);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean state) {
        enabled = state;
    }

    public void render(Graphics g) {
        for (int i = 0; i < images.size(); ++i) {
            BufferedImage img = images.get(i);
            g.drawImage(img, imageOffsets.get(i).x, imageOffsets.get(i).y, Math.min(img.getWidth(), w),
                    Math.min(img.getHeight(), w), null);
        }
        btnClose.render(g);
    }
}
