import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Vitaliy
 */
public class DrawPanel extends javax.swing.JPanel {

    /**
     * Creates new form DrawPanel
     */
    private Labyrint maze;
    private Image mazeimg;
    boolean mazevisible, playeractive, pathvisible, viewportactive;
    private Graphics2D gg;
    private int scale, x, y, viewp;
    private static final int z = 4;
    private Timer t;
    private boolean[][] aa;
    private Cell[][] a;

    public DrawPanel() {
        initComponents();
        mazevisible = false;
        playeractive = false;
        pathvisible = false;
        viewp = 3;
        viewportactive = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        gg = (Graphics2D) this.getGraphics();
    }

    public void newMaze(int size, int scale) {
        stoptimer();
        maze = new Labyrint(size);
        this.scale = scale;
        a = maze.returnMaze();
        Pathfinder pf = new Pathfinder(maze);
        aa = pf.returnPath();
        mazeimg = this.createImage(size * scale, size * scale);
        Graphics2D mg = (Graphics2D) mazeimg.getGraphics();
        mg.setStroke(new BasicStroke(z));
        maze.draw(mg, scale);
        mg.setColor(Color.YELLOW);
        mg.fillRect(z/2, z/2, scale-z, scale-z);
        mg.fillRect(maze.getSize()*scale - scale+ z/2, maze.getSize()*scale - scale+ z/2, scale-z, scale-z);
        mazevisible = true;
        playeractive = false;
        pathvisible = false;
        updateGraphics();
    }

    public void newMaze() {
        maze = new Labyrint();
        this.scale = 30;
        a = maze.returnMaze();
        Pathfinder pf = new Pathfinder(maze);
        aa = pf.returnPath();
        mazeimg = this.createImage(this.getWidth(), this.getHeight());
        Graphics2D mg = (Graphics2D) mazeimg.getGraphics();
        mg.setStroke(new BasicStroke(z));
        maze.draw(mg, scale);
        mg.setColor(Color.YELLOW);
        mg.fillRect(z/2, z/2, scale-z, scale-z);
        mg.fillRect(maze.getSize()*scale - scale+ z/2, maze.getSize()*scale - scale+ z/2, scale-z, scale-z);
        mazevisible = true;
        playeractive = false;
        pathvisible = false;
        updateGraphics();
    }

