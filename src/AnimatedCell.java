import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimatedCell extends JPanel implements ActionListener {

    char symbol = ' ';
    float progress = 0f; // 0  - 1.0
    Timer timer;

    boolean winning;

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public AnimatedCell() {
        setBackground(Color.black);
        setBorder(BorderFactory.createLineBorder(new Color(255, 0, 255), 3));
        setPreferredSize( new Dimension(150,150));
    }
    public void setWinning(boolean win){
        winning = win;
        repaint();
    }

    public void setSymbol(char s) {
        if (symbol != ' ') return;
        symbol = s;
        startAnimation();
    }

    private void startAnimation() {
        progress = 0f;
        timer = new Timer(16, e -> {
            progress += 0.05f;
            if (progress > 1f) {
                progress = 1f;
                timer.stop();
            }
            repaint();
        });
        timer.start();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (symbol == ' ') return;

        Graphics2D g2 = (Graphics2D) g;

        if (winning) {
            g2 = (Graphics2D) g;
            g2.setColor(new Color(255, 0, 255, 60)); // neon glow
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        if (winning) {
            g2.setColor(Color.MAGENTA); // winning color
        } else {
            g2.setColor(Color.CYAN); // normal
        }
        g2.setStroke(new BasicStroke(6));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int pad = 20;

        if (symbol == 'X') {
            drawAnimatedX(g2, w, h, pad);
        } else if (symbol == 'O') {
            drawAnimatedO(g2, w, h, pad);
        }
        g2.dispose();
    }
    public void reset() {
        symbol = ' ';
        progress = 0f;
        repaint();
    }


    private void drawAnimatedO(Graphics2D g2, int w, int h, int pad) {
        int size = Math.min(w, h) - 2 * pad;
        g2.drawArc(
                pad,
                pad,
                size,
                size,
                90,
                (int) (-360 * progress)
        );

    }

    private void drawAnimatedX(Graphics2D g2, int w, int h, int pad) {

        float p = progress;

        // First line
        if (p <= 0.5f) {
            g2.drawLine(
                    pad,
                    pad,
                    (int) (pad + (w - 2 * pad) * (p * 2)),
                    (int) (pad + (h - 2 * pad) * (p * 2))
            );
        } else {
            g2.drawLine(pad, pad, w - pad, h - pad);
        }
        // Second line
        if (p > 0.5f) {
            float t = (p - 0.5f) * 2;
            g2.drawLine(
                    w - pad,
                    pad,
                    (int) (w - pad - (w - 2 * pad) * t),
                    (int) (pad + (h - 2 * pad) * t)
            );
        }
    }
}
