package dungeonmania.entities;

import dungeonmania.util.Position;

public class Boulder extends StaticEntity{
    public Boulder(Position position, String type, String ID, boolean IsInteractable) {
        super(position,type, ID, IsInteractable);
    }

    


    
    @Override
    public void interactEntity() {
        // boulder moves by the direction of the player
        
    }


}
