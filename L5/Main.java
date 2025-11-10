import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

// =================== MODEL ===================
class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String author;
    private String publisher;
    private int year;
    private int pages;
    private double price;

    public Book(String title, String author, String publisher, int year, int pages, double price) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.pages = pages;
        this.price = price;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public int getYear() { return year; }
    public int getPages() { return pages; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %d | %d pages | $%.2f",
                title, author, publisher, year, pages, price);
    }
}

// =================== VIEW ===================
class BookView {
    private final BookController controller; 
    private final Scanner scanner;

    public BookView(BookController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void printBooks(List<String> formattedBooks) {
        if (formattedBooks == null || formattedBooks.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        for (String s : formattedBooks) {
            System.out.println(s);
        }
    }

    public void printMenu() {
        System.out.println("\n=== Menu ===");
        System.out.println("1. List books by author");
        System.out.println("2. List books by publisher");
        System.out.println("3. List books published after a year");
        System.out.println("4. Sort books by publisher");
        System.out.println("5. Save books to file (serialization)");
        System.out.println("6. Load books from file (serialization)");
        System.out.println("7. Encrypt a text file (shift-by-key) -> produce encrypted file");
        System.out.println("8. Decrypt a file created by option 7");
        System.out.println("9. Find line with maximum number of words in a text file");
        System.out.println("10. Count HTML tags at URL (lexicographic order)");
        System.out.println("11. Count HTML tags at URL (order by frequency ascending)");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }
    
    private int readIntSafe() {
        while (true) {
            String s = scanner.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException ex) {
                System.out.print("Please enter a valid integer: ");
            }
        }
    }

    public void start() {
        boolean running = true;
        System.out.println("Initial books:");
        printBooks(controller.getAllBooksFormatted()); 

        while (running) {
            printMenu();
            int choice = readIntSafe();

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter author: ");
                        String author = scanner.nextLine();
                        printBooks(controller.filterByAuthorFormatted(author));
                        break;
                    case 2:
                        System.out.print("Enter publisher: ");
                        String publisher = scanner.nextLine();
                        printBooks(controller.filterByPublisherFormatted(publisher));
                        break;
                    case 3:
                        System.out.print("Enter year: ");
                        int year = readIntSafe();
                        printBooks(controller.filterAfterYearFormatted(year));
                        break;
                    case 4:
                        controller.sortByPublisher();
                        System.out.println("Books sorted by publisher:");
                        printBooks(controller.getAllBooksFormatted());
                        break;
                    case 5:
                        System.out.print("Enter path to save file (e.g. books.dat): ");
                        String outPath = scanner.nextLine();
                        controller.saveBooks(outPath);
                        System.out.println("Saved successfully to " + outPath);
                        break;
                    case 6:
                        System.out.print("Enter path to load file (e.g. books.dat): ");
                        String inPath = scanner.nextLine();
                        controller.loadBooks(inPath);
                        System.out.println("Loaded successfully. Current dataset replaced.");
                        printBooks(controller.getAllBooksFormatted());
                        break;
                    case 7:
                        System.out.print("Enter path of source text file to encrypt: ");
                        String srcEnc = scanner.nextLine();
                        System.out.print("Enter path of destination encrypted file: ");
                        String dstEnc = scanner.nextLine();
                        System.out.print("Enter single character key (e.g. 'K'): ");
                        String keyStr = scanner.nextLine();
                        if (keyStr.isEmpty()) {
                            System.out.println("Empty key. Cancelled.");
                            break;
                        }
                        int keyInt = Integer.parseInt(keyStr); 

                        try (
                             FileReader fr = new FileReader(srcEnc);
                             EncryptWriter ew = new EncryptWriter(new FileWriter(dstEnc), keyInt)) {
                            
                            char[] buf = new char[4096]; 
                            int r;
                            while ((r = fr.read(buf)) != -1) {
                                ew.write(buf, 0, r);
                            }
                            System.out.println("Encryption finished to " + dstEnc);
                        } catch (IOException ex) {
                            System.out.println("Error during encryption: " + ex.getMessage());
                        }
                        break;
                    case 8:
                        System.out.print("Enter path of encrypted file: ");
                        String encPath = scanner.nextLine();
                        System.out.print("Enter path for decrypted output: ");
                        String decOut = scanner.nextLine();
                        System.out.print("Enter single character key (same as used to encrypt): ");
                        String keyStr2 = scanner.nextLine();
                        if (keyStr2.isEmpty()) {
                            System.out.println("Empty key. Cancelled.");
                            break;
                        }
                        int keyInt2 = Integer.parseInt(keyStr2);
                        
                        try (
                             FileReader fr = new FileReader(encPath);
                             DecryptReader dr = new DecryptReader(fr, keyInt2);
                             FileWriter fw = new FileWriter(decOut)) {
                            
                            char[] buf = new char[4096]; 
                            int r;
                            while ((r = dr.read(buf)) != -1) {
                                fw.write(buf, 0, r);
                            }
                            System.out.println("Decryption finished to " + decOut);
                        } catch (IOException ex) {
                            System.out.println("Error during decryption: " + ex.getMessage());
                        }
                        break;
                    case 9:
                        System.out.print("Enter text file path to analyze: ");
                        String path = scanner.nextLine();
                        try {
                            String best = FileManager.lineWithMaxWords(path);
                            if (best == null) System.out.println("File empty or not found lines.");
                            else System.out.println("Line with max words:\n" + best);
                        } catch (IOException ex) {
                            System.out.println("Error reading file: " + ex.getMessage());
                        }
                        break;
                    case 10:
                        System.out.print("Enter URL (including http/https): ");
                        String url = scanner.nextLine();
                        try {
                            Map<String, Integer> counts = TagCounter.countTagsFromURL(url);
                            System.out.println("Tags in lexicographic order:");
                            TagCounter.printLexicographic(counts);
                        } catch (IOException ex) {
                            System.out.println("Error fetching/parsing URL: " + ex.getMessage());
                        }
                        break;
                    case 11:
                        System.out.print("Enter URL (including http/https): ");
                        String url2 = scanner.nextLine();
                        try {
                            Map<String, Integer> counts2 = TagCounter.countTagsFromURL(url2);
                            System.out.println("Tags ordered by frequency:");
                            TagCounter.printByFrequency(counts2);
                        } catch (IOException ex) {
                            System.out.println("Error fetching/parsing URL: " + ex.getMessage());
                        }
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception ex) { 
                System.out.println("An unexpected application error occurred: " + ex.getMessage());
            }
        }
    }
}

