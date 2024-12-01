package com.telros.users.views;

import com.telros.users.data.entities.UserEntity;
import com.telros.users.data.entities.UserEntityRole;
import com.telros.users.security.SecurityService;
import com.telros.users.services.impl.UserServiceImpl;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@PageTitle("Users")
@Route(value = "users", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
public class MainView extends Div {

    private UserServiceImpl userService;
    private SecurityService securityService;

    public MainView(UserServiceImpl userService,
                    SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
        setSizeFull();
        addClassNames("users-view");

        VerticalLayout layout = new VerticalLayout(createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    private Component createGrid() {

        Grid<UserEntity> grid = new Grid<>(UserEntity.class, false);
        grid.addColumn(UserEntity::getId).setHeader("ID").setSortable(true);
        grid.addColumn(UserEntity::getLogin).setHeader("Логин").setSortable(true);
        grid.addColumn(UserEntity::getPassword).setHeader("Пароль").setSortable(true);
        grid.addColumn(UserEntity::getName).setHeader("Имя").setSortable(true);
        grid.addColumn(UserEntity::getMiddleName).setHeader("Отчество").setSortable(true);
        grid.addColumn(UserEntity::getSurname).setHeader("Фамилия").setSortable(true);
        grid.addColumn(UserEntity::getBirthday).setHeader("Дата").setSortable(true);
        grid.addColumn(UserEntity::getPhone).setHeader("Телефон");
        grid.addColumn(UserEntity::getEmail).setHeader("Почта");
        grid.addColumn(UserEntity::getPhoto).setHeader("Фото");

        String login = securityService.getAuthenticatedUser().getUsername();
        UserEntity user = userService.findUserByLogin(login);

        Notification notification = Notification
                .show("Вход совершен пользователем " + login);
        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
        notification.setDuration(10000);

        if(user.getRole().equals(UserEntityRole.ROLE_ADMIN)){
            List<UserEntity> users = userService.findAllUsers();
            grid.setItems(users);
        }
        else {
            grid.setItems(user);
        }
        return grid;
    }
    private Image getThePhoto(String url){
        StreamResource imageResource = new StreamResource(url,
                () -> getClass().getResourceAsStream(url));

        return new Image(imageResource, "");
    }

}
