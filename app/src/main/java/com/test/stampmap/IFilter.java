package com.test.stampmap;

import org.json.JSONObject;

public interface IFilter {
    boolean hasMatch(JSONObject stampSet, String searchTerm);
    int filterType();
}
