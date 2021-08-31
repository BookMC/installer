package org.bookmc.installer;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bookmc.installer.backend.BookMetadataLoader;
import org.bookmc.installer.backend.InstallerOperations;
import org.bookmc.installer.impl.mojang.utils.MojangDirectoryUtils;
import org.bookmc.installer.ui.swing.InstallerFrame;
import org.bookmc.installer.utils.constants.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws Throwable {
        LOGGER.info("Starting Book Installer!");
        if (GraphicsEnvironment.isHeadless() || args.length > 0) {
            LOGGER.info("Detected request for CLI, continuing with CLI...");
            doCli(args);
        } else {
            try {
                InstallerFrame frame = new InstallerFrame();
                frame.setVisible(true);
            } catch (Throwable t) {
                JOptionPane.showMessageDialog(null, t.getMessage(), "An error has occured!", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private static void doCli(String[] args) throws ExecutionException, InterruptedException {
        OptionParser parser = new OptionParser();
        ArgumentAcceptingOptionSpec<File> minecraftDirectory = parser
            .acceptsAll(Arrays.asList("minecraftDir", "minecraftDirectory", "directory", "dir"), "The directory to your VANILLA minecraft install.")
            .withRequiredArg()
            .ofType(File.class)
            .defaultsTo(MojangDirectoryUtils.providePlatformDirectory());

        ArgumentAcceptingOptionSpec<String> gameVersion = parser.accepts("gameVersion", "The version of the game you would like to install Book for (1.17 etc.)")
            .withRequiredArg();

        ArgumentAcceptingOptionSpec<String> loaderVersion = parser.accepts("loaderVersion", "The version of book-loader (https://github.com/BookMC/book-loader)")
            .withOptionalArg()
            .defaultsTo(getDefaultLoader());

        ArgumentAcceptingOptionSpec<String> hookVersion = parser.accepts("hookVersion", "The version of the Book minecraft-hooks (https://github.com/BookMC/minecraft)")
            .withOptionalArg()
            .defaultsTo(getDefaultHook());

        ArgumentAcceptingOptionSpec<Boolean> server = parser.accepts("server", "Whether you would like to install book for a server or not")
            .withOptionalArg()
            .ofType(Boolean.class)
            .defaultsTo(false);

        OptionSet set = parser.parse(args);

        String gameVersionValue = set.valueOf(gameVersion);
        String hookVersionValue = set.valueOf(hookVersion);
        String loaderVersionValue = set.valueOf(loaderVersion);
        boolean serverValue = set.valueOf(server);
        File minecraftDirectoryValue = set.valueOf(minecraftDirectory);

        LOGGER.info("Starting installation process with current data: Game Version: {}, Hook Version: {}, Loader Verison: {}, Server: {}, Minecraft Directory: {}", gameVersionValue, hookVersionValue, loaderVersionValue, serverValue, minecraftDirectoryValue.getAbsolutePath());

        InstallerOperations.install(gameVersionValue, hookVersionValue, loaderVersionValue, Constants.RELEASE_CHANNEL, !serverValue, minecraftDirectoryValue).whenComplete((unused, throwable) -> LOGGER.info("Successfully installed " + InstallerOperations.createVersionId(hookVersionValue, gameVersionValue, loaderVersionValue)));
    }

    private static String getDefaultHook() throws ExecutionException, InterruptedException {
        return BookMetadataLoader.getHookVersions().get().get(0).getVersion();
    }

    private static String getDefaultLoader() throws ExecutionException, InterruptedException {
        return BookMetadataLoader.getBookLoaderVersions().get().get(0).getVersion();
    }
}
