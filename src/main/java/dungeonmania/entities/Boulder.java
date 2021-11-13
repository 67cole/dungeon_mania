package dungeonmania.entities;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Collection;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Dungeon;

public class Boulder extends StaticEntity{

    public Boulder(Position position, String type, String ID, boolean isInteractable) {
        super(position,type, ID, isInteractable);
    }
    
    @Override
    public void entityFunction(List<Entity> entities, Character player, Direction direction, Dungeon main) {
        // we need to move the boulder by the direction
        switch(direction) {
            case UP:
                super.moveUpward();
                break;

            case DOWN:
                super.moveDownward();
                break;
            
            case LEFT:
                super.moveLeft();
                break;
            
            case RIGHT:
                super.moveRight();
                break;
            case NONE:
                break;
        }   
        

    }

}
