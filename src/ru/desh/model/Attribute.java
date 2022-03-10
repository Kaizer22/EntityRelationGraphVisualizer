package ru.desh.model;

public class Attribute {
    private String name;
    private boolean isKeyAttribute;
    private boolean isWeakAttribute;

    public Attribute(String name, boolean isKeyAttribute, boolean isWeakAttribute) {
        this.name = name;
        this.isKeyAttribute = isKeyAttribute;
        this.isWeakAttribute = isWeakAttribute;
    }

    public String getName() {
        return name;
    }

    public boolean isKeyAttribute() {
        return isKeyAttribute;
    }

    public boolean isWeakAttribute() {
        return isWeakAttribute;
    }
}
