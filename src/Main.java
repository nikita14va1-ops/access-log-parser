import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int correctFileCount = 0;

        while (true) {
            System.out.println("Введите путь к файлу:");
            String path = scanner.nextLine();

            File file = new File(path);
            if (!file.exists() || file.isDirectory()) {
                System.out.println(!file.exists() ? "Файл не существует." : "Указанный путь ведёт к папке.");
                continue;
            }

            System.out.println("Путь указан верно");
            correctFileCount++;
            System.out.println("Это файл номер " + correctFileCount);

            try {
                Statistics stats = new Statistics();
                BufferedReader reader = new BufferedReader(new FileReader(path));
                String line;
                int parsedCount = 0;

                while ((line = reader.readLine()) != null) {
                    if (line.length() > 1024) {
                        reader.close();
                        throw new RuntimeException("Обнаружена строка длиннее 1024 символов (длина: " + line.length() + ")");
                    }

                    try {
                        LogEntry entry = new LogEntry(line);
                        stats.addEntry(entry);
                        parsedCount++;
                    } catch (Exception e) {
                        // Пропускаем некорректные строки
                        continue;
                    }
                }
                reader.close();

                System.out.println("Успешно обработано строк: " + parsedCount);
                double trafficRate = stats.getTrafficRate();
                System.out.printf("Средний объём трафика за час: %.2f байт/час%n", trafficRate);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}