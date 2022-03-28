package com.bridgelabz.addressbook;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressBookDataBaseService
{
    public Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    private PreparedStatement addressBookDataStatement;
    private static AddressBookDataBaseService addressBookDBService;
    public AddressBookDataBaseService()
    {
    }
    public static AddressBookDataBaseService getInstance() {
        if (addressBookDBService == null) {
            addressBookDBService = new AddressBookDataBaseService();
        }
        return addressBookDBService;
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/Address_Book_Service?useSSL=false";
        String userName = "root";
        String password = "K@inu786";
        connection = DriverManager.getConnection(jdbcURL, userName, password);
        System.out.println(connection + " connection successful");
        return connection;
    }

    public List<AddressBookData> readDate() {
        String query = "SELECT * from addressBook";
        return this.getAddressBookDataUsingDB(query);
    }

    private List<AddressBookData> getAddressBookDataUsingDB(String query) {
        List<AddressBookData> addressBookList = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            addressBookList = this.getAddressBookData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookList;
    }

    private List<AddressBookData> getAddressBookData(ResultSet resultSet) {
        List<AddressBookData> addressBookList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String typeId = resultSet.getString("type");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String phoneNumber = resultSet.getString("mobileNumber");
                String email = resultSet.getString("email");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                String zip = resultSet.getString("zip");
                LocalDate date_added = resultSet.getDate("date_added").toLocalDate();
                addressBookList.add(new AddressBookData(typeId, firstName, lastName, phoneNumber, email, city, state, zip, date_added));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookList;
    }
    private void prepareStatementForAddressBook() {
        try {
            Connection connection = this.getConnection();
            String sql = "SELECT * FROM addressBook WHERE `firstName` = ?";
            addressBookDataStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<AddressBookData> getAddressBookData(String firstName) {
        List<AddressBookData> addressBookDataList = null;
        if (this.addressBookDataStatement == null) {
            this.prepareStatementForAddressBook();
        }
        try {
            addressBookDataStatement.setString(1, firstName);
            ResultSet resultSet = addressBookDataStatement.executeQuery();
            addressBookDataList =this.getAddressBookData(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressBookDataList;
    }

    public int updateAddressBookRecord(String name, String phoneNumber) throws AddressBookException {
        String query = String.format("update addressBook set mobileNumber = '%s' where firstName= '%s' ;", phoneNumber, name);
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            return statement.executeUpdate(query);
        }catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DatabaseException);
        }
    }
    public List<AddressBookData> getEmployeePayrollForDateRange(LocalDate startDate, LocalDate endDate) {
        String query = String.format("SELECT * FROM addressBook WHERE date_added BETWEEN '%s' AND '%s';",
                Date.valueOf(startDate), Date.valueOf(endDate));
        return this.getAddressBookDataUsingDB(query);
    }
    public Map<String, Double> getCountOfContactsByCity() {
        String query = "SELECT city,COUNT(city) as count from addressBook group by city;";
        Map<String, Double> countOfContacts = new HashMap<>();
        try (Connection connection = this.getConnection()) {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String city = resultSet.getString("city");
                double count = resultSet.getDouble("count");
                countOfContacts.put(city, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countOfContacts;
    }
    public Map<String, Double> getCountOfContactsByState() {
        String query = "SELECT state,COUNT(state) as count from addressBook group by state;";
        Map<String, Double> countOfContacts = new HashMap<>();
        try (Connection connection = this.getConnection()) {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String state = resultSet.getString("state");
                double count = resultSet.getDouble("count");
                countOfContacts.put(state, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countOfContacts;
    }
    public AddressBookData addNewContact(String type, String firstName, String lastName, String mobileNumber,
                                         String email, String city, String state, String zip, LocalDate localDate) {
        Connection connection = null;
        AddressBookData addressBookData=null;
        try {
            connection = this.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Statement statement = connection.createStatement()) {
            String sql = String.format("INSERT INTO addressBook (`type`,`firstName`,`lastName`,`mobileNumber`,`email`,`city`,`state`,`zip`,`date_added`) " +
                            " VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                    type,firstName,lastName,mobileNumber,email,city,state,zip,Date.valueOf(localDate));
            System.out.println(sql+" sql");
            int rowAffected = statement.executeUpdate(sql);
            if (rowAffected == 1) {
                addressBookData = new AddressBookData(type,firstName,lastName,mobileNumber,email,city,state,zip,localDate);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return addressBookData;
    }
}
