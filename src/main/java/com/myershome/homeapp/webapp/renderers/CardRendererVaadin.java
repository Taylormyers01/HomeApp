package com.myershome.homeapp.webapp.renderers;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class CardRendererVaadin extends Div {


     private H4 titleText;
     private Image content;
     public CardRendererVaadin(){
         new CardRendererVaadin("empty Title");
     }

     public CardRendererVaadin(String title){
         Div container = new Div();
         content = new Image();
         titleText = new H4(title);
         setHeight("200px");
         setWidth("150px");
         setMaxWidth("150px");
         setMaxHeight("200px");
         StreamResource imageResource = new StreamResource("question_mark.jpg",
                 () -> getClass().getResourceAsStream("/images/question_mark.jpg"));

         content.setSrc(imageResource);
         content.getStyle().set("opacity", ".25");
         container.add(titleText);
         add(content, container);

         addClassNames(LumoUtility.BoxShadow.MEDIUM, LumoUtility.Background.PRIMARY_10, LumoUtility.Flex.GROW_NONE,
                 LumoUtility.Overflow.HIDDEN, LumoUtility.BorderRadius.SMALL);

         container.addClassNames(LumoUtility.Background.CONTRAST_20, LumoUtility.TextAlignment.CENTER,
                  LumoUtility.Overflow.HIDDEN);
         container.setHeight("25%");
         content.setMaxHeight("75%");

     }

     public void setImageByUrl(String imageUrl){
         content.getStyle().set("opacity", ".5");
         content.setSrc(imageUrl);
     }
     public void setImageByPath(String imagePath){
         StreamResource imageResource = new StreamResource("question_mark.jpg",
                 () -> getClass().getResourceAsStream("/images/question_mark.jpg"));
         content.getStyle().set("opacity", ".5");
         content.setSrc(imageResource);
     }
}
