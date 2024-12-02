package com.telros.users.views;

import com.telros.users.data.entities.UserEntity;
import com.telros.users.data.entities.UserEntityRole;
import com.telros.users.security.SecurityService;
import com.telros.users.services.impl.UserServiceImpl;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import jakarta.annotation.security.RolesAllowed;

import java.util.List;

//класс Vaadin вкладки списка пользователей
@PageTitle("Users")
@Route(value = "users", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
public class MainView extends Div {

    private UserServiceImpl userService;
    private SecurityService securityService;
    private VerticalLayout layout;
    private HorizontalLayout line1;
    private HorizontalLayout line2;


    public MainView(UserServiceImpl userService,
                    SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
        setSizeFull();
        addClassNames("users-view");

        line1 = new HorizontalLayout();
        line1.setPadding(true);
        line1.setAlignItems(FlexComponent.Alignment.CENTER);
        line2 = new HorizontalLayout();
        line2.setPadding(true);
        line2.setAlignItems(FlexComponent.Alignment.CENTER);

        layout = new VerticalLayout(createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        line1.add(layout);
        add(line1, line2);
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

        //добавление функций администратора, в случае наличия прав пользователя
        if(user.getRole().equals(UserEntityRole.ROLE_ADMIN)){
            grid.setSelectionMode(Grid.SelectionMode.MULTI);

            Button delete = new Button("Удалить");
            delete.setEnabled(false);
            delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
            delete.getStyle().set("margin-inline-start", "auto");
            HorizontalLayout footer = new HorizontalLayout();
            footer.getStyle().set("flex-wrap", "wrap");
            footer.add(delete);
            line2.add(footer);

            delete.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
                @Override
                public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                    for(UserEntity item:grid.getSelectedItems()){
                        userService.deleteUser(item);
                        Notification notification = Notification
                                .show("Пользователь " + item.getLogin() + " удален");
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        notification.setPosition(Notification.Position.TOP_CENTER);
                        notification.setDuration(10000);
                        UI.getCurrent().getPage().reload();
                    }
                }
            });

            grid.addSelectionListener(selection -> {
                int size = selection.getAllSelectedItems().size();
                delete.setEnabled(size != 0);
            });

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
