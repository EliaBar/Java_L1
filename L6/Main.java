import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class SimpleTranslator {
    private Map<String, String> dictionary;

    public SimpleTranslator() {
        dictionary = new HashMap<>();
    }

    public void addWordPair(String englishWord, String ukrainianWord) {
        dictionary.put(englishWord.toLowerCase(), ukrainianWord.toLowerCase());
    }

    public String translatePhrase(String phrase) {
        StringBuilder translatedPhrase = new StringBuilder();
        
        String[] words = phrase.split("\\s+");

        for (String word : words) {
            String cleanWord = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
            String punctuation = word.replaceAll("[a-zA-Z]", "");

            String translation;
            if (dictionary.containsKey(cleanWord)) {
                translation = dictionary.get(cleanWord);
            } else {
                translation = cleanWord.isEmpty() ? "" : "[" + cleanWord + "]";
            }
            
            translatedPhrase.append(translation).append(punctuation).append(" ");
        }

        return translatedPhrase.toString().trim();
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SimpleTranslator translator = new SimpleTranslator();

        translator.addWordPair("hello", "привіт");
        translator.addWordPair("world", "світ");
        translator.addWordPair("java", "джава");
        translator.addWordPair("is", "це");
        translator.addWordPair("language", "мова");
        translator.addWordPair("programming", "програмування");
        translator.addWordPair("cat", "кіт");
        translator.addWordPair("dog", "пес");

        System.out.println("=== КОНСОЛЬНИЙ ПЕРЕКЛАДАЧ ===");
        System.out.println("Базовий словник завантажено.");

        while (true) {
            System.out.println("\nБажаєте додати нові слова до словника? (y/n):");
            String choice = scanner.nextLine().trim().toLowerCase();
            
            if (choice.equals("n")) {
                break;
            } else if (choice.equals("y")) {
                System.out.print("Введіть слово англійською: ");
                String eng = scanner.nextLine().trim();
                System.out.print("Введіть переклад українською: ");
                String ukr = scanner.nextLine().trim();

                if (!eng.isEmpty() && !ukr.isEmpty()) {
                    translator.addWordPair(eng, ukr);
                    System.out.println("Пару додано!");
                } else {
                    System.out.println("Помилка: слова не можуть бути порожніми.");
                }
            }
        }

        System.out.println("\n--- ЕТАП ПЕРЕКЛАДУ ---");
        System.out.println("Введіть фразу англійською мовою (наприклад: 'Hello world is Java'):");
        String inputPhrase = scanner.nextLine();

        String result = translator.translatePhrase(inputPhrase);
        
        System.out.println("\nРезультат перекладу:");
        System.out.println(result);
        scanner.close();
    }
}