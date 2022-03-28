package com.bridgelabz.addressbook;
public class AddressBookException extends Throwable {
    enum ExceptionType {
        DatabaseException,
    }
    public ExceptionType type;

    public AddressBookException(String message, ExceptionType type) {
        super(message);
        this.type = type;

    }

}
