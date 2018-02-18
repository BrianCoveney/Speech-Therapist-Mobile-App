package com.brian.speechtherapistapp.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 2/17/18.
 */

public class ChildList {
    private List<Child> childList;

    public ChildList() {
        this.childList = new ArrayList<>();
    }

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }

    public void add(Child child) {
        this.childList.add(child);
    }

}
