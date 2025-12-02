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

                int totalRequests = 0;
                int yandexBotCount = 0;
                int googleBotCount = 0;

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.length() > 1024) {
                        reader.close();
                        throw new RuntimeException("Обнаружена строка длиннее 1024 символов (длина: " + line.length() + ")");
                    }


                    int lastQuote = line.lastIndexOf('"');
                    int secondLastQuote = line.lastIndexOf('"', lastQuote - 1);

                    if (secondLastQuote == -1 || lastQuote <= secondLastQuote) {
                        totalRequests++;
                        continue;
                    }

                    String userAgent = line.substring(secondLastQuote + 1, lastQuote);


                    int openParen = userAgent.indexOf('(');
                    int closeParen = userAgent.indexOf(')', openParen);
                    if (openParen == -1 || closeParen == -1) {
                        totalRequests++;
                        continue;
                    }

                    String firstBrackets = userAgent.substring(openParen + 1, closeParen);
                    String[] parts = firstBrackets.split(";", -1);

                    if (parts.length >= 2) {
                        String fragment = parts[1].trim();
                        if (!fragment.isEmpty()) {
                            int slashIndex = fragment.indexOf('/');
                            String botName = (slashIndex != -1) ? fragment.substring(0, slashIndex) : fragment;

                            if ("YandexBot".equals(botName)) {
                                yandexBotCount++;
                            } else if ("Googlebot".equals(botName)) {
                                googleBotCount++;
                            }
                        }
                    }

                    totalRequests++;
                }

                reader.close();

                double yandexRatio = totalRequests > 0 ? (double) yandexBotCount / totalRequests : 0.0;
                double googleRatio = totalRequests > 0 ? (double) googleBotCount / totalRequests : 0.0;

                System.out.printf("Доля запросов от YandexBot: %.2f%% (%d из %d)%n",
                        yandexRatio * 100, yandexBotCount, totalRequests);
                System.out.printf("Доля запросов от Googlebot: %.2f%% (%d из %d)%n",
                        googleRatio * 100, googleBotCount, totalRequests);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}