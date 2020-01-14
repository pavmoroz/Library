
import Database.LibraryDBFunctions;
import Database.MyDataSource;
import GUI.TableFrame;
import POJO.Book;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        A new book: BooksResource\Ulysses.jpg Author: James Joyce

        createEmbeddedDBWithValues();

        List<Book> books = LibraryDBFunctions.selectAllBooks();

        TableFrame tableFrame = new TableFrame(books);
        tableFrame.start();

    }

    // hard code values to the embedded database, if Oracle database is not available
    private static void createEmbeddedDBWithValues() {
        String authorCreation = "CREATE TABLE Author (\n" +
                "    idAuthor int NOT NULL,\n" +
                "    name varchar(50) NOT NULL,\n" +
                "    CONSTRAINT Author_pk PRIMARY KEY (idAuthor))";

        String bookCreation = "CREATE TABLE Book (\n" +
                "    idBook int  NOT NULL,\n" +
                "    name varchar(50)  NOT NULL,\n" +
                "    price int  NOT NULL,\n" +
                "    bookCover blob  NOT NULL,\n" +
                "    idAuthor int  NOT NULL,\n" +
                "    CONSTRAINT Book_pk PRIMARY KEY (idBook))";

        String bookAuthorConstraint = "ALTER TABLE Book ADD CONSTRAINT Book_Author\n" +
                "    FOREIGN KEY (idAuthor)\n" +
                "    REFERENCES Author (idAuthor)";

        String insertAuthors = "INSERT INTO\n" +
                "Author (IDAUTHOR, NAME) \n" +
                "VALUES (1,'Miguel De Cervantes'),\n" +
                "(2,'John Bunyan'),\n" +
                "(3,'Daniel Defoe'),\n" +
                "(4,'Jonathan Swift'),\n" +
                "(5,'Mary Shelley'),\n" +
                "(6,'Jonathan Stroud')";

        String insertBooks = "INSERT INTO\n" +
                "Book (IDBOOK, NAME, PRICE, BOOKCOVER, IDAUTHOR) \n" +
                "VALUES (1,'Don Quixote', 184, ?, 1),\n" +
                "(2,'Frankenstein', 56, ?, 5),\n" +
                "(3,'Gulliver''s Travels', 167, ?, 4),\n" +
                "(4,'Pilgrim''s Progress', 99, ?, 2),\n" +
                "(5,'Robinson Crusoe', 12, ?, 3),\n" +
                "(6,'The Amulet of Samarkand', 199, ?, 6)";

        try (Connection connection = MyDataSource.getConnection()) {
            PreparedStatement stmt;
            if (!tableExists("author", connection) || !tableExists("book", connection)) {
                stmt = connection.prepareStatement(authorCreation);
                stmt.execute();

                stmt = connection.prepareStatement(bookCreation);
                stmt.execute();
                stmt = connection.prepareStatement(bookAuthorConstraint);
                stmt.execute();
                stmt = connection.prepareStatement(insertAuthors);
                stmt.executeUpdate();
                stmt = connection.prepareStatement(insertBooks);
                stmt.setBinaryStream(1, new FileInputStream("BooksResource\\HardCodedValues\\Don Quixote.png"));
                stmt.setBinaryStream(2, new FileInputStream("BooksResource\\HardCodedValues\\Frankenstein.jpg"));
                stmt.setBinaryStream(3, new FileInputStream("BooksResource\\HardCodedValues\\Gulliver's Travels.jpg"));
                stmt.setBinaryStream(4, new FileInputStream("BooksResource\\HardCodedValues\\Pilgrim's Progress.jpg"));
                stmt.setBinaryStream(5, new FileInputStream("BooksResource\\HardCodedValues\\Robinson Crusoe.jpg"));
                stmt.setBinaryStream(6, new FileInputStream("BooksResource\\HardCodedValues\\The Amulet of Samarkand.jpg"));
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            if (e.getSQLState().equals("XJ015")) {
                System.out.println("Derby shutdown normally");
            } else {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static boolean tableExists(String tableName, Connection conn)
            throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet result = meta.getTables(null, null, tableName.toUpperCase(), null);
        return result.next();
    }
}
