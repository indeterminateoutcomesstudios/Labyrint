
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Vitaliy
 */
public class Cell implements Serializable{
    private boolean[] walls;
    public Cell(){
        walls = new boolean[4];
        walls[0] = true;
        walls[1] = true;
        walls[2] = true;
        walls[3] = true;
    }
    public void setWall(int wall, boolean t){
        walls[wall] = t;
    }
    public boolean getWall(int wall){
        return walls[wall];
    }
}
