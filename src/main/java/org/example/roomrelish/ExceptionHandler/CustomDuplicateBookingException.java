package org.example.roomrelish.ExceptionHandler;

import com.mongodb.DuplicateKeyException;

public class CustomDuplicateBookingException extends Exception{
    public CustomDuplicateBookingException(DuplicateKeyException e) {
        super(e.getMessage());
    }
}
