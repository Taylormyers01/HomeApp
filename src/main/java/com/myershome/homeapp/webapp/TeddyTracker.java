package com.myershome.homeapp.webapp;

import com.myershome.homeapp.model.TeddyEvent;
import com.myershome.homeapp.services.Constants;
import com.myershome.homeapp.services.TeddyEventService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
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
        add(createButtons());
        setSizeFull();
        configureGrid();
        add(getToolbar(), grid);

        updateList();
    }
    private HorizontalLayout createButtons(){
        HorizontalLayout layout = new HorizontalLayout();
        Button bottle = new Button();
        SvgIcon bottleIcon = new SvgIcon(new StreamResource("bottle.svg",
                () -> getClass().getResourceAsStream("/icons/bottle.svg")));
        bottleIcon.setSize("50px");
        bottle.setIcon( bottleIcon);
        bottle.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_TERTIARY);
        bottle.addClickListener(e -> {
            Dialog d = createDialog(TeddyEvent.builder().eventType(Constants.TeddyEventType.BOTTLE).eventDate(LocalDate.now()).build());
            d.open();
        });

        Button diaper = new Button();
        SvgIcon diaperIcon = new SvgIcon(new StreamResource("diaper.svg",
                () -> getClass().getResourceAsStream("/icons/diaper.svg")));
        diaperIcon.setSize("50px");
        diaper.setIcon(diaperIcon);
        diaper.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_TERTIARY);
        diaper.addThemeName(LumoUtility.FlexDirection.COLUMN_REVERSE);
        diaper.addClickListener(e -> {
            createDialog(TeddyEvent.builder().eventType(Constants.TeddyEventType.DIAPER).eventDate(LocalDate.now()).build()).open();

        });

        Button nap = new Button();
        SvgIcon napIcon = new SvgIcon(new StreamResource("sleeping-baby.svg",
                () -> getClass().getResourceAsStream("/icons/sleeping-baby.svg")));
        napIcon.setSize("50px");
        nap.setIcon(napIcon);
        nap.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_TERTIARY);
        nap.addClickListener(e -> {
            createDialog(TeddyEvent.builder().eventType(Constants.TeddyEventType.NAP).eventDate(LocalDate.now()).build()).open();
        });

        Button bath = new Button();
        SvgIcon bathIcon = new SvgIcon(new StreamResource("bath.svg",
                () -> getClass().getResourceAsStream("/icons/bath.svg")));
        bathIcon.setSize("50px");
        bathIcon.setColor("White");
        bath.setIcon(bathIcon);
        bath.addThemeVariants(ButtonVariant.LUMO_LARGE, ButtonVariant.LUMO_TERTIARY);
        bath.addClickListener(e -> {
            createDialog(TeddyEvent.builder().eventType(Constants.TeddyEventType.BATH).eventDate(LocalDate.now()).build()).open();
        });
        layout.add(bottle, diaper, nap, bath);
        return layout;
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
//        Notification.show(filterType.getValue().toUpperCase() +getDate() );
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
                    case BATH -> sr = new StreamResource("bath.svg",
                    () -> getClass().getResourceAsStream("/icons/bath.svg"));
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
        grid.addColumn(event -> {
            if (event.getTimeOfEvent() != null){
               return event.getTimeOfEvent().format(DateTimeFormatter.ofPattern("HH:mm"));
            }
            return " ";
        }).setHeader("Time");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.getColumns().forEach(col -> col.setSortable(true));
        grid.setVisible(true);

        grid.addThemeVariants(GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
        grid.setSelectionMode(SelectionMode.SINGLE);
        grid.asSingleSelect().addValueChangeListener(event ->{
            Dialog d = createDialog(event.getValue());
            if(d != null){
                d.open();
            }
        });
    }

    private Dialog createDialog(TeddyEvent event){
        if (event != null){
            Dialog dialog = new Dialog();
            dialog.getElement().setAttribute("aria-label", "Event Info");
            if(event != null){
                FormLayout fieldLayout = createDialogLayout(event);
                dialog.add(fieldLayout);
                fieldLayout.setWidthFull();
            }
            dialog.setHeaderTitle("Event Info");

            Button closeButton = new Button(new Icon("lumo", "cross"),
                    (e) -> dialog.close());
            closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

            Button deleteButton = new Button("Delete", (e) ->
            {
                System.out.println(event);
                service.delete(event);
                updateList();
                dialog.close();            
            });
            deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            deleteButton.getStyle().set("margin-left", "auto");

            Button saveButton = new Button("Save", (e) -> {
                service.save(event);
                System.out.println(event);
                updateList();
                dialog.close();
            });
            saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        

            dialog.getHeader().add(closeButton);
            dialog.getFooter().add(deleteButton, saveButton);
            return  dialog;
        }else{
            return null;
        }
    }

    private FormLayout createDialogLayout(TeddyEvent event) {
        Select<Constants.TeddyEventType> eventType = new Select<>();
        eventType.setItems(Constants.TeddyEventType.eventTypes);
        if(event.getEventType() != null){
            eventType.setValue(event.getEventType());
        }
        eventType.setEnabled(false);
        eventType.getStyle().set("padding-top", "0");
        eventType.addValueChangeListener((e) -> event.setEventType(e.getValue()));


        DatePicker eventDate = new DatePicker("Event Date");
        if(event.getEventDate() != null){
            eventDate.setValue(event.getEventDate());
        }
        eventDate.addValueChangeListener(e -> event.setEventDate(eventDate.getValue()));

        Select<String> amount = new Select<>();
        amount.setLabel("Amount");
        if(event.getEventType() != null){
            switch (event.getEventType()){
                case BOTTLE -> amount.setItems(Constants.bottleAmount);
                case DIAPER -> amount.setItems(Constants.diaperAmount);
                default -> amount.setEnabled(false);
            }
        }else{
            amount.setEnabled(false);
        }
        if (event.getAmount() != null){
            amount.setValue(event.getAmount());
        }
        amount.addValueChangeListener(e -> {
           event.setAmount(e.getValue());
        });

        TimePicker eventTime = new TimePicker();
        eventTime.setLabel("Time of " + event.getEventType().value);
        eventTime.setStep(Duration.ofMinutes(15));
        if(event.getTimeOfEvent() != null){
            eventTime.setValue(event.getTimeOfEvent());
        }else{
            eventTime.setValue(LocalTime.now());
            event.setTimeOfEvent(LocalTime.now());
        }
        eventTime.addValueChangeListener(e -> event.setTimeOfEvent(e.getValue()));


        FormLayout fieldLayout = new FormLayout(eventType, eventDate,
                eventTime,
                amount);
        fieldLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );
        fieldLayout.setColspan(eventType, 2);
        fieldLayout.setColspan(amount, 2);

        return fieldLayout;
    }
}
