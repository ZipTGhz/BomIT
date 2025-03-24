package Components.Front;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;

public class GameButton extends MouseAdapter {
    private int offset_x = 0, offset_y = 0;
    private BufferedImage normal, clicked, hover;
    private BufferedImage current;

    public GameButton(BufferedImage normal, BufferedImage clicked, BufferedImage hover, int offset_x, int offset_y) {
        this.normal = normal;
        this.clicked = clicked;
        this.hover = hover;

        this.offset_x = offset_x;
        this.offset_y = offset_y;

        this.current = this.normal;
    }

    public void render(Graphics g) {
        g.drawImage(current, offset_x, offset_y, current.getWidth(), current.getHeight(), null);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (this.clicked != null && isInside(e.getPoint())) {
            this.current = this.clicked;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.current = this.normal;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isInside(e.getPoint()))
            this.current = this.hover;
        else
            this.current = this.normal;
    }

    private boolean isInside(Point p) {
        return p.getX() >= offset_x && p.getX() <= offset_x + current.getWidth() && p.getY() >= offset_y
                && offset_y <= offset_y + current.getHeight();
    }

}
