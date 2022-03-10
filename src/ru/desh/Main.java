package ru.desh;

import ru.desh.model.Relation;
import ru.desh.model.RelationType;
import ru.desh.my_custom_model.Car;
import ru.desh.my_custom_model.CarShop;
import ru.desh.my_custom_model.Ford;
import ru.desh.my_custom_model.Nissan;
import ru.desh.porcessor.EntityRelationGraph;
import ru.desh.porcessor.EntityRelationGraphVisualizer;
import ru.desh.porcessor.impl.GraphvizVisualizer;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Car car = new Car();
        CarShop carShop = new CarShop();
        Ford ford = new Ford();
        Nissan nissan = new Nissan();
        var g = new EntityRelationGraph(
                List.of(
                        new Relation(ford, car, RelationType.ONE_TO_0_MANY, "Produces"),
                        new Relation(nissan, car, RelationType.ONE_TO_0_MANY, "Produces"),
                        new Relation(carShop, car,RelationType.ONE_TO_0_MANY, "Sells")
                ),
                List.of(ford, carShop, nissan, car)
        );
        //Needs to install Graphviz
        //https://graphviz.org/download/
        EntityRelationGraphVisualizer viz = new GraphvizVisualizer(
                "\"C:\\Program Files\\Graphviz\\bin\\dot.exe\"");
        viz.visualizeEntityRelationGraph("", g);
    }
}
