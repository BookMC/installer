package org.bookmc.installer.backend.metadata;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bookmc.installer.backend.comparator.VersionComparator;
import org.bookmc.installer.backend.metadata.data.VersionData;
import org.bookmc.installer.utils.constants.Constants;
import org.bookmc.installer.utils.http.HttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record BookComponentSupplier(String component) implements Supplier<List<VersionData>> {
    @Override
    public List<VersionData> get() {
        List<VersionData> data = new ArrayList<>();

        String json = HttpUtils.get(Constants.METADATA_API + "/" + component + "/versions");
        JsonObject object = JsonParser.parseString(json).getAsJsonObject();
        JsonArray array = object.getAsJsonArray("versions");
        for (JsonElement element : array) {
            if (element instanceof JsonObject obj) {
                data.add(new VersionData(obj.get("version").getAsString(), obj.get("url").getAsString()));
            }
        }

        data.sort(new VersionComparator());
        return data;
    }
}
