import java.util.*;


// GAME CORE STATE

class Game {

    UI ui;

    Player player = new Player();
    World world = new World();

    ELog eLog = new ELog();

    Shop shopS = new Shop(eLog);
    NPC npcS = new NPC(eLog);
    Combat combatS = new Combat(eLog);
    Exploration expS = new Exploration(world, eLog);

    void setUI(UI ui) {
        
        this.ui = ui;
    }

    void trigger(String e) {
        
        eLog.add(e);
        if (ui != null) ui.refreshUI(this);
    }
}


// PLAYER

class Player {

    int hp = 100;
    int gold = 0;

    void dmg(int a) {
        
        hp -= a;
    }

    void addGold(int g) {
        
        gold += g;
    }
}


// WORLD (EMPTY)

class World {

    List<String> locs = new ArrayList<>();

    void addLoc(String l) {
        
        locs.add(l);
    }
}


// EVENT LOG

class ELog {

    List<String> log = new ArrayList<>();

    void add(String e) {
        
        log.add(e);
    }

    List<String> get() {
        
        return log;
    }
}


// SHOP

class Shop {

    ELog eLog;

    Shop(ELog eLog) {
        
        this.eLog = eLog;
    }

    void openShop(Player p) {
        
        eLog.add("Shop opened");
    }

    void buy(Player p, String item, int cost) {
        
        eLog.add("Buy: " + item);
    }
}


// NPC

class NPC {

    ELog eLog;

    NPC(ELog eLog) {
        
        this.eLog = eLog;
    }

    void talk(String id) {
        
        eLog.add("Talk NPC: " + id);
        DialogueEngine.trigger(id);
    }
}


// COMBAT

class Combat {

    ELog eLog;

    Combat(ELog eLog) {
        
        this.eLog = eLog;
    }

    void encounter(String mob) {
        
        eLog.add("Encounter: " + mob);
    }

    void attack(Player p, String mob) {
        
        eLog.add("Attack: " + mob);
    }
}


// EXPLORATION 

class Exploration {

    World world;
    ELog eLog;

    Exploration(World world, ELog eLog) {
        
        this.world = world;
        this.eLog = eLog;
    }

    void move(String loc) {
        
        eLog.add("Move: " + loc);
    }
}


// DIALOGUE ENGINE

class DialogueEngine {

    static void trigger(String id) {
        
        // empty
    }
}