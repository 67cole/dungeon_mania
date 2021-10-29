package dungeonmania.entities.CollectibleEntities;

import dungeonmania.entities.CollectibleEntity;
import dungeonmania.util.Position;

public class Bomb extends CollectibleEntity{
    public Bomb(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
    }
}
