package org.bookmc.installer.backend.comparator;

import com.github.zafarkhaja.semver.Version;
import org.bookmc.installer.backend.metadata.data.VersionData;

import java.util.Comparator;

public class VersionComparator implements Comparator<VersionData> {
    @Override
    public int compare(VersionData o1, VersionData o2) {
        return Version.valueOf(o2.getVersion()).compareTo(Version.valueOf(o1.getVersion()));
    }
}
