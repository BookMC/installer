package org.bookmc.installer.backend;

import org.bookmc.installer.backend.metadata.BookComponentSupplier;
import org.bookmc.installer.backend.metadata.MinecraftVersionSupplier;
import org.bookmc.installer.backend.metadata.data.VersionData;
import org.bookmc.installer.utils.constants.Constants;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BookMetadataLoader {
    public static CompletableFuture<List<VersionData>> getBookLoaderVersions() {
        return CompletableFuture.supplyAsync(new BookComponentSupplier(Constants.LOADER_COMPONENT));
    }

    public static CompletableFuture<List<VersionData>> getHookVersions() {
        return CompletableFuture.supplyAsync(new BookComponentSupplier(Constants.MINECRAFT_COMPONENT));
    }

    public static CompletableFuture<List<String>> getMinecraftVersions(String hookVersion) {
        return CompletableFuture.supplyAsync(new MinecraftVersionSupplier(hookVersion));
    }
}
