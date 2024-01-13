/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.model;

import aeternus.controller.GameEngine;
import java.awt.Image;
import java.util.Random;

/**
 *
 * @author User
 */
public class Item {
    private String id;
    private String rarity;
    private String name;
    private Image image;
    private String stat;
    private int shopPrice = -1;
    GameEngine gm;
    
    public Item(String id, String name, String rarity, GameEngine gm, Image image, String stat){
        this.id = id;
        this.name = name;
        this.rarity = rarity;
        this.gm = gm;
        this.image = image;
        this.stat = stat;
        this.shopPrice = calcPrice();
    }

    public String getId() {
        return id;
    }
    
    public int getShopPrice(){
        return shopPrice;
    }
    
    public Image getImg(){
        return image;
    }

    public String getRarity() {
        return rarity;
    }
    
    public int getRarityTier(){
        int out = 0;
        switch(rarity){
            case "common":
                out = 0;
            break;
            case "uncommon":
                out = 1;
            break;
            case "rare":
                out = 2;
            break;
            case "epic":
                out = 3;
            break;
            case "legendary":
                out = 4;
            break;
        }
        return out;
    }
    
    public String getColor(){
        String out = "";
        switch(rarity){
            case "common":
                out = "ffffff";
            break;
            case "uncommon":
                out = "00ff00";
            break;
            case "rare":
                out = "0000ff";
            break;
            case "epic":
                out = "ff00ff";
            break;
            case "legendary":
                out = "ffff00";
            break;
        }
        return out;
    }
    
    public void boostRarity(){
        switch(rarity){
            case "common":
                rarity = "uncommon";
            break;
            case "uncommon":
                rarity = "rare";
            break;
            case "rare":
                rarity = "epic";
            break;
            case "epic":
                rarity = "legendary";
            break;
        }
    }

    public String getName() {
        return name;
    }
    
    public String getStat(){
        return stat;
    }
    
    public int calcPrice(){
        int out = 10000;
        switch(rarity){
            case "common":
                out *= 1;
            break;
            case "uncommon":
                out *= 2;
            break;
            case "rare":
                out *= 3;
            break;
            case "epic":
                out *= 4;
            break;
            case "legendary":
                out *= 5;
            break;
        }
        Random rnd = new Random();
        out += rnd.nextInt(10000);
        return out;
    }
    
    public int getPrice(){
        return shopPrice;
    }
    
    public Boolean equip(int slot){
    return true;}
}
