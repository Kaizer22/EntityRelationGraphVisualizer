package ru.desh.porcessor.impl;

import lombok.Value;
import ru.desh.model.Attribute;
import ru.desh.model.CommonVisualizedEntity;
import ru.desh.model.Relation;
import ru.desh.model.RelationType;
import ru.desh.porcessor.EntityRelationGraph;
import ru.desh.porcessor.EntityRelationGraphVisualizer;

import java.io.*;
import java.util.UUID;


public class GraphvizVisualizer implements EntityRelationGraphVisualizer {
    private final DOTRenderer renderer;

    public record Pair(String A, String B) {}
    private static class DOTRenderer {
        private String DOTPath;

        private boolean isCommited = false;
        private boolean isFileOpened = false;
        private String bufFileName;
        private FileWriter fileWriter = null;

        public DOTRenderer(String DOTPath) {
            this.DOTPath = DOTPath;
        }

        DOTRenderer initRenderer() throws IOException {
            bufFileName = UUID.randomUUID() + ".dot";
            isCommited = false;
            File file = new File(bufFileName);

            fileWriter = new FileWriter(file);
            file.deleteOnExit();
            if(file.createNewFile()) {
                System.out.println("DOTRenderer - buffer file created successfully");
            }else {
                System.out.println("DOTRenderer - buffer file already exists");
            }
            isFileOpened = true;
            fileWriter.write("graph EntityRelationDiagram { \n");
            return this;
        }

        DOTRenderer setFont(String fontName) throws IOException {
            if (isFileOpened) {
                fileWriter.write("fontname=\"%s\"; \n".formatted(fontName));
            }
            return this;
        }

        DOTRenderer setFontSize(int fontSize) throws IOException {
            if (isFileOpened) {
                fileWriter.write("fontsize=%d; \n".formatted(fontSize));
            }

            return this;
        }

        DOTRenderer addEntity(CommonVisualizedEntity entity) throws IOException {
            if (isFileOpened) {
                var attr = entity.getAttributes();
                var name = entity.getName();
                fileWriter.write("node [shape=box style=solid]; %s;\n".formatted(name));
                for (Attribute a : attr) {
                    addAttribute(name, a);
                }
            }
            return this;
        }

        DOTRenderer addAttribute(String parentEntityName, Attribute attribute) throws IOException {
            if (isFileOpened) {
                var bufferName = "attr" + UUID.randomUUID().toString().replace("-", "");
                var style = attribute.isKeyAttribute() ? "dotted" : "solid";
                fileWriter.write("node [shape=ellipse style=%s]; {node [label=\"%s\"] %s;} ;\n"
                        .formatted(style, attribute.getName(),
                                bufferName));
                fileWriter.write("%s -- %s;\n"
                        .formatted(parentEntityName, bufferName));
            }
            return this;
        }

        DOTRenderer addRelation(Relation relation) throws IOException {
            var n = relation.Name();
            var bufferRelName = "rel" + UUID.randomUUID().toString().replace("-", "");
            if (isFileOpened) {
                fileWriter.write(("node [shape=diamond,style=filled,color=lightgrey]; {node [label=\"%s\"] %s;}; \n")
                        .formatted(n, bufferRelName));

                //Relation example:
                // ONE_TO_MANY (ONE Relation.A to MANY Relation.B)
                // EntityA (1) --- (1...Many) EntityB
                var relPair = getRelationIndexPair(relation.Rel());
                fileWriter.write("%s -- %s [label=\"%s\", len=1.00];\n"
                        .formatted(relation.A().getName(), bufferRelName, relPair.A));
                fileWriter.write("%s -- %s [label=\"%s\", len=1.00];\n"
                        .formatted(bufferRelName, relation.B().getName(), relPair.B));
            }
            return this;
        }

        private Pair getRelationIndexPair(RelationType relationType) {
            switch (relationType) {
                case ZERO_TO_ONE -> {return new Pair("0","1");}
                case ZERO_TO_0_ONE -> {return new Pair("0","0,1");}
                case ZERO_TO_0_MANY -> {return new Pair("0","0..n");}
                case ZERO_TO_1_MANY -> {return new Pair("0","1..n");}
                case ONE_TO_ONE -> {return new Pair("1","1");}
                case ONE_TO_0_ONE -> {return new Pair("1","0,1");}
                case ONE_TO_0_MANY -> {return new Pair("1", "0..n");}
                case ONE_TO_1_MANY -> {return new Pair("1", "1..n");}
                case ONE_MANY_TO_0_MANY -> {return new Pair("1..n", "0..n");}
                case ONE_MANY_TO_1_MANY -> {return new Pair("1..n", "1..n");}
                case ZERO_MANY_TO_0_MANY -> {return new Pair("0..n","0..n");}
                case ZERO_MANY_TO_1_MANY -> {return new Pair("0..n","1..n");}
            }
            return new Pair("-1","-1");
        }

        DOTRenderer setLabel(String label) throws IOException {
            if (isFileOpened) {
                fileWriter.write(
                        "label = \"%s\";\n".formatted(label)
                );
            }
            return this;
        }

        DOTRenderer Commit() throws IOException {
            if (isFileOpened) {
                fileWriter.write("}");
                fileWriter.flush();
                fileWriter.close();
                isFileOpened = false;
                isCommited = true;
            }
            return this;
        }

        void SaveToSVG(String path) throws IOException {
            String outputPath = path + "ERD_" + UUID.randomUUID() + ".svg" ;
            if (!isFileOpened && isCommited) {
                ProcessBuilder processBuilder = new ProcessBuilder();
                //TODO configure path!
                ;
                processBuilder.command(DOTPath, "-Tsvg", bufFileName);

                try {
                    Process process = processBuilder.start();
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(process.getInputStream()));

                    File file = new File(outputPath);
                    fileWriter = new FileWriter(file);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fileWriter.write(line);
                    }

                    fileWriter.flush();
                    fileWriter.close();
                    int exitCode = process.waitFor();
                    System.out.println("\nExited with error code : " + exitCode);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public GraphvizVisualizer(String DOTPath) {
        this.renderer = new DOTRenderer(DOTPath);
    }

    @Override
    public void visualizeEntityRelationGraph(String resultSVGPath, EntityRelationGraph graph) {
        try {
            renderer.initRenderer()
                    .setFont("Helvetica,Arial,sans-serif")
                    .setFontSize(25)
                    .setLabel("Entity Relation Diagram");
            for (CommonVisualizedEntity entity: graph.getEntities()) {
                renderer.addEntity(entity);
            }
            for (Relation r: graph.getRelations()) {
                renderer.addRelation(r);
            }
            renderer.Commit()
                    .SaveToSVG(resultSVGPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


