package com.bridgelabz.addressbook;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AddressBookService {
    private List<AddressBookData> addressBookList;

    public enum IOService {DB_IO}

    private static AddressBookDataBaseService addressBookDBService;

    public AddressBookService() {
        addressBookDBService = AddressBookDataBaseService.getInstance();
    }

    public List<AddressBookData> readAddressBookData(IOService ioService) {
        if (ioService.equals(IOService.DB_IO)) {
            this.addressBookList = addressBookDBService.readDate();
        }
        return addressBookList;
    }
    public void updateRecord(String name, String phoneNumber) throws AddressBookException {
        int result = addressBookDBService.updateAddressBookRecord(name, phoneNumber);
        if (result==0)return;
        AddressBookData  addressBookData=this.getAddressBookData(name);
        if (addressBookData!=null) addressBookData.phoneNumber=phoneNumber;
    }

    private AddressBookData getAddressBookData(String name) {
        return this.addressBookList.stream()
                .filter(addressBookData -> addressBookData.firstName.equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean checkRecordSyncWithDB(String name) {
        List<AddressBookData> addressBookData= addressBookDBService.getAddressBookData(name);
        System.out.println(addressBookData);
        return addressBookData.get(0).equals(getAddressBookData(name));
    }
    public List<AddressBookData> readEmployeePayrollForDateRange(IOService ioService, LocalDate startDate, LocalDate endDate) {
        if (ioService.equals(IOService.DB_IO)) {
            return addressBookDBService.getEmployeePayrollForDateRange(startDate, endDate);
        }
        return null;
    }
    public Map<String, Double> contactsByCity(IOService ioService) {
        if (ioService.equals(IOService.DB_IO)) {
            return addressBookDBService.getCountOfContactsByCity();
        }
        return null;
    }
    public Map<String, Double> contactsByState(IOService ioService) {
        if (ioService.equals(IOService.DB_IO)) {
            return addressBookDBService.getCountOfContactsByState();
        }
        return null;
    }
    public void addNewContact(String firstName, String lastName, String typeid,String phoneNumber, String city, String state,
                              String zip, String email, LocalDate localDate) {
        addressBookList.add(addressBookDBService.addNewContact(typeid, firstName, lastName,
                phoneNumber,email,city,state,zip,localDate));
    }


}