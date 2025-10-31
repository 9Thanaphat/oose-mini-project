
import javax.swing.*;
import java.awt.*;

public class AddBookPanel extends JPanel {
    private JTextField titleField, authorField;
    private BookManager manager;
    private Runnable updateCallback;

    public AddBookPanel(BookManager manager, Runnable updateCallback) {
        this.manager = manager;
        this.updateCallback = updateCallback;

        setLayout(new GridLayout(4, 2, 5, 5));
        setBorder(BorderFactory.createTitledBorder("Add Book"));

        titleField = new JTextField();
        authorField = new JTextField();

        JButton chooseImageButton = new JButton("Choose Image");
        JLabel imagePathLabel = new JLabel("No file selected");
        final String[] selectedImagePath = {null};

        chooseImageButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);
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
                JOptionPane.showMessageDialog(this, "Please fill in both Title and Author.");
                return;
            }
            Book book = new Book(title, author, selectedImagePath[0]);
            manager.addBook(book);
            titleField.setText("");
            authorField.setText("");
            imagePathLabel.setText("No file selected");
            selectedImagePath[0] = null;
            updateCallback.run();
        });

        add(new JLabel("Title:"));
        add(titleField);
        add(new JLabel("Author:"));
        add(authorField);
        add(chooseImageButton);
        add(imagePathLabel);
        add(new JLabel(""));
        add(addButton);
    }
}
