package com.myershome.homeapp.webapp.renderers;


import com.myershome.homeapp.services.Constants;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.react.ReactAdapterComponent;


@Tag("react-card")
@JsModule("./ReactCard.tsx")
public class ReactCard extends ReactAdapterComponent {

    public ReactCard(){
        this(Constants.CARDTYPES.ICON, "", null);
    }

    public ReactCard(Constants.CARDTYPES cardtype){
        this(cardtype,"", null);
    }

    public ReactCard(String title){
        this(Constants.CARDTYPES.ICON, title, null);
    }

    public ReactCard(Constants.CARDTYPES cardType, String title, String value){
        this.setType(cardType.value);
        this.setTitle(title);
        this.setValue(value);
    }

//    public String getValue() {
//        return getState("value", String.class);
//    }


    public void setTitle(String value) {
        setState("title", value);
    }

    public void setValue(String value){
        setState("value", value);
    }

    public void setType(String value){
        setState("type", value);
    }

    public void setSubtitle(String value){
        setState("subtitle", value);
    }

//    public void addValueChangeListener(SerializableConsumer<String> onChange) {
//        addStateChangeListener("value", String.class, onChange);
//    }
}
