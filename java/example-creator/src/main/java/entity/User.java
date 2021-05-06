package entity;

public class User {


    public String username = "/html/body/div[5]/div[3]/div/div[1]/div/div[2]/div/table/tbody/tr[1]/td[1]/span";
    public String name = "/html/body/div[5]/div[1]/div/div[1]/div/div[2]/div/table/tbody/tr[1]/td[2]/span";
    public String adress = "/html/body/div[5]/div[1]/div/div[1]/div/div[2]/div/table/tbody/tr[2]/td[1]/span";
    public String city = "/html/body/div[5]/div[1]/div/div[1]/div/div[2]/div/table/tbody/tr[3]/td[1]/span";
    public String postcode = "/html/body/div[5]/div[1]/div/div[1]/div/div[2]/div/table/tbody/tr[3]/td[2]/span";
    public String dateOfBirth = "/html/body/div[5]/div[1]/div/div[1]/div/div[2]/div/table/tbody/tr[4]/td[2]/span";
    public String gender = "/html/body/div[5]/div[1]/div/div[1]/div/div[2]/div/table/tbody/tr[4]/td[1]/span";
    public String phone = "/html/body/div[5]/div[1]/div/div[1]/div/div[2]/div/table/tbody/tr[6]/td[1]/span";
    public String email = "/html/body/div[5]/div[1]/div/div[1]/div/div[2]/div/table/tbody/tr[6]/td[2]/span";
    public String linkToMailBox = "/html/body/div[5]/div[1]/div/div[1]/div/div[2]/div/table/tbody/tr[6]/td[2]/a";


    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", adress='" + adress + '\'' +
                ", city='" + city + '\'' +
                ", postcode='" + postcode + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", linkToMailBox='" + linkToMailBox + '\'' +
                '}';
    }
}