    private void loadMaze(ObjectInputStream ff) {
        stoptimer();
        try {
            maze = (Labyrint) ff.readObject();
            a = maze.returnMaze();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage());
        }
        Pathfinder pf = new Pathfinder(maze);
        aa = pf.returnPath();
        mazeimg = this.createImage(this.getWidth(), this.getHeight());
        Graphics2D mg = (Graphics2D) mazeimg.getGraphics();
        mg.setStroke(new BasicStroke(z));
        maze.draw(mg, scale);
        mg.setColor(Color.YELLOW);
        mg.fillRect(z/2, z/2, scale-z, scale-z);
        mg.fillRect(maze.getSize()*scale - scale+ z/2, maze.getSize()*scale - scale+ z/2, scale-z, scale-z);
        mazevisible = true;
        playeractive = false;
        pathvisible = false;
        updateGraphics();
    }

    public void startplayer(int x, int y) {
        if (mazevisible == true) {
            stoptimer();
            this.x = x;
            this.y = y;
            playeractive = true;
            updateGraphics();
        }
    }

    public void enablepath() {
        if (mazevisible == true) {
            this.pathvisible = true;
            updateGraphics();
        }
    }

    public void updateGraphics() {
        if (mazevisible == true) {
            gg.clearRect(0, 0, this.getWidth(), this.getHeight());
            if ((viewportactive == true) && (playeractive == true)) {
                gg.setClip(x * scale - viewp * scale - z / 2, y * scale - viewp * scale - z / 2, (viewp) * scale + (viewp + 1) * scale + z, (viewp) * scale + (viewp + 1) * scale + z);
            } else if (viewportactive == false) {
                gg.setClip(null);
            }
            gg.drawImage(mazeimg, 0, 0, this);
        }

        if (pathvisible == true) {
            gg.setColor(Color.RED);
            for (int xx = 0; xx < maze.getSize(); xx++) {
                for (int yy = 0; yy < maze.getSize(); yy++) {
                    if (aa[xx][yy] == true) {
                        gg.fillRect(xx * scale + z / 2, yy * scale + z / 2, scale - z, scale - z);
                    }
                }
            }
        }
        if (playeractive == true) {
            gg.setColor(Color.GREEN);
            gg.fillRect(x * scale + z / 2, y * scale + z / 2, scale - z, scale - z);
        }

    }

    private void testVictory() {
        if ((x == maze.getSize() - 1) && (y == maze.getSize() - 1)) {
            JOptionPane.showMessageDialog(null, "Vittoria!!!");
            startplayer(0, 0);
        }
    }

    public void moveleft() {
        if (playeractive == true) {
            if ((x > 0) && (a[x][y].getWall(3) == false)) {
                x = x - 1;
                updateGraphics();
                testVictory();
            }
        }
    }

    public void moveright() {
        if (playeractive == true) {
            if ((x < maze.getSize() - 1) && (a[x][y].getWall(1) == false)) {
                x = x + 1;
                updateGraphics();
                testVictory();
            }
        }
    }

    public void moveup() {
        if (playeractive == true) {
            if ((y > 0) && (a[x][y].getWall(0) == false)) {
                y = y - 1;
                updateGraphics();
                testVictory();
            }
        }
    }

    public void movedown() {
        if (playeractive == true) {
            if ((y < maze.getSize() - 1) && (a[x][y].getWall(2) == false)) {
                y = y + 1;
                updateGraphics();
                testVictory();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 871, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 659, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if ((evt.getKeyCode() == KeyEvent.VK_W) || (evt.getKeyCode() == KeyEvent.VK_UP)) {
            moveup();
        } else if ((evt.getKeyCode() == KeyEvent.VK_S) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
            movedown();
        } else if ((evt.getKeyCode() == KeyEvent.VK_D) || (evt.getKeyCode() == KeyEvent.VK_RIGHT)) {
            moveright();
        } else if ((evt.getKeyCode() == KeyEvent.VK_A) || (evt.getKeyCode() == KeyEvent.VK_LEFT)) {
            moveleft();
        }
    }//GEN-LAST:event_formKeyPressed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        gg = (Graphics2D) this.getGraphics();
    }//GEN-LAST:event_formComponentShown

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        updateGraphics();
    }//GEN-LAST:event_formComponentResized


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public void hidePath() {
        this.pathvisible = false;
        updateGraphics();
    }

    public void setViewport(Integer a) {
        if (playeractive == true) {
            if (a <0) {
                viewportactive = false;
                updateGraphics();
            } else {
                viewportactive = true;
                viewp = a;
                updateGraphics();
            }
        } else {
            viewportactive = false;
            updateGraphics();
        }
    }

    public void movepiece() {
        int r = (new Random()).nextInt(24);
        for (int i = 0; i < 4; i++) {
            if (a[x][y].getWall(Labyrint.dirs[r][i]) == false) {
                if (Labyrint.dirs[r][i] == 0) {
                    moveup();
                } else if (Labyrint.dirs[r][i] == 1) {
                    moveright();
                } else if (Labyrint.dirs[r][i] == 2) {
                    movedown();
                } else if (Labyrint.dirs[r][i] == 3) {
                    moveleft();
                }
                break;
            }
        }
    }

    public void starttimer() {
        if ((t == null) && (playeractive == true)) {
            t = new Timer(100, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent ae) {
                    movepiece();
                }
            });
            t.start();
        } else if ((t != null) && (t.isRunning() == false)) {
            t.start();
        }
    }

    public void stoptimer() {
        if ((t != null) && (t.isRunning())) {
            t.stop();
        }
    }

    public void stopgame() {
        if (playeractive == true) {
            stoptimer();
            this.x = 0;
            this.y = 0;
            playeractive = false;
            updateGraphics();
        }
    }

    public void saveToFile(File f) {
        FileOutputStream fileOut = null;
        ObjectOutputStream out = null;
        try {
            fileOut = new FileOutputStream(f);
            out = new ObjectOutputStream(fileOut);
            out.writeObject(maze);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage());
        } finally {
            try {
                out.close();
                fileOut.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage());
            }
        }
    }

    public void loadFromFile(File f) {
        FileInputStream fileIn = null;
        ObjectInputStream in = null;
        try {
            fileIn = new FileInputStream(f);
            in = new ObjectInputStream(fileIn);
            loadMaze(in);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage());
        } finally {
            try {
                in.close();
                fileIn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage());
            }
        }
    }
}
