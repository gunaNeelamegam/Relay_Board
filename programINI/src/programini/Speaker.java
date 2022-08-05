/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programini;

/**
 *
 * @author user
 */
class Speaker {

    private String name;
    private String password;

    Speaker(String na, String pass) {
        //   throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        this.name = na;
        this.password = pass;
    }

    Speaker(String guna, String codec, String guna0) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        this.name = guna;
        this.codec = codec;
        this.password = guna0;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Speaker{" + "name=" + name + ", password=" + password + ", codec=" + codec + '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public String getPassword() {
        return password;
    }

    public String getCodec() {
        return codec;
    }
    private String codec;

}
