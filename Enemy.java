import java.util.*;

class Enemy {

    String name;
    int hp;
    int dmg;

    int x, y;

    Random r = new Random();

    List<Item> drops = new ArrayList<>();

    Enemy(String n, int h, int d) {

        name = n;
        hp = h;
        dmg = d;
    }

    boolean dead() {

        return hp <= 0;
    }

    void takeDamage(Game g, int a) {

        hp -= a;
    }

    String drop() {

        if (drops.isEmpty()) return null;

        return drops.get(r.nextInt(drops.size())).name;
    }

    void aiMove(Game g) {

        int d = r.nextInt(4);

        if (d == 0) x++;
        if (d == 1) x--;
        if (d == 2) y++;
        if (d == 3) y--;
    }

    void aiAttack(Game g) {

        g.player.hp -= dmg;
        g.eLog.add(name + " attacks");
    }
}