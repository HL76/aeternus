/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.model;

import java.awt.Graphics;
import java.awt.Image;

/**
 *
 * @author User
 */
public class Cell extends Sprite{
    private Image[] images;
    public Cell(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
    }
    
    public Cell(int x, int y, int width, int height, Image[] images) {
        super(x, y, width, height, images[0]);
        this.images = images;
    }
    
    public void draw(Graphics g, Boolean floor) {
        g.drawImage(image, x, y, width, height, null);
        for(int i = 1; i < 5; i++){
            if(images[i] != null){
                g.drawImage(images[i], x, y, width, height, null);
            }
        }
    }
}
