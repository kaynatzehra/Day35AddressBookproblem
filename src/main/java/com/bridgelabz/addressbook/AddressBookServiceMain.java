package com.bridgelabz.addressbook;
import java.util.List;

public class AddressBookServiceMain {
    private List<AddressBookData> addressBookList;

    public enum IOService {DB_IO}

    private static AddressBookDBService addressBookDBService;

    public AddressBookServiceMain() {
        addressBookDBService = AddressBookDBService.getInstance();
    }

    public List<AddressBookData> readAddressBookData(IOService ioService) {
        if (ioService.equals(IOService.DB_IO)) {
            this.addressBookList = addressBookDBService.readDate();
        }
        return addressBookList;
    }
}