package dungeonmania.entities;

import java.util.List;

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
        System.out.println("trying to move");
        switch(direction) {
            case UP:
            System.out.println("trying to move up");
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
