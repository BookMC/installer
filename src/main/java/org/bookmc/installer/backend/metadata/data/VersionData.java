package org.bookmc.installer.backend.metadata.data;

public class VersionData {
    private final String version;
    private final String url;

    public VersionData(String version, String url) {
        this.version = version;
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return version;
    }
}
