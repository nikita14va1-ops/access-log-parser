import java.util.Scanner;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        int correctFileCount = 0; // счётчик корректных файлов

        while (true) {
            System.out.println("Введите путь к файлу:");
            String path = new Scanner(System.in).nextLine(); // ← как на первом скриншоте

            File file = new File(path); // ← как на втором скриншоте
            boolean fileExists = file.exists(); // ← как на втором скриншоте
            boolean isDirectory = file.isDirectory(); // ← как на третьем скриншоте

            if (!fileExists || isDirectory) {
                if (!fileExists) {
                    System.out.println("Файл не существует.");
                } else {
                    System.out.println("Указанный путь ведёт к папке, а не к файлу.");
                }
                continue; // продолжаем цикл
            }

            System.out.println("Путь указан верно");
            correctFileCount++;
            System.out.println("Это файл номер " + correctFileCount);
        }
    }
}
