class Item {

    String name;
    int cost;
    String type;

    Item(String n, int c, String t) {

        name = n;
        cost = c;
        type = t;
    }

    void apply(Game g) {

        if (type.equals("heal")) {

            g.player.hp += 20;
        }

        if (type.equals("gold")) {

            g.player.gold += 10;
        }
    }
}

class Items {

    static Item potion = new Item("Potion", 10, "heal");
    static Item apple = new Item("Apple", 5, "heal");
    static Item coin = new Item("Coin", 0, "gold");

    static Item sword = new Item("Sword", 25, "weapon");

    static Item[] all = {
        potion,
        apple,
        coin,
        sword
    };
}