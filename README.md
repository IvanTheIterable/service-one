## Задача
Сделать два сервиса (путь это будет spring-boot), которые выполняют следующие действия.
### Сервис 1
При получении HTTP GET запроса (endpoint любой) 
- формирует байтовый массив из 200Кб случайных (!) данных
- кладёт их в Redis
### Сервис 2
- забирает массив из Redis
- формирует для данных массива электронную подпись по алгоритму ECDSA (набор параметров prime256-v1)
- кладёт подпись в Redis
### Сервис 1
- забирает подпись из Redis
- проверяет её корректность
- возвращает в ответ на HTTP GET запрос результат проверки
### Дополнительные условия
- После проверки подписи данные в Redis больше не нужны
- Ключи подписи могут быть фиксированными, но должна быть функция их генерации
(грубо говоря, откуда они взялись)
- Response должен содержать корректный JSON, даже в случае возникновения любого
исключения
### Предпочтения (необязательно)
- сборка через Maven
