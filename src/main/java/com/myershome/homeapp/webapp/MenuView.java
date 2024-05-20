package com.myershome.homeapp.webapp;

import com.myershome.homeapp.webapp.renderers.CardRenderer;
import com.myershome.homeapp.model.Meal;
import com.myershome.homeapp.services.Constants;
import com.myershome.homeapp.services.MealService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayoutVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;



@Route(value = "/menu", layout = MainLayout.class)
public class MenuView extends Div {
    private static final Logger LOG = LoggerFactory.getLogger(MenuView.class);


    List<Filler> fillers = new ArrayList<>();
    MealService service;
    Div primary;
    SplitLayout splitLayout;
    TextField filterText = new TextField();

    MenuView(MealService service){
        this.service = service;
        HorizontalLayout header;

        header = createHeader();
        add(header);
        header.setWidthFull();
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);


        update();

    }

    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        Button clearMeals = new Button("Clear meals", (e) ->{
            List<Meal> meals = service.findAllByMealDayNotNull();
            LOG.info(meals.toString());
            meals.forEach(meal -> meal.setMealDay(null));
            service.saveAll(meals);
            update();});
//        clearMeals.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        clearMeals.getStyle().set("margin-right", "auto").set("margin-left", "2%");


        Button addMeal = new Button(new Icon("lumo", "plus"),
                e -> {
                    Meal meal = new Meal();
                    meal.setMealName("");
                    MealRenderer mealRenderer = new MealRenderer(meal);
                    header.add(mealRenderer.dialog);
                    mealRenderer.dialog.open();
                });

        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> update());
        filterText.getStyle().set("margin-right", "30%");
        header.add(clearMeals,addMeal, filterText);
        return header;
    }

    public Div configurePrimary(){
        fillers.clear();
        Div primary = new Div();

        for(Constants.Days day: Constants.Days.dayArray){
            H5 dayHeader = new H5(day.value + ":");
            dayHeader.addClassName(LumoUtility.Margin.LARGE);
            primary.add(dayHeader);
            Optional<Meal> meal = service.findByMealDay(day);
            if(meal.isPresent()){
                primary.add(new MealRenderer(meal.get()));
            }else{
                Filler filler = new Filler(" ", day);
                Image image = new Image();
                image.addClassNames(LumoUtility.Height.XLARGE,
                        LumoUtility.Width.FULL);
                filler.addContent(image);
                fillers.add(filler);
                primary.add(filler);
            }
        }
        primary.addClassNames(LumoUtility.Padding.SMALL);
        return primary;
    }

    public FormLayout configureSecondary(){
        FormLayout secondary = new FormLayout();
        List<Meal> meals = service.findAllByMealDayIsNull();
        if(!filterText.getValue().isEmpty()){
            meals = meals.stream().filter(meal -> meal.getMealName().contains(filterText.getValue())).toList();
        }
        for(Meal meal: meals){
            secondary.add(new MealRenderer(meal), 1);
        }
        secondary.setHeightFull();
        return secondary;
    }

    public void update(){
        if(splitLayout!=null){
            remove(splitLayout);
        }
        var secondary = configureSecondary();
        splitLayout = new SplitLayout(configurePrimary(),secondary);
        secondary.addClassName(LumoUtility.Flex.AUTO);
        splitLayout.addThemeVariants(SplitLayoutVariant.LUMO_MINIMAL);
        splitLayout.setSplitterPosition(40);
        add(splitLayout);
    }



class MealRenderer extends CardRenderer implements DragSource<MealRenderer>, HasStyle {
    Meal meal;
    String imageUrl = "https://www.shutterstock.com/shutterstock/photos/1083445310/display_1500/stock-vector-food-with-spoon-and-fork-symbol-logo-design-1083445310.jpg";

    Dialog dialog;
    Boolean readOnly = true;

