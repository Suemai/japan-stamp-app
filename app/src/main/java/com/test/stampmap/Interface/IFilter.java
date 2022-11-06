package com.test.stampmap.Interface;

import com.test.stampmap.Stamp.StampSet;

public interface IFilter {
    boolean hasMatch(StampSet stampSet);
    int filterType();

    String getValue();
}
