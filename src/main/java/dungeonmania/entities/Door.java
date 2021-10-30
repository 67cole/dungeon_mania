package dungeonmania.entities;

import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class Door extends StaticEntity {
    private int keyType;

    public Door(Position position, String type, String ID, boolean isInteractable, int keyType) {
        super(position,type, ID, isInteractable);
        this.keyType = keyType;
    } 

    public int getKeyType() {
        return this.keyType;
    }

    @Override
    public void entityFunction(List<Entity> entities, Character player, Direction direction, Dungeon main) {  
    
    }
    
}
