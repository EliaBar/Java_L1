import java.util.ArrayList;
import java.util.Scanner;

public class L1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a string of words:");
        String input = scanner.nextLine();

        String[] words = input.split("\\s+");
        ArrayList<String> result = new ArrayList<>();

        for (String word : words) {
            if (word.matches("[a-zA-Z]+")) {
                int vowels = 0;
                int consonants = 0;
                for (char c : word.toLowerCase().toCharArray()) {
                    if ("aeiouy".indexOf(c) >= 0) {
                        vowels++;
                    } else {
                        consonants++;
                    }
                }
                if (vowels == consonants) {
                    result.add(word);
                }
            }
        }
        String[] output = result.toArray(new String[0]);

        System.out.println("Words with an equal number of vowels and consonants:");
        for (String s : output) {
            System.out.println(s);
        }
        scanner.close();
    }
}