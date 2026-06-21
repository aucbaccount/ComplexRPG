import java.util.*;

class Game {

    UI ui;

    Player player = new Player();

    Map<String, Chunk> chunks = new HashMap<>();
    String currentChunk = "0,0";

    ELog eLog = new ELog();

    List<Enemy> enemies = new ArrayList<>();

    Inventory inv = new Inventory();

    Shop currentShop;

    boolean inCombat = false;
    boolean inShop = false;

    Enemy activeEnemy;

    Random r = new Random();

    long lastSpawn = 0;
    long lastMove = 0;

    void setUI(UI ui) {

        this.ui = ui;
    }

    void tick() {

        long now = System.currentTimeMillis();

        if (now - lastSpawn > 3500) {

            spawnEnemy();
            lastSpawn = now;
        }

        if (now - lastMove > 2000) {

            for (Enemy e : enemies)
                e.aiMove(this);

            lastMove = now;
        }
    }

    void movePlayer(int dx, int dy) {

        if (inCombat || inShop) return;

        player.x += dx;
        player.y += dy;

        wrapChunk();
        checkEnemy();
        checkShop();
    }

    void wrapChunk() {

        if (player.x < 0 || player.x > 19 || player.y < 0 || player.y > 19) {

            changeChunk();
        }
    }

    void changeChunk() {

        saveChunk();

        int cx = Integer.parseInt(currentChunk.split(",")[0]);
        int cy = Integer.parseInt(currentChunk.split(",")[1]);

        if (player.x < 0) cx--;
        if (player.x > 19) cx++;
        if (player.y < 0) cy--;
        if (player.y > 19) cy++;

        currentChunk = cx + "," + cy;

        loadChunk();

        player.x = 10;
        player.y = 10;
    }

    void saveChunk() {

        Chunk c = chunks.getOrDefault(currentChunk, new Chunk());

        c.shopX = r.nextInt(20);
        c.shopY = r.nextInt(20);

        chunks.put(currentChunk, c);
    }

    void loadChunk() {

        chunks.putIfAbsent(currentChunk, new Chunk());
    }

    void trigger(String e) {

        eLog.add(e);
    }

    void spawnEnemy() {

        Enemy e = EnemySpawning.create();

        e.x = r.nextInt(20);
        e.y = r.nextInt(20);

        enemies.add(e);

        eLog.add("Enemy spawned");
    }

    void checkEnemy() {

        for (Enemy e : enemies) {

            if (e.x == player.x && e.y == player.y && !e.dead()) {

                startCombat(e);
                return;
            }
        }
    }

    void startCombat(Enemy e) {

        activeEnemy = e;
        inCombat = true;

        ui.setCombat(true);

        eLog.add("Encounter: " + e.name);
    }

    void hit() {

        if (!inCombat || activeEnemy == null) return;

        activeEnemy.takeDamage(this, 10);

        if (activeEnemy.dead()) {

            String drop = activeEnemy.drop();

            if (drop != null)
                inv.add(new Item(drop, 0, "drop"));

            eLog.add("Enemy defeated");

            endCombat();
            return;
        }

        activeEnemy.aiAttack(this);
    }

    void useItem() {

        if (inCombat) {

            Item i = inv.use();

            if (i != null) {

                i.apply(this);
                eLog.add("Used " + i.name);
            }
        }
    }

    void run() {

        endCombat();

        eLog.add("Ran away");
    }

    void endCombat() {

        inCombat = false;
        activeEnemy = null;

        ui.setCombat(false);
    }

    void checkShop() {

        Chunk c = chunks.get(currentChunk);

        if (c != null && c.shopX == player.x && c.shopY == player.y) {

            if (currentShop == null) {

                currentShop = new Shop();
            }

            ui.showShopButton(true);

        } else {

            ui.showShopButton(false);
        }
    }

    void openShop() {

        if (currentShop == null) return;

        inShop = true;

        ui.showShop(currentShop);
    }

    void buy(Item item) {

        if (player.gold >= item.cost) {

            player.gold -= item.cost;
            inv.add(item);

            eLog.add("Bought " + item.name);
        }
    }
}

class Player {

    int x = 10;
    int y = 10;

    int hp = 100;
    int gold = 50;
}

class Chunk {

    int shopX = -1;
    int shopY = -1;
}

class Inventory {

    List<Item> items = new ArrayList<>();

    void add(Item i) {

        items.add(i);
    }

    Item use() {

        if (items.isEmpty()) return null;

        return items.remove(0);
    }
}

class Shop {

    List<Item> items = new ArrayList<>();

    Shop() {

        items.add(Items.potion);
        items.add(Items.sword);
        items.add(Items.apple);
    }
}

class ELog {

    List<String> log = new ArrayList<>();

    void add(String s) {

        log.add(s);
    }
}

class EnemySpawning {

    static Enemy create() {

        return new Enemy("Slime", 30, 5);
    }
}