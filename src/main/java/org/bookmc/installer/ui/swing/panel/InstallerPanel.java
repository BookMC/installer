package org.bookmc.installer.ui.swing.panel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bookmc.installer.backend.BookMetadataLoader;
import org.bookmc.installer.backend.InstallerOperations;
import org.bookmc.installer.backend.metadata.data.VersionData;
import org.bookmc.installer.impl.mojang.utils.MojangDirectoryUtils;
import org.bookmc.installer.utils.constants.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class InstallerPanel extends JPanel {
    private final Logger LOGGER = LogManager.getLogger(this);

    private JComboBox<String> gameVersionComboBox;
    private JComboBox<VersionData> hookVersionComboBox;
    private JComboBox<VersionData> loaderVersionComboBox;
    private JButton findButton;
    private JTextField installLocationTextField;
    private JButton installButton;

    public InstallerPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        newRow(panel -> panel.add(createLogo(getLogo64())));

        newRow(panel -> {
            JLabel title = new JLabel("Loader Version");
            loaderVersionComboBox = new JComboBox<>();
            BookMetadataLoader.getBookLoaderVersions().whenComplete((data, throwable) -> {
                for (VersionData version : data) {
                    loaderVersionComboBox.addItem(version);
                }
            });

            panel.add(title);
            panel.add(loaderVersionComboBox);
        });

        newRow(panel -> {
            hookVersionComboBox = new JComboBox<>();
            BookMetadataLoader.getHookVersions().whenComplete((data, throwable) -> {
                for (VersionData version : data) {
                    hookVersionComboBox.addItem(version);
                }
                refreshGameComboBox();
            });

            hookVersionComboBox.addActionListener(e -> refreshGameComboBox());

            panel.add(new JLabel("Hook Version"));
            panel.add(hookVersionComboBox);
        });

        newRow(panel -> {
            JLabel title = new JLabel("Game Version");
            gameVersionComboBox = new JComboBox<>();
            panel.add(title);
            panel.add(gameVersionComboBox);
        });

        newRow(panel -> {
            installLocationTextField = new JTextField(MojangDirectoryUtils.providePlatformDirectory().getAbsolutePath());
            findButton = new JButton("Find");

            findButton.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = chooser.showOpenDialog(panel);

                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    installLocationTextField.setText(file.getAbsolutePath());
                }
            });

            panel.add(new JLabel("Install Location"));
            panel.add(installLocationTextField);
            panel.add(findButton);
        });

        newRow(panel -> {
            installButton = new JButton("Install");

            installButton.addActionListener(e -> {
                gameVersionComboBox.setEnabled(false);
                hookVersionComboBox.setEnabled(false);
                loaderVersionComboBox.setEnabled(false);
                installLocationTextField.setEnabled(false);
                findButton.setEnabled(false);
                installButton.setEnabled(false);

                VersionData loaderVersion = (VersionData) loaderVersionComboBox.getSelectedItem();
                VersionData hookVersion = (VersionData) hookVersionComboBox.getSelectedItem();
                String gameVersion = (String) gameVersionComboBox.getSelectedItem();

                if (loaderVersion == null || hookVersion == null || gameVersion == null) {
                    gameVersionComboBox.setEnabled(true);
                    hookVersionComboBox.setEnabled(true);
                    loaderVersionComboBox.setEnabled(true);
                    installLocationTextField.setEnabled(true);
                    findButton.setEnabled(true);
                    installButton.setEnabled(true);

                    String message = "Could not find loaderVersion/hookVersion (loaderVersion/hookVersion == null) Event: Install";
                    JOptionPane.showMessageDialog(this, message, "Failure!", JOptionPane.ERROR_MESSAGE);
                    throw new IllegalStateException(message);
                }

                File dir = new File(installLocationTextField.getText());

                if (!dir.exists()) {
                    gameVersionComboBox.setEnabled(true);
                    hookVersionComboBox.setEnabled(true);
                    loaderVersionComboBox.setEnabled(true);
                    installLocationTextField.setEnabled(true);
                    findButton.setEnabled(true);
                    installButton.setEnabled(true);

                    String message = "You must install Minecraft before you can continue!";
                    JOptionPane.showMessageDialog(this, message, "Failure!", JOptionPane.ERROR_MESSAGE);
                    throw new IllegalStateException(message);
                }

                InstallerOperations.install(gameVersion, hookVersion.getVersion(), loaderVersion.getVersion(), Constants.RELEASE_CHANNEL, true, dir).whenComplete((unused, throwable) -> {
                    gameVersionComboBox.setEnabled(true);
                    hookVersionComboBox.setEnabled(true);
                    loaderVersionComboBox.setEnabled(true);
                    installLocationTextField.setEnabled(true);
                    findButton.setEnabled(true);
                    installButton.setEnabled(true);
                    JOptionPane.showMessageDialog(this, "Successfully installed Book! Make sure to restart the launcher. Have a good day :)", "Success!", JOptionPane.INFORMATION_MESSAGE);
                });
            });

            panel.add(installButton);
        });
    }

    private InputStream getLogo64() {
        return getClass().getResourceAsStream(Constants.LOGO_64);
    }

    private JLabel createLogo(InputStream stream) {
        if (stream != null) {
            try {
                return new JLabel(new ImageIcon(ImageIO.read(stream)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        throw new IllegalStateException("Failed to find icon logo_64.png");
    }

    public void newRow(Consumer<JPanel> consumer) {
        JPanel panel = new JPanel();
        consumer.accept(panel);

        space(panel);
        add(panel);
    }

    private void space(JPanel panel) {
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
    }

    public void refreshGameComboBox() {
        VersionData versionData = (VersionData) hookVersionComboBox.getSelectedItem();
        if (versionData != null) {
            if (gameVersionComboBox.getItemCount() > 0) {
                gameVersionComboBox.removeAllItems();
            }

            BookMetadataLoader.getMinecraftVersions(versionData.toString()).whenComplete((data, throwable) -> {
                if (data.size() <= 0) {
                    gameVersionComboBox.setEnabled(false);
                    installButton.setEnabled(false);
                } else {
                    gameVersionComboBox.setEnabled(true);
                    installButton.setEnabled(true);
                    for (String version : data) {
                        gameVersionComboBox.addItem(version);
                    }
                }
            });
        }
    }
}
