package dungeonmania.entities.CollectibleEntities;

import dungeonmania.entities.CollectibleEntity;
import dungeonmania.util.Position;

public class Key extends CollectibleEntity{
    private int keyNum;

    public Key(Position position, String type, String ID, boolean IsInteractable, int keyNum) {
        super(position,type, ID, IsInteractable);
        this.keyNum = keyNum;
    }   

    public int getKeyNum() {
        return this.keyNum;
    }

    

}

