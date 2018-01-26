import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.Random;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Vitaliy
 */

/* 
    La classe che genera il labirinto utilizza il algoritmo ricorsivo con backtrack, 
    come descritto su Wikipedia(http://en.wikipedia.org/wiki/Maze_generation_algorithm#Recursive_backtracker)
*/
public class Labyrint  implements Serializable{

    private Cell[][] lab;
    private boolean[][] vis;
    static final int[][] dirs = {{0, 1, 2, 3}, {0, 1, 3, 2}, {0, 2, 1, 3}, {0, 2, 3, 1}, {0, 3, 1, 2}, {0, 3, 2, 1}, {1, 0, 2, 3}, {1, 0, 3, 2}, {1, 2, 0, 3}, {1, 2, 3, 0}, {1, 3, 0, 2}, {1, 3, 2, 0}, {2, 0, 1, 3}, {2, 0, 3, 1}, {2, 1, 0, 3}, {2, 1, 3, 0}, {2, 3, 0, 1}, {2, 3, 1, 0}, {3, 0, 1, 2}, {3, 0, 2, 1}, {3, 1, 0, 2}, {3, 1, 2, 0}, {3, 2, 0, 1}, {3, 2, 1, 0}};
    private int size;
    
    public Labyrint(int size) {
        this.size = size;
        lab = new Cell[size][size];
        vis = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                lab[i][j] = new Cell();
                vis[i][j] = false;
            }
        }
    }
    
    public Labyrint(){
        this(10);
    }

    private void create(int x, int y) {
        vis[x][y] = true;
        int dir = new Random().nextInt(24);
        for (int i = 0; i < 4; i++) {
            if ((dirs[dir][i] == 0) && (y > 0) && (vis[x][y - 1] == false)) {
                lab[x][y].setWall(0, false);
                lab[x][y - 1].setWall(2, false);
                create(x, y - 1);
            } 
            if ((dirs[dir][i] == 1) && (x < size-1) && (vis[x + 1][y] == false)) {
                lab[x][y].setWall(1, false);
                lab[x + 1][y].setWall(3, false);
                create(x + 1, y);
            } 
            if ((dirs[dir][i] == 2) && (y < size-1) && (vis[x][y + 1] == false)) {
                lab[x][y].setWall(2, false);
                lab[x][y + 1].setWall(0, false);
                create(x, y + 1);
            } 
            if ((dirs[dir][i] == 3) && (x > 0) && (vis[x - 1][y] == false)) {
                lab[x][y].setWall(3, false);
                lab[x - 1][y].setWall(1, false);
                create(x - 1, y);
            }
        }
    }

    public void draw(Graphics2D g, int scale) {
        g.setBackground(Color.WHITE);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, this.size*scale, this.size*scale);
        g.setBackground(Color.BLACK);
        g.setColor(Color.BLACK);
        for (int xx = 0; xx < this.size; xx++) {
            for (int yy = 0; yy < this.size; yy++) {
                if (this.lab[xx][yy].getWall(0) == true) {
                    g.drawLine(xx * scale, yy * scale, xx * scale + scale, yy * scale);
                }
                if (this.lab[xx][yy].getWall(1) == true) {
                    g.drawLine(xx * scale + scale, yy * scale, xx * scale + scale, yy * scale + scale);
                }
                if (this.lab[xx][yy].getWall(2) == true) {
                    g.drawLine(xx * scale + scale, yy * scale + scale, xx * scale, yy * scale + scale);
                }
                if (this.lab[xx][yy].getWall(3) == true) {
                    g.drawLine(xx * scale, yy * scale + scale, xx * scale, yy * scale);
                }
            }
        }
    }
    
    public int getSize(){
        return this.size;
    }
    
    public Cell[][] returnMaze(){
        this.create(0, 0);
        return this.lab;
    }
    
}
