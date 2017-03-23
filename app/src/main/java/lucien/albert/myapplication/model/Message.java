package lucien.albert.myapplication.model;

/**
 * Created by alberluc on 27/01/2017.
 */
public class Message {

    private int userID;
    private String message;
    private String date;
    private String username;
    private String imageUrl;

    public int getUserID() {
        return userID;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public Message(){

    }

}
