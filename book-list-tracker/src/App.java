import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class App {
    private JFrame frame;
    private JTextField searchField;
    private JPanel bookListPanel;
    private BookManager manager = new BookManager();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.manager.loadBooksFromFile();
            app.createGUI();
        });
    }

    private void createGUI() {
        frame = new JFrame("Book List Tracker");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Use the new AddBookPanel
        AddBookPanel addPanel = new AddBookPanel(manager, () -> updateBookList(manager.getBooks()));

        // search book
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Book"));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchBook());
        searchPanel.add(new JLabel("Keyword:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Combine addPanel and searchPanel at the top
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(addPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        // book list
        bookListPanel = new JPanel();
        bookListPanel.setLayout(new BoxLayout(bookListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(bookListPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Book List"));

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        updateBookList(manager.getBooks());
        frame.setVisible(true);
    }

    private void updateBookList(ArrayList<Book> books) {
        bookListPanel.removeAll();
        for (Book book : books) {
            // Use the new BookPanel for each book
            BookPanel card = new BookPanel(book, manager, () -> updateBookList(manager.getBooks()));
            bookListPanel.add(card);
        }
        bookListPanel.revalidate();
        bookListPanel.repaint();
    }

    private void searchBook() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            updateBookList(manager.getBooks());
            return;
        }
        ArrayList<Book> results = new ArrayList<>();
        for (Book book : manager.getBooks()) {
            if (book.getTitle().toLowerCase().contains(keyword) || book.getAuthor().toLowerCase().contains(keyword)) {
                results.add(book);
            }
        }
        updateBookList(results);
    }
}
