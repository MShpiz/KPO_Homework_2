# ДЗ 2 по КПО
*Выпонила:* Панфилова Мария, БПИ226
## База данных
Для работы программы необходимо создать postgres сервер hw2, с базой данных hw2. С логин и пароль можно записать [сюда](https://github.com/MShpiz/KPO_Homework_2/blob/review/Homework2/src/main/resources/application.properties)
## Паттерны
В проекте были использованы паттерны Одиночка, Фасад, Состояние, Строитель.
Одиночка для работы с базой данных, потому что в котлине он потоко-безопасный, а лучших альтернатив я не знаю. 
Фасад - то что этот проект сделан на кторе, является реализацией данного паттерна, тк пользователь обращается к интерфейсу ktor а не работает на прямую с системой.
Состояние - для того чтобы определять поведение заказа в разных состояниях (не приготовленный, готовящийся, приготовленный).
Строитель - для сборки заказа.

## Как пользоваться
### get запросы
/menu - получить меню
### post запросы
/addMealsToMenu - добавляет блюдо в меню (доступно только администраторам)
/cancel_order - отменить заказ (доступно только посетителям)
/addMealsToMenu - добавляет блюдо в меню (доступно только администраторам)
/activity - получить информацию об активности пользователей (доступно только администраторам)
/income - получить полную сумму дохода с момента открытия (доступно только администраторам)
/changePrice - изменить цену блюда (доступно только администраторам)
/increase_meal_amount - увеличивает количество блюд на 1 (доступно только администраторам)
/login - общая авторизация с помощью логина и хеша пароля (число), в результате отдает токен, который далее используется для отправки запросов.
/logout - выход из аккаунта
/orderMeal - добавляет блюдо в заказ (доступно только посетителям)
/pay - оплата заказа (доступно только посетителям)
/place_order -отправляет заказ текущего пользователя готовиться (доступно только посетителям)
/register - регистрация пользователей (если role=true - регистрируется админ, иначе - посетитель)
/remove_meal_from_menu - удаляет блюдо из меню (доступно только администраторам)
/remove_meal_from_order - удаляет блюдо из заказа текущего пользователя (доступно только администраторам)
