package ru.desh.my_custom_model;

import ru.desh.model.Attribute;
import ru.desh.model.CommonVisualizedEntity;

public class Car implements CommonVisualizedEntity {
    @Override
    public Attribute[] getAttributes() {
        return new Attribute[]{
                new Attribute("Model", false, false),
                new Attribute("DateOfManufacturing", false, false),
                new Attribute("SerialNumber", true, false)
        };
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
