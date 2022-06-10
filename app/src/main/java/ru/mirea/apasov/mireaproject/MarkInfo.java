package ru.mirea.apasov.mireaproject;

import androidx.annotation.NonNull;

public class MarkInfo {
    String name;
    String description;
    String address;

    MarkInfo(String name, String description, String address){
        this.name = name;
        this.description = description;
        this.address = address;
    }

    @NonNull
    @Override
    public String toString() {
        return  name + '\n' +
                description + '\n' +
                address;
    }
}

