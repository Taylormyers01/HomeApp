package com.myershome.homeapp.webapp.renderers;

import com.myershome.homeapp.model.IngredientItem;
import com.myershome.homeapp.model.Meal;
import com.myershome.homeapp.services.Constants;
import com.myershome.homeapp.services.IngredientItemService;
import com.myershome.homeapp.services.MealService;
import com.myershome.homeapp.webapp.MenuView;
import com.myershome.homeapp.webapp.Utilities;

import com.myershome.homeapp.webapp.ValidationMessage;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldBase;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.DoubleRangeValidator;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class MealRendererV2 extends ReactCard implements DragSource<MealRendererV2>, HasStyle, HasSize, HasComponents, ClickNotifier<MealRendererV2> {
    public Meal meal;
    String imageUrl = "https://www.shutterstock.com/shutterstock/photos/1083445310/display_1500/stock-vector-food-with-spoon-and-fork-symbol-logo-design-1083445310.jpg";
    IngredientItemService ingredientItemService;
    MealService service;
    public Dialog dialog;
    private static final Logger LOG = LoggerFactory.getLogger(MealRendererV2.class);

    public MealRendererV2(Meal meal, MealService mealService, IngredientItemService ingredientItemService) {

        super(meal.getMealName());
        this.ingredientItemService = ingredientItemService;
        this.service = mealService;
        this.meal = meal;
        if (meal.getPictureUrl() != null) {
            this.setValue(meal.getPictureUrl().toString());
        }
        dialog = configureDialog();
        addClickListener(e -> dialog.open());
        add(dialog);
    }

    public Dialog configureDialog() {
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Meal Info");
        VerticalLayout fieldLayout = createDialogLayout(dialog);
        dialog.add(fieldLayout);
        dialog.setHeaderTitle("Meal Info");

        Button closeButton = new Button(new Icon("lumo", "cross"),
                (e) -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button deleteButton = new Button("Delete", (e) -> {
            service.delete(meal);
            if(this.getParent().isPresent() && this.getParent().get() instanceof MenuView hold){
                hold.update();
            }
            dialog.close();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().set("margin-left", "auto");

        Button saveButton = new Button("Save", (e) -> {
            LOG.info("Meal name:" + meal.getMealName() + "-");
            try {
                if (meal.getMealName() == "") {
                    Notification.show("Meal name can not be empty");
                } else {
                    service.save(meal);

                    fieldLayout.getChildren().forEach(field -> {
                        if (field instanceof TextField) {
                            ((TextField) field).setReadOnly(true);
                        } else if (field instanceof ComboBox<?>) {
                            ((ComboBox<?>) field).setReadOnly(true);
                        }
                    });
                    if(this.getParent().isPresent() && this.getParent().get() instanceof MenuView hold){
                        hold.update();
                    }
                    dialog.close();
                }
            } catch (Exception exception) {
                Utilities.errorNotification(exception.getLocalizedMessage());
                System.out.println(exception.getLocalizedMessage());
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button editButton = new Button("Edit", (e) -> {
            fieldLayout.getChildren().forEach(field -> {
                if (field instanceof TextFieldBase<?, ?>) {
                    ((TextFieldBase<?, ?>) field).setReadOnly(false);
                } else if (field instanceof ComboBox<?>) {
                    ((ComboBox<?>) field).setReadOnly(false);
                }
            });
            saveButton.setEnabled(true);
        });
        editButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        dialog.getHeader().add(closeButton);
        dialog.getFooter().add(deleteButton, editButton, saveButton);
        dialog.setWidth("1000px");
        fieldLayout.setWidthFull();

        return dialog;
    }

    private VerticalLayout createDialogLayout(Dialog dialog) {
        TextField nameField = new TextField("Meal Name", meal.getMealName(),
                "Meal name");
        nameField.getStyle().set("padding-top", "0");
        nameField.addValueChangeListener((e) -> meal.setMealName(e.getValue()));

        TextField recipeUrl = new TextField("Recipe URL");
        if (meal.getRecipeUrl() != null) {
            recipeUrl.setValue(meal.getRecipeUrl());
        }
        recipeUrl.addValueChangeListener(e -> meal.setRecipeUrl(recipeUrl.getValue()));

        TextField imageUrl = new TextField("Image URL");
        if (meal.getPictureUrl() != null) {
            imageUrl.setValue(meal.getPictureUrl().toString());
        }
        imageUrl.addValueChangeListener(e -> {
            try {
                meal.setPictureUrl(new URL(imageUrl.getValue()));
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            }
        });

        TextArea mealInstruction = new TextArea("Instructions");
        if (meal.getDirections() != null) {
            mealInstruction.setValue(meal.getDirections());
        }
        mealInstruction.setMinHeight("150px");
        mealInstruction.setMaxHeight("250px");
        mealInstruction.addValueChangeListener(e -> meal.setDirections(e.getValue()));

        VerticalLayout ingGrid = configureIngredientItemGrid(meal);
        ingGrid.setWidthFull();

        ComboBox<Constants.Days> day = new ComboBox<>();
        day.setItems(Constants.Days.dayArray);
        day.setValue(meal.getMealDay());
        day.addValueChangeListener(e -> meal.setMealDay(e.getValue()));

        VerticalLayout fieldLayout = new VerticalLayout(nameField, recipeUrl,
                imageUrl,
                mealInstruction, ingGrid, day);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        fieldLayout.getStyle().set("width", "300px").set("max-width", "100%");

        mealInstruction.setReadOnly(false);
        recipeUrl.setReadOnly(false);
        nameField.setReadOnly(false);
        day.setReadOnly(false);
        imageUrl.setReadOnly(false);

        return fieldLayout;
    }

    private VerticalLayout configureIngredientItemGrid(Meal m) {
        ValidationMessage amountValidationMessage = new ValidationMessage();
        ValidationMessage measurementValidationMessage = new ValidationMessage();
        ValidationMessage ingNameValidationMessage = new ValidationMessage();

        Grid<IngredientItem> grid = new Grid<>(IngredientItem.class, false);
        Editor<IngredientItem> editor = grid.getEditor();

        Grid.Column<IngredientItem> amountColumn = grid.addColumn(ingItem -> ingItem.amountFrac())
                .setHeader("Amount")
                .setWidth("100px").setFlexGrow(0);
        Grid.Column<IngredientItem> measureColumn = grid.addColumn(IngredientItem::getMeasurement)
                .setHeader("Measurement").setWidth("160px").setFlexGrow(0);
        Grid.Column<IngredientItem> ingredientName = grid.addColumn(ingItem -> {
            return ingItem.getIngName();
        }).setHeader("Ingredient Name");
        ingredientName.setFlexGrow(3);
        Grid.Column<IngredientItem> editColumn = grid.addComponentColumn(ingItem -> {
            Button editButton = new Button(LumoIcon.EDIT.create());
            editButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                grid.getEditor().editItem(ingItem);
            });
            return editButton;
        }).setWidth("65px").setFlexGrow(0);

        Grid.Column<IngredientItem> deleteColumn = grid.addComponentColumn(ingItem -> {
            Button deleteButton = new Button(VaadinIcon.CLOSE.create());
            deleteButton.addClickListener(e -> {

                if (deleteButton.hasThemeName("contrast")) {
                    service.removeIngItem(ingItem, m);
                    grid.setItems(meal.getIngredientItems());
                }
                deleteButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

            });
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                    ButtonVariant.LUMO_ERROR);
            return deleteButton;
        }).setWidth("65px").setFlexGrow(0);

        Binder<IngredientItem> binder = new Binder<>(
                IngredientItem.class);
        editor.setBinder(binder);
        editor.setBuffered(false);

        NumberField amountField = new NumberField();
        amountField.setWidthFull();
        binder.forField(amountField).asRequired("Amount must not be empty")
                .withValidator(new DoubleRangeValidator("Invalid number", null, null))
                .withStatusLabel(amountValidationMessage)
                .bind(IngredientItem::getAmount, IngredientItem::setAmount);
        amountColumn.setEditorComponent(amountField);

        Select<Constants.Measurements> meaBox = new Select<>();
        meaBox.setWidthFull();
        meaBox.setItems(Constants.Measurements.values());
        binder.forField(meaBox).asRequired("Measurement must not be empty")
                .withStatusLabel(measurementValidationMessage)
                .bind(IngredientItem::getMeasurement, IngredientItem::setMeasurement);
        measureColumn.setEditorComponent(meaBox);

        TextField ingNameField = new TextField();
        ingNameField.setWidthFull();
        binder.forField(ingNameField).asRequired("Name must not be empty").withStatusLabel(ingNameValidationMessage)
                .bind(IngredientItem::getIngName, IngredientItem::setIngName);
        ingredientName.setEditorComponent(ingNameField);

        Button saveButton = new Button(LumoIcon.CHECKMARK.create(), e -> {
            LOG.info("Editor Item: " + editor.getItem());
            ingredientItemService.save(editor.getItem());
            refreshGrid(grid, m);
        });
        HorizontalLayout actions = new HorizontalLayout(
                saveButton);
        actions.setPadding(false);
        editColumn.setEditorComponent(actions);

        editor.addCancelListener(e -> {
            ingNameValidationMessage.setText("");
            measurementValidationMessage.setText("");
            amountValidationMessage.setText("");
        });

        refreshGrid(grid, m);

        Button addIngItem = new Button("Add", LumoIcon.PLUS.create());
        addIngItem.addClickListener(event -> {
            List<IngredientItem> hold = new ArrayList<>();
            if (m.getIngredientItems() != null) {
                hold = m.getIngredientItems();
            }
            IngredientItem ing = ingredientItemService.createIngItem();
            hold.add(ing);
            m.setIngredientItems(hold);
            refreshGrid(grid, m);
        });
        HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.addClassNames(LumoUtility.Border.LEFT, LumoUtility.Border.BOTTOM, LumoUtility.Border.RIGHT,
                LumoUtility.BorderColor.CONTRAST_10, LumoUtility.Padding.NONE);
        hLayout.add(addIngItem);
        addIngItem.setWidthFull();

//        getThemeList().clear();
//        getThemeList().add("spacing-s");
        add(ingNameValidationMessage, measurementValidationMessage,
                amountValidationMessage);

        VerticalLayout output = new VerticalLayout();
        output.setWidthFull();
        grid.setWidthFull();
        hLayout.setWidthFull();
        output.add(grid, hLayout);
        return output;
    }

    private void refreshGrid(Grid<IngredientItem> grid, Meal meal) {
        if (meal.getIngredientItems() != null) {
            List<IngredientItem> ingredientItems = meal.getIngredientItems();
            grid.setItems(ingredientItems);
        }
    }

}
