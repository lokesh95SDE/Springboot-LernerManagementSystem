package com.springAi.LernerManagementSystem.exceptions;

// Extends Exception -->Checked Exception --> Must be handled by try catch or throws
// Extends Runtime -->Unchecked Exceotion --> no need to be handled by try catch or throws

//When to use Checked Exceptions: You use these when you expect a failure could happen due to external factors
//(like a user trying to open a file that doesn't exist) and you want to force the developer to write a backup plan (a catch block) to handle it gracefully.

//When to use Unchecked Exceptions: You use these for critical bugs or logic failures where the program usually can't
// (or shouldn't) try to recover automatically. It essentially tells the program, "The developer made a mistake here, fail fast so they can fix the code."


/**
 * Runtime exception thrown when a Learner is not found.
 *
 * This is an unchecked exception (extends {@code RuntimeException}). The service throws it
 * to signal that a requested resource doesn't exist. The controller maps it to a 404 response
 * using an {@code @ExceptionHandler} method.
 */
public class LearnerNotFoundException extends Exception {

    /**
     * Create the exception with a human-readable message.
     * @param message message describing the error
     */
    public LearnerNotFoundException(String message)
    {
        super(message);
    }

}
