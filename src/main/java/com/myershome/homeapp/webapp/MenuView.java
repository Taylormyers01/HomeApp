package com.myershome.homeapp.webapp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.myershome.homeapp.webapp.renderers.CardRendererVaadin;
import com.myershome.homeapp.webapp.renderers.GridRenderer;
import com.myershome.homeapp.webapp.renderers.MealRendererV2;
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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
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
    VerticalLayout secondary = new VerticalLayout();
    VerticalLayout primary = new VerticalLayout();

    MenuView(MealService service, IngredientItemService ingredientItemService) {
        this.service = service;
        this.ingredientItemService = ingredientItemService;

        setSizeFull();
        setup();

    }

    private void setup() {
        configureSecondary2();
        configurePrimary();

        primary.setSizeFull();
        primary.setHeight("40%");
        secondary.setHeight("60%");

        add(primary, secondary);

    }

    private HorizontalLayout createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        Button addMeal = new Button(new Icon("lumo", "plus"),
                e -> {
                    Meal meal = new Meal();
                    meal.setMealName("");
                    MealRendererV2 mealRenderer = new MealRendererV2(meal, service, ingredientItemService);
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
                    MealRendererV2 mr = new MealRendererV2(m, service, ingredientItemService);
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



    public void configurePrimary() {
        fillers.clear();
        HorizontalLayout mealDays = new HorizontalLayout();
        HorizontalLayout header = new HorizontalLayout();
        GridRenderer gridLayout = new GridRenderer();
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
            tile.setWidth("140px");
            tile.setHeight("140px");
            H5 dayHeader = new H5(day.value + ":");
            tile.add(dayHeader);
            Optional<Meal> meal = service.findByMealDay(day);
            if (meal.isPresent()) {
                MealRendererV2 m2 = new MealRendererV2(meal.get(), service, ingredientItemService);
//                m2.addClassNames(LumoUtility.BorderColor.CONTRAST_60, LumoUtility.BorderRadius.MEDIUM, LumoUtility.Border.ALL);
                tile.add(m2);
            } else {
                Filler filler = new Filler(" ", day);
                Image image = new Image();
                image.addClassNames(LumoUtility.Height.XLARGE,
                        LumoUtility.Width.FULL);
                filler.content = image;
                fillers.add(filler);
//                filler.addClassNames(LumoUtility.BorderColor.CONTRAST_60, LumoUtility.BorderRadius.MEDIUM, LumoUtility.Border.ALL);
                tile.add(filler);

            }
            gridLayout.add(tile);
        }
        mealDays.addClassNames(LumoUtility.Padding.XSMALL);
        mealDays.setSizeFull();
        primary.addClassName(LumoUtility.Padding.SMALL);
        primary.add(header, gridLayout);
    }

    public void configureSecondary2() {
        var header = createHeader();
        header.setWidthFull();
        secondary.add(header);

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

    public void update() {
        secondary.removeAll();
        primary.removeAll();

        configureSecondary2();
        configurePrimary();

    }



    class Filler extends CardRendererVaadin implements HasStyle {
        private DropTarget<Filler> dropTarget;

        public Filler(String titleText, Constants.Days day) {
            super(titleText);

            setClassName("filler");

            getStyle().set("width", "130px");
            getStyle().set("height", "140px");
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
