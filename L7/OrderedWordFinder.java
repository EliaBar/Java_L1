import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream; 

public class OrderedWordFinder {
    private static boolean isOrdered(String word) {
        if (word == null || word.length() <= 1) {
            return true;
        }

  
        return IntStream.range(0, word.length() - 1) 
                   .allMatch(i -> {
                       char current = word.charAt(i);    
                       char next = word.charAt(i + 1); 
                       return current <= next; 
                   });
    }

    public static String[] findOrderedWords(String inputString) {
        if (inputString == null || inputString.trim().isEmpty()) {
            return new String[0];
        }

        return Stream.of(inputString.split("\\s+"))
                     .filter(s -> !s.trim().isEmpty())
                     .filter(OrderedWordFinder::isOrdered)
                     .toArray(String[]::new);      
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== ORDERED WORD FINDER (Stream API) ===");
        System.out.println("Enter a line of words (e.g., 'aet cat bill 123'):");
        
        String input = scanner.nextLine();

        if (input.trim().isEmpty()) {
             System.out.println("Input is empty. Exiting.");
             return;
        }
        
        String[] result = findOrderedWords(input);
        
        System.out.println("\nInput String: \"" + input + "\"");
        System.out.println("Ordered Words Found:");
        System.out.println(Arrays.toString(result));

        scanner.close();
    }
}