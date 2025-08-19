import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class DatasetReader {
    public List<Book> readBooks(String csvPath) {
        List<Book> books = new ArrayList<>();
        Path path = Paths.get(csvPath);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String header = reader.readLine();
            if (header == null) {
                return books;
            }
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                List<String> columns = parseCsvLine(line);
                if (columns.size() != 7) {

                    continue;
                }

                String title = unquote(columns.get(0));
                String author = unquote(columns.get(1));
                double userRating = parseDoubleSafe(columns.get(2));
                int reviews = parseIntSafe(columns.get(3));
                int price = parseIntSafe(columns.get(4));
                int year = parseIntSafe(columns.get(5));
                String genre = unquote(columns.get(6));

                books.add(new Book(title, author, userRating, price, reviews, year, genre));
            }
        } catch (IOException e) {
            System.err.println("Failed to read CSV file: " + e.getMessage());
        }
        return books;
    }

    private List<String> parseCsvLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
             
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++; 
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        return result;
    }

    private String unquote(String value) {
        String trimmed = value.trim();
        if (trimmed.length() >= 2 && trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            return trimmed.substring(1, trimmed.length() - 1);
        }
        return trimmed;
    }

    private int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}


