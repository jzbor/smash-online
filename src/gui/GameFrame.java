package gui;

import resources.Design;
import util.Config;
import util.Logger;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    // @TODO setDefaultCloseOperation: cleanup and then shut down cleanly
    private static final GameFrame instance = new GameFrame();
    private static final Dimension DEFAULT_SIZE = new Dimension(1024, 768);

    private GameFrame() {
        super(Config.TITLE);

        // set up frame
        createMenu();

        // init frame
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(DEFAULT_SIZE);

        Logger.log("Initialized frame");
    }

    private void createMenu() {
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        JMenu fileMenu = new JMenu("Program");
        JMenu prefMenu = new JMenu("Settings");
        JMenu gameMenu = new JMenu("Game");
        menubar.add(fileMenu);
        menubar.add(gameMenu);
        menubar.add(prefMenu);
        addFileMenuItems(fileMenu);
        addPrefMenuItems(prefMenu);
        addGameMenuItems(gameMenu);

        menubar.setBackground(Design.getPrimaryColor());
        fileMenu.setBackground(Design.getPrimaryColor());
        prefMenu.setBackground(Design.getPrimaryColor());
        gameMenu.setBackground(Design.getPrimaryColor());
        setDesign(menubar, Design.getPrimaryColor(), Design.getSecondaryColor(), Design.getDefaultFont());
    }

    private void addFileMenuItems(JMenu fileMenu) {

    }

    private void addGameMenuItems(JMenu gameMenu) {

    }

    private void addPrefMenuItems(JMenu prefMenu) {

    }

    private void setDesign(JMenuBar menuBar, Color backColor, Color foreColor, Font font) {
        menuBar.setBackground(backColor);
        menuBar.setFont(font);
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            menu.setFont(font);
            menu.setBackground(backColor);
            menu.setForeground(foreColor);
            for (int j = 0; j < menu.getItemCount(); j++) {
                JMenuItem item = menu.getItem(j);
                item.setFont(font);
                item.setBackground(backColor);
                item.setForeground(foreColor);
            }
        }
    }


    public static GameFrame getInstance() {
        return instance;
    }
}
