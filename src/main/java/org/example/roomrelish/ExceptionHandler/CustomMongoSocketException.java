package org.example.roomrelish.ExceptionHandler;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoSocketException;

public class CustomMongoSocketException extends Exception{
    public CustomMongoSocketException(MongoSocketException e) {
        super(e.getMessage());
    }
}
