package net.alashkar.vaadin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;



@Route("")
public class MainView extends VerticalLayout {
    private PersonRepository repository;

    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");
    private EmailField email = new EmailField("Email");

    private Binder<Person> binder = new Binder<>(Person.class);
    private Grid<Person> grid = new Grid<>(Person.class);

    public MainView(PersonRepository repository){
        this.repository = repository;

        grid.setColumns("id", "firstName", "lastName", "email");
        VerticalLayout vl = new VerticalLayout();
        vl.setAlignItems(Alignment.CENTER);

        vl.add(getForm(), grid);
        add(vl);
        refreshGrid();
    }

    private Component getForm() {
        var formLayout = new HorizontalLayout();
        formLayout.setAlignItems(Alignment.BASELINE);
        var addButton = new Button("ADD");
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickShortcut(Key.ENTER);

        formLayout.add(firstName, lastName, email, addButton);

        binder.bindInstanceFields(this);

        addButton.addClickListener(click -> {
            try{
                var person = new Person();
                binder.writeBean(person);
                repository.save(person);
                binder.readBean(new Person());
                refreshGrid();
            }catch (ValidationException e){
                //
            }

        });
        return formLayout;
    }

    private void refreshGrid() {
        grid.setItems(repository.findAll());
    }
}
