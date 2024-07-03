import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JPanel implements ActionListener, KeyListener {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 300;
    private static final int GROUND_HEIGHT = 20;
    private static final int DINOSAUR_SIZE = 40;
    private static final int OBSTACLE_WIDTH = 20;
    private static final int OBSTACLE_HEIGHT = 40;
    private static final int INITIAL_JUMP_VELOCITY = 15;
    private static final int GRAVITY = 1;
    private static final int OBSTACLE_INTERVAL = 250;
    private static final int OBSTACLE_VELOCITY = 10;

    private int dinoX;
    private int dinoY;
    private int jumpVelocity;
    private boolean jumping;
    private boolean gameOver;

    private ImageIcon dinosaurIcon;
    private ImageIcon cactusIcon;

    private Obstacle obstacle;
    private Timer timer;

    public Main() {
        dinoX = 100;
        dinoY = WINDOW_HEIGHT - GROUND_HEIGHT - DINOSAUR_SIZE;
        jumpVelocity = 0;
        jumping = false;
        gameOver = false;

        // Cargar imágenes
        dinosaurIcon = new ImageIcon("dinosaur.png");
        cactusIcon = new ImageIcon("cactus.png");

        obstacle = new Obstacle(WINDOW_WIDTH, WINDOW_HEIGHT - GROUND_HEIGHT - OBSTACLE_HEIGHT, OBSTACLE_WIDTH, OBSTACLE_HEIGHT);

        timer = new Timer(20, this);
        timer.start();

        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibuja el suelo
        g.setColor(Color.GRAY);
        g.fillRect(0, WINDOW_HEIGHT - GROUND_HEIGHT, WINDOW_WIDTH, GROUND_HEIGHT);

        // Dibuja el dinosaurio
        g.drawImage(dinosaurIcon.getImage(), dinoX, dinoY, DINOSAUR_SIZE, DINOSAUR_SIZE, null);

        // Dibuja el obstáculo
        g.drawImage(cactusIcon.getImage(), obstacle.getX(), obstacle.getY(), OBSTACLE_WIDTH, OBSTACLE_HEIGHT, null);

        // Si es el fin del juego, muestra el mensaje
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            FontMetrics fm = g.getFontMetrics();
            String gameOverMsg = "GAME OVER";
            int x = (WINDOW_WIDTH - fm.stringWidth(gameOverMsg)) / 2;
            int y = WINDOW_HEIGHT / 2;
            g.drawString(gameOverMsg, x, y);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // Actualiza la posición del dinosaurio
            dinoY -= jumpVelocity;
            jumpVelocity -= GRAVITY;

            // Controla el salto
            if (dinoY >= WINDOW_HEIGHT - GROUND_HEIGHT - DINOSAUR_SIZE) {
                dinoY = WINDOW_HEIGHT - GROUND_HEIGHT - DINOSAUR_SIZE;
                jumping = false;
                jumpVelocity = 0;
            }

            // Actualiza la posición del obstáculo
            obstacle.move();

            // Verifica la colisión con el obstáculo
            if (obstacle.getX() <= dinoX + DINOSAUR_SIZE && dinoX <= obstacle.getX() + OBSTACLE_WIDTH &&
                    dinoY + DINOSAUR_SIZE >= obstacle.getY() && dinoY <= obstacle.getY() + OBSTACLE_HEIGHT) {
                gameOver = true;
            }

            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE && !jumping && !gameOver) {
            jumping = true;
            jumpVelocity = INITIAL_JUMP_VELOCITY;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    class Obstacle {
        private int x;
        private int y;
        private int width;
        private int height;

        public Obstacle(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public void move() {
            x -= OBSTACLE_VELOCITY;

            // Respawn del obstáculo al llegar al final de la pantalla
            if (x + width < 0) {
                x = WINDOW_WIDTH;
                y = WINDOW_HEIGHT - GROUND_HEIGHT - OBSTACLE_HEIGHT;
            }
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Juego del Dinosaurio");
                Main game = new Main();
                frame.add(game);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
