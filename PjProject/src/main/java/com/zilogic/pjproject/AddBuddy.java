package com.zilogic.pjproject;

class AddBuddy {

    String accountPass;

    String buddyUserName;

    public String getAccountPass() {
        return accountPass;
    }

    public void setAccountPass(String accountPass) {
        this.accountPass = accountPass;
    }

    public String getBuddyUserName() {
        return buddyUserName;
    }

    public void setBuddyUserName(String buddyUserName) {
        this.buddyUserName = buddyUserName;
    }

    @Override
    public String toString() {
        return "AddBuddy{" + "accountPass=" + accountPass + ", buddyUserName=" + buddyUserName + '}';
    }

}
