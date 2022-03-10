package ru.desh.my_custom_model;

import ru.desh.model.Attribute;
import ru.desh.model.CommonVisualizedEntity;

public class CarShop implements CommonVisualizedEntity {
    @Override
    public Attribute[] getAttributes() {
        return new Attribute[]{
                new Attribute("Address", false, false),
                new Attribute("Working hours", false, false),
                new Attribute("Shop ID", true, false)
        };
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
