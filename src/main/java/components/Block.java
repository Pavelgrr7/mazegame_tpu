//package components;
//
//import back.GameObject;
//import org.jbox2d.dynamics.contacts.Contact;
//import org.joml.Vector2f;
//import util.AssetPool;
//
//public abstract class Block extends Component {
//    private transient boolean active = true;
//
//    @Override
//    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal) {
//        PlayerController playerController = obj.getComponent(PlayerController.class);
//        if (active && playerController != null && contactNormal.y < -0.8f) {
//            playerHit(playerController);
//        }
//    }
//
//    public void setInactive() { this.active = false; }
//
//    abstract void playerHit(PlayerController playerController);
//}
