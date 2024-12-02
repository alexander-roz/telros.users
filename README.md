## Users list
#### Web-приложение на Java с использованием SpringBoot и БД PostgreSQL. <br /> front-end полностью написан на Vaadin.
#### При запуске приложения, пользователь с правами доступа USER не может получить доступ к общему списку, <br /> пользователь с правами ADMIN имеет доступ к списку всех пользователей, внесенных в БД, возможность удаления и добавления. <br /> Чтобы активировать кнопку удалить, необходимо выбрать пользователя через checkbox

#### Для запуска приложения необходимо создать пустую postgres базу данных users_list, <br /> и проверить параметры в файле src/main/resources/application.properties, а именно: <br /> datasource: <br />username, password <br /> <br /> а также порты: <br /> server.port (:8080) <br /> datasource.url (:5432).
#### При запуске приложения, в базу данных автоматически вносятся два тестовых пользователя с правами ROLE_ADMIN и ROLE_USER
#### Для входа с правами ROLE_ADMIN <br /> login: admin <br /> password: admin
#### Для входа с правами ROLE_USER <br /> login: user <br /> password: user <br />
#### Вход от имени администратора
![1  login form](https://github.com/user-attachments/assets/94a0c5d4-581c-4970-b5e3-74a85ecf4fce)
#### Отображение списка пользователей
![2  users view](https://github.com/user-attachments/assets/8939a034-7696-4100-98cd-a31fcf65313a)
#### Добавление нового пользователя с правами USER
![3  newuserview](https://github.com/user-attachments/assets/76ffe6d9-343f-4c4a-b54c-f489502144f5)
#### Отображение списка пользователей с учетом внесенного
![4  addedJack](https://github.com/user-attachments/assets/d1ae11c3-13b4-4f96-b007-e0862689bd9a)
#### вход от имени нового пользователя с правами USER
![5  userlogin](https://github.com/user-attachments/assets/79f3ebba-675a-468b-a779-cdf3c54f0a64)
