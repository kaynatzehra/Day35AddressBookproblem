package com.bridgelabz.addressbook;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;
import static com.bridgelabz.addressbook.AddressBookService.IOService.DB_IO;
import static org.junit.jupiter.api.Assertions.*;

class AddressBookServiceTest
{
    @Test
    public void givenAddressBookInDB_WhenRetrieved_ShouldMatchThePeopleCount() {
        AddressBookService addressBookService = new AddressBookService();
        List<AddressBookData> addressBookDataList = addressBookService.readAddressBookData(DB_IO);
        assertEquals(5,addressBookDataList.size());
    }
    @Test
    public void givenNewPhoneNumber_ShouldUpdateTheRecordAndSyncWithDataBase() throws AddressBookException {
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(DB_IO);
        addressBookService.updateRecord("Kaynat", "9967587648");
        boolean result = addressBookService.checkRecordSyncWithDB("Kaynat");
        assertTrue(result);
    }
    @Test
    public void givenDate_ShouldRetrieveTheAddressBookRecord_BasedOnTheParticularRange() {
        AddressBookService addressBookService = new AddressBookService();
        addressBookService.readAddressBookData(DB_IO);
        LocalDate startDate = LocalDate.of(2018, 1, 1);
        LocalDate endDate = LocalDate.now();
        List< AddressBookData> employeePayrollData= addressBookService.readEmployeePayrollForDateRange(DB_IO, startDate, endDate);
        assertEquals(0,employeePayrollData.size());
    }
}