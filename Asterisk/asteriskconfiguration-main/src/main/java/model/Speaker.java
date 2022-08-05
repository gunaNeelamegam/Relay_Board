/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Objects;

/**
 *
 * @author sridhar
 */
public class Speaker {

    String displayName;
    String protocol;
    String voiceQuality;
    String aor;
    String context;
    String codecDisallow;
    String codecAllow;
    String auth;
    String type;
    String callerid;
    String auth_type;
    String password;
    String userName;
    String max_contacts;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getVoiceQuality() {
        return voiceQuality;
    }

    public void setVoiceQuality(String voiceQuality) {
        this.voiceQuality = voiceQuality;
    }

    public String getAor() {
        return aor;
    }

    public void setAor(String aor) {
        this.aor = aor;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getCodecDisallow() {
        return codecDisallow;
    }

    public void setCodecDisallow(String codecDisallow) {
        this.codecDisallow = codecDisallow;
    }

    public String getCodecAllow() {
        return codecAllow;
    }

    public void setCodecAllow(String codecAllow) {
        this.codecAllow = codecAllow;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getCallerid() {
        return callerid;
    }

    public void setCallerid(String callerid) {
        this.callerid = callerid;
    }

    public String getAuth_type() {
        return auth_type;
    }

    public void setAuth_type(String auth_type) {
        this.auth_type = auth_type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMax_contacts() {
        return max_contacts;
    }

    public void setMax_contacts(String max_contacts) {
        this.max_contacts = max_contacts;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        //return callerid + ":" + userName;
        return userName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Speaker other = (Speaker) obj;
        if (!Objects.equals(this.userName, other.userName)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return this.userName.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

}
