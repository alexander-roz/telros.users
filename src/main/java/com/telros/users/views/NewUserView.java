package com.telros.users.views;

import com.telros.users.data.entities.UserEntity;
import com.telros.users.data.entities.UserEntityRole;
import com.telros.users.services.UserService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.io.InputStream;
import java.time.LocalDate;

@PageTitle("New user")
@Route(value = "person", layout = MainLayout.class)
@Uses(Icon.class)
//@RolesAllowed("ADMIN")
public class NewUserView extends Composite<VerticalLayout> {
    UserService userService;
    private TextField login;
    private TextField password;
    private TextField name;
    private TextField midName;
    private TextField surname;
    private DatePicker birthday;
    private TextField phone;
    private EmailField email;
    private Upload photo;
    private Checkbox role;


    public NewUserView(UserService userService) {
        Notification notification = Notification
                .show("Для добавления пользователя - заполните данные. " +
                        "Обязательные поля: логин, пароль, имя, фамилия");
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.setDuration(10000);
        this.userService = userService;
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth("100%");
        verticalLayout.setMaxWidth("800px");
        verticalLayout.setHeight("min-content");

        HorizontalLayout line = new HorizontalLayout();
        line.setPadding(true);
        line.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout line1 = new HorizontalLayout();
        line1.setPadding(true);
        line1.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout line2 = new HorizontalLayout();
        line2.setPadding(true);
        line2.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout line3 = new HorizontalLayout();
        line3.setPadding(true);
        line3.setAlignItems(FlexComponent.Alignment.CENTER);


        verticalLayout.add(line);
        verticalLayout.add(line1);
        verticalLayout.add(line2);
        verticalLayout.add(line3);

        login = new TextField();
        login.setLabel("Логин");
        password = new TextField();
        password.setLabel("Пароль");

        name = new TextField();
        name.setLabel("Имя");
        midName = new TextField();
        midName.setLabel("Отчество");
        surname = new TextField();
        surname.setLabel("Фамилия");
        birthday = new DatePicker("Дата рождения");
        phone = new TextField();
        phone.setLabel("Телефон");

        email = new EmailField();
        email.setLabel("Email");
        email.getElement().setAttribute("name", "email");
        email.setErrorMessage("Enter a valid email address");
        email.setClearButtonVisible(true);
        email.setInvalid(true);

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        photo = new Upload(buffer);
        photo.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);

            // Do something with the file data
            // processFile(inputStream, fileName);
        });
        role = new Checkbox();
        role.setLabel("Права администратора");

        HorizontalLayout layoutRow = new HorizontalLayout();
        layoutRow.addClassName(LumoUtility.Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");

        Button saveButton = new Button();
        saveButton.setText("Сохранить");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layoutRow.add(saveButton);

        saveButton.addClickListener(clickEvent -> {
            saveThePerson(login.getValue(),
                    password.getValue(),
                    name.getValue(),
                    midName.getValue(),
                    surname.getValue(),
                    birthday.getValue(),
                    phone.getValue(),
                    email.getValue(),
                    role.getValue());
        });

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);


        getContent().add(verticalLayout);
        line.add(login);
        line.add(password);
        line1.add(name);
        line1.add(midName);
        line1.add(surname);
        line2.add(birthday);
        line2.add(phone);
        line2.add(email);
        line3.add(photo);
        line3.add(role);
        verticalLayout.add(layoutRow);
    }

    private void saveThePerson(String login,
                               String password,
                               String name,
                               String midName,
                               String surname,
                               LocalDate birthday,
                               String phone,
                               String email,
                               boolean role){
        UserEntity user = new UserEntity();

        if(login.isEmpty()||password.isEmpty()&&name.isEmpty()&&surname.isEmpty()) {
            Notification notification = Notification
                    .show("Заполните данные: логин, пароль, имя, фамилия");
            notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        }
        else {
            user.setLogin(login);
            user.setPassword(password);
            user.setName(name);
            user.setMiddleName(midName);
            user.setSurname(surname);
            user.setBirthday(birthday);
            user.setPhone(phone);
            user.setEmail(email);
            if(!role){
                user.setRole(UserEntityRole.USER);
            }
            else {
                user.setRole(UserEntityRole.ADMINISTRATOR);
            }
            if (userService.checkTheLogin(user)) {
                Notification notification = Notification
                        .show("Пользователь с данным логином уже внесен!");
                notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
            } else {
                userService.addUser(user);
                Notification notification = Notification
                        .show("Данные пользователя сохранены");
                notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
            }
        }
    }
}
