# Hangman

```
Input symbol: d
Maximum attempts: 6.
Number of used attempts: 2.
  -----
  |   |
  |   0
  |   |
  |   |
  |
  |
  |
 ---
You doesn't guess the letter.
C_C_A
BD
```

## Описание
Добро пожаловать в игру "Виселица"! Это классическая текстовая игра, в которой игроку нужно угадывать слово по буквам. В этой версии игры добавлен ряд улучшений, таких как возможность выбора категории слов, различные уровни сложности и система подсказок.

## Установка и запуск

### Требования

Для запуска игры необходима установка следующих зависимостей:
- Java 22+
- Maven (для сборки проекта)

### Установка

1. Склонируйте репозиторий с игрой:
    ```bash
    git clone https://github.com/vihlancevk/Hangman.git
    ```
2. Перейдите в директорию проекта:
    ```bash
    cd Hangman
    ```
3. Соберите проект с помощью Maven:
    ```bash
    mvn clean install
	mvn dependency:copy-dependencies
    ```
4. Запустите:
   ```bash
   java -cp target/java-1.0.0.jar:target/dependency/* backend.academy.Main
   ```
