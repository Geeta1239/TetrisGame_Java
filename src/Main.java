import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

/**
 * ===== Abstract Shape Class =====
 * Base class representing a Tetris shape.
 * Defines common attributes and methods for all specific shapes.
 */
abstract class Shape {
    protected int x, y;                 // Position of the shape on the grid
    protected Color color;              // Color of the shape
    protected int[][] blocks;           // Shape matrix (1 = filled block, 0 = empty)

    /**
     * Constructor initializes shape structure and color.
     * @param blocks 2D array defining shape structure
     * @param color  Color of the shape
     */
    public Shape(int[][] blocks, Color color) {
        this.blocks = blocks;
        this.color = color;
        this.x = 4;  // Start horizontally centered
        this.y = 0;  // Start at top
    }

    /** Abstract method for rotating the shape (implemented by subclasses). */
    public abstract void rotate();

    /** Getter for shape blocks. */
    public int[][] getBlocks() { return blocks; }

    /** Getter for shape color. */
    public Color getColor() { return color; }

    /** Getter for X coordinate. */
    public int getX() { return x; }

    /** Getter for Y coordinate. */
    public int getY() { return y; }

    /** Move shape one block down. */
    public void moveDown() { y++; }

    /** Move shape one block left. */
    public void moveLeft() { x--; }

    /** Move shape one block right. */
    public void moveRight() { x++; }
}

// ===== Specific Shape Implementations =====

/** I-shape (straight line) */
class IShape extends Shape {
    public IShape() { super(new int[][]{{1,1,1,1}}, Color.cyan); }

    /** Rotates the shape by transposing the matrix. */
    @Override
    public void rotate() {
        int[][] newBlocks = new int[blocks[0].length][blocks.length];
        for(int i=0;i<blocks.length;i++)
            for(int j=0;j<blocks[0].length;j++)
                newBlocks[j][i] = blocks[i][j];
        blocks = newBlocks;
    }
}

/** O-shape (square) */
class OShape extends Shape {
    public OShape() { super(new int[][]{{1,1},{1,1}}, Color.yellow); }

    /** No rotation needed for square shape. */
    @Override public void rotate() {}
}

/** T-shape */
class TShape extends Shape {
    public TShape() { super(new int[][]{{0,1,0},{1,1,1}}, Color.magenta); }

    /** Rotate shape by rotating matrix 90 degrees clockwise. */
    @Override
    public void rotate() {
        int[][] newBlocks = new int[blocks[0].length][blocks.length];
        for(int i=0;i<blocks.length;i++)
            for(int j=0;j<blocks[0].length;j++)
                newBlocks[j][blocks.length-1-i] = blocks[i][j];
        blocks = newBlocks;
    }
}

/** S-shape */
class SShape extends Shape {
    public SShape() { super(new int[][]{{0,1,1},{1,1,0}}, Color.green); }

    /** Rotate matrix 90 degrees clockwise. */
    @Override
    public void rotate() {
        int[][] newBlocks = new int[blocks[0].length][blocks.length];
        for(int i=0;i<blocks.length;i++)
            for(int j=0;j<blocks[0].length;j++)
                newBlocks[j][blocks.length-1-i] = blocks[i][j];
        blocks = newBlocks;
    }
}

/** Z-shape */
class ZShape extends Shape {
    public ZShape() { super(new int[][]{{1,1,0},{0,1,1}}, Color.red); }

    /** Rotate matrix 90 degrees clockwise. */
    @Override
    public void rotate() {
        int[][] newBlocks = new int[blocks[0].length][blocks.length];
        for(int i=0;i<blocks.length;i++)
            for(int j=0;j<blocks[0].length;j++)
                newBlocks[j][blocks.length-1-i] = blocks[i][j];
        blocks = newBlocks;
    }
}

/** J-shape */
class JShape extends Shape {
    public JShape() { super(new int[][]{{1,0,0},{1,1,1}}, Color.blue); }

    /** Rotate matrix 90 degrees clockwise. */
    @Override
    public void rotate() {
        int[][] newBlocks = new int[blocks[0].length][blocks.length];
        for(int i=0;i<blocks.length;i++)
            for(int j=0;j<blocks[0].length;j++)
                newBlocks[j][blocks.length-1-i] = blocks[i][j];
        blocks = newBlocks;
    }
}

/** L-shape */
class LShape extends Shape {
    public LShape() { super(new int[][]{{0,0,1},{1,1,1}}, Color.orange); }

