package com.myershome.homeapp.list.view.renderers;

import com.myershome.homeapp.model.Meal;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class CardRenderer extends Div {
    Div div = new Div();
    Div content = new Div();
    H4 title = new H4();
    String titleText;

    public CardRenderer(String titleText){

        this.titleText = titleText;
        title.setText(titleText);

        title.addClassNames(LumoUtility.Background.CONTRAST_5,
                LumoUtility.TextColor.PRIMARY, LumoUtility.Padding.SMALL,
                LumoUtility.Border.BOTTOM,
                LumoUtility.BorderColor.CONTRAST_10);
        div.addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST_10,
                LumoUtility.BorderRadius.SMALL,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Margin.MEDIUM);
        content.addClassNames(LumoUtility.Flex.GROW,
                LumoUtility.Padding.XSMALL);
        div.add(content, title);
        div.setWidth("150px");
//        div.addClassNames(LumoUtility.Flex.SHRINK_NONE);
        add(div);
    }

    public void addContent(Component component){
        this.content.add(component);
    }
}
