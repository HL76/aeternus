/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.model;

import java.awt.Image;
import java.text.DecimalFormat;

/**
 *
 * @author User
 */
public class Player extends Sprite{
    private double velx;
    private double vely;
    private int maxHP;
    private double hp;
    private double damage;
    private int[] stats = new int[6];
    
    public Player(int x, int y, int width, int height, Image image, int maxHP, Double damage, int[] stats) {
        super(x, y, width, height, image);
        this.maxHP = maxHP;
        this.hp = maxHP * 1.0;
        this.damage = damage;
        this.stats = stats;
    }
    
    public Player(int x, int y, int width, int height, Image image) {
        super(x, y, width, height, image);
        this.maxHP = 0;
        this.hp = 0.0;
        this.damage = 0.0;
    }
    
    public int getStat(int idx){
        return stats[idx];
    }
    
    public int getMaxHp(){
        return maxHP;
    }
    
    public Double getHp(){
        return hp;
    }
    
    public Double getDamage(){
        int scale = (int) Math.pow(10, 2);
        return (double) Math.round(damage * scale) / scale;
    }
    
    public void setHp(Double hp){
        this.hp = hp;
    }
    
    public void moveX() {
        x += velx;
    }
    
    public void moveY(){
        y += vely;
    }
    
    public void stepBack(){
        x += -velx;
        y += -vely;
    }

    public double getVelx() {
        return velx;
    }
    
    public double getVely() {
        return vely;
    }

    public void setVelx(double velx) {
        this.velx = velx;
    }
    
    public void setVely(double vely) {
        this.vely = vely;
    }
}
