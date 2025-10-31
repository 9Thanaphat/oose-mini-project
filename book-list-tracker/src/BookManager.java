import java.io.*;
import java.util.ArrayList;
import javax.swing.*;

public class BookManager {
    private ArrayList<Book> books = new ArrayList<>();
    private static final String DATA_FILE = "books.txt";

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        books.add(book);
        saveBooksToFile();
    }

    public void removeBook(Book book) {
        books.remove(book);
        saveBooksToFile();
    }

    public void loadBooksFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            books.clear();
            while ((line = reader.readLine()) != null) {
                Book book = Book.fromString(line);
                if (book != null) books.add(book);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading file: " + e.getMessage());
        }
    }

    public void saveBooksToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Book book : books) {
                writer.println(book.toString());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving file: " + e.getMessage());
        }
    }
}
