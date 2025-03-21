package com.myershome.homeapp.webapp.renderers;

import com.myershome.homeapp.services.Constants;
import com.vaadin.flow.component.ClickNotifier;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Pre;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.security.core.parameters.P;

public class IngItemCard extends ReactCard implements HasComponents, ClickNotifier<IngItemCard> {
    String mainTitle;
    String secondaryTitle;
    String content;

    public IngItemCard(String mainTitle, String secondaryTitle, String content){
        super(Constants.CARDTYPES.CONTENT, mainTitle, secondaryTitle);
        this.mainTitle = mainTitle;
        this.secondaryTitle = secondaryTitle;
        this.content = content;
        setSubtitle(content);
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Ingredient Items");
        this.add(dialog);
        dialog.add(createDialogLayout());
        dialog.setWidth("1000px");

        this.addClickListener(event -> {
            dialog.open();
        });
    }

    private VerticalLayout createDialogLayout() {
        H4 title = new H4(secondaryTitle);
        Pre ingredients = new Pre(content);
        ingredients.setWidthFull();
        return new VerticalLayout(title, ingredients);
    }
}
