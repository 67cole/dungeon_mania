package dungeonmania.entities.CollectableEntities;

import dungeonmania.entities.CollectableEntity;
import dungeonmania.util.Position;

public class Key extends CollectableEntity{
    private int keyNum;

    public Key(Position position, String type, String ID, boolean IsInteractable, int keyNum) {
        super(position,type, ID, IsInteractable);
        this.keyNum = keyNum;
    }   

    public int getKeyNum() {
        return this.keyNum;
    }
}

