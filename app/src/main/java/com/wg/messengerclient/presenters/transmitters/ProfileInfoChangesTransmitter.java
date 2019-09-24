package com.wg.messengerclient.presenters.transmitters;

public class ProfileInfoChangesTransmitter {
    private String name, surname, birthday;
    private boolean name_changed, surname_changed, birthday_changed;



    public ProfileInfoChangesTransmitter(String name, String surname, String birthday, boolean name_changed, boolean surname_changed, boolean birthday_changed) {
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.name_changed = name_changed;
        this.surname_changed = surname_changed;
        this.birthday_changed = birthday_changed;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getBirthday() {
        return birthday;
    }

    public boolean isNameChanged() {
        return name_changed;
    }

    public boolean isSurnameChanged() {
        return surname_changed;
    }

    public boolean isBirthday_changed() {
        return birthday_changed;
    }
}
