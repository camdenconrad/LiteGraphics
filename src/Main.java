import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.*;

public class Main {
    private static final int framerate = 165;
    private static final AtomicBoolean pickedUp = new AtomicBoolean(false);
    private static final ArrayList<Point> toClear = new ArrayList<>();
    private static final ArrayList<Thread> points = new ArrayList<>();
    private static final ArrayList<TPoint> locals = new ArrayList<>(2);
    private static final ArrayList<TPoint> gitems = new ArrayList<>();
    private static final AtomicInteger clicks = new AtomicInteger();
    private static final int moveSize = 6;
    private static final AtomicBoolean doGrav = new AtomicBoolean(false);
    public static JFrame frame = new JFrame();
    public static JTextArea text = new JTextArea();
    public static GUI gui = new GUI();
    public static double timeMultiplier = 1;
    public static Point center;
    private static Point lastLocation;
    private static Point firstLocation;
    private static boolean accend;
    private static boolean decend;
    private static double orgin;
    private static double degrees;
    private static double radius;
    private static int size;
    private static String[][] array;
    private static String chars;
    private static Random rnd;
    private static double scale = 1;
    private static double dx;
    private static double dy;
    private static double scaleSize;
    private static Dimension screenSize;
    private static int fontSize;
    private static Point cursorLocation;
    private static int cursorScaler = 1;
    private static Image graphics;
    private static final double globalYaw = 0;
    private static final double globalPitch = 0;
    private static ArrayList<Point> toDraw = new ArrayList<>();


    public static void main(String[] args) {

        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        text.setSize(screenSize.width, screenSize.height);
        //frame.setMaximumSize(new Dimension((screenSize.height * 2) - 900, screenSize.height - 100));
        //frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        graphics = new Image(screenSize.width, screenSize.height);
        frame.add(graphics);


        //frame.add(gui.getPanel());
        //gui.getPanel().add(text);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        fontSize = 6;
        scaleSize = (fontSize * (4.0 / 3.0));
        System.out.println(scaleSize);
        System.out.println(screenSize);

        text.setFont(new Font("Courier New", Font.BOLD, fontSize));
        text.setFocusable(false);
        Color displayColor = new Color(Color.HSBtoRGB(200, 100, 0));
        text.setBackground(displayColor);

        text.setForeground(new Color(222, 255, 0));

        // Transparent 16 x 16 pixel cursor image.
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

// Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");

// Set the blank cursor to the JFrame.
        text.setCursor(blankCursor);


        degrees = 0;
        radius = 90;
        size = screenSize.height;

        array = new String[(int) (screenSize.height / scaleSize)][(int) (screenSize.width / scaleSize)];


        //!@#$%^&*(){}|\/.,<>?~`==_-;:'"
        chars = " ";
        rnd = new Random();
        setUp();

        reset(array, chars, rnd);

        center = new Point(screenSize.width / 2, screenSize.height / 2);
        lastLocation = MouseInfo.getPointerInfo().getLocation();

        cursorLocation = new TPoint(screenSize.width / 2, screenSize.height / 2);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    center = new Point(center.x - moveSize, center.y);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    center = new Point(center.x + moveSize, center.y);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    center = new Point(center.x, center.y + moveSize);
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    center = new Point(center.x, center.y - moveSize);
                }
                if (e.isShiftDown()) {
                    scale *= 1.01;
                    //System.out.println("Zooming");
                }
                if (e.isAltDown()) {
                    scale = 1;
                    center = new Point((int) (screenSize.width / scaleSize / 2), (int) (screenSize.height / scaleSize / 2));
                    //System.out.println("Zooming");
                }
                if (e.isControlDown()) {
                    if (e.getKeyCode() == KeyEvent.VK_Z) {
                        try {
                            locals.remove(locals.size() - 1);
                        } catch (IndexOutOfBoundsException ignored) {
                        }
                    }
                }
                // ands 45*
                if (e.getKeyCode() == KeyEvent.VK_A && e.getKeyCode() == KeyEvent.VK_S) {
                    center = new Point(center.x - moveSize, center.y + moveSize);

                }
                if (e.getKeyCode() == KeyEvent.VK_A && e.getKeyCode() == KeyEvent.VK_W) {
                    center = new Point(center.x - moveSize, center.y - moveSize);

                }
                if (e.getKeyCode() == KeyEvent.VK_D && e.getKeyCode() == KeyEvent.VK_S) {
                    center = new Point(center.x + moveSize, center.y + moveSize);

                }
                if (e.getKeyCode() == KeyEvent.VK_D && e.getKeyCode() == KeyEvent.VK_W) {
                    center = new Point(center.x + moveSize, center.y - moveSize);

                }

