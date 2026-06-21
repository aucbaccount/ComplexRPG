import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.List;

// WINDOW

public class UI extends JFrame {

    GameUI gameUI;
    LogUI logUI;
    ActionUI actUI;

    public UI(Game game) {
        
        setTitle("Complex RPG");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gameUI = new GameUI();
        logUI = new LogUI();
        actUI = new ActionUI(game);

        add(gameUI, BorderLayout.CENTER);
        add(logUI, BorderLayout.EAST);
        add(actUI, BorderLayout.SOUTH);
    }

    void showUI() {
        
        setVisible(true);
    }

    void refreshUI(Game game) {
        
        logUI.update(game.eLog.get());
    }
}


// WORLD PANEL

class GameUI extends JPanel {

    GameUI() {
        
        setBackground(Color.BLACK);
    }

    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.drawString("WORLD VIEW (EMPTY)", 50, 50);
    }
}


// LOG PANEL

class LogUI extends JPanel {

    JTextArea area = new JTextArea(30, 20);

    LogUI() {
        
        area.setEditable(false);
        setLayout(new BorderLayout());
        add(new JScrollPane(area), BorderLayout.CENTER);
    }

    void update(List<String> log) {
        
        StringBuilder sb = new StringBuilder();
        for (String s : log) sb.append(s).append("\n");
        area.setText(sb.toString());
    }
}


// ACTION PANEL

class ActionUI extends JPanel {

    ActionUI(Game game) {
        
        setLayout(new FlowLayout());

        JButton explore = new JButton("Explore");
        JButton shop = new JButton("Shop");
        JButton talk = new JButton("Talk");
        JButton fight = new JButton("Fight");

        explore.addActionListener(e ->
                game.trigger("Explore"));

        shop.addActionListener(e ->
                game.trigger("Shop"));

        talk.addActionListener(e ->
                game.trigger("Talk"));

        fight.addActionListener(e ->
                game.trigger("Fight"));

        add(explore);
        add(shop);
        add(talk);
        add(fight);
    }
}


// STARTUP

class Main {

    public static void main(String[] args) {
        
        Game game = new Game();
        UI ui = new UI(game);

        game.setUI(ui);

        ui.showUI();
    }
}