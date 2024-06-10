package com.myershome.homeapp.webapp;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "/teddy-tracker", layout = MainLayout.class)
public class TeddyTracker extends VerticalLayout {

    TeddyTracker(){
        add(new H1("Teddy Tracker V1.0"));
    }
}
