/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.model;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author User
 */
public class Labyrinth {
    
    private final int BLOCK_SIZE = 120;
    private final int ACROSS = 29;
    private final int DOWN = 30;
    ArrayList<Cell> cells;
    ArrayList<Cell> outer;
    ArrayList<Cell> floorTiles;

    public Labyrinth(){
        genLevel();
    }
    
    private int[][] maze = new int[ACROSS][DOWN];
    private ArrayList<int[]> fronts = new ArrayList<int[]>();
    private int[][] floor = new int[ACROSS][DOWN];
    
    //Labyrinth generation logic
    private void genLevel(){
        cells = new ArrayList<>();
        outer = new ArrayList<>();
        floorTiles = new ArrayList<>();
        //Fill labyrinth with walls
        for(int i = 0; i < ACROSS; i++){
            for(int j = 0; j < DOWN; j++){
                maze[i][j] = 1;
            }
        }
        //random starting point for generation
        int x = ((int) (Math.random() * ACROSS/2) + 10);
        int y = ((int) (Math.random() * DOWN/2) + 10);
        if(x % 2 == 0){
            x++;
        }
        if(y % 2 == 0){
            y++;
        }
        int[] temp = {x+2, y};
        fronts.add(temp);
        int[] temp2 = {x, y-2};
        fronts.add(temp2);
        int[] temp3 = {x-2,y};
        fronts.add(temp3);
        int[] temp4 = {x, y+2};
        fronts.add(temp4);
        maze[x][y] = 0;
        while(fronts.size() > 0){
            int r = (int) (Math.random() * fronts.size());
            int i = fronts.get(r)[0];
            int j = fronts.get(r)[1];
            int [] passage = randomValidNeighbor(i,j);
            maze[passage[0]][passage[1]] = 0;
            maze[i][j] = 0;
            markFrontiers(i, j);
            fronts.remove(r);
        }
        for(int i = 0; i < ACROSS; i++){
            for(int j = 0; j < DOWN; j++){
                if(maze[i][j] == 0){
                    floor[i][j] = 1;
                }
            }
        }
        for(int i = 0; i < ACROSS; i++){
            for(int j = 0; j < DOWN; j++){
                if(j < DOWN-1){
                    if(j == DOWN-2){
                        Image image = new ImageIcon("src/images/castlewall.png").getImage();
                        cells.add(new Cell(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, new Image[]{image, null, null, null, null}));
                    }else{
                        if(maze[i][j] == 1 && maze[i][j+1] != 1){
                            Random rnd = new Random();
                            Image image = new ImageIcon("src/images/Labyrinth/Castle/Walls/" + rnd.nextInt(5) + ".png").getImage();
                            Image shadow = new ImageIcon("src/images/Labyrinth/Shadows/2.png").getImage();
                            cells.add(new Cell(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, new Image[]{image, null, null, shadow, null}));
                        }else if(maze[i][j] == 1 && maze[i][j+1] == 1){
                            Image image = new ImageIcon("src/images/castledark.png").getImage();
                            cells.add(new Cell(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, new Image[]{image, null, null, null, null}));
                        }else{
                            Image image = new ImageIcon("src/images/floor1.png").getImage();
                            Image[] imgs = new Image[5];
                            imgs[0] = image;
                            for(int f = 0; f < 4; f++){
                                Image newImg = new ImageIcon("src/images/Labyrinth/Shadows/" + f + ".png").getImage();
                                imgs[f+1] = newImg;
                            }
                            if(maze[i][j-1] == 0){
                                imgs[1] = null;
                            }
                            if(maze[i+1][j] == 0){
                                imgs[2] = null;
                            }
                            if(maze[i][j+1] == 0){
                                imgs[3] = null;
                            }
                            if(maze[i-1][j] == 0){
                                imgs[4] = null;
                            }
                            
                            floorTiles.add(new Cell(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, imgs));
                        }
                    }
                }
            }
        }
        
        for(int i = 0; i < ACROSS*4; i++){
            for(int j = 0; j < DOWN*4; j++){
                if((j < 10 || i < 10) || (j > 37 || i > 38)){
                    Image image = new ImageIcon("src/images/castledark.png").getImage();
                    outer.add(new Cell((i * BLOCK_SIZE)-(BLOCK_SIZE * 10), (j * BLOCK_SIZE)-(BLOCK_SIZE * 10), BLOCK_SIZE, BLOCK_SIZE, image));
                }
            }
        }
    }
    
