package model;

import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.sql.*;
import java.util.HashMap;

/**
 * Created by Julien on 02.06.15.
 */
public class Database {

    private static Database INSTANCE = null;
    private Connection conn = null;

    private Database() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://:3306/tweets", "root", "");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Database();
        }
        return INSTANCE;
    }

    public static void main(String[] args) throws SQLException {
        Database.getInstance().addTweet(null);
    }

    public void addTweet(Status status) throws SQLException {
        HashMap<String, Long> table = new HashMap<>();

        Statement statement = conn.createStatement();
        for (HashtagEntity hashtag : status.getHashtagEntities()) {
            String hashTag = hashtag.getText().toLowerCase();

            boolean hasFound = false;
            do {
                ResultSet resultat = statement.executeQuery("SELECT PK FROM hashtag WHERE HASHTAG like '" + hashTag + "'");
                if (!resultat.next()) {
                    System.out.println("Hashtag doesn't exist");
                    statement.executeUpdate("INSERT INTO hashtag (hashtag) VALUES ('" + hashTag + "')");
                } else {
                    long id = resultat.getLong("PK");
                    System.out.println(id);
                    hasFound = true;
                    table.put(hashTag, id);
                }
            } while (!hasFound);
        }

        PreparedStatement stmt = null;
        stmt = conn.prepareStatement("INSERT INTO tweet (id, userId, msg, date) values (?, ?, ?, ?)");
        stmt.setString(1, String.valueOf(status.getId()));
        stmt.setString(2, String.valueOf(status.getUser().getId()));
        stmt.setString(3, status.getText());
        stmt.setString(4, String.valueOf(status.getCreatedAt()));
        stmt.executeUpdate();


        for (long v : table.values()) {
            String qry = "INSERT INTO tweetsxhashtags (tweetId, hashtagId) VALUES ('" + status.getId() + "'," + v + ");";
            statement.executeUpdate(qry);
        }

        statement.close();
    }
}
