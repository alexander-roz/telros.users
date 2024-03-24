package com.telros.users.views;

import com.telros.users.data.entities.UserEntity;
import com.telros.users.services.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

@PageTitle("Users")
@Route(value = "users", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Uses(Icon.class)
public class AdminView extends Div {

    private Grid<UserEntity> grid;
    private final UserService userService;

    public AdminView(UserService userService) {
        this.userService = userService;
        setSizeFull();
        addClassNames("users-view");

        VerticalLayout layout = new VerticalLayout(createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    private Component createGrid() {
        grid = new Grid<>(UserEntity.class, false);
        grid.addColumn(UserEntity::getId).setHeader("ID");
        grid.addColumn(UserEntity::getLogin).setHeader("Логин");
        grid.addColumn(UserEntity::getPassword).setHeader("Пароль");
        grid.addColumn(UserEntity::getName).setHeader("Имя");
        grid.addColumn(UserEntity::getMiddleName).setHeader("Отчество");
        grid.addColumn(UserEntity::getSurname).setHeader("Фамилия");
        grid.addColumn(UserEntity::getBirthday).setHeader("Дата");
        grid.addColumn(UserEntity::getPhone).setHeader("Телефон");
        grid.addColumn(UserEntity::getEmail).setHeader("Почта");
        grid.addColumn(UserEntity::getPhoto).setHeader("Фото");
        List<UserEntity> users = userService.findAllUsers();
        grid.setItems(users);

        return grid;
    }

}
