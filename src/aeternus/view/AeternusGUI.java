/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aeternus.view;

import java.awt.Component;
import javax.swing.JPanel;
import aeternus.controller.DialougeSystem;
import aeternus.controller.EngineMenu;
import aeternus.controller.EscapeMenu;
import aeternus.controller.GameEngine;
import aeternus.controller.InventoryManager;
import aeternus.controller.ShopManager;
import aeternus.model.InfoText;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author User
 */
public class AeternusGUI {
    
    private MainFrame s = new MainFrame(this);
    private Component[] clist = s.getRootPane().getContentPane().getComponents();
    private volatile boolean dialougeState = false;
    private boolean activeLabyrinth = false;
    private JLabel backgroundImage = new JLabel();
    private JLabel subBackground = new JLabel();
    private JLabel transitionCover = new JLabel();
    private JLabel subTransition = new JLabel();
    private ArrayList<JLabel> mapLocations = new ArrayList<JLabel>();
    private ArrayList<JLabel> locationOptions = new ArrayList<JLabel>();
    private InventoryManager inventory;
    private JLabel soulCount = new JLabel();
    private JLabel invLabel = new JLabel();
    private GameEngine game;
    private GameEngine.locations currentLocale;
    private LocalTime portalEntry = LocalTime.now().minusMinutes(10);
    private MouseListener ml;
    private Boolean endTrigger = false;
    private Boolean fade = false;
    private ShopManager shop;
    private EscapeMenu esc;
    private EngineMenu engine;
    
