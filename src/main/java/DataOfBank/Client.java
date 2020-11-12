package DataOfBank;

import java.util.Objects;

/*
В классе клиента предопреелены поля и методы,
которые обязатеьно есть у кажого клиента банка
 */
public class Client {

    private String FirstName;
    private String LastName;
    private String Passport;

    Client(){}

    public Client(String FirstName, String LastName, String Passport) {
        this.FirstName=FirstName;
        this.LastName=LastName;
        this.Passport=Passport;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPassport() {
        return Passport;
    }

    public void setPassport(String passport) {
        Passport = passport;
    }

    @Override
    public String toString() {
        return ("\n" + "Имя: " + getFirstName() + " "+ "Фамилия: "
                + getLastName() + " " + "Серия,номер паспорта: "+getPassport());
    }
    public String[] ClientData() {
        String[] ClientData={getFirstName(),getLastName(),getPassport()};
        return ClientData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(Passport, client.Passport);
    }


}