    /** Rotate matrix 90 degrees clockwise. */
    @Override
    public void rotate() {
        int[][] newBlocks = new int[blocks[0].length][blocks.length];
        for(int i=0;i<blocks.length;i++)
            for(int j=0;j<blocks[0].length;j++)
                newBlocks[j][blocks.length-1-i] = blocks[i][j];
        blocks = newBlocks;
    }
}

/**
 * ===== Score Manager =====
 * Handles reading and saving the high score in a text file.
 */
class ScoreManager {
    private static final String FILE_NAME = "score.txt"; // File to store score

    /** Load high score (name and score) from file. */
    public static String[] loadHighScore() {
        try(BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line = br.readLine();
            if(line != null){
                String[] parts = line.split(",");
                return new String[]{parts[0], parts[1]}; // {name, score}
            }
        } catch(Exception e) {}
        return new String[]{"None","0"}; // Default if no score found
    }

    /** Save new high score (name, score) to file. */
    public static void saveHighScore(String name, int score){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))){
            bw.write(name + "," + score);
        } catch(IOException e){ e.printStackTrace(); }
    }
}

/**
 * ===== Board Class =====
 * Represents the game area, manages shape movement, collisions,
 * scoring, and rendering.
 */
class Board extends JPanel {
    private final int ROWS = 20, COLS = 10, CELL_SIZE = 30;
    private Shape currentShape, nextShape;
    private Color[][] grid;
    private Timer timer;
    private int score = 0;
    private String playerName;
    private String highScoreName;
    private int highScore;

