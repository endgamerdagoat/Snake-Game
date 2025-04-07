import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class Snake extends JPanel implements ActionListener, KeyListener {
    JFrame frame;
    Timer timer;
    
    
    final int dim = 40;
    final int GRID_WIDTH = 19;
    final int GRID_HEIGHT = 19;
    final int MAX_SEGMENTS = GRID_WIDTH * GRID_HEIGHT;
    
    
    int[] snakeX = new int[MAX_SEGMENTS];
    int[] snakeY = new int[MAX_SEGMENTS];
    int snakeLength;
    
    
    int foodX, foodY;

    int dx = 1;
    int dy = 0;

    int score;
    boolean gameOver;
    boolean scoreSubmitted;
    
    String[] hiNames = new String[10];
    int[] hiScores = new int[10];
    
    public Snake() {
        frame = new JFrame("Snake");
        frame.add(this);
        frame.setSize(1200, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.addKeyListener(this);
        
        loadHighScores();
        setup();
        
        
        timer = new Timer(150, this);
        timer.start();
    }

    public void setup() {
        snakeLength = 1;
        snakeX[0] = 5;
        snakeY[0] = 10;
        score = 0;
        gameOver = false;
        dx = 1;
        dy = 0;
        scoreSubmitted = false;
        generateFood();
    }
    
    public void generateFood() {
        do {
            foodX = (int)(Math.random()*GRID_WIDTH);
            foodY = (int)(Math.random()*GRID_HEIGHT);
        } while(isSnake(foodX, foodY));
    }
    
    public boolean isSnake(int x, int y) {
        for(int i = 0; i < snakeLength; i++) {
            if(snakeX[i] == x && snakeY[i] == y)
                return true;
        }
        return false;
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int gridPixelWidth = GRID_WIDTH * dim;
        int gridPixelHeight = GRID_HEIGHT * dim;
        
        
        int offsetX = (panelWidth - gridPixelWidth) / 2;
        int offsetY = (panelHeight - gridPixelHeight) / 2;
        
        
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, panelWidth, panelHeight);
        
        

        g.setColor(Color.GRAY);
        g.drawRect(offsetX, offsetY, (GRID_WIDTH) * dim, (GRID_WIDTH) * dim);
        
        for(int i = 0; i < snakeLength; i++) {
            int segX = offsetX + snakeX[i] * dim;
            int segY = offsetY + snakeY[i] * dim;
            g.setColor(Color.GREEN);
            g.fillRect(segX, segY, dim, dim);
            g.setColor(Color.BLACK);
            g.drawRect(segX, segY, dim, dim);
        }
        
        
        g.setColor(Color.RED);
        g.fillRect(offsetX + foodX * dim, offsetY + foodY * dim, dim, dim);
        
        
        if(!gameOver) {
            g.setColor(Color.WHITE);
            Font scoreFont = new Font("Haettenschweiler", Font.BOLD, 30);
            g.setFont(scoreFont);
            g.drawString("Score: " + score, 20, 40);
        }
        
        else {
            
            g.setColor(Color.RED);
            
            
            Font gameOverFont = new Font("Haettenschweiler", Font.BOLD, 50);
            g.setFont(gameOverFont);
            FontMetrics fmGameOver = g.getFontMetrics();
            int goMsgWidth = fmGameOver.stringWidth("GAME OVER");
            
            int gameOverX = (panelWidth - goMsgWidth) / 2;
            int gameOverY = (panelHeight - gridPixelHeight) / 2 + 150;
            g.drawString("GAME OVER", gameOverX, gameOverY);
            
            
            Font restartFont = new Font("Haettenschweiler", Font.BOLD, 45);
            g.setFont(restartFont);
            FontMetrics fmRestart = g.getFontMetrics();
            int restartWidth = fmRestart.stringWidth("Press R to restart");
            int restartX = (panelWidth - restartWidth) / 2;
            int restartY = gameOverY + fmGameOver.getHeight() + 10;
            g.drawString("Press R to restart", restartX, restartY);

            Font hiScoreFont = new Font("Haettenschweiler", Font.BOLD, 30);
            g.setFont(hiScoreFont);
            FontMetrics fmHiScore = g.getFontMetrics();
            int hiScoreWidth = fmHiScore.stringWidth("Hi-Scores:");
            int hiScoreX = (panelWidth - hiScoreWidth) / 2;
            int hiScoreY = restartY + fmRestart.getHeight() + 10;
            g.drawString("Hi-Scores:", hiScoreX, hiScoreY);
            
            
            int scoresY = hiScoreY + fmHiScore.getHeight() + 20;
            for(int i = 0; i < 10; i++) {
                String line = (i + 1) + ". " + hiNames[i] + " : " + hiScores[i];
                int lineWidth = fmHiScore.stringWidth(line);
                int lineX = (panelWidth - lineWidth) / 2;
                int lineY = scoresY + i * (fmHiScore.getHeight() + 5);
                g.drawString(line, lineX, lineY);
            }
        }
    }
    
    
    public void actionPerformed(ActionEvent e) {
        if(!gameOver) {
            moveSnake();
        }
        
        else {
            
            if(!scoreSubmitted && score > 0 && score >= hiScores[9]) {
                updateHighScores(score);
                scoreSubmitted = true;
            }
        }
        repaint();
    }

    
    public void moveSnake() {
        int newHeadX = snakeX[snakeLength - 1] + dx;
        int newHeadY = snakeY[snakeLength - 1] + dy;
        
        
        if(newHeadX < 0 || newHeadX >= GRID_WIDTH || newHeadY < 0 || newHeadY >= GRID_HEIGHT) {
            gameOver = true;
            return;
        }
        
        if(isSnake(newHeadX, newHeadY)) {
            gameOver = true;
            return;
        }
        
        
        if(newHeadX == foodX && newHeadY == foodY) {
            snakeX[snakeLength] = newHeadX;
            snakeY[snakeLength] = newHeadY;
            snakeLength++;
            score++;

            generateFood();
        }
        
        else {
            
            for(int i = 0; i < snakeLength - 1; i++) {
                snakeX[i] = snakeX[i + 1];
                snakeY[i] = snakeY[i + 1];
            }
            snakeX[snakeLength - 1] = newHeadX;
            snakeY[snakeLength - 1] = newHeadY;
        }
    }
    
    
    public void loadHighScores() {
        File file = new File("hiScores.txt");
        if(!file.exists()) {
            for(int i = 0; i < 10; i++) {
                hiNames[i] = "---";
                hiScores[i] = 0;
            }
            saveHighScores();
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int i = 0;
            while((line = br.readLine()) != null && i < 10) {
                String[] parts = line.trim().split(" ");
                if(parts.length >= 2) {
                    hiNames[i] = parts[0];
                    hiScores[i] = Integer.parseInt(parts[1]);
                    i++;
                }
            }
            for(i = 0; i < 10; i++) {
                hiNames[i] = "---";
                hiScores[i] = 0;
            }

        } catch (IOException e) {
            for(int i = 0; i < 10; i++) {
                hiNames[i] = "---";
                hiScores[i] = 0;
            }

        }
    }
    
    
    public void saveHighScores(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("hiScores.txt"))) {
            for(int i = 0; i < 10; i++) {
                bw.write(hiNames[i] + " " + hiScores[i]);
                bw.newLine();
            }
        }
        catch (IOException e) {
            
        }
    }
    
    public void updateHighScores(int newScore) {
        if(newScore >= hiScores[9]) {
            String name = JOptionPane.showInputDialog(frame, "New High Score! Enter a 3-letter name:");
            if(name == null || name.trim().isEmpty()) {
                name = "---";
            }
            
            else {
                name = name.trim();
                if(name.length() > 3) {
                    JOptionPane.showMessageDialog(
							null, "Your input exceeds the character limit. Please enter again.",
							"Failure", JOptionPane.ERROR_MESSAGE);
                }

                while(name.length() < 3)
                    name += "-";
            }
            hiScores[9] = newScore;
            hiNames[9] = name;
            
            for(int i = 9; i > 0; i--) {
                if(hiScores[i] > hiScores[i - 1]) {
                    int temp = hiScores[i];
                    hiScores[i] = hiScores[i - 1];
                    hiScores[i - 1] = temp;
                    String tempName = hiNames[i];
                    hiNames[i] = hiNames[i - 1];
                    hiNames[i - 1] = tempName;
                }
                else
                    break;
            }
            saveHighScores();
        }
    }
    
    
    
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if((key == KeyEvent.VK_W || key == KeyEvent.VK_UP) && dy != 1) {
            dx = 0;
            dy = -1;
        } else if((key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) && dy != -1) {
            dx = 0;
            dy = 1;
        } else if((key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) && dx != 1) {
            dx = -1;
            dy = 0;
        } else if((key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) && dx != -1) {
            dx = 1;
            dy = 0;
        } else if(key == KeyEvent.VK_R && gameOver) {
            setup();
        }
    }
    
    public void keyReleased(KeyEvent e) { }
    public void keyTyped(KeyEvent e) { }
    
    public static void main(String[] args) {
        Snake s = new Snake();
    }
}
