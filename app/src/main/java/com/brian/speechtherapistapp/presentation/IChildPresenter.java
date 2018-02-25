package com.brian.speechtherapistapp.presentation;

import com.brian.speechtherapistapp.view.IChildView;

/**
 * Created by brian on 04/02/18.
 */

public interface IChildPresenter {
    void setView(IChildView childView);
    void saveChild();
    void loadChildDetails();
}