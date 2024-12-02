package com.telros.users.views;

import com.telros.users.data.entities.UserEntityRole;
import com.telros.users.security.SecurityService;
import com.telros.users.services.UserService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.userdetails.UserDetails;
import org.vaadin.lineawesome.LineAwesomeIcon;

import static com.telros.users.views.LoginView.notificationCounter;

//Основной слой приложения
@Route(value = "")
@RolesAllowed({"ROLE_ADMIN", "ROLE_USER"})
public class MainLayout extends AppLayout {

    private H2 viewTitle;
    private SideNavItem addUser;
    private final transient AuthenticationContext authContext;
    private final UserService userService;


    public MainLayout(SecurityService securityService,
                      AuthenticationContext authContext,
                      UserService userService) {
        this.authContext = authContext;
        this.userService = userService;

        UserDetails currentUser = securityService.getAuthenticatedUser();



        H1 logo = new H1("Telros users");
        logo.addClassName("logo");
        HorizontalLayout
                header =
                authContext.getAuthenticatedUser(UserDetails.class)
                        .map(user -> {
                            Button logout = new Button("Logout", click ->
                                    this.authContext.logout());

                            Span loggedUser = new Span("Welcome " +
                                    userService.findUserByLogin(user.getUsername()).getName());

                            return new HorizontalLayout(logo, loggedUser, logout);
                        }).orElseGet(() -> new HorizontalLayout(logo));

        addToNavbar(header);
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();

        if (notificationCounter == 1) {
            notificationCounter++;
            Notification welcome = Notification
                    .show("Вход совершен пользователем " + currentUser.getUsername());
            welcome.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
            welcome.setPosition(Notification.Position.TOP_CENTER);
            welcome.setDuration(7000);
        }

        //исключение функции добавления пользователя при входе с ролью USER
        if (currentUser.getAuthorities().contains(UserEntityRole.ROLE_USER)) {
            addUser.setVisible(false);
            if (notificationCounter == 2) {
                notificationCounter++;
                Notification notification = Notification
                        .show("права администратора отсутствуют");
                notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
                notification.setPosition(Notification.Position.BOTTOM_CENTER);
                notification.setDuration(7000);
            }
        } else {
            if (notificationCounter == 2) {
                notificationCounter++;
                Notification notification = Notification
                        .show("функции администратора доступны");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.BOTTOM_CENTER);
                notification.setDuration(7000);
            }
        }
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Users list");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    //Добавление страниц просмотра зарегистрированных пользователей и создание нового для внесения в БД
    //на боковую панель навигации
    private SideNav createNavigation() {
        SideNav nav = new SideNav();
        //создание вкладки со списком пользователей
        SideNavItem users = new SideNavItem("Users", MainView.class, LineAwesomeIcon.FILTER_SOLID.create());

        int counter = userService.findAllUsers().size();
        Span inboxCounter = new Span(String.valueOf(counter));
        inboxCounter.getElement().getThemeList().add("badge pill");
        inboxCounter.getElement().setAttribute("aria-label",
                "users count");
        users.setSuffixComponent(inboxCounter);

        //создание вкладки с добавлением пользователя
        addUser = new SideNavItem("New user", NewUserView.class, LineAwesomeIcon.USER.create());

        nav.addItem(users);
        nav.addItem(addUser);
        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}

