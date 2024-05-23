// Продвинутая работа с исключениями в Java

// Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке, разделенные пробелом:
// Фамилия Имя Отчество датарождения номертелефона пол
// Форматы данных:
// фамилия, имя, отчество - строки
// дата_рождения - строка формата dd.mm.yyyy
// номер_телефона - целое беззнаковое число без форматирования
// пол - символ латиницей f или m.

// Приложение должно проверить введенные данные по количеству. Если количество не совпадает с требуемым, вернуть код ошибки, обработать его и показать пользователю сообщение, что он ввел меньше и больше данных, чем требуется.

// Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры. Если форматы данных не совпадают, нужно бросить исключение, соответствующее типу проблемы. Можно использовать встроенные типы java и создать свои. Исключение должно быть корректно обработано, пользователю выведено сообщение с информацией, что именно неверно.

// Если всё введено и обработано верно, должен создаться файл с названием, равным фамилии, в него в одну строку должны записаться полученные данные, вида

// <Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>

// Однофамильцы должны записаться в один и тот же файл, в отдельные строки.

// Не забудьте закрыть соединение с файлом.

// При возникновении проблемы с чтением-записью в файл, исключение должно быть корректно обработано, пользователь должен увидеть стектрейс ошибки.

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class AdvancedExceptionHandling {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите данные (Фамилия Имя Отчество датарождения номертелефона пол):");
        String input = scanner.nextLine();

        try {
            processInput(input);
        } catch (InvalidInputException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлом:");
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void processInput(String input) throws InvalidInputException, IOException {
        String[] parts = input.split(" ");

        if (parts.length != 6) {
            throw new InvalidInputException("Некорректное количество данных. Ожидалось 6, получено: " + parts.length);
        }

        String lastName = parts[0];
        String firstName = parts[1];
        String middleName = parts[2];
        String birthDate = parts[3];
        String phoneNumber = parts[4];
        String gender = parts[5];

        validateBirthDate(birthDate);
        validatePhoneNumber(phoneNumber);
        validateGender(gender);

        String data = String.join(" ", lastName, firstName, middleName, birthDate, phoneNumber, gender);
        writeFile(lastName, data);
    }

    private static void validateBirthDate(String birthDate) throws InvalidInputException {
        if (!birthDate.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
            throw new InvalidInputException(
                    "Неверный формат даты рождения. Ожидалось dd.mm.yyyy, получено: " + birthDate);
        }
    }

    private static void validatePhoneNumber(String phoneNumber) throws InvalidInputException {
        if (!phoneNumber.matches("\\d+")) {
            throw new InvalidInputException(
                    "Неверный формат номера телефона. Ожидалось целое число, получено: " + phoneNumber);
        }
    }

    private static void validateGender(String gender) throws InvalidInputException {
        if (!gender.equals("f") && !gender.equals("m")) {
            throw new InvalidInputException("Неверный формат пола. Ожидалось 'f' или 'm', получено: " + gender);
        }
    }

    private static void writeFile(String lastName, String data) throws IOException {
        String fileName = lastName + ".txt";
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName), StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.write(data);
            writer.newLine();
        }
    }
}

class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}
