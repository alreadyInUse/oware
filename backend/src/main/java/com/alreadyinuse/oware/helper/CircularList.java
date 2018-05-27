package com.alreadyinuse.oware.helper;

import java.util.ArrayList;

public class CircularList<T> extends ArrayList<T> {

    @Override
    public T get(int index) {
        return super.get(index > 0 ? index % size() : (size() + index) % size());
    }
}
