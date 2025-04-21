package Components.Front;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import Util.UtilityTools;

public class GameButton extends MouseAdapter {
    public static final int NORMAL = 0, CLICKED = 1, HOVER = 2;

    private boolean toggle = false;
    private int offset_x = 0, offset_y = 0;
    private BufferedImage normal, clicked, hover;
    private BufferedImage current;

    private Runnable onClickAction;

    public GameButton(BufferedImage normal, BufferedImage clicked, BufferedImage hover, int x, int y) {
        this.normal = normal;
        this.clicked = clicked;
        this.hover = hover;

        this.offset_x = x;
        this.offset_y = y;

        this.current = this.normal;
    }

    public GameButton(BufferedImage normal, BufferedImage clicked, BufferedImage hover, int x, int y, int w, int h) {
        this(normal != null ? UtilityTools.scaleImage(normal, w, h) : null,
                clicked != null ? UtilityTools.scaleImage(clicked, w, h) : null,
                hover != null ? UtilityTools.scaleImage(hover, w, h) : null, x, y);
    }

    public void setLocation(int x, int y) {
        this.offset_x = x;
        this.offset_y = y;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    public void setOnClickAction(Runnable action) {
        this.onClickAction = action;
    }

    public void registerAdapter(Component parent) {
        parent.addMouseListener(this);
        parent.addMouseMotionListener(this);
    }

    public void render(Graphics g) {
        g.drawImage(current, offset_x, offset_y, current.getWidth(), current.getHeight(), null);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (toggle == false && this.clicked != null && isInside(e.getPoint())) {
            this.current = this.clicked;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (toggle == false)
            this.current = this.normal;
        else if (isInside(e.getPoint()))
            this.current = getCurrentState() == NORMAL ? this.clicked : this.normal;

        if (isInside(e.getPoint()) && onClickAction != null)
            onClickAction.run();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (toggle == false) {
            if (this.hover != null && isInside(e.getPoint()))
                this.current = this.hover;
            else
                this.current = this.normal;
        }
    }

    public int getCurrentState() {
        if (current == normal)
            return NORMAL;
        if (current == clicked)
            return CLICKED;
        if (current == hover)
            return HOVER;
        return -1;
    }

    public int getWidth() {
        return normal.getWidth();
    }

    public int getHeight() {
        return normal.getHeight();
    }

    private boolean isInside(Point p) {
        return p.getX() >= offset_x && p.getX() <= offset_x + current.getWidth() && p.getY() >= offset_y
                && p.getY() <= offset_y + current.getHeight();
    }
}
