package com.myershome.homeapp.webapp.renderers;


import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.react.ReactAdapterComponent;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.theme.lumo.LumoUtility;


@Tag("react-card")
@JsModule("./ReactCard.tsx")
public class ReactCard extends ReactAdapterComponent {

//    https://images.unsplash.com/photo-1615789591457-74a63395c990?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8YmVhdXRpZnVsJTIwY2F0fGVufDB8fDB8fHww
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
