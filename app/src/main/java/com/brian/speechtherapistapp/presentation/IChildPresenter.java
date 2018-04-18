package com.brian.speechtherapistapp.presentation;

import com.brian.speechtherapistapp.models.Child;
import com.brian.speechtherapistapp.view.IChildView;

import java.util.List;

/**
 * Created by brian on 04/02/18.
 */

public interface IChildPresenter {
    void setView(IChildView childView);
    void saveChild();
    void loadChildDetails();
    List<Child> getChildren();
}
