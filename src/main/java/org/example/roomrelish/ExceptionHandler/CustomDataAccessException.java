package org.example.roomrelish.ExceptionHandler;

import com.mongodb.DuplicateKeyException;
import org.springframework.dao.DataAccessException;

public class CustomDataAccessException extends Exception {

    public CustomDataAccessException(DataAccessException e) {
        super(e.getMessage());
    }
}
