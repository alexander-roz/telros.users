## Users list
#### Web-приложение на Java по учету пользователей с использованием SpringBoot и БД PostgreSQL. <br /> front-end полностью написан на Vaadin.
#### При запуске приложения, пользователь с правами доступа USER не может получить доступ к общему списку, <br /> пользователь с правами ADMIN имеет доступ к списку всех пользователей, внесенных в БД, возможность удаления и добавления. <br /> Чтобы активировать кнопку удалить, необходимо выбрать пользователя через checkbox

#### Для запуска приложения необходимо создать пустую postgres базу данных users_list, <br /> и проверить параметры в файле src/main/resources/application.properties, а именно: <br /> datasource: <br />username, password <br /> <br /> а также порты: <br /> server.port (:8080) <br /> datasource.url (:5432).
#### При запуске приложения, в базу данных автоматически вносятся два тестовых пользователя с правами ROLE_ADMIN и ROLE_USER 
#### Для входа с правами ROLE_ADMIN <br /> login: admin <br /> password: admin
#### Для входа с правами ROLE_USER <br /> login: user <br /> password: user <br />
#### Вход от имени администратора
![1  login form](https://github.com/user-attachments/assets/94a0c5d4-581c-4970-b5e3-74a85ecf4fce)
#### Отображение списка пользователей
![2 userslist](https://github.com/user-attachments/assets/83ad8adf-7767-4235-826a-4d66f424e79f)
#### Добавление нового пользователя с правами USER
![3 newuser](https://github.com/user-attachments/assets/f2bed8f6-db2a-4a42-b568-5de5caa1cf38)
#### Отображение списка пользователей с учетом внесенного
![4 newoneadded](https://github.com/user-attachments/assets/8a5638c2-81de-4ec8-8d1d-9d3dcb02fc91)
#### вход от имени нового пользователя с правами USER
![5 newoneentered](https://github.com/user-attachments/assets/16f42c64-4fa2-41e2-a3f0-342b6c6719f5)
