//package scenes;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import components.Component;
//import components.ComponentDeserializer;
//import back.Camera;
//import back.GameObject;
//import back.GameObjectDeserializer;
//import back.Transform;
//import graphics.Renderer;
//import org.joml.Vector2f;
//import physics2d.Physics2D;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class MenuScene {
//
//    private Renderer renderer = new Renderer();
//    private Camera camera;
//    private boolean isRunning = false;
//    private List<GameObject> gameObjects = new ArrayList<>();
//    private boolean levelLoaded;
//    private SceneInitializer sceneInitializer;
//
//    public MenuScene(SceneInitializer sceneInitializer) {
//        this.sceneInitializer = sceneInitializer;
//        this.renderer = new Renderer();
//        this.gameObjects = new ArrayList<>();
//        this.isRunning = false;
//    }
//
//    public void init() {
//        this.camera = new Camera(new Vector2f(-250, 0));
////        this.sceneInitializer.loadResources(this);
////        this.sceneInitializer.init(this);
//    }
//
//    public void start() {
//        for (int i=0; i < gameObjects.size(); i++) {
//            GameObject go = gameObjects.get(i);
//            go.start();
//            this.renderer.add(go);
//        }
//        isRunning = true;
//    }
//
////    public void addGameObjectToScene(GameObject go) {
////        if (!isRunning) {
////            gameObjects.add(go);
////        } else {
////            gameObjects.add(go);
////            go.start();
////            this.renderer.add(go);
////        }
////    }
//
////    public void destroy(){
////        for (GameObject go : gameObjects) {
////            go.destroy();
////        }
////    }
//
//    public GameObject getObject(int gameObjectId) {
//        Optional<GameObject> result = this.gameObjects.stream()
//                .filter(gameObject -> gameObject.getUid() == gameObjectId)
//                .findFirst();
//        return result.orElse(null);
//    }
//
////    public void editorUpdate(float dt) {
////        this.camera.adjustProjection();
////        for (int i=0; i < gameObjects.size(); i++) {
////            GameObject go = gameObjects.get(i);
////            go.editorUpdate(dt);
////
////            if (go.isDead()) {
////                gameObjects.remove(i);
////                this.renderer.destroyGameObject(go);
////                this.physics2D.destroyGameObject(go);
////                i--;
////            }
////        }
////    }
//
//    public void update(float dt){
//        this.camera.adjustProjection();
//        for (int i=0; i < gameObjects.size(); i++) {
//            GameObject go = gameObjects.get(i);
//            go.update(dt);
//
//            if (go.isDead()) {
//                gameObjects.remove(i);
//                this.renderer.destroyGameObject(go);
//                i--;
//            }
//        }
//    };
//    public void render(){
//        this.renderer.render();
//    };
//
//    public Camera camera() {
//        return this.camera;
//    }
//
//    public void imgui() {
//        this.sceneInitializer.imgui();
//    }
//
////    public GameObject createGameObject(String name) {
////        GameObject go = new GameObject(name);
////        go.addComponent(new Transform());
////        go.transform = go.getComponent(Transform.class);
////        return go;
////    }
//
//    public void save() {
//        Gson gson = new GsonBuilder()
//                .setPrettyPrinting()
//                .registerTypeAdapter(Component.class, new ComponentDeserializer())
//                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
//                .enableComplexMapKeySerialization()
//                .create();
//
//        try {
//            FileWriter writer = new FileWriter("properties.txt");
//            List<GameObject> propToSerialize = new ArrayList<>();
//            for (GameObject obj : this.gameObjects) {
//                if (obj.doSerialization()) {
//                    propToSerialize.add(obj);
//                }
//            }
//            writer.write(gson.toJson(propToSerialize));
//            writer.close();
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void load() {
//        Gson gson = new GsonBuilder()
//                .setPrettyPrinting()
//                .registerTypeAdapter(Component.class, new ComponentDeserializer())
//                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
//                .enableComplexMapKeySerialization()
//                .create();
//
//        String inFile = "";
//        try {
//            inFile = new String(Files.readAllBytes(Paths.get("properties.txt")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (!inFile.equals("")) {
//            int maxGoId = -1;
//            int maxCompId = -1;
//            GameObject[] objs = gson.fromJson(inFile, GameObject[].class);
//            for (int i=0; i < objs.length; i++) {
//                //addGameObjectToScene(objs[i]);
//
//                for (Component c : objs[i].getAllComponents()) {
//                    if (c.getUid() > maxCompId) {
//                        maxCompId = c.getUid();
//                    }
//                }
//                if (objs[i].getUid() > maxGoId) {
//                    maxGoId = objs[i].getUid();
//                }
//            }
//
//            maxGoId++;
//            maxCompId++;
//            GameObject.init(maxGoId);
//            Component.init(maxCompId);
//        }
//    }
//
//    public GameObject getGameObject(String gameObjectName) {
//        Optional<GameObject> result = this.gameObjects.stream()
//                .filter(gameObject -> gameObject.name.equals(gameObjectName))
//                .findFirst();
//        return result.orElse(null);
//    }
//}