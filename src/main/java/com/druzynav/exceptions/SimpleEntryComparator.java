package com.druzynav.exceptions;

import com.druzynav.models.user.User;

import java.util.AbstractMap;
import java.util.Comparator;

public class SimpleEntryComparator implements Comparator<AbstractMap.SimpleEntry<User, Double>> {
    public int compare(AbstractMap.SimpleEntry<User, Double> entry1, AbstractMap.SimpleEntry<User, Double> entry2) {
        return entry2.getValue().compareTo(entry1.getValue());
    }
}