    /**
     * Constructor initializes game board, loads high score, and starts the game timer.
     * @param playerName name of the current player
     */
    public Board(String playerName){
        this.playerName = playerName;
        setPreferredSize(new Dimension(COLS*CELL_SIZE + 150, ROWS*CELL_SIZE));
        setBackground(Color.black);
        grid = new Color[ROWS][COLS];

        // Load existing high score
        String[] hs = ScoreManager.loadHighScore();
        highScoreName = hs[0];
        highScore = Integer.parseInt(hs[1]);

        // Initialize shapes
        currentShape = generateRandomShape();
        nextShape = generateRandomShape();

        // Set up timer for continuous game loop
        timer = new Timer(500, e -> gameLoop());
        timer.start();

        // Handle keyboard input
        addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){ handleInput(e); }
        });
        setFocusable(true);
    }

    /** Generate a random Tetris shape. */
    private Shape generateRandomShape(){
        Random rand = new Random();
        int n = rand.nextInt(7);
        return switch(n){
            case 0 -> new IShape();
            case 1 -> new OShape();
            case 2 -> new TShape();
            case 3 -> new SShape();
            case 4 -> new ZShape();
            case 5 -> new JShape();
            case 6 -> new LShape();
            default -> new OShape();
        };
    }

    /** Spawn a new shape at the top after previous one lands. */
    private void spawnShape(){
        currentShape = nextShape;
        nextShape = generateRandomShape();
        currentShape.x = 4;
        currentShape.y = 0;
    }

    /** Main game loop called periodically by timer. */
    private void gameLoop(){
        if(!moveShapeDown()){
            mergeShape();
            clearLines();
            spawnShape();

            // Game over condition
            if(!canMove(currentShape, currentShape.getX(), currentShape.getY())){
                timer.stop();
                if(score > highScore) ScoreManager.saveHighScore(playerName, score);
                JOptionPane.showMessageDialog(this, "Game Over! " + playerName + "'s Score: " + score);
                System.exit(0);
            }
        }
        repaint();
    }

    /** Move shape down if possible. Returns true if moved, false otherwise. */
    private boolean moveShapeDown(){
        if(canMove(currentShape, currentShape.getX(), currentShape.getY() + 1)){
            currentShape.moveDown();
            return true;
        }
        return false;
    }

    /** Check if a shape can move to the given position (collision detection). */
    private boolean canMove(Shape shape, int newX, int newY){
        int[][] blocks = shape.getBlocks();
        for(int i=0;i<blocks.length;i++)
            for(int j=0;j<blocks[0].length;j++)
                if(blocks[i][j]==1){
                    int x = newX + j;
                    int y = newY + i;
                    // Check for wall or block collision
                    if(x < 0 || x >= COLS || y >= ROWS || (y >= 0 && grid[y][x] != null))
                        return false;
                }
        return true;
    }

    /** Merge the landed shape into the grid. */
    private void mergeShape(){
        int[][] blocks = currentShape.getBlocks();
        for(int i=0;i<blocks.length;i++)
            for(int j=0;j<blocks[0].length;j++)
                if(blocks[i][j]==1)
                    grid[currentShape.getY()+i][currentShape.getX()+j] = currentShape.getColor();
    }

    /** Clear full lines and update score. */
    private void clearLines(){
        for(int i=0;i<ROWS;i++){
            boolean full = true;
            for(int j=0;j<COLS;j++)
                if(grid[i][j]==null) full = false;

            if(full){
                score += 100;
                for(int k=i;k>0;k--) grid[k] = grid[k-1].clone();
                grid[0] = new Color[COLS];
            }
        }
    }

    /** Handle keyboard inputs for controlling shape movement and rotation. */
    private void handleInput(KeyEvent e){
        switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT -> {
                if(canMove(currentShape, currentShape.getX()-1, currentShape.getY()))
                    currentShape.moveLeft();
            }
            case KeyEvent.VK_RIGHT -> {
                if(canMove(currentShape, currentShape.getX()+1, currentShape.getY()))
                    currentShape.moveRight();
            }
            case KeyEvent.VK_DOWN -> moveShapeDown();
            case KeyEvent.VK_UP -> currentShape.rotate();
            case KeyEvent.VK_ESCAPE -> {
                timer.stop();
                if(score > highScore) ScoreManager.saveHighScore(playerName, score);
                int option = JOptionPane.showConfirmDialog(this,"Do you want to quit?","Quit Game",JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.YES_OPTION) System.exit(0);
                else timer.start();
            }
        }
        repaint();
    }

    /** Paints the game grid, shapes, and score on screen. */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        // Draw grid lines
        g.setColor(Color.darkGray);
        for(int i=0;i<=ROWS;i++) g.drawLine(0,i*CELL_SIZE,COLS*CELL_SIZE,i*CELL_SIZE);
        for(int j=0;j<=COLS;j++) g.drawLine(j*CELL_SIZE,0,j*CELL_SIZE,ROWS*CELL_SIZE);

        // Draw placed blocks
        for(int i=0;i<ROWS;i++)
            for(int j=0;j<COLS;j++)
                if(grid[i][j]!=null){
                    g.setColor(grid[i][j]);
                    g.fillRect(j*CELL_SIZE,i*CELL_SIZE,CELL_SIZE,CELL_SIZE);
                }

        // Draw current moving shape
        int[][] blocks = currentShape.getBlocks();
        g.setColor(currentShape.getColor());
        for(int i=0;i<blocks.length;i++)
            for(int j=0;j<blocks[0].length;j++)
                if(blocks[i][j]==1)
                    g.fillRect((currentShape.getX()+j)*CELL_SIZE,(currentShape.getY()+i)*CELL_SIZE,CELL_SIZE,CELL_SIZE);

        // Draw next shape preview
        g.setColor(Color.white);
        g.drawString("Next:", COLS*CELL_SIZE + 10, 20);
        int[][] nextBlocks = nextShape.getBlocks();
        for(int i=0;i<nextBlocks.length;i++){
            for(int j=0;j<nextBlocks[0].length;j++){
                int x = COLS*CELL_SIZE + 20 + j*CELL_SIZE/2;
                int y = 30 + i*CELL_SIZE/2;
                g.setColor(Color.darkGray);
                g.drawRect(x,y,CELL_SIZE/2,CELL_SIZE/2);
                if(nextBlocks[i][j]==1){
                    g.setColor(nextShape.getColor());
                    g.fillRect(x,y,CELL_SIZE/2,CELL_SIZE/2);
                    g.setColor(Color.darkGray);
                    g.drawRect(x,y,CELL_SIZE/2,CELL_SIZE/2);
                }
            }
        }

        // Draw player and high score
        g.setColor(Color.white);
        g.drawString(playerName+"'s Score: "+score, COLS*CELL_SIZE+10, 120);
        g.drawString("High Score: "+highScoreName+" - "+highScore, COLS*CELL_SIZE+10, 140);
    }
}

/**
 * ===== Main Class =====
 * Entry point of the game.
 * Launches a JFrame with the Tetris Board.
 */
class Main {
    public static void main(String[] args){
        String playerName = JOptionPane.showInputDialog("Enter your name:");
        if(playerName == null || playerName.trim().isEmpty()) playerName = "Player";

        JFrame frame = new JFrame("Basic Tetris - Player Name & Score");
        frame.add(new Board(playerName));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}