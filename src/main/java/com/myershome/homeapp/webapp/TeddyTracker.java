package com.myershome.homeapp.webapp;

import com.myershome.homeapp.model.TeddyEvent;
import com.myershome.homeapp.services.Constants;
import com.myershome.homeapp.services.TeddyEventService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Route(value = "/teddy-tracker", layout = MainLayout.class)
public class TeddyTracker extends Div {
    Grid<TeddyEvent> grid = new Grid<>(TeddyEvent.class);
    Select<String> filterType;
    Select<String> filterTime;
    TeddyEventService service;
    TeddyTracker(TeddyEventService teddyEventService){
        this.service = teddyEventService;
        add(new H1("Teddy Tracker V1.0"));
        setSizeFull();
        configureGrid();
        add(getToolbar(), grid);

        updateList();
    }

    private Component getToolbar() {
        filterType = new Select<>();
        filterType.setItems("Bottle", "Nap", "Diaper", "All");
        filterType.setValue("All");
        filterType.addValueChangeListener(click -> updateList());

        filterTime = new Select<>();
        filterTime.setItems("Today","This Week", "All");
        filterTime.setValue("All");
        filterTime.addValueChangeListener(click -> updateList());
        var toolbar = new HorizontalLayout(filterType, filterTime);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        Notification.show(filterType.getValue().toUpperCase() +getDate() );
        if (Objects.equals(filterType.getValue(), "All")){
            grid.setItems(service.findAllByEventDateAfter(getDate()));
        }else{
            grid.setItems(service.findAllByEventType(Constants.TeddyEventType.valueOf(filterType.getValue().toUpperCase()), getDate()));
        }
    }

    private LocalDate getDate(){
        return switch (filterTime.getValue()) {
            case "Today" -> LocalDate.now().minusDays(1);
            case "This Week" -> LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() + 1);
            default -> LocalDate.EPOCH;
        };
    }
    private void configureGrid() {
        grid.addClassNames("teddy-event-grid");
        grid.setSizeFull();
        grid.setColumns("eventType");
        grid.getColumnByKey("eventType").setRenderer(new ComponentRenderer<>(event -> {
            StreamResource sr = null;
            if (event.getEventType() != null) {
                switch (event.getEventType()) {
                    case BOTTLE -> sr = new StreamResource("bottle.svg",
                            () -> getClass().getResourceAsStream("/icons/bottle.svg"));
                    case DIAPER -> sr = new StreamResource("diaper.svg",
                            () -> getClass().getResourceAsStream("/icons/diaper.svg"));
                    case NAP -> sr = new StreamResource("sleeping-baby.svg",
                            () -> getClass().getResourceAsStream("/icons/sleeping-baby.svg"));
                }
            }else{
                sr = new StreamResource("baby.svg",
                        () -> getClass().getResourceAsStream("/icons/baby.svg"));
            }

            SvgIcon icon = new SvgIcon(sr);
            return icon;
        }));
        grid.addColumn(TeddyEvent::getAmount).setHeader("Amount");
        grid.addColumn(TeddyEvent::getEventDate).setHeader("Date");
        grid.addColumn(event -> event.getTimeOfEvent().format(DateTimeFormatter.ofPattern("HH:mm"))).setHeader("Time");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.getColumns().forEach(col -> col.setSortable(true));
        grid.setVisible(true);

    }

