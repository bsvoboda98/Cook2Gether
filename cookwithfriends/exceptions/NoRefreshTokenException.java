package com.bee.cookwithfriends.exceptions;

public class NoRefreshTokenException extends RuntimeException {
    public NoRefreshTokenException(String message){
        super(message);
    }
}
