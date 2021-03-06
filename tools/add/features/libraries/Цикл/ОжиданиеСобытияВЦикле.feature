# language: ru

@IgnoreOnOFBuilds
@IgnoreOn82Builds
@IgnoreOnWeb


@tree



Функциональность: Ожидание события в цикле
	Как разработчик
	Хочу иметь возможность выполнять несколько шагов в течение нужного числа секунд
	И завершить выполнение этих шагов при наступлении нужного мне события
	Чтобы выполнять сложные бизнес-процессы

Контекст:
	Дано Я запускаю сценарий открытия TestClient или подключаю уже существующий с закрытием всех окон кроме "* Vanessa ADD"



Сценарий: Ожидание события в цикле и выход из него по событию
	Когда Я открываю VanessaADD в режиме TestClient со стандартной библиотекой
	И В поле с именем "КаталогФичСлужебный" я указываю путь к служебной фиче "ДляПроверкиОжиданиеВыходаИзЦиклаПоСобытию"
	И Я нажимаю на кнопку перезагрузить сценарии в Vanessa-ADD TestClient
	И Я нажимаю на кнопку выполнить сценарии в Vanessa-ADD TestClient

	И в таблице "ДеревоТестов" я перехожу к строке:
		| 'Наименование'                                                     |
		| 'Для проверки ожидание события в цикле и выход из него по событию' |
	И в таблице "ДеревоТестов" я разворачиваю текущую строку


	И     в таблице "ДеревоТестов" я перехожу к строке:
		| 'Наименование'                          |
		| 'Если "$СлужебнаяПеременная$<>5" Тогда' |
	И     в таблице "ДеревоТестов" я разворачиваю текущую строку

	И Пауза 1

	И в таблице "ДеревоТестов" я разворачиваю строку:
		| 'Наименование'                     |
		| 'И в течение 20 секунд я выполняю' |
	И в таблице "ДеревоТестов" я разворачиваю строку:
		| 'Наименование'                         |
		| 'Если "$СлужебнаяПеременная$=5" Тогда' |


	Тогда таблица "ДеревоТестов" стала равной:
		| 'Статус'  | 'Наименование'                                                                                  |
		| ''        | 'ДляПроверкиОжиданиеВыходаИзЦиклаПоСобытию.feature'                                             |
		| ''        | 'ДляПроверкиОжиданиеВыходаИзЦиклаПоСобытию'                                                     |
		| 'Success' | 'Для проверки ожидание события в цикле и выход из него по событию'                              |
		| 'Success' | 'И Я запоминаю значение выражения "0" в переменную "СлужебнаяПеременная"'                       |
		| 'Success' | 'И в течение 20 секунд я выполняю'                                                              |
		| 'Success' | 'И Я запоминаю значение выражения "$СлужебнаяПеременная$+1" в переменную "СлужебнаяПеременная"' |
		| 'Success' | 'Если "$СлужебнаяПеременная$=5" Тогда'                                                          |
		| 'Success' | 'Тогда я прерываю цикл'                                                                         |
		| 'Success' | 'Если "$СлужебнаяПеременная$<>5" Тогда'                                                         |
		| ''        | 'Тогда я вызываю исключение "Ожидали другое значение у СлужебнаяПеременная"'                    |
