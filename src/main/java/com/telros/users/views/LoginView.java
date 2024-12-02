package com.telros.users.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;

//класс Vaadin для Login формы
@PageTitle("Authentication")
@Route(value = "login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver{
    private final LoginForm login = new LoginForm();
    private final transient AuthenticationContext authContext;
    public static int notificationCounter;

    public LoginView(AuthenticationContext authContext){
        this.authContext = authContext;
        System.out.println("> LoginView constructor started");
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        login.setAction("login");
        add(new H1("Telros users"), login);
        notificationCounter = 1;
    }
    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        System.out.println("> class LoginView, beforeEnter method started");
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}
