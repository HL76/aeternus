/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.controller;

import aeternus.model.Monster;
import aeternus.model.Player;
import aeternus.model.Sprite;
import aeternus.view.AeternusGUI;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author User
 */
public class CombatEngine {
    //Enemy types can be adjusted here.
    public enum enemy{
        KNIGHT(10, 2), MAGE(5, 5), VANGUARD(20, 2);
        private int hp;
        private int dmg;
        
        enemy(int hp, int dmg){
            this.hp = hp;
            this.dmg = dmg;
        }
        
        public int getHp(){
            return hp;
        }
        
        public int getDmg(){
            return dmg;
        }
    }
    
    private ArrayList<JLabel> combatMenu = new ArrayList<JLabel>();
    private Player p;
    private Monster m;
    private LabyrinthEngine lab;
    private PopupFloatingText pop;
    private AeternusGUI gui;
    private GameEngine game;
    
    public CombatEngine(Monster mon, JPanel dest, Player pl, LabyrinthEngine lab, AeternusGUI gui, GameEngine game){
        this.lab = lab;
        this.p = pl;
        this.m = mon;
        this.gui = gui;
        this.game = game;
        renderMenu(dest, m);
    }
    
    //Renders Combat menu
    private void renderMenu(JPanel dest, Monster m){
        JLabel bg, img1, img2, dmg1, dmg2, hp1, hpBox1, hp2, hpBox2, stats, str, dex, con, lck, inT;
        bg = new JLabel();
        img1 = new JLabel();
        img2 = new JLabel();
        dmg1 = new JLabel();
        dmg2 = new JLabel();
        hp1 = new JLabel();
        hp2 = new JLabel();
        hpBox1 = new JLabel();
        hpBox2 = new JLabel();
        stats = new JLabel();
        str = new JLabel();
        con = new JLabel();
        dex = new JLabel();
        lck = new JLabel();
        inT = new JLabel();
        combatMenu = new ArrayList<>(List.of(bg, img1, img2, dmg1, dmg2, hpBox1, hpBox2, hp1, hp2, stats, str, dex, inT, con, lck));
        for(JLabel l : combatMenu){
            lab.labelFactory(l, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 30, 150),
                new Font("Agency FB", 1, 36));
            dest.add(l, 0);
        }
        bg.setBounds(100, 100, 1720, 880);
        img1.setBounds(180, 140, 460, 460);
        img1.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource(
                                   "/images/PLAYER.png")).getImage().getScaledInstance(460, 460, Image.SCALE_DEFAULT)));
        img2.setBounds(1280, 140, 460, 460);
        img2.setIcon(new ImageIcon(m.getImg().getScaledInstance(460, 460, Image.SCALE_DEFAULT)));
        
        dmg1.setBackground(new Color(146, 0, 0, 0));
        dmg2.setBackground(new Color(146, 0, 0, 0));
        dmg1.setName("playerDamage");
        dmg2.setName("monsterDamage");
        dmg1.setBounds(180, 140, 460, 460);
        dmg2.setBounds(1280, 140, 460, 460);
        dest.setComponentZOrder(dmg1, 0);
        dest.setComponentZOrder(dmg2, 0);
        
        hp1.setBounds(180, 620, (int)(460*(p.getHp()/p.getMaxHp())), 60);
        hp1.setBackground(new Color(0, 90, 0));
        hp1.setName("hp1");
        hpBox1.setBounds(180, 620, 460, 60);
        
        hp2.setBounds(1280, 620, 460, 60);
        hp2.setBackground(new Color(0, 90, 0));
        hp2.setName("hp2");
        hpBox2.setBounds(1280, 620, 460, 60);
        
        stats.setBounds(180, 700, 460, 240);
        str.setBounds(180, 700, 460, 40);
        dex.setBounds(180, 750, 460, 40);
        con.setBounds(180, 800, 460, 40);
        lck.setBounds(180, 850, 460, 40);
        inT.setBounds(180, 900, 460, 40);
        
        str.setText("Strength: " + p.getStat(0));
        con.setText("Constitution: " + p.getStat(1));
        dex.setText("Dexterity: " + p.getStat(2));
        inT.setText("Intelligence: " + p.getStat(3));
        lck.setText("Luck: " + p.getStat(4));
        
        pop = new PopupFloatingText(dest, 15);
        dest.revalidate();
        dest.repaint();
        simulateCombat();
    }
    
    //Simulates combat encounter
    private void simulateCombat(){
        Random rnd = new Random();
        Thread one = new Thread() {
            Boolean x = rnd.nextBoolean();
            public void run() {
                while(p.getHp() > 0 && m.getHp() > 0){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CombatEngine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    attack(x);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(CombatEngine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if(m.getHp() > 0 && p.getHp() > 0){
                        attack(!x);
                    }
                }
                if(p.getHp() > 0){
                    //monster beaten
                    lab.getPlayer().setHp(p.getHp());
                    lab.deleteMonster();
                    lab.rollLvl();
                    lab.resume();
                }else{
                    //player died
                    lab.exit(false);
                }
                for(JLabel j : combatMenu){
                    j.getParent().remove(j);
                }
                lab.deleteCombat();
            }
        };
        one.start();
        
    }
    
    //Specialized fade effects
    public void fadeIn(JLabel l, int length){
            try {
                Color c = l.getBackground();
                for(int i = 0; i < 200; i++){
                    l.setBackground(new Color(c.getRed(), c.getGreen(), c.getBlue(), i));
                    l.getParent().validate();
                    l.getParent().repaint();
                    Thread.sleep(length);
                }
            } catch(InterruptedException v) {
                System.out.println(v);
            }
    }
    
    public void fadeOut(JLabel l, int length){
            try {
                Color c = l.getBackground();
                for(int i = 0; i < 200; i++){
                    l.setBackground(new Color(c.getRed(), c.getGreen(), c.getBlue(), 200-i));
                    l.getParent().validate();
                    l.getParent().repaint();
                    Thread.sleep(length);
                }
            } catch(InterruptedException v) {
                System.out.println(v);
            }
    }
    
    //Custom fade varieties
    private void attackEffect(Sprite s, Boolean hit){
        if(s instanceof Player){
            for(JLabel j : combatMenu){
                if(j.getName() != null && j.getName().equals("monsterDamage")){
                    fadeIn(j, 1);
                    fadeOut(j, 1);
                }
            }
        }else{
            for(JLabel j : combatMenu){
                if(j.getName() != null && j.getName().equals("playerDamage")){
                    if(!hit){
                        j.setBackground(Color.white);
                    }
                    fadeIn(j, 1);
                    fadeOut(j, 1);
                    j.setBackground(new Color(146, 0, 0, 0));
                }
            }
        }
    }
    
    //Attack logic
    private void attack(Boolean dir){
        Random rnd = new Random();
        if(dir){
            if(rnd.nextInt(100)+1 < (p.getStat(4)/p.getStat(5))){
                pop.spawnEffect("Critical! " + String.valueOf(p.getDamage()*(p.getStat(3)/100 + 2)), false, new Point(1510, 300), 200);
                m.setHp(m.getHp()-p.getDamage()*(p.getStat(3)/100 + 2));
                attackEffect(p, true);
            }else{
                m.setHp(m.getHp()-p.getDamage());
                pop.spawnEffect(String.valueOf(p.getDamage()), false, new Point(1510, 300), 200);
                attackEffect(p, true);
            }
        }else{
            if(rnd.nextInt(100)+1 <= (p.getStat(2) / 5)){
                //miss
                pop.spawnEffect("Miss", true, new Point(410, 300), 200);
                attackEffect(m, false);
                return;
            }
            
            if(rnd.nextInt(100)+1 <= ((p.getStat(0) +  game.getArmor()) / 5)){
                //withstood
                pop.spawnEffect("Withstood", true, new Point(410, 300), 200);
                attackEffect(m, false);
                return;
            }
            
            p.setHp(p.getHp()-m.getDamage());
            pop.spawnEffect(String.valueOf(m.getDamage()), false, new Point(410, 300), 200);
            attackEffect(m, true);
            
        }
        for(JLabel j : combatMenu){
            if(j.getName() != null && j.getName().equals("hp1")){
                j.setBounds(180, 620, (int)(460*(p.getHp()/p.getMaxHp())), 60);
                j.getParent().revalidate();
                j.getParent().repaint();
            }else if(j.getName() != null && j.getName().equals("hp2")){
                j.setBounds(1280, 620, (int)(460*(m.getHp()/m.getMaxHp())), 60);
                j.getParent().revalidate();
                j.getParent().repaint();
            }
        }
    }
        
    public ArrayList<JLabel> getMenu(){
        return combatMenu;
    }
    
}
