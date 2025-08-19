import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

class Driver {
    public static void main(String[] args) {
        
        String csvPath = args.length > 0 ? args[0] : "data.csv";

        DatasetReader reader = new DatasetReader();
        List<Book> books = reader.readBooks(csvPath);

        if (books.isEmpty()) {
            System.out.println("No books loaded. Ensure data.csv exists at: " + csvPath);
            return;
        }

       
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Loaded books: " + books.size());
            System.out.println("Enter a command: countByAuthor | listAuthors | booksByAuthor | booksByRating | pricesByAuthor | exit");
            while (true) {
                System.out.print("> ");
                String cmd = scanner.nextLine().trim();
                if (cmd.equalsIgnoreCase("exit")) break;
                switch (cmd) {
                    case "countByAuthor": {
                        System.out.print("Author name: ");
                        String author = scanner.nextLine();
                        int count = countBooksByAuthor(books, author);
                        System.out.println("Books by '" + author + "': " + count);
                        break;
                    }
                    case "listAuthors": {
                        Set<String> authors = getAllAuthors(books);
                        System.out.println("Total authors: " + authors.size());
                        authors.forEach(System.out::println);
                        break;
                    }
                    case "booksByAuthor": {
                        System.out.print("Author name: ");
                        String author = scanner.nextLine();
                        List<String> titles = getBooksByAuthor(books, author);
                        if (titles.isEmpty()) {
                            System.out.println("No books found for: " + author);
                        } else {
                            titles.forEach(System.out::println);
                        }
                        break;
                    }
                    case "booksByRating": {
                        System.out.print("Rating (e.g., 4.8): ");
                        String r = scanner.nextLine();
                        double rating;
                        try { rating = Double.parseDouble(r); } catch (Exception e) { System.out.println("Invalid rating"); break; }
                        List<Book> rated = getBooksByRating(books, rating);
                        System.out.println("Found: " + rated.size());
                        rated.stream()
                             .sorted(Comparator.comparing(Book::getTitle))
                             .forEach(b -> System.out.println(b.getTitle() + " by " + b.getAuthor()));
                        break;
                    }
                    case "pricesByAuthor": {
                        System.out.print("Author name: ");
                        String author = scanner.nextLine();
                        Map<String, Integer> priceMap = getBookPricesByAuthor(books, author);
                        if (priceMap.isEmpty()) {
                            System.out.println("No books found for: " + author);
                        } else {
                            priceMap.forEach((title, price) -> System.out.println(title + " : $" + price));
                        }
                        break;
                    }
                    default:
                        System.out.println("Unknown command.");
                }
            }
        }
    }

    static int countBooksByAuthor(List<Book> books, String authorName) {
        int count = 0;
        for (Book b : books) {
            if (b.getAuthor().equalsIgnoreCase(authorName)) count++;
        }
        return count;
    }

    static Set<String> getAllAuthors(List<Book> books) {
        Set<String> authors = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (Book b : books) {
            authors.add(b.getAuthor());
        }
        return authors;
    }

    static List<String> getBooksByAuthor(List<Book> books, String authorName) {
        List<String> titles = new ArrayList<>();
        for (Book b : books) {
            if (b.getAuthor().equalsIgnoreCase(authorName)) {
                titles.add(b.getTitle());
            }
        }
        return titles;
    }

    static List<Book> getBooksByRating(List<Book> books, double rating) {
        List<Book> result = new ArrayList<>();
        for (Book b : books) {
            if (Double.compare(b.getUserRating(), rating) == 0) {
                result.add(b);
            }
        }
        return result;
    }

    static Map<String, Integer> getBookPricesByAuthor(List<Book> books, String authorName) {
        Map<String, Integer> result = new HashMap<>();
        for (Book b : books) {
            if (b.getAuthor().equalsIgnoreCase(authorName)) {
                result.put(b.getTitle(), b.getPrice());
            }
        }
        return result;
    }
}


