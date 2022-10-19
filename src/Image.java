import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Image extends JPanel {

    private int width;
    private int height;

        private BufferedImage img;

        public Image(int width, int height) {
            this.width = width;
            this.height = height;


            img = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < img.getWidth(); x++) {
                for (int y = 0; y < img.getHeight(); y++) {
                    img.setRGB(x, y, Color.OPAQUE);
                }
            }

//            Graphics2D g2d = img.createGraphics();
//            g2d.setColor(Color.RED);
//            g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
//            g2d.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(img, 0, 0, this);
            g2d.dispose();
        }

        public void drawPx(int x, int y) {
            try {
                img.setRGB(x, y, Color.ORANGE.getRGB());
            } catch (IndexOutOfBoundsException ignored){}
        }

    public void drawPx(int x, int y, Color c) {
            img.setRGB(x, y, c.getRGB());
    }

        public void drawPx(Point p) {
            img.setRGB(p.x, p.y, Color.ORANGE.getRGB());
        }


        public void redraw() {
            for (int x = 0; x < img.getWidth(); x++) {
                for (int y = 0; y < img.getHeight(); y++) {
                    img.setRGB(x, y, Color.OPAQUE);
                }
            }
        }
        public void redraw(ArrayList<TPoint> pts) {
            for(Point p : pts) {
                img.setRGB(p.x, p.y, Color.OPAQUE);
            }
        }
    }