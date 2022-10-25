# ExchangeCurrencies

Test exchange currencies app -  Android приложение, показывающее актуальные курсы валют по запросу


### Стек технологий:

* MVVM
* Hilt
    * Для внедрения зависимостей. Dependency Injection (инъекция или внедрение зависимости) — это зависимость одного класса от другого. т.е. для полноценной работы одного класса нужна инициализация другого(их) класса. Hilt это библиотека которая помогает реализовать «внедрение зависимости. Hilt значительно упрощает жизнь разработчикам, помогая им как можно меньше писать шаблонного кода.
    * вложенный элемент 2.2
* Coroutines
    * Kotlin coroutines - блоки кода, которые могут выполняться параллельно с остальным кодом и работать асинхронно друг с другом. В результате программа выполняет несколько функций одновременно.
* Retrofit + Moshi
    * Retrofit самая популярная и удобная библиотека для работы с сетью. С ее помощью можно очень удобно парсить json'ы, отправлять HTTP запросы и получать ответы. 
    Библиотекой удобно пользоваться для запроса к различным веб-сервисам с командами GET, POST, PUT, DELETE. Может работать в асинхронном режиме, что избавляет от лишнего кода.
    Почему Moshi, а не Gson? Причин много, одна из них - удобства адаптеров.
* Compose
Почему compose? Переходить с xml на compose то же самое, что с Android на iPhone. Назад возвращаться не захочется)) Мало того, что compose очень педалируется гуглом, так он еще и очень удобный и неприхотливый. По моему опыту работы с ним, он способен на многое)) Даже на кастомные сложные UI элементы. 
Например вместо убогого написания recycler view, адаптера для него и прочего, можно просто использовать LazyColumn от compose, который внутри себя и содержит тот самый recycler и не знать горя. 
* Многомодульность. 
В проекте 4 модуля:
1. app
2. core - для общих компонентов
3. feature - для фичи
4. navigation - для навигации.

Следование принципам SOLID


### Что можно было бы доработать в данном приложении?

1. Во первых, доработать логику работы иконки(избранное) элемента из списка. 
2. Добавить диалог, предупреждаюший перед удалением всех элементов их избранного. 
3. Добавить обработку ошибок как от сервера так и на стороне юзера.
4. Было бы неплохо добавить слежение за курсами в режиме лайв даты, но этого не позволяет используемое апи.
5. Подкрутить gradle таски типа detekt, ktlint и тд. Не хватило времени.
6. Подкрутить CI/CD. yaml файл есть, в гите настроить не успел 


[device-2022-10-25-033748.webm](https://user-images.githubusercontent.com/44145716/197661896-b5e99d1e-43d0-4e04-b8f6-8775860da228.webm)
