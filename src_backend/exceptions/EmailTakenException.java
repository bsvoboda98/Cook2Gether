package com.bee.cookwithfriends.exceptions;

public class EmailTakenException extends RuntimeException{
    public EmailTakenException(String message){
        super(message);
    }
}
