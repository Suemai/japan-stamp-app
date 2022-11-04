package com.test.stampmap.Interface;

import org.json.JSONObject;

public interface IFilter {
    boolean hasMatch(JSONObject stampSet, String searchTerm);
    int filterType();

    String getValue();
}
