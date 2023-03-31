package com.gesturegenius;

import java.util.Comparator;

public class AlphabetComparator implements Comparator<ItemClass> {

    @Override
    public int compare(ItemClass it1, ItemClass it2) {
        return it1.getEnglishName().compareTo(it2.getEnglishName());
    }
}
