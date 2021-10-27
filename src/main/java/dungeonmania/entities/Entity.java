package dungeonmania.entities;
import dungeonmania.util.Position;


public interface Entity {
    Position getPosition();
    String getType();
    String getID();
    boolean getIsInteractable();
}