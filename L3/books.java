import java.util.*;

//  MODEL 

class Book {
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

// VIEW 


class BookView {
    public void printBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            for (Book book : books) {
                System.out.println(book);
            }
        }
    }

    public void printMenu() {
        System.out.println("\n=== Menu ===");
        System.out.println("1. List books by author");
        System.out.println("2. List books by publisher");
        System.out.println("3. List books published after a year");
        System.out.println("4. Sort books by publisher");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }
}

// CONTROLLER 


class BookController {
    private List<Book> books;
    private BookView view;

    public BookController(List<Book> books, BookView view) {
        this.books = books;
        this.view = view;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        view.printBooks(books); 
        while (true) {
            view.printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); 
            switch (choice) {
                case 1:
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    view.printBooks(filterByAuthor(author));
                    break;
                case 2:
                    System.out.print("Enter publisher: ");
                    String publisher = scanner.nextLine();
                    view.printBooks(filterByPublisher(publisher));
                    break;
                case 3:
                    System.out.print("Enter year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();
                    view.printBooks(filterAfterYear(year));
                    break;
                case 4:
                    sortByPublisher();
                    System.out.println("Books sorted by publisher:");
                    view.printBooks(books);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private List<Book> filterByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getAuthor().equalsIgnoreCase(author)) {
                result.add(book);
            }
        }
        return result;
    }

    private List<Book> filterByPublisher(String publisher) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getPublisher().equalsIgnoreCase(publisher)) {
                result.add(book);
            }
        }
        return result;
    }

    private List<Book> filterAfterYear(int year) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getYear() > year) {
                result.add(book);
            }
        }
        return result;
    }

    private void sortByPublisher() {
        books.sort(Comparator.comparing(Book::getPublisher));
    }
}

// MAIN
public class books {
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

        BookView view = new BookView();
        BookController controller = new BookController(books, view);
        controller.start();
    }
}