                if (e.getKeyCode() == KeyEvent.VK_W) {
                    center = new Point(center.x, center.y - moveSize);
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    center = new Point(center.x, center.y + moveSize);
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    center = new Point(center.x + moveSize, center.y);
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    center = new Point(center.x - moveSize, center.y);
                }


            }
        });

        frame.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
//                if(locals.size() < 2) {
                    //addPoint(MouseInfo.getPointerInfo().getLocation(), clicks.get());
//                } else {
//                    locals.set(0, new TPoint((int) MouseInfo.getPointerInfo().getLocation().getX(), (int) MouseInfo.getPointerInfo().getLocation().getY()));
//                }
                clicks.addAndGet(1);
                if (SwingUtilities.isRightMouseButton(e)) {
                    doGrav.set(!doGrav.get());
                    //removePoint(MouseInfo.getPointerInfo().getLocation());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                gitems.clear();
                firstLocation = MouseInfo.getPointerInfo().getLocation();
                locals.add(0, new TPoint((int) MouseInfo.getPointerInfo().getLocation().getX(), (int) MouseInfo.getPointerInfo().getLocation().getY()));
                locals.add(1, new TPoint((int) MouseInfo.getPointerInfo().getLocation().getX(), (int) MouseInfo.getPointerInfo().getLocation().getY()));

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                pickedUp.set(false);
                gitems.clear();
                velocitize(new TPoint(firstLocation) ,locals.get(1));
                locals.clear();
                //gitems.add(new TPoint(MouseInfo.getPointerInfo().getLocation().x,MouseInfo.getPointerInfo().getLocation().y));
                //doGrav.set(true);
            }
        });

        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
//                lastLocation = MouseInfo.getPointerInfo().getLocation();
//                pickedUp.set(true);
//                dx = round((lastLocation.x - firstLocation.x) / scaleSize);
//                dy = round((lastLocation.y - firstLocation.y) / scaleSize);
//
//                center = new Point((int) (center.getX() + dx), (int) (center.getY() + dy));


//                if(firstLocation.equals(lastLocation)) {
                    gitems.clear();
//                }
                //firstLocation = lastLocation;
                locals.set(1, new TPoint((int) MouseInfo.getPointerInfo().getLocation().getX(), (int) MouseInfo.getPointerInfo().getLocation().getY()));
                velocitize(new TPoint(firstLocation) ,locals.get(1));


                if (SwingUtilities.isLeftMouseButton(e)) {
                    //locals.set(1, new TPoint((int) MouseInfo.getPointerInfo().getLocation().getX(), (int) MouseInfo.getPointerInfo().getLocation().getY()));
                    // addPoint(MouseInfo.getPointerInfo().getLocation(), clicks.get());
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    //doGrav.set(!doGrav.get());
                    //removePoint(MouseInfo.getPointerInfo().getLocation());
                }
            }
        });

        frame.addMouseWheelListener(e -> {
            if (!e.isControlDown()) {
                if (e.getWheelRotation() < 1) {
                    scale *= abs(e.getWheelRotation() / 1.1);
                    //globalYaw += 0.1;
                } else {
                    scale *= abs(e.getWheelRotation() * 1.1);
                    //globalYaw -= 0.1;
                }
            } else {
                if (e.getWheelRotation() < 1) {
                    scale *= abs(e.getWheelRotation() / 1.5);
                } else {
                    scale *= abs(e.getWheelRotation() * 1.5);
                }
            }

        });

        Thread update = new Thread(() -> {
//            try {
//                splash();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            addPoint(new Point((
//                            new Random().nextInt(cursorLocation.x - 200,cursorLocation.x + 200)),
//                            (new Random().nextInt(cursorLocation.y - 200,
//                                    cursorLocation.y + 200))),
//                    clicks.get());
            while (true) {
                //addPoint(center, clicks.get());

                //plotCube(100,100,100);
                //                    if (checkPoints()) {
//                        addPoint(new Point((
//                                        new Random().nextInt(cursorLocation.x - 200,cursorLocation.x + 200)),
//                                        (new Random().nextInt(cursorLocation.y - 200,
//                                                cursorLocation.y + 200))),
//                                clicks.get());
//                    }
                //plotPoint(MouseInfo.getPointerInfo().getLocation().getX(),MouseInfo.getPointerInfo().getLocation().getY());

                //doWaves();

                //doCursor();
                addPoint(MouseInfo.getPointerInfo().getLocation(), clicks.get());
                frame.setTitle("Zoom: " + scale + "");
//                try {
//                    Thread.sleep(1000 / framerate);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
                //printArray(array);
                //reset(array, chars, rnd);
                graphics.updateUI();
                //draw();
                graphics.redraw();
                if (doGrav.get()) {
                    //gravity();
                }
                try {
                    threadCheck();
                } catch (IndexOutOfBoundsException | NullPointerException ignored) {
                }

                for(int i = 0; i < gitems.size()-1; i++) {
                    drawLine(gitems.get(i), gitems.get(i+1), new Color(255,255,255));
                }
            }
        });
        update.start();

        //center = cursorLocation;
        //Point center = new Point(0, 0);

