package ru.desh.porcessor;

import ru.desh.model.CommonVisualizedEntity;
import ru.desh.model.Relation;

import java.util.List;

public class EntityRelationGraph {
    private List<Relation> relations;
    private List<CommonVisualizedEntity> entities;

    public EntityRelationGraph(List<Relation> relations, List<CommonVisualizedEntity> entities) {
        this.relations = relations;
        this.entities = entities;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public List<CommonVisualizedEntity> getEntities() {
        return entities;
    }
}
