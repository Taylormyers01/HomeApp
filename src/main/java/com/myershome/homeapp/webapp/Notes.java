package com.myershome.homeapp.webapp;

import com.myershome.homeapp.services.MealService;
import com.myershome.homeapp.webapp.renderers.ReactCard;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "Notes", layout = MainLayout.class) // map view to the root
public class Notes extends VerticalLayout {

    MealService service;

    // public Notes() {
    // setSizeFull();
    // WysiwygE wysiwygE = new WysiwygE("150px", "150px");
    // String string = " ";
    // wysiwygE.addValueChangeListener(i -> System.out.println(i.getValue()));
    // CardRenderer cd = new CardRenderer("Notes3");
    // cd.addContent(wysiwygE);
    // cd.setMaxHeight("300px");
    // cd.setMaxWidth("300px");

    // add(cd);
    // // add(wysiwygE);
    // }

    public Notes(MealService service){
//        String s = "duh";
//        this.service = service;
//        GridRenderer gr = new GridRenderer();
//        List<Meal> meals = service.findAllByMealDayIsNull();
//        gr.setSizeFull();
        setSizeFull();
//        for(Meal m: meals){
//            CardRendererVaadin cd = new CardRendererVaadin(m.getMealName());
//            gr.add(cd);
//        }
//        add(gr);

        add(new ReactCard());
    }
}
