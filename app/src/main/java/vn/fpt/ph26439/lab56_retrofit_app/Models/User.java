package vn.fpt.ph26439.lab56_retrofit_app.Models;

import com.google.gson.annotations.SerializedName;

public class User {

    private String _id;
    private String ten;
    private String diachi;
    private String email;
    private String sodt;
    private String image;


    public User(String ten, String diachi, String email, String sodt, String image) {
        this.ten = ten;
        this.diachi = diachi;
        this.email = email;
        this.sodt = sodt;
        this.image = image;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSodt() {
        return sodt;
    }

    public void setSodt(String sodt) {
        this.sodt = sodt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" +
                "ten='" + ten + '\'' +
                ", diachi='" + diachi + '\'' +
                ", email='" + email + '\'' +
                ", sodt='" + sodt + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
