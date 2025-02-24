package com.myershome.homeapp.webapp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.myershome.homeapp.webapp.renderers.GridRenderer;
import com.myershome.homeapp.webapp.renderers.MealRendererV2;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.theme.lumo.Lumo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myershome.homeapp.model.IngredientItem;
import com.myershome.homeapp.model.Meal;
import com.myershome.homeapp.services.Constants;
import com.myershome.homeapp.services.Constants.Measurements;
import com.myershome.homeapp.services.IngredientItemService;
import com.myershome.homeapp.services.MealService;
import com.myershome.homeapp.webapp.renderers.CardRenderer;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
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
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route(value = "/menu", layout = MainLayout.class)
public class MenuView extends VerticalLayout {
    private static final Logger LOG = LoggerFactory.getLogger(MenuView.class);
    List<Filler> fillers = new ArrayList<>();
    MealService service;
    IngredientItemService ingredientItemService;
    TextField filterText = new TextField();
    VerticalLayout secondary;

    MenuView(MealService service, IngredientItemService ingredientItemService) {
        this.service = service;
        this.ingredientItemService = ingredientItemService;
        setSizeFull();
        update();
    }

    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        Button addMeal = new Button(new Icon("lumo", "plus"),
                e -> {
                    Meal meal = new Meal();
                    meal.setMealName("");
                    MealRenderer mealRenderer = new MealRenderer(meal, false);
                    header.add(mealRenderer.dialog);
                    mealRenderer.dialog.open();
                });
        Button parser = configureParserButton();
        parser.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> update());
        addMeal.getStyle().set("margin-left", "1%");
        header.add(addMeal, filterText, parser);
        return header;
    }

    private Button configureParserButton() {
        Dialog dialog = new Dialog();
        dialog.getElement().setAttribute("aria-label", "Parse Recipe");

        VerticalLayout fieldLayout = new VerticalLayout();
        TextField printPageURL = new TextField("Recipe Print Page URL", "print URL");
        fieldLayout.add(printPageURL);
        dialog.add(fieldLayout);
        dialog.setHeaderTitle("Parse Recipe");
        Button save = new Button("Save", e -> {
            if (printPageURL.getValue() != null) {
                try {
                    Meal m = service.parseMeal(printPageURL.getValue());
                    // LOG.info("Created meal " + m);
                    MealRenderer mr = new MealRenderer(m, false);
                    mr.readOnly = false;
                    LOG.info("PARSER BUTTON DONE");
                    add(mr.dialog);
                    mr.dialog.open();
                    dialog.close();
                } catch (Exception except) {
                    Notification.show(except.toString());
                }
            }
        });
        dialog.add(save);

        return new Button("Parse from URL", e -> {
            dialog.open();
        });

    }



    public VerticalLayout configurePrimary() {
        fillers.clear();
        VerticalLayout primary = new VerticalLayout();
        HorizontalLayout mealDays = new HorizontalLayout();
        HorizontalLayout header = new HorizontalLayout();
        DwGridLayout gridLayout = new DwGridLayout(7, 1);
        Button clearMeals = new Button("Clear meals", (e) -> {
            List<Meal> meals = service.findAllByMealDayNotNull();
            LOG.info(meals.toString());
            meals.forEach(meal -> meal.setMealDay(null));
            service.saveAll(meals);
            update();
        });
        header.setWidthFull();
        clearMeals.getStyle().set("margin-left", "1%");
        header.add(clearMeals);
        mealDays.add(header);
        for (Constants.Days day : Constants.Days.dayArray) {
            Div tile = new Div();
            H5 dayHeader = new H5(day.value + ":");
            tile.add(dayHeader);
            Optional<Meal> meal = service.findByMealDay(day);
            if (meal.isPresent()) {
                tile.add(new MealRendererV2(meal.get(), service, ingredientItemService));
            } else {
                Filler filler = new Filler(" ", day);
                Image image = new Image();
                image.addClassNames(LumoUtility.Height.XLARGE,
                        LumoUtility.Width.FULL);
                filler.addContent(image);
                fillers.add(filler);
                tile.add(filler);

            }
            gridLayout.addComponent(day.ordinal(), 0, tile);
        }
        mealDays.addClassNames(LumoUtility.Padding.XSMALL);
        mealDays.setSizeFull();
        primary.addClassName(LumoUtility.Padding.SMALL);
        primary.add(header, gridLayout);
        primary.setHeightFull();
        return primary;
    }

    public VerticalLayout configureSecondary2() {
        VerticalLayout secondary = new VerticalLayout();
        var header = createHeader();
        header.setWidthFull();
        secondary.add(header);

        return secondary;
    }

    public void update() {
        removeAll();
        if (secondary != null) {
            secondary.removeAll();
        }
        secondary = configureSecondary2();
        var primary = configurePrimary();
        primary.setSizeFull();
        primary.setHeight("40%");
        // secondary.setSizeFull();
        secondary.setHeight("60%");

//        UI.getCurrent().getPage().retrieveExtendedClientDetails(details -> {
//            handleScreenWidth(details.getBodyClientWidth());
//        });
        handleScreenWidthV2();
        add(primary, secondary);
    }

    public void handleScreenWidthV2(){
        GridRenderer gr = new GridRenderer();
        List<Meal> meals = service.findAllByMealDayIsNull();
        if (!filterText.getValue().isEmpty()) {
            meals = meals.stream().filter(meal -> meal.getMealName().toLowerCase()
                    .contains(filterText.getValue().toLowerCase())).toList();
        }
        meals = meals.stream().sorted(Comparator.comparing(Meal::getMealName)).toList();
        for (Meal m: meals){
            gr.add(new MealRendererV2(m, service, ingredientItemService));
        }
        secondary.add(gr);
    }

    private void handleScreenWidth(int screenWidth) {
        VerticalLayout gridLayout = new VerticalLayout();
        gridLayout.setClassName("Grid-Layout");
        List<Meal> meals = service.findAllByMealDayIsNull();
        String hold = new CardRenderer("test").maxWidth;
        int cardWidth = Integer.parseInt(hold.split("px")[0]) + 50;
        int col = screenWidth / cardWidth;
        int rows = (meals.size() / col) + 1;
        DwGridLayout grid = new DwGridLayout(col, rows);
        if (!filterText.getValue().isEmpty()) {
            meals = meals.stream().filter(meal -> meal.getMealName().toLowerCase()
                    .contains(filterText.getValue().toLowerCase())).toList();
        }
        meals = meals.stream().sorted(Comparator.comparing(Meal::getMealName)).toList();
        int x = 0;
        int mealNum = 0;
        while (x < rows) {
            for (int y = 0; y < col; y++) {
                if (mealNum < meals.size()) {
                    grid.addComponent(y, x, new MealRenderer(meals.get(mealNum), true));
                    mealNum++;
                }
            }
            x++;
        }
        gridLayout.add(grid);
        grid.setDisplayBorder(true);
        secondary.add(gridLayout);
    }

    class MealRenderer extends CardRenderer implements DragSource<MealRenderer> {
        Meal meal;
        String imageUrl = "https://www.shutterstock.com/shutterstock/photos/1083445310/display_1500/stock-vector-food-with-spoon-and-fork-symbol-logo-design-1083445310.jpg";

        Dialog dialog;
        Boolean readOnly;

        public MealRenderer(Meal meal, Boolean readOnly) {
            super(meal.getMealName());
            this.readOnly = readOnly;
            this.meal = meal;
            Image image = new Image();
            if (meal.getPictureUrl() != null) {
                image.setSrc(meal.getPictureUrl().toString());
                image.setAlt(meal.getMealName());
            } else {
                image.setSrc(imageUrl);
                image.setAlt(meal.getMealName());
            }
            this.addContent(image);
            DragSource<MealRenderer> dragSource = DragSource.create(this);
            dragSource.setDragData(this);
            setDraggable(true);

            dragSource.addDragStartListener(e -> {
                LOG.info("started drag event for mealId: " + meal.getId());
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
                if (!e.isSuccessful()) {
                    meal.setMealDay(null);
                    try {
                        service.save(meal);
                    } catch (Exception exception) {
                        Utilities.errorNotification(exception.getLocalizedMessage());
                        System.out.println(exception.getLocalizedMessage());
                    }
                    update();
                }
            });

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
                update();
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
                        update();
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

            mealInstruction.setReadOnly(readOnly);
            recipeUrl.setReadOnly(readOnly);
            nameField.setReadOnly(readOnly);
            day.setReadOnly(readOnly);
            imageUrl.setReadOnly(readOnly);

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

            Select<Measurements> meaBox = new Select<>();
            meaBox.setWidthFull();
            meaBox.setItems(Measurements.values());
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

            getThemeList().clear();
            getThemeList().add("spacing-s");
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

    class Filler extends CardRenderer implements HasStyle {
        private DropTarget<Filler> dropTarget;
        private String titleText;

        public Filler(String titleText, Constants.Days day) {
            super(titleText);
            this.titleText = titleText;

            setClassName("filler");

            getStyle().set("width", "130px");
            getStyle().set("height", "140px");
            getStyle().set("border", "solid hsla(214, 87%, 92%, 0.69)");
            getStyle().set("border-radius", "5px 5px 5px 5px");
            dropTarget = DropTarget.configure(this);
            dropTarget.addDropListener(e -> e.getDragData().ifPresent(o -> {
                LOG.info("Dropped on day: " + day);
                if (o instanceof MealRenderer mealRenderer) {
                    mealRenderer.meal.setMealDay(day);
                    LOG.info("MealID: " + mealRenderer.meal.getId());
                    try {
                        service.save(mealRenderer.meal);
                    } catch (Exception exception) {
                        Utilities.errorNotification(exception.toString());
                        LOG.error("Exception Thrown: " + exception);
                    }

                    update();
                }
            }));

        }
    }
}
