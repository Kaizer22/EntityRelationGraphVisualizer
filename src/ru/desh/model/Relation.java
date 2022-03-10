package ru.desh.model;

public class Relation {
    private final CommonVisualizedEntity entityA;
    private final CommonVisualizedEntity entityB;
    private final RelationType relationType;
    private final String       relationName;

    public Relation(CommonVisualizedEntity entityA, CommonVisualizedEntity entityB, RelationType relationType, String relationName) {
        this.entityA = entityA;
        this.entityB = entityB;
        this.relationType = relationType;
        this.relationName = relationName;
    }

    public CommonVisualizedEntity A() {
        return entityA;
    }

    public CommonVisualizedEntity B() {
        return entityB;
    }

    public RelationType Rel() {
        return relationType;
    }

    public String Name() {
        return relationName;
    }
}
