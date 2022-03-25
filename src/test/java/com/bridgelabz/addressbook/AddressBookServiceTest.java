package com.bridgelabz.addressbook;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bridgelabz.addressbook.AddressBookService.IOService.DB_IO;
import static org.junit.jupiter.api.Assertions.*;

class AddressBookServiceTest
{
    @Test
    public void givenAddressBookInDB_WhenRetrieved_ShouldMatchThePeopleCount() {
        AddressBookService addressBookService = new AddressBookService();
        List<AddressBookData> addressBookDataList = addressBookService.readAddressBookData(DB_IO);
        assertEquals(7,addressBookDataList.size());
    }
}