// =================== FILE MANAGER ===================
class FileManager {
    public static void saveBooks(List<Book> books, String filepath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filepath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(books);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Book> loadBooks(String filepath) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(filepath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                return (List<Book>) obj;
            } else {
                throw new IOException("File does not contain a List<Book> object.");
            }
        }
    }

    public static String lineWithMaxWords(String filepath) throws IOException {
        String bestLine = null;
        int maxWords = -1;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.trim().isEmpty() ? new String[0] : line.trim().split("\\s+");
                if (tokens.length > maxWords) {
                    maxWords = tokens.length;
                    bestLine = line;
                }
            }
        }
        return bestLine;
    }
}

// =================== ENCRYPTION FILTER STREAMS ===================
class EncryptWriter extends FilterWriter {
    private final int key;

    protected EncryptWriter(Writer out, int key) {
        super(out);
        this.key = key;
    }

    @Override
    public void write(int c) throws IOException {
        super.write((char) (c + key));
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        char[] buf = new char[len];
        for (int i = 0; i < len; i++) buf[i] = (char) (cbuf[off + i] + key);
        super.write(buf, 0, len);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
        write(str.toCharArray(), off, len);
    }
}

class DecryptReader extends FilterReader {
    private final int key;

    protected DecryptReader(Reader in, int key) {
        super(in);
        this.key = key;
    }

    @Override
    public int read() throws IOException {
        int c = super.read();
        if (c == -1) return -1;
        return (char) (c - key);
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int r = super.read(cbuf, off, len);
        if (r == -1) return -1;
        for (int i = off; i < off + r; i++) cbuf[i] = (char) (cbuf[i] - key);
        return r;
    }
}