//        while(degrees < 360) {
//            plotPoint(degrees, array, center, radius, value);
//            degrees += 5;
//        }

        accend = true;
        decend = false;
        orgin = 0;
    }

    private static void doWaves() {
        ArrayList<Point> corners = new ArrayList<>();
        degrees++;
        for (int i = 0; i < screenSize.width; i += 50) {
            for (int j = 0; j < screenSize.height; j += 50) {
                double xCor = i;
                double yCor = j;
                Position toPlot = new Position(xCor - cos(toRadians(degrees * new Random().nextDouble()) * 10), yCor + toRadians(degrees * new Random().nextDouble()) * 5);
                plotPoint((int) (toPlot.getX() * scale), (int) (toPlot.getY() * scale), new Color(181, 126, 220));

                //corners.add(new Point((int) (center.getX() - toPlot.getX() * scale), (int) (center.getY() - toPlot.getY() * scale)));
            }
        }


        //corners.sort(Comparator.comparingInt(o -> (o.y + o.x)));

        Color c;

        for (Point p : corners) {
            for (Point lp : corners) {
                if (p.y == lp.y) {
                    c = new Color(255, 0, 0);
                } else if (p.x == lp.x) {
                    c = new Color(255, 255, 0);
                } else if (p.x < lp.x && p.y < lp.y) {
                    c = new Color(0, 255, 0);
                } else {
                    c = new Color(0, 0, 255);
                }

                drawLine(p, lp, c);
            }
        }
        corners.clear();
    }

    private static void gravity() {

        ArrayList<TPoint> forRemoval = new ArrayList<>();

        for (TPoint l : gitems) {
            if (l.y < screenSize.height) {
                graphics.drawPx(l.x,l.y);
                //gitems.set(gitems.indexOf(l), new TPoint(l.x, l.y + (1 + l.t), l.getT() * 2));

            }
            if (l.y > screenSize.height - 2) {
                forRemoval.add(l);
            }
        }
        //graphics.redraw(forRemoval);
        gitems.removeAll(forRemoval);
        forRemoval.clear();
    }

    private static void removePoint(Point location) {
        for (Point l : locals) {
            if (within(location.x, l.x, 10)) {
                if (within(location.y, l.y, 10)) {
                    locals.remove(l);
                    break;
                }
            }
        }
    }

    private static boolean checkPoints() {
        boolean found = false;

        for (Point l : locals) {
            System.out.println((center.x - l.x) + ":" + (center.y - l.y));
            if (within(cursorLocation.x, center.x + l.x, 5)) {
                if (within(cursorLocation.y, center.y + l.y, 5)) {
                    cursorScaler++;
                    locals.remove(l);
                    found = true;
                    break;
                }
            }
        }
        return found;

    }

    private static boolean within(int x, int x1, int i) {
        return abs(x - x1) <= i;
    }

    private static void splash() throws InterruptedException {
        //text.setForeground(new Color(new Random().nextInt(0,255),new Random().nextInt(0,255),new Random().nextInt(0,255)));
        //text.setForeground(new Color(255, 128, 0));
        text.setForeground(new Color(181, 126, 220));

        //timeMultiplier = new Random().nextDouble(0.2,1.5);
        timeMultiplier = 1;

        accend = true;
        decend = false;
        orgin = 0;
        degrees = 30;
        radius = 10;
        Thread run = new Thread(() -> {
            while (accend) {

                if (accend) {
                    //radius++;
                    //timeMultiplier += 0.003;
                }
                if (radius >= (size / 16.0)) {
                    decend = true;
                    accend = false;
                    radius = 1;
                }
                if (decend) {
                    radius = 1;
                    timeMultiplier = 1;
                }
                if (radius <= 1) {
                    accend = true;
                    decend = false;
                }

//                degrees = 30 + orgin;
//                while (degrees < 360 + orgin) {
//                    for (int i = 0; i < 4; i++) {
//                        plotArc(degrees + ((360.0 / 4.0) * i), center, radius);
//                        plotArc(degrees + ((360.0 / 4.0) * i), center, radius * 2);
//                        plotArc(degrees + ((360.0 / 4.0) * i), center, radius * 3);
//
//                        //plotArc(degrees+180 + ((360.0/4.0) * i), center, radius);
//                    }
//                    degrees += 0.03;
//                    radius += 0.03;
//                }

//                degrees = 0;
//                while (degrees < 360 + orgin) {
//                    plotArc(degrees, center, radius);
//                    degrees += 1;
//                    if(degrees == 360)
//                        degrees = 0;
//                }
//
//                degrees = 0 + orgin;
//                while (degrees < 340+90 + orgin) {
//                    plotPoint(degrees, array, center, radius * 3);
//                    degrees += 1;
//                }

                //saw wave
//                for(int x = -100; x < 100; x++) {
//                    for(int y = -100; y < 100; y++) {
//                        plotPoint(64 * x, (screenSize.height/2.0) + (-64 * (frac(1,2)-frac(cos(frac(PI*x,4)), sin(frac(PI*x,4))))));
//                    }
//                }


                orgin -= 1;
            }
            text.setForeground(new Color(0, 0, 0));
            //reset(array, chars, rnd);
        });
        run.start();


    }

    private static void addPoint(Point location, int i) {

        text.setForeground(new Color(181, 126, 220));
        AtomicInteger degrees = new AtomicInteger(30);
//        Thread run = new Thread(() -> {
//            while (clicks.get() < i + 3) {
////                degrees.set(0);
////                while (degrees.get() < 360) {
////                    plotArc(degrees.get(), array, location.getX(), location.getY(), 6);
////                    degrees.addAndGet(1);
////                }
//                plotPoint(location.getX() - center.getX(),location.getY() - center.getY(),10);
//                //text.setForeground(new Color(0, 0, 0));
//            }
//
//            //reset(array, chars, rnd);
//        });

        //locals.add(new TPoint((int) location.getX(), (int) location.getY()));
        //points.add(run);

        if(locals.size() == 0) {
            locals.add(new TPoint((int) location.getX(), (int) location.getY()));
        }
        else {
            locals.add(1, locals.get(0));
            locals.set(0, new TPoint((int) location.getX(), (int) location.getY()));
            try {
                locals.remove(2);
            }
            catch (IndexOutOfBoundsException ignored){}
        }

        //run.start();
    }

    private static double frac(double a, double b) {
        return a / b;
    }

    private static void reset(String[][] array, String chars, Random rnd) {
        int j = 0;
        int o = 0;
        for (String[] string : array) {
            for (String ignored : string) {
                array[j][o] = String.valueOf(chars.charAt(rnd.nextInt(chars.length())));
                o++;
            }
            o = 0;
            j++;
        }


//        try {
//            for (Point p : toClear) {
//                array[p.y][p.x] = String.valueOf(chars.charAt(rnd.nextInt(chars.length())));
//                //toClear.remove(p);
//            }
//        } catch (ConcurrentModificationException | NullPointerException ignored) {}
    }

    private static void plotArc(double degrees, Point center, double radius) {
        try {
            double inRadians = toRadians(degrees);

            //Math.tanh(inRadians * Math.cos(inRadians)) //  * new Random().nextDouble(0.5, 0.7)
            double xCor = cos(inRadians) * radius;

            double yCor = sin(inRadians) * radius;
            double z = inRadians;

            double pitch = atan((xCor) / z);
            double yaw = atan(yCor / z);
            System.out.println(pitch + " : " + yaw);

            int x = (int) ((pitch * (screenSize.width / 10)));
            int y = (int) ((yaw * (screenSize.height / 10)));


            //double z = 0;
            Position toPlot = new Position((center.getX() + x) * scale, (center.getY() + y) * scale);

            graphics.drawPx((int) toPlot.getX(), (int) toPlot.getY(), new Color(181, 126, 220));


        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    private static void plotCube(int x, int y, int z) {
        ArrayList<Point> corners = new ArrayList<>();

        double hw = screenSize.width / 2.0;
        double hh = screenSize.height / 2.0;

        double fl_top = hw / tan(90 / 2.0);
        double fl_side = hh / tan(90 / 2.0);

        double d = (fl_top + fl_side) / 2;

        degrees += 1;

        for (int i = (x * -1); i < x; i += x) {
            for (int j = (y * -1); j < y; j += y) {
                for (int k = (z * -1); k < z; k += z) {
                    double xCor = i;
                    double yCor = j;
                    double zCor = k;
                    double pitch = atan(xCor) * cos(toRadians(0)); // left|right // 0 flat
                    double yaw = atan(yCor) * sin(toRadians(90)); // straight // 90 flat
                    //double roll = atan(zCor) * tan(toRadians(degrees));
//
//                    double lx = pitch * xCor;
//                    double ly = yaw * yCor;
//                    double lz = roll * zCor;

                    Position toPlot = new Position(((xCor * d) / (xCor + d)), ((yCor * d) / yCor + d));
                    plotPoint((int) (center.getX() - toPlot.getX() * scale), (int) (center.getY() - toPlot.getY() * scale), new Color(181, 126, 220));
                    corners.add(new Point((int) (center.getX() - toPlot.getX() * scale), (int) (center.getY() - toPlot.getY() * scale)));
                }
            }
        }


        //corners.sort(Comparator.comparingInt(o -> (o.y + o.x)));

        Color c;

        for (Point p : corners) {
            for (Point lp : corners) {
                if (p.y == lp.y) {
                    c = new Color(255, 0, 0);
                } else if (p.x == lp.x) {
                    c = new Color(255, 255, 0);
                } else if (p.x < lp.x && p.y < lp.y) {
                    c = new Color(0, 255, 0);
                } else {
                    c = new Color(0, 0, 255);
                }

                drawLine(p, lp, c);
            }
        }
        corners.clear();

    }

    private static void plotCursor(double degrees, String[][] array, double x, double y, double radius) {
        try {
            double inRadians = toRadians(degrees);

            //Math.tanh(inRadians * Math.cos(inRadians))
            double xCor = cos(inRadians) * new Random().nextDouble(0.5, 0.6);

            double yCor = sin(inRadians) * new Random().nextDouble(0.5, 0.6);

            Position toPlot = new Position((x + (xCor * radius)), (y + (yCor * radius)));
            //toClear.add(new Point((int) round(toPlot.getX()),(int) round(toPlot.getY())));
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    private static void doCursor() {
        //MouseInfo.getPointerInfo().getLocation().getX(),MouseInfo.getPointerInfo().getLocation().getY()
        int i = 0;
        while (i < 360) {
            plotCursor(i, array, MouseInfo.getPointerInfo().getLocation().getX() / scaleSize, MouseInfo.getPointerInfo().getLocation().getY() / scaleSize, 2 * (3.0 / fontSize) * cursorScaler);
            i += 1;
        }
    }

//    private static void plotPoint(double x, double y) {
//        try {
//            text.setForeground(new Color(255, 128, 0));
//            String chars = "!@#$%^&*(){}|\\/,<>?~`==_-;:'";
//            Random rnd = new Random();
//            array[(int) round(center.getY() + (y / scaleSize) * scale)][(int) round(center.getX() + (x / scaleSize) * scale)] = String.valueOf(chars.charAt(rnd.nextInt(chars.length())));
//        } catch (IndexOutOfBoundsException ignored) {
//        }
//    }

    private static void plotPoint(double x, double y) {
        try {
            graphics.drawPx((int) x, (int) y);
            locals.add(new TPoint((int) x, (int) y));
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    private static void plotPoint(double x, double y, Color c) {
        if ((x > 0 && x < screenSize.width) && (y > 0 && y < screenSize.height)) {
            graphics.drawPx((int) x, (int) y, c);
        }
    }

    private static void printArray(String[][] array) throws InterruptedException {
        StringBuilder line = new StringBuilder();

        for (String[] ints : array) {
            for (String i : ints) {
                if (!Objects.equals(i, " ")) {
                    line.append(i).append(" ");
                } else {
                    line.append(i).append(" ");
                    //line.append(ANSI_COLOR).append(i).append(" ").append(ANSI_RESET);
                }
            }
            line.append("\n");

            //System.out.print("\r");
            //System.out.println(line);

        }
        text.setText(line.toString());
        //Thread.sleep((long) (33 * timeMultiplier * 0.5));
        //text.updateUI();
        //Thread.sleep((long) (33 * timeMultiplier * 0.5));
    }


    private static int getPoint(int[][] array, Point px) {
        return array[(int) px.getX()][(int) px.getY()];
    }

    private static void setUp() {
        int j = 0;
        int o = 0;
        for (String[] string : array) {
            for (String ignored : string) {
                array[j][o] = String.valueOf(chars.charAt(rnd.nextInt(chars.length())));
                o++;
            }
            o = 0;
            j++;
        }
    }

    private static void threadCheck() {
        if (locals.size() >= 2) {
            for (int i = 0; i < locals.size() - 1; i++) {
                drawLine(i, new Color(126,75,13));
            }
        }


    }

    private static void drawLine(int i, Color c) {
        for (int x = 0; x < locals.get(i + 1).getX() - locals.get(i).getX(); x++) {
            for (int y = 0; y < locals.get(i + 1).getY() - locals.get(i).getY(); y++) {
                plotPoint(x + locals.get(i).getX(), x * frac(locals.get(i + 1).getY() - locals.get(i).getY(), locals.get(i + 1).getX() - locals.get(i).getX()) + locals.get(i).getY(), c);
            }
        }
        for (int x = 0; x < locals.get(i).getX() - locals.get(i + 1).getX(); x++) {
            for (int y = 0; y < locals.get(i).getY() - locals.get(i + 1).getY(); y++) {
                plotPoint(x + locals.get(i + 1).getX(), x * frac(locals.get(i).getY() - locals.get(i + 1).getY(), locals.get(i).getX() - locals.get(i + 1).getX()) + locals.get(i + 1).getY(), c);
            }
        }
        for (int x = 0; x < locals.get(i).getX() - locals.get(i + 1).getX(); x++) {
            for (int y = 0; y < locals.get(i + 1).getY() - locals.get(i).getY(); y++) {
                plotPoint(x + locals.get(i + 1).getX(), x * frac(locals.get(i).getY() - locals.get(i + 1).getY(), locals.get(i).getX() - locals.get(i + 1).getX()) + locals.get(i + 1).getY(), c);
            }
        }
        for (int x = 0; x < locals.get(i + 1).getX() - locals.get(i).getX(); x++) {
            for (int y = 0; y < locals.get(i).getY() - locals.get(i + 1).getY(); y++) {
                plotPoint(x + locals.get(i).getX(), x * frac(locals.get(i + 1).getY() - locals.get(i).getY(), locals.get(i + 1).getX() - locals.get(i).getX()) + locals.get(i).getY(), c);
            }
        }

        if (locals.get(i).x == locals.get(i + 1).x) {
            if (locals.get(i).y > locals.get(i + 1).y) {
                for (int y = 0; y < (locals.get(i).y - locals.get(i + 1).y); y++) {
                    plotPoint(locals.get(i).x, locals.get(i + 1).y + y, c);
                }
            } else {
                for (int y = 0; y < (locals.get(i + 1).y - locals.get(i).y); y++) {
                    plotPoint(locals.get(i).x, locals.get(i).y + y, c);
                }
            }
        }


    }

    private static double square(double a) {
        return a * a;
    }

    private static void drawLine(Point p1, Point p2, Color c) {

        if (p2.getX() > p1.getX()) {
            if (p2.getY() > p1.getY()) {
                for (int x = 0; x <= p2.getX() - p1.getX(); x++) {
                    for (int y = 0; y <= p2.getY() - p1.getY(); y++) {
                        plotPoint(x + p1.getX(), x * frac(p2.getY() - p1.getY(), p2.getX() - p1.getX()) + p1.getY(), c);
                    }
                }
            } else {
                for (int x = 0; x <= p2.getX() - p1.getX(); x++) {
                    for (int y = 0; y <= p1.getY() - p2.getY(); y++) {
                        plotPoint(x + p1.getX(), x * frac(p2.getY() - p1.getY(), p2.getX() - p1.getX()) + p1.getY(), c);
                    }
                }
            }
        } else {
            if (p2.getY() > p1.getY()) {
                for (int x = 0; x <= p1.getX() - p2.getX(); x++) {
                    for (int y = 0; y <= p2.getY() - p1.getY(); y++) {
                        plotPoint(x + p2.getX(), x * frac(p1.getY() - p2.getY(), p1.getX() - p2.getX()) + p2.getY(), c);
                    }
                }
            } else {
                for (int x = 0; x <= p1.getX() - p2.getX(); x++) {
                    for (int y = 0; y <= p1.getY() - p2.getY(); y++) {
                        plotPoint(x + p2.getX(), x * frac(p1.getY() - p2.getY(), p1.getX() - p2.getX()) + p2.getY(), c);
                    }
                }
            }

        }
        if (p1.x == p2.x) {
            if (p1.y > p2.y) {
                for (int y = 0; y < (p1.y - p2.y); y++) {
                    plotPoint(p1.x, p2.y + y, c);
                }
            } else {
                for (int y = 0; y < (p2.y - p1.y); y++) {
                    plotPoint(p1.x, p1.y + y, c);
                }
            }
        }

    }

    private static void velocitize(TPoint p, Point v) {

        int height = abs(v.y - p.y);
        int width = abs(v.x - p.x);

        gitems.clear();


        double h = 0;
        double w = 0;

        while (p.y < screenSize.height) {

            if(h < height) {
                if (v.y > p.y) {
                    p = new TPoint(p.x, p.y - (height / 2), p.getT());
                }
                else {
                    p = new TPoint(p.x, p.y + (height / 2), p.getT());
                }
                h += 1;
            }
            if(w < width) {
                if (v.x < p.x) {
                    p = new TPoint(p.x + (width / 2), p.y, p.getT());
                }
                else {
                    p = new TPoint(p.x - (width / 2), p.y, p.getT());
                }
                w += 1;
            }

            p = new TPoint(p.x, (int) (p.y + (1 + p.t)), p.getT() * 2);
            gitems.add(p);
            if(p.y > 1080/2) {
                height /= 2;
                h = 0;
                width /= 2;
                w = 0;
            }

            if(p.x < screenSize.width && p.y < screenSize.height) {
                graphics.drawPx(p.x, p.y);
                graphics.drawPx(p.x+1, p.y+1);
                graphics.drawPx(p.x+1, p.y-1);
                graphics.drawPx(p.x-1, p.y+1);
                graphics.drawPx(p.x-1, p.y-1);
            }

            if(gitems.size() >= 3) {
                System.err.println(gitems.size());
                for (int i = 0; i < gitems.size() - 2; i++) {
                    interpolate(gitems.get(i), gitems.get(1+i), gitems.get(2+i));
                }
//                for(int i = 0; i < toDraw.size()-1; i++) {
//                    drawLine(toDraw.get(i), toDraw.get(i+1), new Color(255,0,255));
//                }
//                for(Point x : toDraw) {
//                    graphics.drawPx(x.x,x.y, new Color(255,0,0));
//                }

            }
//            if (p.y > screenSize.height - 2) {
//                gitems.remove(p);
//            }
        }
//        for(int i = 0; i < gitems.size()-1; i++) {
//            drawLine(gitems.get(i), gitems.get(i+1), new Color(255,255,255));
//        }
//        gitems.clear();
    }

    static void interpolate(TPoint p1, TPoint p2, TPoint p3) {
        toDraw.clear();
        for(int i = 0; i < ( abs(p3.x-p1.x) ); i++) {
            double dy = p2.y + ((p1.x - p2.x) * frac((p3.y - p2.y),(p3.x - p2.x)));
            graphics.drawPx(p1.x + i, (int) dy);
            //toDraw.add(new Point(p1.x + i, (int) dy));
        }

    }



    // Math.sqrt(square(locals.get(1).getX()-locals.get(0).getX())) + square(locals.get(1).getY()-locals.get(0).getY())/scaleSize


}
