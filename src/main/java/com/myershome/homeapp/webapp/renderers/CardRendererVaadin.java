package com.myershome.homeapp.webapp.renderers;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class CardRendererVaadin extends Div {


     private H4 titleText;
     public Image content;
     public CardRendererVaadin(){
         new CardRendererVaadin("empty Title");
     }

     public CardRendererVaadin(String title){
         Div container = new Div();
         this.content = new Image();
         titleText = new H4(title);

         StreamResource imageResource = new StreamResource("question_mark.jpg",
                 () -> getClass().getResourceAsStream("/images/question_mark.jpg"));
         content.setSrc(imageResource);
         content.getStyle().set("opacity", ".05");

         container.add(titleText);
         add(content, container);

         addClassNames(
                 LumoUtility.BoxShadow.XSMALL,
                 LumoUtility.Background.TRANSPARENT,
                 LumoUtility.BorderRadius.MEDIUM,
                 LumoUtility.JustifyContent.CENTER,
                 LumoUtility.Padding.NONE,
                 LumoUtility.Margin.NONE,
                 LumoUtility.BorderColor.CONTRAST_30,
                 LumoUtility.Border.ALL,
                 LumoUtility.Overflow.HIDDEN,
                 LumoUtility.Flex.GROW_NONE,
                 LumoUtility.Flex.SHRINK
         );

         container.addClassNames(
                 LumoUtility.Background.CONTRAST_30,
                 LumoUtility.TextAlignment.CENTER,
                 LumoUtility.Overflow.HIDDEN,
                 LumoUtility.Margin.NONE
         );
         titleText.addClassNames(LumoUtility.TextOverflow.CLIP);
         container.setWidthFull();
         container.setMaxHeight("25px");
         content.setMaxHeight("175%");
         content.setWidthFull();
         setMaxWidth("150px");
         setMaxHeight("200px");


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
