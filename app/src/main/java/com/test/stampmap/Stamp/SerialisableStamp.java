package com.test.stampmap.Stamp;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public interface SerialisableStamp {

    default JSONObject serialise(){
        JSONObject object = new JSONObject();
        try {
            String className = this.getClass().getName();
            object.put("class", className);
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                if (!accessible) field.setAccessible(true);
                String key = field.getName();
                Object value = field.get(this);
                if (!accessible) field.setAccessible(false);
                if (value instanceof List) {
                    List<?> list = (List<?>) value;
                    if (!list.isEmpty() && list.get(0) instanceof SerialisableStamp) {
                        JSONArray stampList = new JSONArray();
                        for (SerialisableStamp stamp : (List<SerialisableStamp>) list) stampList.put(stamp.serialise());
                        object.put(key, stampList);
                    }
                } else if (value instanceof GeoPoint) {
                    GeoPoint point = (GeoPoint) value;
                    JSONArray coords = new JSONArray();
                    coords.put(point.getLatitude());
                    coords.put(point.getLongitude());
                    object.put(key, coords);
                } else object.put(key, value);
            }
        } catch (JSONException | IllegalAccessException e){
            Log.i("ERROR", "shit's fucked");
        }
        return object;
    }

    static SerialisableStamp deserialise(JSONObject object) {

        Class<?> objectClass;
        SerialisableStamp stampObject = null;
        try {
            String className = object.getString("class");
            objectClass = Class.forName(className);
        }
        catch (JSONException | ClassNotFoundException e){
            Log.i("ERROR", "class not found, skipping");
            return null;
        }

        try {
            Constructor<?> constructor = objectClass.getDeclaredConstructors()[0];

            List<String> paramNames = Arrays.stream(constructor.getParameters()).map(Parameter::getName).collect(Collectors.toList());
            Object[] params = new Object[constructor.getParameterCount()];
            for (Iterator<String> it = object.keys(); it.hasNext(); ) {
                String currentKey = it.next();
                if (currentKey.equals("class") || !paramNames.contains(currentKey)) continue;
                if (currentKey.equals("coordinates")) {
                    JSONArray coords = object.getJSONArray(currentKey);
                    GeoPoint point = new GeoPoint(coords.getDouble(0), coords.getDouble(1));
                    params[paramNames.indexOf(currentKey)] = point;
                } else if (currentKey.equals("stamps")) {
                    List<SerialisableStamp> stampList = new ArrayList<>();
                    JSONArray stamps = object.getJSONArray(currentKey);
                    for (int i = 0; i < stamps.length(); i++) stampList.add(deserialise(stamps.getJSONObject(i)));
                    params[paramNames.indexOf(currentKey)] = stampList;
                }
                else {
                    params[paramNames.indexOf(currentKey)] = object.get(currentKey);
                }
            }
            stampObject = (SerialisableStamp) constructor.newInstance(params);
        }
        catch (JSONException | InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e){
            Log.i("ERROR", "class fucked, skipping");
        }
        return stampObject;
    }
}
