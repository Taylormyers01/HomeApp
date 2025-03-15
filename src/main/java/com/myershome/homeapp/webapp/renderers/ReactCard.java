package com.myershome.homeapp.webapp.renderers;


import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.react.ReactAdapterComponent;


@Tag("react-card")
@JsModule("./ReactCard.tsx")
public class ReactCard extends ReactAdapterComponent {

    public ReactCard(){
        this("", null);
    }
    public ReactCard(String title){
        this(title, null);
    }

    public ReactCard(String title, String value){
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

//    public void addValueChangeListener(SerializableConsumer<String> onChange) {
//        addStateChangeListener("value", String.class, onChange);
//    }
}
