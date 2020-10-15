package com.example.message.Model;

public class Chats {

    String message, sender, reciever;
    boolean isseen;


    public Chats(String message, String sender, String reciever, boolean isseen) {
        this.message = message;
        this.sender = sender;
        this.reciever = reciever;
        this.isseen = isseen;
    }

    public Chats() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}
