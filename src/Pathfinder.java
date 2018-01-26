/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Vitaliy
 */

class Pathfinder {

    private int[][] a;
    private int size;
    private Labyrint l;
    private boolean[][] aa;
    private Cell[][] ll;

    public Pathfinder(Labyrint t) {
        size = t.getSize();
        l = t;
        a = new int[size][size];
        aa = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a[i][j] = 0;
                aa[i][j] = false;
            }
        }
        a[0][0] = 1;
        ll=t.returnMaze();
    }

    private void findPath(int i, int j) {
        int x, y;
        while (a[size - 1][size - 1] == 0) {
            for (x = 0; x < size; x++) {
                for (y = 0; y < size; y++) {
                    if (a[x][y] != 0) {
                        if ((y > 0) && (ll[x][y].getWall(0) == false) && (a[x][y - 1] == 0)) {
                            a[x][y - 1] = a[x][y] + 1;
                        }
                        if ((x > 0) && (ll[x][y].getWall(3) == false) && (a[x - 1][y] == 0)) {
                            a[x - 1][y] = a[x][y] + 1;
                        }
                        if ((y < size - 1) && (ll[x][y].getWall(2) == false) && (a[x][y + 1] == 0)) {
                            a[x][y + 1] = a[x][y] + 1;
                        }
                        if ((x < size - 1) && (ll[x][y].getWall(1) == false) && (a[x + 1][y] == 0)) {
                            a[x + 1][y] = a[x][y] + 1;
                        }
                    }

                }
            }
        }
        x = size - 1;
        y = size - 1;
        aa[x][y] = true;
        while ((x != 0) || (y != 0)) {
            if ((x > 0) && (a[x - 1][y] == a[x][y] - 1)) {
                aa[x-1][y] = true;
                x = x - 1;
            } else if ((x < size - 1) && (a[x + 1][y] == a[x][y] - 1)) {
                aa[x+1][y] = true;
                x = x + 1;
            } else if ((y > 0) && (a[x][y - 1] == a[x][y] - 1)) {
                aa[x][y-1] = true;
                y = y - 1;
            } else if ((y < size - 1) && (a[x][y + 1] == a[x][y] - 1)) {
                aa[x][y+1] = true;
                y = y + 1;
            }
        }
    }
    
    public boolean[][] returnPath(){
        findPath(0, 0);
        return aa;
    }

}
