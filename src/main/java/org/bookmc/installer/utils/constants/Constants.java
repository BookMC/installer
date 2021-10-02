package org.bookmc.installer.utils.constants;

public class Constants {
    public static final String INSTALLER_VERSION = "%INSTALLER_VERSION%";
    public static final String METADATA_API = "https://metadata.bookmc.org/v1";
    public static final String MAVEN_REPO = "https://maven.bookmc.org/";
    public static final String MAVEN_REPO_RELEASES = MAVEN_REPO + "releases/";
    public static final String MAVEN_REPO_STATIC = MAVEN_REPO + "static/";

    public static final String PACKAGE = "org.bookmc";
    public static final String LOADER_COMPONENT = "book-loader";
    public static final String MINECRAFT_COMPONENT = "minecraft";

    public static final String RELEASE_CHANNEL = "release";

    public static final String MAIN_CLASS = "org.bookmc.loader.impl.launch.QuiltClient";

    public static final String LOGO = "/logo.png";
    public static final String LOGO_64 = "/logo_64.png";

    public static final String VERSION_JSON = MAVEN_REPO_STATIC + "/net/minecraft/version.json";
}
