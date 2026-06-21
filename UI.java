import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

class UI extends JFrame {

    Game game;

    GamePanel gameP;
    LogPanel logP;
    ActionPanel actP;

    enum UIState {
        NORMAL,
        COMBAT,
        SHOP
    }

    UIState state = UIState.NORMAL;

    UI(Game g) {

        game = g;

        setTitle("RPG Engine");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gameP = new GamePanel(g);
        logP = new LogPanel();
        actP = new ActionPanel(g);

        add(gameP, BorderLayout.CENTER);
        add(logP, BorderLayout.EAST);
        add(actP, BorderLayout.SOUTH);

        setFocusable(true);

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
            .addKeyEventDispatcher(e -> {

                if (e.getID() != KeyEvent.KEY_PRESSED) return false;

                if (state != UIState.NORMAL) return false;

                int k = e.getKeyCode();

                if (k == KeyEvent.VK_W) game.movePlayer(0, -1);
                if (k == KeyEvent.VK_S) game.movePlayer(0, 1);
                if (k == KeyEvent.VK_A) game.movePlayer(-1, 0);
                if (k == KeyEvent.VK_D) game.movePlayer(1, 0);

                return false;
            });

        Timer t = new Timer(16, e -> {

            game.tick();
            refreshUI(game);
            repaint();
        });

        t.start();
    }

    void showUI() {

        setVisible(true);
    }

    void refreshUI(Game g) {

        logP.set(g.eLog.log);
    }

    void setCombat(boolean b) {

        state = b ? UIState.COMBAT : UIState.NORMAL;

        actP.setCombat(b);
    }

    void showShopButton(boolean b) {

        actP.shop.setVisible(b);
    }

    void showShop(Shop s) {

        state = UIState.SHOP;

        StringBuilder sb = new StringBuilder();

        for (Item i : s.items) {

            sb.append(i.name)
              .append(" : ")
              .append(i.cost)
              .append("\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString());

        state = UIState.NORMAL;
    }
}

class GamePanel extends JPanel {

    Game game;

    GamePanel(Game g) {

        game = g;
        setBackground(Color.BLACK);
    }

    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // GRID
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {

                g.setColor(Color.DARK_GRAY);
                g.fillRect(x * 30, y * 30, 28, 28);
            }
        }

        g.setColor(Color.BLUE);
        g.fillOval(game.player.x * 30, game.player.y * 30, 20, 20);

        g.setColor(Color.RED);

        for (Enemy e : game.enemies) {

            if (e == null || e.dead()) continue;

            int ex = e.x * 30;
            int ey = e.y * 30;

            g.fillOval(ex + 5, ey + 5, 10, 10);
        }
    }
}

class LogPanel extends JPanel {

    JTextArea area = new JTextArea(30, 20);

    LogPanel() {

        setLayout(new BorderLayout());
        add(new JScrollPane(area), BorderLayout.CENTER);
    }

    void set(List<String> l) {

        StringBuilder sb = new StringBuilder();

        for (String s : l)
            sb.append(s).append("\n");

        area.setText(sb.toString());
    }
}

class ActionPanel extends JPanel {

    JButton explore = new JButton("Explore");
    JButton item = new JButton("Item");
    JButton hit = new JButton("Hit");
    JButton run = new JButton("Run");
    JButton shop = new JButton("Shop");

    ActionPanel(Game g) {

        setLayout(new FlowLayout());

        explore.addActionListener(e -> g.trigger("Explore"));
        item.addActionListener(e -> showInv(g));
        hit.addActionListener(e -> g.hit());
        run.addActionListener(e -> g.run());
        shop.addActionListener(e -> g.openShop());

        add(explore);
        add(item);
        add(hit);
        add(run);
        add(shop);

        setCombat(false);
    }

    void showInv(Game g) {

        StringBuilder sb = new StringBuilder();

        for (Item i : g.inv.items) {

            sb.append(i.name).append("\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString());
    }

    void setCombat(boolean b) {

        hit.setVisible(b);
        run.setVisible(b);
    }
}

class Main {

    public static void main(String[] args) {

        Game g = new Game();
        UI ui = new UI(g);

        g.setUI(ui);

        Enemy e = EnemySpawning.create();

        ui.showUI();
    }
}