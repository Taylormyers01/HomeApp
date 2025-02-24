package com.myershome.homeapp.webapp.renderers;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;

@CssImport("./styles/card-style.css")
public class CardRenderer extends Div {
        Div content = new Div();
        H4 title = new H4();
        String titleText;
        public String maxHeight = "200px";
        public String maxWidth = "150px";

        public CardRenderer(String titleText) {

                this.titleText = titleText;
                title.setText(titleText);
//                 title.setId("id");
                title.addClassName("title");
                content.addClassName("container");
                add(content, title);
                addClassName("card");
                title.setWidthFull();
                setSizeFull();
        }

        public void addContent(Component component) {
                ((HasSize) component).setMaxWidth("150px");
                ((HasSize) component).setMaxHeight("110px");
                this.content.add(component);
        }
}
