
import javax.swing.*;
import java.awt.*;

public class BookPanel extends JPanel {

    public BookPanel(Book book, BookManager manager, Runnable updateCallback) {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        setMaximumSize(new Dimension(700, 120));

        JLabel imageLabel;
        if (book.getImagePath() != null && !book.getImagePath().isEmpty()) {
            ImageIcon icon = new ImageIcon(book.getImagePath());
            Image scaled = icon.getImage().getScaledInstance(80, 100, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(scaled));
        } else {
            imageLabel = new JLabel("[No Image]");
            imageLabel.setHorizontalAlignment(JLabel.CENTER);
            imageLabel.setPreferredSize(new Dimension(80, 100));
        }

        JLabel info = new JLabel("<html><b>" + book.getTitle() + "</b><br>" + book.getAuthor() + "</html>");
        info.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this book?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                manager.removeBook(book);
                updateCallback.run();
            }
        });

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(info, BorderLayout.CENTER);
        rightPanel.add(deleteButton, BorderLayout.EAST);

        add(imageLabel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }
}
