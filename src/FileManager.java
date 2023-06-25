import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileManager {

    private FileWriter gameReportWriter;
    private Scanner gameReportReader;
    private File gameReportFile;

    public FileManager() {

        try {

            gameReportFile = new File("game_report.txt");
            gameReportWriter = new FileWriter("game_report.txt", true);
            gameReportReader = new Scanner(gameReportFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeGameTitle(String targetSequence) {

        int gameCounter = getGameCounter() + 1;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime currentTime = LocalDateTime.now();

        try {

            gameReportWriter.write("Game №" + gameCounter + " " + formatter.format(currentTime)
                    + " " + "Загаданная строка " + targetSequence + "\n");
            gameReportWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Game №" + gameCounter + " " + formatter.format(currentTime));
    }

    public void writeWinLog(int tryCount) {

        String tryDefinition = "";
        String count = String.valueOf(tryCount);

        if (count.length() >= 2 && count.substring(count.length() - 2).startsWith("1")) {
            tryDefinition = " попыток.";
        } else {
            String numberIdentifier = count.substring(count.length() - 1);
            switch (numberIdentifier) {
                case "1" -> tryDefinition = " попытку.";
                case "2", "3", "4" -> tryDefinition = " попытки.";
                case "5", "6", "7", "8", "9" -> tryDefinition = " попыток.";
            }
        }

        try {
            gameReportWriter.write("Строка была угадана за " + tryCount + tryDefinition + "\n");
            gameReportWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeIntermediateLog(int cowCount, int bullCount, String userSequence) {

        String cowResult = "";
        String bulResult = "";

        switch (cowCount) {
            case 0 -> cowResult = "коров";
            case 1 -> cowResult = "корова";
            case 2, 3, 4 -> cowResult = "коровы";
        }

        switch (bullCount) {
            case 0 -> bulResult = "быков";
            case 1 -> bulResult = "бык";
            case 2, 3, 4 -> bulResult = "быка";
        }

        try {
            gameReportWriter.write("Запрос: " + userSequence + " Ответ: " +
                    cowCount + " " + cowResult + " " +
                    bullCount + " " + bulResult + "\n");
            gameReportWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Запрос: " + userSequence + " Ответ: " +
                cowCount + " " + cowResult + " " +
                bullCount + " " + bulResult);
    }

    public int getGameCounter() {

        Pattern pattern = Pattern.compile("\\b(Game)[ ]\\B(№)\\d+");
        String lastGameTitle = "";

        if (!gameReportReader.hasNext()) {
            return 0;
        }
        while (gameReportReader.hasNext()) {
            String input = gameReportReader.nextLine();
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                lastGameTitle = matcher.group();
            }
        }
        return Integer.parseInt(lastGameTitle.substring(lastGameTitle.indexOf(" ") + 2));
    }
}
