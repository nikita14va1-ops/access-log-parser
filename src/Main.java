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
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (!fileExists || isDirectory) {
                if (!fileExists) {
                    System.out.println("Файл не существует.");
                } else {
                    System.out.println("Указанный путь ведёт к папке, а не к файлу.");
                }
                continue;
            }

            System.out.println("Путь указан верно");
            correctFileCount++;
            System.out.println("Это файл номер " + correctFileCount);

            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);

                int totalLines = 0;
                int maxLength = 0;
                int minLength = Integer.MAX_VALUE;
                String line;

                while ((line = reader.readLine()) != null) {
                    int length = line.length();


                    if (length > 1024) {
                        throw new RuntimeException("Обнаружена строка длиннее 1024 символов (длина: " + length + ")");
                    }

                    totalLines++;
                    maxLength = Math.max(maxLength, length);
                    minLength = Math.min(minLength, length);
                }

                reader.close();

                if (totalLines == 0) {
                    System.out.println("Файл пустой");
                    minLength = 0;
                }

                System.out.println("Общее количество строк в файле: " + totalLines);
                System.out.println("Длина самой длинной строки: " + maxLength);
                System.out.println("Длина самой короткой строки: " + minLength);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}