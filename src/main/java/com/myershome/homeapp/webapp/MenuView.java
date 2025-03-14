package com.myershome.homeapp.webapp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.myershome.homeapp.webapp.renderers.CardRendererVaadin;
import com.myershome.homeapp.webapp.renderers.GridRenderer;
import com.myershome.homeapp.webapp.renderers.MealRendererV2;
import com.myershome.homeapp.webapp.renderers.ReactCard;
import com.vaadin.flow.component.dnd.DragSource;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myershome.homeapp.model.Meal;
import com.myershome.homeapp.services.Constants;
import com.myershome.homeapp.services.IngredientItemService;
import com.myershome.homeapp.services.MealService;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dnd.DropTarget;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route(value = "/menu", layout = MainLayout.class)
public class MenuView extends VerticalLayout {
    private static final Logger LOG = LoggerFactory.getLogger(MenuView.class);
    List<Filler> fillers = new ArrayList<>();
    MealService service;
    IngredientItemService ingredientItemService;
    TextField filterText = new TextField();
    private VerticalLayout secondary = new VerticalLayout();
    private VerticalLayout primary = new VerticalLayout();
    private GridRenderer primaryGrid = new GridRenderer();
    private GridRenderer secondaryGrid = new GridRenderer();
    private final Tab menu;
    private final Tab ingredients;
    private VerticalLayout menuContent = new VerticalLayout();
    private VerticalLayout content = new VerticalLayout();

    MenuView(MealService service, IngredientItemService ingredientItemService) {
        this.service = service;
        this.ingredientItemService = ingredientItemService;
        menu = new Tab("Menu");
        ingredients = new Tab("Ingredients");
        Tabs tabs = new Tabs();
//        tabs.setAutoselect(false);

        tabs.addSelectedChangeListener(
                event -> setContent(event.getSelectedTab()));
        tabs.add(menu, ingredients);

        tabs.setWidthFull();
        setSizeFull();
        setup();

        add(tabs, content);

    }
    private void setContent(Tab tab) {
        content.removeAll();
        if (tab == null) {
            return;
        }
        if (tab.equals(menu)) {
            content.add(menuContent);
        } else if (tab.equals(ingredients)) {
            content.add(new Paragraph("This is the Payment tab"));
        }
    }

    private void setup() {
        configureSecondary2();
        configurePrimary();

        primary.setSizeFull();
        primary.setHeight("40%");
        secondary.setHeight("60%");

        menuContent.add(primary, secondary);

    }


    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        Button addMeal = new Button(new Icon("lumo", "plus"),
                e -> {
                    Meal meal = new Meal();
                    meal.setMealName("");
                    MealRendererV2 mealRenderer = menuMealGenerator(meal);
                    header.add(mealRenderer.dialog);
                    mealRenderer.dialog.open();
                });
        Button parser = configureParserButton();
        parser.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> {
            secondaryGrid.removeAll();
            configureGrid("SECONDARY");
        });
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
                    MealRendererV2 mr = menuMealGenerator(m);
                    LOG.info("PARSER BUTTON DONE");
                    menuContent.add(mr.dialog);
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


    public void configurePrimary() {
        fillers.clear();
        HorizontalLayout mealDays = new HorizontalLayout();
        HorizontalLayout header = new HorizontalLayout();
        primaryGrid = new GridRenderer();
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
        configureGrid("PRIMARY");
        mealDays.addClassNames(LumoUtility.Padding.XSMALL);
        mealDays.setSizeFull();
        primary.addClassName(LumoUtility.Padding.SMALL);
        primary.add(header, primaryGrid);
    }


    public void configureSecondary2() {
        var header = createHeader();
        header.setWidthFull();
        secondary.add(header);
        configureGrid("SECONDARY");
        secondary.add(secondaryGrid);
    }


    private void configureGrid(String key){
       switch (key){
           case "PRIMARY":
               for (Constants.Days day : Constants.Days.dayArray) {
                   Div tile = new Div();
//                   tile.setWidth("140px");
//                   tile.setHeight("140px");
                   H3 dayHeader = new H3(day.value + ":");
                   tile.add(dayHeader);
                   Optional<Meal> meal = service.findByMealDay(day);
                   if (meal.isPresent()) {
                       ReactCard m2 = menuMealGeneratorV2(meal.get());
                       tile.add(m2);
                   } else {
                       Filler filler = new Filler(" ", day);
                       fillers.add(filler);
                       tile.add(filler);

                   }
                   primaryGrid.add(tile);
               }
               break;
           case "SECONDARY":
               List<Meal> meals = service.findAllByMealDayIsNull();
               if (!filterText.getValue().isEmpty()) {
                   meals = meals.stream().filter(meal -> meal.getMealName().toLowerCase()
                           .contains(filterText.getValue().toLowerCase())).toList();
               }
               meals = meals.stream().sorted(Comparator.comparing(Meal::getMealName)).toList();
               for (Meal m: meals){
                   secondaryGrid.add(menuMealGeneratorV2(m));
               }
               break;


       }

    }

    public void update() {
        primaryGrid.removeAll();
        secondaryGrid.removeAll();

        configureGrid("PRIMARY");
        configureGrid("SECONDARY");
    }


    private MealRendererV2 menuMealGenerator(Meal meal){
        MealRendererV2 rendererV2 = new MealRendererV2(meal, service, ingredientItemService);

        DragSource<MealRendererV2> dragSource = DragSource.create(rendererV2);
        dragSource.setDragData(rendererV2);
        rendererV2.setDraggable(true);

        dragSource.addDragStartListener(e -> {
            LOG.info("started drag event for mealId: " + meal.getId());
            e.getComponent().meal.setMealDay(null);
        });
        dragSource.addDragEndListener(e -> {
            if (!e.isSuccessful()) {
                meal.setMealDay(null);
                LOG.info("Failed Drag Event");
                try {
                    service.save(meal);
                    update();
                } catch (Exception exception) {
                    Utilities.errorNotification(exception.getLocalizedMessage());
                    System.out.println(exception.getLocalizedMessage());
                }
            }
        });
        return rendererV2;
    }

    private ReactCard menuMealGeneratorV2(Meal meal){
        if(meal.getPictureUrl() != null){
            return new ReactCard(meal.getMealName(), meal.getPictureUrl().toString());
        }
        return new ReactCard(meal.getMealName(), null);
    }


    class Filler extends ReactCard implements HasStyle {
        private DropTarget<Filler> dropTarget;

        public Filler(String titleText, Constants.Days day) {
            super(titleText);

            dropTarget = DropTarget.configure(this);

            this.dropTarget.setActive(true);
            this.setVisible(true);

            dropTarget.addDropListener(e -> e.getDragData().ifPresent(o -> {
                LOG.info("Dropped on day: " + day);
                if (o instanceof MealRendererV2 mealRenderer) {
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