    //Shifts tiles accorting to given direction
    public void shiftField(int x, int y){
        for(Cell c : floorTiles){
            c.setX(c.getX() + x);
            c.setY(c.getY() + y);
        }
        
        for(Cell c : cells){
            c.setX(c.getX() + x);
            c.setY(c.getY() + y);
        }
        
        for(Cell c : outer){
            c.setX(c.getX() + x);
            c.setY(c.getY() + y);
        }
    }

    //Picks neightbor when generating labyrinth
    private int[] randomValidNeighbor(int x, int y){
        int[] res = new int[2];
        boolean run = true;
        while(run){
            int rnd = (int) ((Math.random() * 4));
            //Check if too close to the edge to avoid out of bounds checks
            if(!((rnd == 0 && x > ACROSS-3) || (rnd == 1 && x < 2) || (rnd == 2 && y > DOWN-3) || (rnd == 3 && y < 2))){
                switch(rnd){
                    case 0:
                        if(maze[x+2][y] == 0){
                            res[0] = x+1;
                            res[1] = y;
                            run = false;
                        }
                    break;
                    case 1:
                        if(maze[x-2][y] == 0){
                            res[0] = x-1;
                            res[1] = y;
                            run = false;
                        }
                    break;
                    case 2:
                        if(maze[x][y+2] == 0){
                            res[0] = x;
                            res[1] = y+1;
                            run = false;
                        }

                    break;
                    case 3:
                        if(maze[x][y-2] == 0){
                            res[0] = x;
                            res[1] = y-1;
                            run = false;
                        }
                    break;
                }
            }
        }
        return res;
    }
    
    //Marks the frontiers for generation
    private void markFrontiers(int x, int y){
        if(x+2 < ACROSS-1){
            if(maze[x+2][y] == 1){
            int[] temp = {x+2, y};
            boolean b = false;
            for(int i = 0; i < fronts.size(); i++){
                if(Arrays.equals(temp, fronts.get(i))){
                    b = true;
                }
            }
            if(!b){
                fronts.add(temp);
            }
        }
        }
        if(x-2 > 0){
            if(maze[x-2][y] == 1){
            int[] temp = {x-2, y};
            boolean b = false;
            for(int i = 0; i < fronts.size(); i++){
                if(Arrays.equals(temp, fronts.get(i))){
                    b = true;
                }
            }
            if(!b){
                fronts.add(temp);
            }
        }
        }
        if(y+2 < DOWN){
            if(maze[x][y+2] == 1){
            int[] temp = {x, y+2};
            boolean b = false;
            for(int i = 0; i < fronts.size(); i++){
                if(Arrays.equals(temp, fronts.get(i))){
                    b = true;
                }
            }
            if(!b){
                fronts.add(temp);
            }
        }
        }
        if(y-2 > 0){
            if(maze[x][y-2] == 1){
            int[] temp = {x, y-2};
            boolean b = false;
            for(int i = 0; i < fronts.size(); i++){
                if(Arrays.equals(temp, fronts.get(i))){
                    b = true;
                }
            }
            if(!b){
                fronts.add(temp);
            }
        }
        }
    }
    
    //Collision logic for monsters
    public boolean collides(Monster m) {
        Cell collidedWith = null;
        for (Cell cell : cells) {
            if (m.collides(cell)) {
                collidedWith = cell;
                break;
            }
        }
        if (collidedWith != null) {
            return true;
        } else {
            return false;
        }
    }
    
    //Collision logic for player
    public boolean playerCollides(Player player) {
        Cell collidedWith = null;
        for (Cell cell : cells) {
            if (player.collides(cell)) {
                collidedWith = cell;
                break;
            }
        }
        if (collidedWith != null) {
            return true;
        } else {
            return false;
        }
    }

    public void draw(Graphics g) {
        for (Cell c : floorTiles) {
            c.draw(g, true);
        }
        
        for (Cell c : cells) {
            c.draw(g, true);
        }
        for(Cell c : outer){
            c.draw(g);
        }
    }
}
