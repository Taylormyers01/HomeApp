package com.myershome.homeapp.webapp;

import com.myershome.homeapp.model.Task;
import com.myershome.homeapp.model.User;
import com.myershome.homeapp.services.Constants;
import com.myershome.homeapp.services.TaskService;
import com.myershome.homeapp.services.UserService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;

import java.util.List;


public class TaskForm extends FormLayout {

    TextField taskName = new TextField("Task Name");
    Checkbox reoccuring = new Checkbox("Reoccurring");
//    ComboBox<User> user = new ComboBox<>("User");
    MultiSelectComboBox<Constants.Days> daysList = new MultiSelectComboBox<>("Days");
    BeanValidationBinder<Task> binder = new BeanValidationBinder<>(Task.class);
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    TaskService service;
    UserService userService;


    public TaskForm(List<Constants.Days> daysList, UserService userService, TaskService service){
        addClassName("task-form");

        this.service = service;
        this.userService = userService;
        binder.bindInstanceFields(this);

//        user.setItems(userService.getAllUser());
//        user.setItemLabelGenerator(User::getUsername);
        this.daysList.setItems(daysList);
        this.daysList.setItemLabelGenerator(days -> days.value);

        add(    taskName,
                reoccuring,
                this.daysList,
//                user,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);


        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));



        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setTask(Task task) {
        binder.setBean(task);
    }

    public static abstract class TaskFormEvent extends ComponentEvent<TaskForm> {
        private Task task;

        protected TaskFormEvent(TaskForm source, Task task) {
            super(source, false);
            this.task = task;
        }

        public Task getTask() {
            return task;
        }
    }

    public static class SaveEvent extends TaskFormEvent {
        SaveEvent(TaskForm source, Task task) {
            super(source, task);
        }
    }

    public static class DeleteEvent extends TaskFormEvent {
        DeleteEvent(TaskForm source, Task task) {
            super(source, task);
        }

    }

    public static class CloseEvent extends TaskFormEvent {
        CloseEvent(TaskForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
