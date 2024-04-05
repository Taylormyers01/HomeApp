package com.myershome.homeapp.list.view;

import com.myershome.homeapp.model.Task;
import com.myershome.homeapp.services.Constants;
import com.myershome.homeapp.services.TaskService;
import com.myershome.homeapp.services.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.Arrays;

@Route(value = "/task", layout = MainLayout.class)
public class TaskView extends VerticalLayout {
    Grid<Task> grid = new Grid<>(Task.class);
    TextField filterText = new TextField();
    TaskForm form;
    TaskService service;
    UserService userService;
    Select<String> sortingButton;

    TaskView(TaskService service, UserService userService) {
        this.service = service;
        this.userService = userService;
        add(new H1("Task View"));
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());



        updateList();
        closeEditor();
    }
    private void configureGrid() {
        grid.addClassNames("task-grid");
        grid.setSizeFull();
        grid.setColumns("completed","taskName");
        grid.getColumnByKey("completed").setRenderer(new ComponentRenderer<>(task -> {
            Button button = new Button();
            button.addClickListener(click -> setCompleted(task));
            button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            if(task.getCompleted() != null ) {
                if (!task.getCompleted()) {
                    button.setIcon(VaadinIcon.CIRCLE_THIN.create());
                    return button;
                }
                button.setIcon(VaadinIcon.CIRCLE.create());
                return button;
            }
            button.setIcon(VaadinIcon.CIRCLE_THIN.create());
            return button;
        }));
        grid.addColumn(task -> task.getUserName()).setHeader("username");
        grid.addColumn(Task::getDaysList).setHeader("dayList");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.getColumns().forEach(col -> col.setSortable(true));
        grid.setVisible(true);

        grid.asSingleSelect().addValueChangeListener(event ->
                editTask(event.getValue()));

    }

    private void setCompleted(Task task) {
        if(task.getCompleted() !=null){
            task.setCompleted(!task.getCompleted());
        }else{
            task.setCompleted(true);
        }
        service.saveTask(task);
        updateList();
    }

    private void editTask(Task task) {
        if(task==null){
            closeEditor();
        }else{
            form.setTask(task);
            form.setVisible(true);
            addClassName("editing");
        }

    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new TaskForm(Arrays.stream(Constants.Days.values()).toList(), userService, service);
        form.setWidth("25em");
        form.addSaveListener(this::saveTask);
        form.addDeleteListener(this::deleteTask);
        form.addCloseListener(e -> closeEditor());
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add task");
        addContactButton.addClickListener(click -> addTask());
        sortingButton = new Select<>();
        sortingButton.setItems("All Tasks", "Completed Tasks", "Today's Tasks");
        sortingButton.setValue("All Tasks");
        sortingButton.addValueChangeListener(click -> updateList());
        var toolbar = new HorizontalLayout(filterText, addContactButton, sortingButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addTask() {
        grid.asSingleSelect().clear();
        editTask(new Task());
    }


    private void updateList() {
        String sorting = sortingButton.getValue();
        switch (sorting){
            case "All Tasks":
                grid.setItems(service.filter(service.findAll(), filterText.getValue()));
                break;
            case "Completed Tasks":
                grid.setItems(service.filter(service.findAllCompleted(true), filterText.getValue()));
                break;
            case "Today's Tasks":
                Constants.Days day = Constants.Days.valueOf(LocalDate.now().getDayOfWeek().toString().toUpperCase());
                grid.setItems(service.filter(service.getAllTaskByDay(day), filterText.getValue()));
        }

//        grid.setItems(service.findAllTasks(filterText.getValue()));
    }

    private void closeEditor() {
        form.setTask(null);
        form.setVisible(false);
        removeClassName("editing");
    }
    private void saveTask(TaskForm.SaveEvent event) {
        service.saveTask(event.getTask());
        updateList();
        closeEditor();
    }
    private void deleteTask(TaskForm.DeleteEvent event) {
        service.deleteTask(event.getTask());
        updateList();
        closeEditor();
    }

}
