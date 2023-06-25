import java.util.*;
import java.util.stream.Collectors;

public class GameManager {
    private static FileManager fileManager = new FileManager();
    private static Scanner scanner = new Scanner(System.in);
    private static Boolean isGuessed = false;
    private static int tryCount = 0;

    public void initializeGame() {

        System.out.println("Введите \"Старт\" для начала игры");

        while (true) {
            String userStartAnswer = scanner.nextLine();
            if (userStartAnswer.equals("Старт")) {
                playGame();
                break;
            }
        }
    }

    public static void playGame() {

        String targetSequence = generateSequence();

        fileManager.writeGameTitle(targetSequence);

        System.out.println("Введите последовательность из 4 цифр.\n(В одну строку, без пробелов.)");

        while (!isGuessed) {

            String userSequence = scanner.nextLine();
            if (userSequence.length() == 4) {
                checkNumber(userSequence, targetSequence);
            } else System.out.println("Введена строка некорректного вида!");

        }
    }

    public static void checkNumber(String userSequence, String targetSequence) {
        int cowCount = 0;
        int bullCount = 0;
        tryCount++;
        if (userSequence.equals(targetSequence)) {
            fileManager.writeWinLog(tryCount);
            tryCount = 0;
            isGuessed = true;
            System.out.println("Верно! Загаданная строка: " + targetSequence);
        } else {

            List<String> targetList = Arrays.asList(targetSequence.split(""));
            List<String> userList = Arrays.asList(userSequence.split(""));
            for (int i = 0; i < userList.size(); i++) {
                String number = userList.get(i);
                if (targetList.contains(number)) {
                    if (targetList.indexOf(number) == i) {
                        bullCount++;
                    } else cowCount++;
                }
            }
            fileManager.writeIntermediateLog(cowCount, bullCount, userSequence);
        }
    }

    public static String generateSequence() {

        return new Random().ints(0, 9)
                .distinct()
                .limit(4)
                .boxed()
                .map(Objects::toString)
                .collect(Collectors.joining(""));
    }
}
