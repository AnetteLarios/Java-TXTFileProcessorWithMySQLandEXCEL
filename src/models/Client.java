package models;
/*
This is the model of the data that the programs is going to extract from the txt file, send it
to the database and then put it into an Excel file.
@author Anette Larios
@since 15.06.2023
 */
public class Client {
    //attributes
    private String name;
    private String phoneNumber;
    private String address;
    private String email;
    private String country;
    private int numberRange;
    private float balance;
    private String rfc;
    private float tax;
    //getters and setters
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getCountry(){
        return country;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public int getNumberRange(){
        return numberRange;
    }

    public void setNumberRange(){
        this.numberRange = numberRange;
    }

    public float getBalance(){
        return balance;
    }

    public void setBalance(float balance){
        this.balance = balance;
    }

    public String getRfc(){
        return rfc;
    }

    public void setRfc(){
        this.rfc = rfc;
    }

    public float getTax(){
        return tax;
    }

    public void setTax(float tax){
        this.tax = tax;
    }
    //class constructor
    public Client (String name, String phoneNumber, String address, String email, String country,
                   int numberRange, float balance, String rfc, float tax){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.country = country;
        this.numberRange = numberRange;
        this.balance = balance;
        this.rfc = rfc;
        this.tax = tax;
    }

}
