package com.myershome.homeapp.list.view;

import com.myershome.homeapp.list.view.renderers.MealRenderer;
import com.myershome.homeapp.model.Meal;
import com.myershome.homeapp.services.MealService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.router.Route;

import java.net.URL;

@Route(value = "/menu", layout = MainLayout.class)
public class MenuView extends Div {

    MealService service;

    MenuView(MealService service){
        this.service = service;
        H1 hold = new H1("left");
        MealRenderer mealRenderer = new MealRenderer(new Meal(1L, "test", null,
                null, null, null));

        SplitLayout splitLayout = new SplitLayout(hold, mealRenderer);
        splitLayout.setHeight("100%");
        splitLayout.setSplitterPosition(50.0);
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);
        add(splitLayout);

    }

}
