package org.bookmc.installer.backend.metadata;

import com.google.gson.*;
import org.bookmc.installer.utils.constants.Constants;
import org.bookmc.installer.utils.http.HttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record MinecraftVersionSupplier(String hookVersion) implements Supplier<List<String>> {
    @Override
    public List<String> get() {
        String url = Constants.VERSION_JSON;
        JsonObject json = JsonParser.parseString(HttpUtils.get(url)).getAsJsonObject();
        if (json.has(hookVersion)) {
            return toString(json.getAsJsonArray(hookVersion));
        }

        return new ArrayList<>();
    }

    private List<String> toString(JsonArray array) {
        List<String> strings = new ArrayList<>();

        for (JsonElement element : array) {
            if (element instanceof JsonPrimitive primitive) {
                strings.add(primitive.getAsString());
            }
        }

        return strings;
    }
}
