package com.peteralbus.controller;

import java.util.Comparator;
import java.util.Map;

class MyComparator implements Comparator<Map.Entry> {
public int compare(Map.Entry o1, Map.Entry o2) {
return ((Double)o2.getValue()).compareTo((Double) o1.getValue());
}
}