    public AeternusGUI(){ 
        s.setVisible(true);
        s.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("images/gameIcon.png")));
        new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
                findPanel("SplashScreen").setVisible(false);
            }
        }, 
        10000
        );
    }

    //Initializes the GUI
    public void initiateGame(Boolean newGame){
        this.game = new GameEngine(this, 73915, newGame);
        inventory = new InventoryManager(this, game);
        ((JPanel) s.getRootPane().getContentPane().getComponent(1)).getComponent(0).setVisible(true);
        new java.util.Timer().schedule( 
            new java.util.TimerTask() {
                @Override
                public void run() {
                    findPanel("MainMenu").setVisible(false);
                    findPanel("Main").setVisible(true);
                    if(newGame){
                        newGame();
                    }else{
                        loadGame();
                    }
                }
            }, 
        5700
        );
        
    }
    
    public JFrame getFrame(){
        return s;
    }
    
    public ShopManager getShop(){
        return shop;
    }
    
    public Boolean getActiveLabyrinth(){
        return activeLabyrinth;
    }
    
    public ArrayList<JLabel> getLocationOptions() {
        return locationOptions;
    }
    
    //Returns a panel based on it's name
    public JPanel findPanel(String name){
        for(Component c : clist){
            if(c.getName().equals(name)){
                return (JPanel)c;
            }
        }
        return null;
    }
    
    //Creates and plays an info card
    private void playInfo(InfoText inf, String text, int length, String path, int speed) throws InterruptedException{
        fadeIn(transitionCover, 5);
        inf.createText(text, length);
        setBackground(path, speed, false);
        fadeOut(transitionCover, 5);
    }
    
    public void setDialougeState(boolean b){
        dialougeState = b;
    }
    
    //Transitions from background-to-background on the main panel
    private void setBackground(String path, int speed, boolean transition){
        if(transition){
            fadeIn(transitionCover, speed);
        }
        backgroundImage.setIcon(new javax.swing.ImageIcon(
                                        new javax.swing.ImageIcon(getClass().getResource(path)).getImage().getScaledInstance(1920, 1080, Image.SCALE_DEFAULT)));
        if(transition){
            fadeOut(transitionCover, speed);
        }
    }
    
    //Transitions from background-to-background from the main to the sub panel
    private void setBackground(String path, int speed, boolean transition, JLabel bg, JPanel to){
        if(transition){
            fadeIn(transitionCover, speed);
        }
        bg.setIcon(new javax.swing.ImageIcon(
                                        new javax.swing.ImageIcon(getClass().getResource(path)).getImage().getScaledInstance(1920, 1080, Image.SCALE_DEFAULT)));
        
        to.setVisible(true);
        to.getParent().revalidate();
        to.getParent().repaint();
        transitionCover.setBackground(new Color(0,0,0,0));
        if(transition){
            fadeOut(subTransition, speed);
        }
    }
    
    //Fading for transitions
    public void fadeIn(JLabel l, int length){
        l.setFocusable(false);
        l.getParent().setComponentZOrder(l, 0);
        l.setVisible(true);
        l.paintImmediately(l.getVisibleRect());
            try {
                for(int i = 0; i < 255; i++){
                    l.setBackground(new Color(0, 0, 0, i));
                    l.getParent().revalidate();
                    l.getParent().repaint();
                    Thread.sleep(length);
                }
            } catch(InterruptedException v) {
                System.out.println(v);
            }
    }
    
    public void fadeOut(JLabel l, int length){
        l.setFocusable(false);
        l.getParent().setComponentZOrder(l, 0);
            try {
                for(int i = 0; i < 255; i++){
                    l.setBackground(new Color(0, 0, 0, 255-i));
                    l.getParent().revalidate();
                    l.getParent().repaint();
                    Thread.sleep(length);
                }
            } catch(InterruptedException v) {
                System.out.println(v);
            }
    }
    
    //Transition creator
    public JLabel createTransition(JPanel dest){
        JLabel tr = new JLabel();
        tr.setForeground(new java.awt.Color(0, 0, 0, 0));
        tr.setBackground(Color.black);
        tr.setOpaque(true);
        tr.setVisible(true);
        dest.add(tr);
        dest.setComponentZOrder(tr, 0);
        tr.setBounds(0, 0, 1920, 1080);
        return tr;
    }
    
    //Background image creator
    public JLabel createBackground(JPanel dest, int z){
        JLabel bg = new JLabel();
        bg.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        bg.setOpaque(false);
        bg.setVisible(true);
        bg.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        dest.add(bg);
        dest.setComponentZOrder(bg, z);
        bg.setBounds(0, 0, 1920, 1080);
        return bg;
    }
    
    //Loads in the general area (Ex.: Square)
    private void loadLocale(GameEngine.locations x, boolean transition){
        currentLocale = x;
        setBackground(x.getPath(), 5, transition);
        loadPointsOfInterest(x);
        game.soulCountThread();
        findPanel("Main").getParent().validate();
        findPanel("Main").getParent().repaint();
    }
    
    //Searches if portal label is visible and refreshes it's text according to the cooldown
    public void refreshPortal(){
        for(JLabel j : locationOptions){
            if(j.getName().equals("Enter Portal")){
                long minutes = Duration.between(LocalTime.now(), portalEntry.plusMinutes(5)).toMinutes();
                long seconds = Duration.between(LocalTime.now(), portalEntry.plusMinutes(5)).toSecondsPart();
                if(!activeLabyrinth){
                    if(minutes > 0 || seconds > 0){
                        j.setEnabled(false);
                        String s = "";
                        if(seconds < 10){
                            s = minutes + ":0" + seconds;
                        }else{
                            s = minutes + ":" + seconds;
                        }

                        j.setText(s);
                        j.getParent().revalidate();
                        j.getParent().repaint();
                    }else if(!j.isEnabled()){
                        j.setEnabled(true);
                        j.setText("Enter Portal");
                    }
                }
            }
        }
    }
    
    //Triggers after you exit a labyrinth
    public void afterPortalExit(Boolean win){
        LocalTime l2 = portalEntry.plusMinutes(5);
        long difference = LocalTime.now().until(l2, ChronoUnit.MINUTES);
        if(win && game.getGenericStat("PortalsEntered") == 0){
            startDialouge("FirstLabyrinthWon");
        }else if(!win && game.getGenericStat("PortalsEntered") == 0){
            startDialouge("FirstLabyrinthLost");
        }
        game.setGenericStat("PortalsEntered", game.getGenericStat("PortalsEntered")+1);
        portalThread(difference);
    }
    
    //Loads all clickable locations on a map (Ex.: Base, Magic Shop)
    private void loadPointsOfInterest(GameEngine.locations x){
        ArrayList<String[]> POI = x.getPOI();
        currentLocale = x;
        setSoulCount(findPanel("Main"));
        esc = new EscapeMenu(findPanel("Main"), this, game);
        findPanel("Main").requestFocus();
        invLabel.setEnabled(true);
        for(String[] point : POI){
            JLabel newLabel = new JLabel();
            newLabel.setIcon(new ImageIcon(new ImageIcon(getClass().getResource(
                                    "/images/transparentBg.png")).getImage().getScaledInstance(130, 130, Image.SCALE_DEFAULT)));
            labelFactory(newLabel, false, game.getLockState(point[5]), new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(255, 255, 255), 
                new Color(0, 0, 0, 100), 
                String.valueOf(point[0]), 
                new Font("Agency FB", 0, 30));
            newLabel.setLayout(null);
            newLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            newLabel.setName(point[5]);
            findPanel("Main").add(newLabel);
            findPanel("Main").setComponentZOrder(newLabel, 1);
            newLabel.setBounds(Integer.parseInt(point[1]), Integer.parseInt(point[2]), Integer.parseInt(point[3]), Integer.parseInt(point[4]));
            newLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if(newLabel.isEnabled()){
                        loadLocation(evt);
                    }
                }
            });
            mapLocations.add(newLabel);
        }
        transitionCover.getParent().setComponentZOrder(transitionCover, 0);
    }
    
    //Deletes the clickable locations
    private void removePointsOfInterest(){
        for(JLabel l : mapLocations){
            l.getParent().remove(l);
        }
        mapLocations.clear();
        backgroundImage.getParent().revalidate();
        backgroundImage.getParent().repaint();
    }
    
    public void setPOI(boolean b){
        for(JLabel l : mapLocations){
            l.setEnabled(b);
        }
    }
    
    //Creates a submenu panel used in every location.
    public void createNewPanel(String name, int z){
        JPanel newP = new JPanel();
        newP.setBackground(new java.awt.Color(0, 0, 0));
        newP.setMinimumSize(new java.awt.Dimension(1920, 1080));
        subBackground = createBackground(newP, 0);
        subTransition = createTransition(newP);
        newP.setName(name);
        newP.setPreferredSize(new java.awt.Dimension(1920, 1080));
        newP.setLayout(null);
        s.getRootPane().getContentPane().add(newP);
        newP.setBounds(0, 0, 1920, 1080);
        s.getRootPane().getContentPane().setComponentZOrder(newP, z);
        clist = s.getRootPane().getContentPane().getComponents();
        findPanel(name).setVisible(false);
        newP.getParent().revalidate();
        newP.getParent().repaint();
    }
    
    //Loads specific location with it's available options (Ex.:Shop, Talk)
    private void loadLocation(java.awt.event.MouseEvent evt){
        createNewPanel("subMenu", 0);
        setSoulCount(findPanel("subMenu"));
        String flag = game.getFlag(GameEngine.interactables.valueOf(evt.getComponent().getName()));
        ArrayList<String[]> options = GameEngine.interactables.valueOf(evt.getComponent().getName()).getOptions();
        Thread one = new Thread() {
            @Override
            public void run() {
                setBackground("/images/Locations/" + evt.getComponent().getName() + ".png", 2, true, subBackground, findPanel("subMenu"));
            }
        };
        one.start();
        setPOI(false);
        if(flag != null && !flag.equals("")){
            startDialouge(flag);
        }
        int cnt = 0;
        for(String[] row : options){
            if(row[0].equals("option")){
                addOption(row[1], GameEngine.interactables.valueOf(evt.getComponent().getName()).toString(), cnt);
                cnt++;
            }
        }
        invLabel.setEnabled(true);
        esc = new EscapeMenu(findPanel("subMenu"), AeternusGUI.this, game);
    }
    
    //Launches the thread that refreshes the portal label
    private void portalThread(long difference){
        Thread two = new Thread() {
            @Override
            public void run() {
                while(difference > 0){
                    refreshPortal();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
            }
            };
        two.start();
    }
    
    //Adds it's given option to the submenu panel (Ex.: Shop)
    private void addOption(String name, String id, int place){
        JLabel newL = new JLabel();
        labelFactory(newL, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(40, 40, 40), 
                name, 
                new Font("Agency FB", 0, 36));
        findPanel("subMenu").add(newL);
        findPanel("subMenu").setComponentZOrder(newL, 0);
        newL.setBounds(610, 400 + (place*100), 700, 50);
        locationOptions.add(newL);
        newL.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(newL.isEnabled()){
                    option(id, name);
                }
            }
        });
    }
    
    //Decides what to do when an option is clicked
    private void option(String id, String name){
        if(!dialougeState){
            setOptions(false);
            String flag = game.getFlag(name);
            if(flag != null && !flag.equals("")){
                startDialouge(flag);
                return;
            }
            switch(name){
                case "Leave":
                    leaveSubMenu();
                break;
                case "Shop":
                    loadShop(id);
                break;
                case "Engine":
                    setOptionsVisibility(false);
                    engine = new EngineMenu(this, game);
                break;
                case "Talk":
                    if(id.equals("MAGICSHOP") && game.getSouls() > 100000000){
                        if(game.endReq()){
                            fade = true;
                            startDialouge("EndReady");
                        }else{
                            startDialouge("EndNotReady");
                        }
                    }else{
                        startDialouge("Casual/" + id + (int)(Math.random()*5));
                    }
                break;
                case "Burn Items":
                    showBurner();
                break;
                case "Merge Items":
                    showMerger();
                break;
                case "Enter Portal":
                    try {
                        if(game.getSouls() < 10000 && game.getGenericStat("PortalsEntered") == 0){
                            startDialouge("NotEnoughSoulsPortal");
                        }else if(game.getSouls() >= 10000 && game.getGenericStat("PortalsEntered") == 0){
                            game.alterSouls(-10000);
                            portalEntry = LocalTime.now();
                            setOptions(false);
                            setOptionsVisibility(false);
                            activeLabyrinth = true;
                            game.enterPortal();
                        }else{
                            portalEntry = LocalTime.now();
                            setOptions(false);
                            setOptionsVisibility(false);
                            activeLabyrinth = true;
                            game.enterPortal();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                break;
            }
        }
    }
    
    public void endFade(){
        Thread x = new Thread(){
            @Override
            public void run(){
                fadeIn(subTransition, 15);
                System.exit(0);
            }
        };
        x.start();
    }
    
    public void showMerger(){
        inventory.showInventory(findPanel("subMenu"), "merge");
    }
    
    public void setOptions(boolean b){
        for(JLabel l : locationOptions){
            l.setEnabled(b);
        }
    }
    
    public void setOptionsVisibility(boolean b){
        for(JLabel l : locationOptions){
            l.setVisible(b);
        }
    }
    
    //Sets up the given label to simplify code view
    public void labelFactory(JLabel j, boolean opacity, boolean vis, int[] alignment, Color fG, Color bG, String text, Font f){
        j.setOpaque(opacity);
        j.setVisible(vis);
        j.setHorizontalAlignment(alignment[0]);
        j.setVerticalAlignment(alignment[1]);
        j.setForeground(fG);
        j.setBackground(bG);
        j.setText(text);
        j.setFont(f);
        j.setName(text);
    }
    
    //Creates label taht displays current souls
    private void setSoulCount(JPanel destination){
        labelFactory(soulCount, true, true, new int[]{SwingConstants.LEFT, SwingConstants.TOP}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 100), 
                String.valueOf(game.getSouls()), 
                new Font("Agency FB", 1, 36));
        destination.add(soulCount, 0);
        soulCount.setBounds(10, 10, 120, 50);
        createInvButton(destination);
    }
    
    public JLabel getInvLabel(){
        return invLabel;
    }
    
    //Creates the label taht activates/deactivates the inventory
    private void createInvButton(JPanel destination){
        invLabel.removeMouseListener(ml);
        invLabel.setEnabled(false);
        labelFactory(invLabel, true, true, new int[]{SwingConstants.CENTER, SwingConstants.CENTER}, 
                new Color(204, 204, 204), 
                new Color(0, 0, 0, 50), 
                "Inv", 
                new Font("Agency FB", 1, 28));
        destination.add(invLabel, 0);
        invLabel.setBounds(1860, 0, 60, 60);
        ml = new MouseAdapter(){
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(!invLabel.isEnabled()){
                    return;
                }
                
                if(inventory.getInventoryMenu().size()>0){
                    inventory.hideInventory(destination);
                    if(shop != null){
                        shop.setAll(true);
                    }
                }else{
                    inventory.showInventory(destination, "");
                    if(shop != null){
                        shop.setAll(false);
                    }
                }
            }
        };
        invLabel.addMouseListener(ml);
    }
    
    public void closeEngine(){
        engine = null;
    }
    
    //Refreshes the label displaying souls and the upgrade options availability
    public void refreshSouls(){
        if(!activeLabyrinth){
            if(game.getSouls() > 100000000){
                soulCount.setForeground(Color.cyan);
                if(!endTrigger){
                    game.setFlag("MAGICSHOP", "Ending");
                    endTrigger = true;
                }
            }
            if(game.getSouls() > 1000000){
                soulCount.setText(String.format("%.1f", game.getSouls()/1000000)+ "M");
            }else if(game.getSouls() > 1000){
                soulCount.setText(String.format("%.0f", game.getSouls()));
            }else{
                soulCount.setText(String.format("%.1f", game.getSouls()));
            }
            soulCount.getParent().revalidate();
            soulCount.getParent().repaint();
            if(engine != null){
                engine.refreshUpgrades();
            }
            
        }
    }
    
    //"Leaves" the submenu panel by destroying it
    public void leaveSubMenu(){
        findPanel("subMenu").getParent().remove(findPanel("subMenu"));
        removePointsOfInterest();
        loadPointsOfInterest(currentLocale);
        s.getRootPane().getContentPane().revalidate();
        s.getRootPane().getContentPane().repaint();
    }
    
    //Creates and runs a dialougesystem
    private void startDialouge(String name){
        dialougeState = true;
        setOptions(false);
        DialougeSystem d = new DialougeSystem(findPanel("subMenu"), this);
        Thread one = new Thread() {
            @Override
            public void run() {
                try {
                    d.play(name);
                    while(dialougeState){}
                    setOptions(true);
                } catch (Exception ex) {
                    Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                game.checkConnections(name);
                game.removeFlag(name);
                if(fade){
                    endFade();
                }
            }
        };
        one.start();
    }
    
    //Decids whether to open the shop based on flags
    private void loadShop(String id){
        if(id.equals("MAGICSHOP")){
            if(game.getSouls() == 0){
                startDialouge("NoSoulsMagic");
            }else if(game.getSouls() < 10000 && !game.getLockState("PORTAL")){
                startDialouge("BeforePortalsMagic");
            }else if(game.getSouls() >= 10000 && !game.getLockState("PORTAL")){
                startDialouge("UnlockPortalsMagic");
            }else{
                shop = new ShopManager(this, game, id);
                shop.openShop();
            }
        }else if(id.equals("WEAPONSHOP")){
            if(!game.getLockState("PORTAL")){
                startDialouge("BeforePortalsWEAPONSHOP");
            }else{
                shop = new ShopManager(this, game, id);
                shop.openShop();
            }
        }
    }
    
    public void showBurner(){
        inventory.showInventory(findPanel("subMenu"), "burn");
    }
    
    public void setActiveLabyrinth(Boolean b){
        activeLabyrinth = b;
    }
    
    //Loads previous save
    public void loadGame(){
        new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    transitionCover = createTransition(findPanel("Main"));
                    backgroundImage = createBackground(findPanel("Main"), 1);
                    setBackground("/images/Locations/menuBackground.png", 5, true);
                    loadLocale(GameEngine.locations.SQUARE, false);
                    
                } catch (Exception ex) {
                    Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 
        1000
);
    }
    
    //Creates a new game
    public void newGame(){
        new java.util.Timer().schedule( 
        new java.util.TimerTask() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    dialougeState = true;
                    DialougeSystem d = new DialougeSystem(findPanel("Main"), AeternusGUI.this);
                    InfoText inf = new InfoText(findPanel("Main"));
                    transitionCover = createTransition(findPanel("Main"));
                    transitionCover.setBackground(Color.cyan);
                    backgroundImage = createBackground(findPanel("Main"), 6);
                    setBackground("/images/Locations/caveBackground.png", 5, true);
                    d.play("Open");
                    while(dialougeState){}
                    Thread.sleep(1000);
                    playInfo(inf, "An hour later...", 3000, "/images/Locations/caveBackground.png", 5);
                    dialougeState = true;
                    d.play("Open2");
                    while(dialougeState){}
                    Thread.sleep(1000);
                    playInfo(inf, "At the surface", 2500, "/images/Locations/menuBackground.png", 5);
                    dialougeState = true;
                    d.play("Open3");
                    while(dialougeState){}
                    Thread.sleep(1000);
                    d.removeAllStuff();
                    loadLocale(GameEngine.locations.SQUARE, false);
                } catch (Exception ex) {
                    Logger.getLogger(AeternusGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 
        1000
);
    }
}