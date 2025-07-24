package com.test.stampmap.Stamp;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.osmdroid.util.GeoPoint;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StampSerialiser {
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(GeoPoint.class, new GeoPointSerialiser()).create();
    public static String serialiseStamps(Map<Integer, StampSet> stamps) {
        return gson.toJson(stamps.values());
    }
    public static Map<Integer, StampSet> deserialiseStamps(String jsonString) {
        List<StampSet> stamps = gson.fromJson(jsonString, new TypeToken<List<StampSet>>(){}.getType());
        return stamps.stream().collect(Collectors.toMap(StampSet::hashCode, Function.identity()));
    }

    private static class GeoPointSerialiser implements JsonSerializer<GeoPoint>, JsonDeserializer<GeoPoint> {
        @Override
        public JsonElement serialize(GeoPoint geoPoint, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonArray arr = new JsonArray(2);
            arr.add(geoPoint.getLatitude());
            arr.add(geoPoint.getLongitude());
            return arr;
        }
        @Override
        public GeoPoint deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonArray arr = jsonElement.getAsJsonArray();
            return new GeoPoint(arr.get(0).getAsDouble(), arr.get(1).getAsDouble());
        }
    }
    private StampSerialiser() {}
}
