package org.bookmc.installer.ui.swing;

import org.bookmc.installer.ui.swing.panel.InstallerPanel;
import org.bookmc.installer.utils.constants.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

public class InstallerFrame extends JFrame {
    public InstallerFrame() throws Throwable {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        setTitle("Book Installer - " + Constants.INSTALLER_VERSION);
        setSize(500, 350);
        setIconImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(Constants.LOGO))));
        add(new InstallerPanel());
        setResizable(false);
        setLocationRelativeTo(null);
    }
}
