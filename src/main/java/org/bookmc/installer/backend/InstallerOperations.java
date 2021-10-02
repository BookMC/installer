package org.bookmc.installer.backend;

import org.bookmc.installer.api.install.java.JavaVersion;
import org.bookmc.installer.impl.install.DefaultBookInstall;
import org.bookmc.installer.impl.mojang.MojangInstallationPlatform;
import org.bookmc.installer.utils.constants.Constants;
import org.bookmc.installer.utils.versionjson.VersionJson;
import org.bookmc.installer.utils.versionjson.library.Library;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

public class InstallerOperations {
    public static CompletableFuture<Void> install(String gameVersion, String hookVersion, String loaderVersion, String channel, boolean client, File minecraftDirectory) {
        return CompletableFuture.runAsync(() -> {
            if (!client) {
                throw new UnsupportedOperationException("You cannot use this method for installation of the client");
            }

            Library[] libraries = new Library[]{
                new Library(String.format("%s:%s:%s:mc%s", Constants.PACKAGE, Constants.MINECRAFT_COMPONENT, hookVersion, gameVersion), Constants.MAVEN_REPO_RELEASES),
                new Library(String.format("%s:%s:%s", Constants.PACKAGE, Constants.LOADER_COMPONENT, loaderVersion), Constants.MAVEN_REPO_RELEASES)
            };

            byte[] versionJson = VersionJson.createToBytes(createVersionId(gameVersion, loaderVersion), gameVersion, channel, Constants.MAIN_CLASS, JavaVersion.JAVA_16, libraries);

            String icon = "data:image/png;base64," + Base64.getEncoder().encodeToString(readLogo());

            try {
                new DefaultBookInstall().install(new MojangInstallationPlatform(minecraftDirectory), versionJson, icon);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public static String createVersionId(String gameVersion, String loaderVersion) {
        return "book-loader-" + loaderVersion + "-" + gameVersion;
    }

    private static byte[] readLogo() {
        try (InputStream stream = InstallerOperations.class.getResourceAsStream(Constants.LOGO)) {
            if (stream != null) {
                return stream.readAllBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("Failed to read the logo!");
    }
}
