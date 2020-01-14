package Database;

import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import POJO.*;


public class LibraryDBFunctions {

    public static List<Book> selectAllBooks() {
        List<Book> books = new ArrayList<>();
        String sqlSelection = "SELECT BOOK.NAME, AUTHOR.NAME AS Authors_Name, PRICE, BOOKCOVER FROM BOOK JOIN AUTHOR ON BOOK.IDAUTHOR = AUTHOR.IDAUTHOR";

        try (Connection connection = MyDataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlSelection)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString(1);
                String authorName = rs.getString(2);
                int price = rs.getInt(3);
                Blob b = rs.getBlob(4);
                byte[] i = b.getBytes(1, (int) b.length());

                FileOutputStream f = new FileOutputStream("BooksResource\\Temporary\\" + rs.getString(1) + ".img");
                f.write(i);
                f.close();

                ImageIcon cover = new ImageIcon("BooksResource\\Temporary\\" + rs.getString(1) + ".img");
                Book tmpBook = new Book(name, authorName, price, cover);
                books.add(tmpBook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }

    public static boolean updateBookPrice(String bookName, int newPrice) {
        String sqlUpdate = "UPDATE BOOK " +
                "SET PRICE = ?" +
                "WHERE NAME = ?";

        try (Connection connection = MyDataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlUpdate)) {

            stmt.setInt(1, newPrice);
            stmt.setString(2, bookName);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertBook(Book book) {
        if (!checkIfItemExists(Author.class, book.getAuthorName())) {
            insertAuthor(new Author(book.getAuthorName()));
        }
        String sqlInsertion = "INSERT INTO BOOK(IDBOOK, NAME, PRICE, BOOKCOVER, IDAUTHOR) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = MyDataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlInsertion);
             FileInputStream f = new FileInputStream(book.getPathToCover())) {

            stmt.setInt(1, getItemsLastID(Book.class) + 1);
            stmt.setString(2, book.getName());
            stmt.setInt(3, book.getPrice());
            stmt.setBinaryStream(4, f, f.available());
            stmt.setInt(5, getAuthorsID(book.getAuthorName()));

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertAuthor(Author author) {
        String sqlInsertion = "INSERT INTO AUTHOR(IDAUTHOR, NAME) " +
                "VALUES (?, ?)";

        try (Connection connection = MyDataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlInsertion)) {

            stmt.setInt(1, getItemsLastID(Author.class) + 1);
            stmt.setString(2, author.getName());

            return stmt.executeUpdate() > 0 ? true : false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean removeBook(String bookName) {
        String sqlUpdate = "DELETE FROM Book WHERE NAME = ?";

        try (Connection connection = MyDataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlUpdate)) {

            stmt.setString(1, bookName);

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getAuthorsID(String authorsName) {
        String sqlSelection = "SELECT idAuthor FROM AUTHOR WHERE UPPER(NAME) = ?";

        try (Connection connection = MyDataSource.getConnection()) {

            PreparedStatement stmt = connection.prepareStatement(sqlSelection);
            stmt.setString(1, authorsName.toUpperCase());

            ResultSet rs = stmt.executeQuery();

            rs.next();
            return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static <T> int getItemsLastID(Class<T> instance) {
        String sqlForLastID = "SELECT MAX(ID" + instance.getSimpleName() + ") FROM " + instance.getSimpleName();

        try (Connection connection = MyDataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlForLastID);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next())
                return rs.getInt(1);
            else
                return 1;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static <T> boolean checkIfItemExists(Class<T> inst, String itemName) {
        String sqlSelection = "SELECT NAME FROM " + inst.getSimpleName() + " WHERE UPPER(NAME) = ?";

        try (Connection connection = MyDataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlSelection)) {

            stmt.setString(1, itemName.toUpperCase());

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {
            return false;
        }
    }
}