package com.example.ahmed_eid.merchantnavigationdrawer;

public class Category {
    int id ;
    String name ;
    String Description ;

    public Category(int id, String name, String description) {
        this.id = id;
        this.name = name;
        Description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
