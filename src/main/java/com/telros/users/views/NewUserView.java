package com.telros.users.views;

import com.telros.users.data.entities.UserEntity;
import com.telros.users.data.entities.UserEntityRole;
import com.telros.users.services.RegexService;
import com.telros.users.services.UserService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
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
import jakarta.annotation.security.RolesAllowed;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

@PageTitle("New user")
@Route(value = "newUser", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed("ROLE_ADMIN")
public class NewUserView extends Composite<VerticalLayout> {
    private UserService userService;
    private TextField login;
    private TextField password;
    private TextField name;
    private TextField midName;
    private TextField surname;
    private DatePicker birthday;
    private TextField phone;
    private EmailField email;
    private Upload photo;
    private String photoLink;
    private Checkbox role;


    public NewUserView(UserService userService) {
        Notification notification = Notification
                .show("Для добавления пользователя - заполните данные. \n" +
                        "Обязательные поля: логин, пароль, имя, фамилия");
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.setDuration(10000);

        this.userService = userService;

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth("100%");
        verticalLayout.setMaxWidth("800px");
        verticalLayout.setHeight("min-content");

        //разметка слоя для добавления функциональных элементов
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

        //добавление элементов для внесения данных нового пользователя
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

        //Получение загружаемого через форму изображения.
        //Хранение в базе данных предполагается в формате ссылки,
        // с возможностью дальнейшего получения по ссылке и использования на странице сайта
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        photo = new Upload(buffer);
        photo.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);

            File targetFile = new File("src/main/resources/img/"+fileName);

            try {
                Files.copy(
                        inputStream,
                        targetFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
                inputStream.close();
                photoLink = targetFile.getPath();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        //Устанавливается наличие прав доступа.
        //Пользователь с правами доступа USER не может получить доступ ко всему списку пользователей,
        //а также доступ к странице с добавлением нового
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

        //Событие при нажатии saveButton -
        // получение внесенных данных формы и сохранение в БД методом saveThePerson()
        saveButton.addClickListener(clickEvent -> {
                saveThePerson(login.getValue(),
                        password.getValue(),
                        name.getValue(),
                        midName.getValue(),
                        surname.getValue(),
                        birthday.getValue(),
                        phone.getValue(),
                        email.getValue(),
                        photoLink,
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

    //Метод сохранения полученных данных формы, с проверкой внесенных данных
    private void saveThePerson(String login,
                               String password,
                               String name,
                               String midName,
                               String surname,
                               LocalDate birthday,
                               String phone,
                               String email,
                               String photo,
                               boolean role){
        UserEntity user = new UserEntity();
        if(login.isEmpty()||password.isEmpty()||name.isEmpty()||surname.isEmpty()) {
            Notification notification = Notification
                    .show("Заполните данные: логин, пароль, имя, фамилия");
            notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
            notification.setPosition(Notification.Position.TOP_CENTER);
        }
        else {
            user.setLogin(login);
            user.setPassword("{noop}" + password);
            user.setName(name);
            user.setMiddleName(midName);
            user.setSurname(surname);
            user.setBirthday(birthday);
            if(RegexService.phoneNumberRefactor(phone).equals("Неверный формат номера")){
                Notification notification = Notification
                        .show("Введен неверный формат номера телефона");
                notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                notification.setPosition(Notification.Position.TOP_CENTER);
            }else {
                user.setPhone(RegexService.phoneNumberRefactor(phone));
            }

            user.setEmail(email);
            user.setPhoto(photo);

            if(!role){
                user.setRole(UserEntityRole.ROLE_USER);
            }
            else {
                user.setRole(UserEntityRole.ROLE_ADMIN);
            }
            if (userService.checkTheLogin(user)) {
                Notification notification = Notification
                        .show("Пользователь с данным логином уже внесен!");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.BOTTOM_CENTER);
            } else {
                userService.addUser(user);
                Notification notification = Notification
                        .show("Данные пользователя сохранены");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.TOP_CENTER);

                UI.getCurrent().getPage().reload();
            }
        }
    }
}