//    private Dialog createDialog(TeddyEvent event){
//        Dialog dialog = new Dialog();
//        dialog.getElement().setAttribute("aria-label", "Event Info");
//        VerticalLayout fieldLayout = createDialogLayout(event);
//        dialog.add(fieldLayout);
//        dialog.setHeaderTitle("Event Info");
//
//        Button closeButton = new Button(new Icon("lumo", "cross"),
//                (e) -> dialog.close());
//        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//
//        Button deleteButton = new Button("Delete", (e) ->
//        {
//            service.delete(event);
//            updateList();
//            dialog.close();
//        });
//        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
//        deleteButton.getStyle().set("margin-left", "auto");
//
//        Button saveButton = new Button("Save", (e) -> {
//            service.save(event);
//            fieldLayout.getChildren().forEach(field -> {
//                if(field instanceof TextField){
//                    ((TextField) field).setReadOnly(true);
//                }else if(field instanceof ComboBox<?>){
//                    ((ComboBox<?>) field).setReadOnly(true);
//                }
//            });
//            System.out.println(event);
//            updateList();
//            dialog.close();
//        });
//        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
//
//        Button editButton = new Button("Edit", (e) -> {
//            fieldLayout.getChildren().forEach(field -> {
//                if(field instanceof TextFieldBase<?,?>){
//                    ((TextFieldBase<?, ?>) field).setReadOnly(false);
//                }else if(field instanceof ComboBox<?>){
//                    ((ComboBox<?>) field).setReadOnly(false);
//                }
//            });
//            saveButton.setEnabled(true);
//        });
//        editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//
//        dialog.getHeader().add(closeButton);
//        dialog.getFooter().add(deleteButton, editButton, saveButton);
//        dialog.setWidth("500px");
//        fieldLayout.setWidthFull();
//        if(event.getEventType() != null){
//            saveButton.setEnabled(false);
//        }else{
//            dialog.open();
//        }
//        return  dialog;
//    }
//
//    private VerticalLayout createDialogLayout(TeddyEvent event) {
//        Select<Constants.TeddyEventType> eventType = new Select<>();
//        eventType.setItems(Constants.TeddyEventType.eventTypes);
//        if(event.getEventType() != null){
//            eventType.setValue(event.getEventType());
//        }
//        eventType.getStyle().set("padding-top", "0");
//        eventType.addValueChangeListener((e) -> event.setEventType(e.getValue()));
//
//
//        DatePicker eventDate = new DatePicker("Event Date");
//        if(event.getEventDate() != null){
//            eventDate.setValue(event.getEventDate());
//        }
//        eventDate.addValueChangeListener(e -> event.setEventDate(eventDate.getValue()));
//
//        TextField amount = new TextField("Amount");
//        if(meal.getPictureUrl() != null){
//            imageUrl.setValue(meal.getPictureUrl().toString());
//        }
//        imageUrl.addValueChangeListener(e -> {
//            try {
//                meal.setPictureUrl(new URL(imageUrl.getValue()));
//            } catch (MalformedURLException ex) {
//                throw new RuntimeException(ex);
//            }
//        });
//
//        TextArea mealInstruction = new TextArea("Instructions");
//        if(meal.getDirections() != null){
//            mealInstruction.setValue(meal.getDirections());
//        }
//        mealInstruction.setMinHeight("150px");
//        mealInstruction.setMaxHeight("250px");
//        mealInstruction.addValueChangeListener(e -> meal.setDirections(e.getValue()));
//
//
//        ComboBox<Constants.Days> day = new ComboBox<>();
//        day.setItems(Constants.Days.dayArray);
//        day.setValue(meal.getMealDay());
//        day.addValueChangeListener(e -> meal.setMealDay(e.getValue()));
//
//        VerticalLayout fieldLayout = new VerticalLayout(nameField, recipeUrl,
//                imageUrl,
//                mealInstruction, day);
//        fieldLayout.setSpacing(false);
//        fieldLayout.setPadding(false);
//        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
//        fieldLayout.getStyle().set("width", "300px").set("max-width", "100%");
//
//        if(!Objects.equals(meal.getMealName(), "")){
//            mealInstruction.setReadOnly(readOnly);
//            recipeUrl.setReadOnly(readOnly);
//            nameField.setReadOnly(readOnly);
//            day.setReadOnly(readOnly);
//            imageUrl.setReadOnly(readOnly);
//        }
//        return fieldLayout;
//    }
}
