package ru.desh.my_custom_model;

import ru.desh.model.Attribute;
import ru.desh.model.CommonVisualizedEntity;

public class Nissan implements CommonVisualizedEntity {
    @Override
    public Attribute[] getAttributes() {
        return new Attribute[]{
                new Attribute("ManufacturerID", true, false),
                new Attribute("DateOfCreation", false, false),
                new Attribute("Capitalization", false, false)
        };
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
