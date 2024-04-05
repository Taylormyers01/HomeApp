package com.myershome.homeapp.list.view.renderers;

import com.myershome.homeapp.model.Meal;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MealRenderer extends Div implements DragSource<MealRenderer>, HasStyle {

//    public MealRenderer(Meal meal){
//        Div container = new Div();
//        container.addClassName("container");
//
//        Div card = new Div();
//        H4 h4 = new H4(meal.getMealName());
//        h4.setClassName(LumoUtility.FontSize.XXLARGE);
//
//        Paragraph paragraph = new Paragraph(" ");
//        if(meal.getRecipeUrl() != null){
//            paragraph.setText(meal.getRecipeUrl().toString());
//        }
//        Avatar avatar = new Avatar(meal.getMealName());
//        avatar.addClassName(LumoUtility.IconSize.LARGE);
//        if(meal.getPictureUrl() != null){
//            avatar.setImage(meal.getPictureUrl().toString());
//        }
//        container.add(h4, paragraph);
//
//        card.add(avatar, container);
//        card.addClassNames("card",
//                LumoUtility.FontSize.XXLARGE,
//                LumoUtility.Margin.MEDIUM,
//                LumoUtility.Padding.MEDIUM);
//
//        setDraggable(true);
//        add(card);
//    }
    Div div = new Div();
    Div content = new Div();
    H4 title = new H4();
    String titleText;

    public MealRenderer(Meal meal) {
        this.titleText = meal.getMealName();
        Image image = new Image("https://thecozycook.com/wp-content/uploads/2022/04/Lasagna-Recipe.jpg","Lasagna");
        title.setText(titleText);
        image.addClassNames(LumoUtility.Height.XLARGE,
                LumoUtility.Width.FULL);
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
        content.add(image);
        div.add(content, title);
        div.setWidth("40%");
        setDraggable(true);
        add(div);
    }

    public void addContent(Component component){
        this.content.add(component);
    }

}


