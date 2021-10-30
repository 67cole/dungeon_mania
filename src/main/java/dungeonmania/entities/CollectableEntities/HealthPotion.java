package dungeonmania.entities.CollectableEntities;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.util.Position;

public class HealthPotion extends CollectableEntity{
    public HealthPotion(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
    }
}