    public MealRenderer(Meal meal) {
        super(meal.getMealName());
        this.meal = meal;
        Image image = new Image();
        if(meal.getPictureUrl() != null){
            image.setSrc(meal.getPictureUrl().toString());
            image.setAlt(meal.getMealName());
        }else{
            image.setSrc(imageUrl);
            image.setAlt(meal.getMealName());
        }
        image.addClassNames(LumoUtility.Height.XLARGE,
                LumoUtility.Width.FULL);
        this.addContent(image);
        DragSource<MealRenderer> dragSource = DragSource.create(this);
        dragSource.setDragData(this);
        setDraggable(true);

        dragSource.addDragStartListener(e ->{
            LOG.info("started drag event");
            e.getComponent().meal.setMealDay(null);
            fillers.stream().filter(filler -> filler.titleText.equals(" "))
                    .forEach(filler -> {
                        filler.dropTarget.setActive(true);
                        filler.setVisible(true);
                        filler.addClassName("contest-drag-over-target");
                    });

        });
        dragSource.addDragEndListener(e -> {
            fillers.stream()
                    .filter(filler -> filler.titleText.equals(" "))
                    .forEach(filler -> {
                        filler.dropTarget.setActive(false);
                        filler.setVisible(false);
                        filler.removeClassName("contest-drag-over-target");
                    });
            if(!e.isSuccessful()){
                meal.setMealDay(null);
                service.save(meal);
                update();
            }
        });

        dialog = configureDialog();

        addClickListener(e -> dialog.open());
        add(dialog);


    }
    public Dialog configureDialog(){
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Meal Info");
        VerticalLayout fieldLayout = createDialogLayout(dialog);
        dialog.add(fieldLayout);
        dialog.setHeaderTitle("Meal Info");

        Button closeButton = new Button(new Icon("lumo", "cross"),
                (e) -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button deleteButton = new Button("Delete", (e) ->
        {
            service.delete(meal);
            update();
            dialog.close();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().set("margin-left", "auto");

        Button saveButton = new Button("Save", (e) -> {
            service.save(meal);
            fieldLayout.getChildren().forEach(field -> {
                if(field instanceof TextField){
                    ((TextField) field).setReadOnly(true);
                }else if(field instanceof ComboBox<?>){
                    ((ComboBox<?>) field).setReadOnly(true);
                }
            });
            update();
            dialog.close();
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button editButton = new Button("Edit", (e) -> {
            fieldLayout.getChildren().forEach(field -> {
                if(field instanceof TextField){
                    ((TextField) field).setReadOnly(false);
                }else if(field instanceof ComboBox<?>){
                    ((ComboBox<?>) field).setReadOnly(false);
                }
            });
            saveButton.setEnabled(true);
        });
        editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        dialog.getHeader().add(closeButton);
        dialog.getFooter().add(deleteButton, editButton, saveButton);
        if(!meal.getMealName().isEmpty()){
            saveButton.setEnabled(false);
        }else{
            dialog.open();
        }
        return  dialog;
    }

    private VerticalLayout createDialogLayout(Dialog dialog) {
        TextField nameField = new TextField("Meal Name", meal.getMealName(),
                "Meal name");
        nameField.getStyle().set("padding-top", "0");
        nameField.addValueChangeListener((e) -> meal.setMealName(e.getValue()));


        TextField recipeUrl = new TextField("Recipe URL");
        if(meal.getRecipeUrl() != null){
            recipeUrl.setValue(meal.getRecipeUrl());
        }
        recipeUrl.addValueChangeListener(e -> meal.setRecipeUrl(recipeUrl.getValue()));

        TextField imageUrl = new TextField("Image URL");
        if(meal.getPictureUrl() != null){
            imageUrl.setValue(meal.getPictureUrl().toString());
        }
        recipeUrl.addValueChangeListener(e -> {
            try {
                meal.setPictureUrl(new URL(imageUrl.getValue()));
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            }
        });

        TextField mealInstruction = new TextField("Instructions");
        if(meal.getDirections() != null){
            mealInstruction.setValue(meal.getDirections());
        }
        mealInstruction.addValueChangeListener(e -> meal.setDirections(e.getValue()));


        ComboBox<Constants.Days> day = new ComboBox<>();
        day.setItems(Constants.Days.dayArray);
        day.setValue(meal.getMealDay());
        day.addValueChangeListener(e -> meal.setMealDay(e.getValue()));

        VerticalLayout fieldLayout = new VerticalLayout(nameField, recipeUrl,
                imageUrl,
                mealInstruction, day);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "300px").set("max-width", "100%");

        if(!Objects.equals(meal.getMealName(), "")){
            mealInstruction.setReadOnly(readOnly);
            recipeUrl.setReadOnly(readOnly);
            nameField.setReadOnly(readOnly);
            day.setReadOnly(readOnly);
            imageUrl.setReadOnly(readOnly);
        }

        return fieldLayout;
    }

}
class Filler extends CardRenderer implements HasStyle{
    private DropTarget<Filler> dropTarget;
    private String titleText;


    public Filler(String titleText, Constants.Days day) {
        super(titleText);
        this.titleText = titleText;

        setClassName("filler");

        dropTarget = DropTarget.configure(this);
        dropTarget.addDropListener(e ->
                e.getDragData().ifPresent(o -> {
                    if (o instanceof MealRenderer mealRenderer){
                        mealRenderer.meal.setMealDay(day);
                        LOG.info(mealRenderer.meal + " has been updated");
                        service.save(mealRenderer.meal);
                        update();
                    }
                }));


    }
}
}