// =================== TAG COUNTER ===================
class TagCounter {
    public static Map<String, Integer> countTagsFromURL(String urlString) throws IOException {
        StringBuilder sb = new StringBuilder();
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line).append("\n");
        }
        String html = sb.toString();

        Pattern p = Pattern.compile("</?\\s*([a-zA-Z0-9:-]+)");
        Matcher m = p.matcher(html);
        Map<String, Integer> counts = new HashMap<>();
        while (m.find()) {
            String tag = m.group(1).toLowerCase();
            counts.put(tag, counts.getOrDefault(tag, 0) + 1);
        }
        return counts;
    }

    public static void printLexicographic(Map<String, Integer> map) {
        map.keySet().stream().sorted()
                .forEach(k -> System.out.printf("%s : %d%n", k, map.get(k)));
    }

    public static void printByFrequency(Map<String, Integer> map) {
        map.entrySet().stream()
                .sorted(Comparator.<Map.Entry<String, Integer>, Integer>comparing(e -> e.getValue())
                        .thenComparing(Map.Entry::getKey))
                .forEach(e -> System.out.printf("%s : %d%n", e.getKey(), e.getValue()));
    }
}

// =================== CONTROLLER ===================
class BookController {
    private List<Book> books;

    public BookController(List<Book> books) {
        this.books = books;
    }
    
    private List<String> formatBooks(List<Book> booksToFormat) {
        if (booksToFormat == null) return Collections.emptyList();
        return booksToFormat.stream()
                    .map(Book::toString)
                    .collect(Collectors.toList());
    }

    public List<String> getAllBooksFormatted() {
        return formatBooks(books);
    }

    public List<String> filterByAuthorFormatted(String author) {
        List<Book> filtered = books.stream()
                                   .filter(b -> b.getAuthor().equalsIgnoreCase(author))
                                   .collect(Collectors.toList());
        return formatBooks(filtered);
    }

    public List<String> filterByPublisherFormatted(String publisher) {
        List<Book> filtered = books.stream()
                                   .filter(b -> b.getPublisher().equalsIgnoreCase(publisher))
                                   .collect(Collectors.toList());
        return formatBooks(filtered);
    }

    public List<String> filterAfterYearFormatted(int year) {
        List<Book> filtered = books.stream()
                                   .filter(b -> b.getYear() > year)
                                   .collect(Collectors.toList());
        return formatBooks(filtered);
    }

    public void sortByPublisher() {
        books.sort(Comparator.comparing(Book::getPublisher));
    }

    public void saveBooks(String outPath) throws IOException {
        FileManager.saveBooks(books, outPath);
    }
    
    public void loadBooks(String inPath) throws IOException, ClassNotFoundException {
        List<Book> loaded = FileManager.loadBooks(inPath);
        if (loaded != null) {
            this.books = loaded; 
        }
    }
}

// =================== MAIN ===================
public class Main {
    public static void main(String[] args) {
        List<Book> books = new ArrayList<>(Arrays.asList(
                new Book("Kobzar", "Taras Shevchenko", "Dnipro", 1840, 350, 15.0),
                new Book("Forest Song", "Lesya Ukrainka", "Bukva", 1912, 200, 12.0),
                new Book("The Stone Host", "Lesya Ukrainka", "Bukva", 1907, 150, 10.0),
                new Book("Valse Melancholique", "Ivan Franko", "Svit", 1896, 300, 18.0),
                new Book("Zakhar Berkut", "Ivan Franko", "Dnipro", 1883, 400, 20.0),
                new Book("Maria", "Ulas Samchuk", "Sofia", 1934, 280, 14.0),
                new Book("The Red Viburnum", "Pavlo Zahrebelnyi", "Bukva", 1965, 320, 16.0),
                new Book("Stolen Happiness", "Ivan Franko", "Svit", 1892, 220, 12.0),
                new Book("On the Edge", "Mykhailo Kotsiubynsky", "Dnipro", 1900, 180, 11.0),
                new Book("Shadows of Forgotten Ancestors", "Mykhailo Kotsiubynsky", "Sofia", 1912, 210, 13.0)
        ));

        Scanner scanner = new Scanner(System.in);
        BookController controller = new BookController(books);
        BookView view = new BookView(controller, scanner);
        view.start();
        scanner.close();
    }
}
