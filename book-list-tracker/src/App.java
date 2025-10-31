import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class App {
    private JFrame frame;
    private JTextField titleField, authorField, searchField;
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

    // add book
    JPanel addPanel = new JPanel(new GridLayout(4, 2, 5, 5));
    addPanel.setBorder(BorderFactory.createTitledBorder("Add Book"));

    titleField = new JTextField();
    authorField = new JTextField();

    JButton chooseImageButton = new JButton("Choose Image");
    JLabel imagePathLabel = new JLabel("No file selected");
    final String[] selectedImagePath = {null};

    chooseImageButton.addActionListener(e -> {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImagePath[0] = chooser.getSelectedFile().getAbsolutePath();
            imagePathLabel.setText(chooser.getSelectedFile().getName());
        }
    });

    JButton addButton = new JButton("Add Book");
    addButton.addActionListener(e -> {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        if (title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in both Title and Author.");
            return;
        }
        Book book = new Book(title, author, selectedImagePath[0]);
        manager.addBook(book);
        titleField.setText("");
        authorField.setText("");
        updateBookList(manager.getBooks());
    });

    addPanel.add(new JLabel("Title:"));
    addPanel.add(titleField);
    addPanel.add(new JLabel("Author:"));
    addPanel.add(authorField);
    addPanel.add(chooseImageButton);
    addPanel.add(imagePathLabel);
    addPanel.add(new JLabel(""));
    addPanel.add(addButton);

    // search book
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
    searchPanel.setBorder(BorderFactory.createTitledBorder("Search Book"));
    searchField = new JTextField(20);
    JButton searchButton = new JButton("Search");
    searchButton.addActionListener(e -> searchBook());
    searchPanel.add(new JLabel("Keyword:"));
    searchPanel.add(searchField);
    searchPanel.add(searchButton);

    // รวม addPanel และ searchPanel ไว้ด้านบน
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
            JPanel card = createBookCard(book);
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

    private JPanel createBookCard(Book book) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setMaximumSize(new Dimension(700, 120));

        JLabel imageLabel;
        if (book.getImagePath() != null) {
            ImageIcon icon = new ImageIcon(book.getImagePath());
            Image scaled = icon.getImage().getScaledInstance(80, 100, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaled));
        } else {
            imageLabel = new JLabel("[No Image]");
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
        }

        JLabel info = new JLabel("<html><b>" + book.getTitle() + "</b><br>" + book.getAuthor() + "</html>");
        info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "Delete this book?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                manager.removeBook(book);
                updateBookList(manager.getBooks());
            }
        });

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(info, BorderLayout.CENTER);
        rightPanel.add(deleteButton, BorderLayout.EAST);

        card.add(imageLabel, BorderLayout.WEST);
        card.add(rightPanel, BorderLayout.CENTER);

        return card;
    }
}
