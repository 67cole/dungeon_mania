package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;
import dungeonmania.util.Position;


public interface Entity {
    Position getPosition();
    String getID();
    boolean getIsInteractable();
    int getX();
    int getY();
    String getType();
}