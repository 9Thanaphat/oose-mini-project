public class Book {
    private String title;
    private String author;
    private String imagePath;

    public Book(String title, String author, String imagePath) {
        this.title = title;
        this.author = author;
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String toString() {
        return title + "|" + author + "|" + (imagePath != null ? imagePath : "");
    }

    public static Book fromString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length >= 2) {
            String title = parts[0];
            String author = parts[1];
            String imagePath = parts.length >= 3 ? parts[2] : null;
            return new Book(title, author, imagePath);
        }
        return null;
    }
}
