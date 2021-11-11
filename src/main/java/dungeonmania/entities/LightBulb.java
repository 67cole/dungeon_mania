package dungeonmania.entities;

import dungeonmania.util.Position;

public class LightBulb extends StaticEntity {

    public LightBulb(Position position, String type, String ID, boolean isInteractable) {
        super(position,type, ID, isInteractable);
    }  

    public void lightOn () {
        this.setType("light_bulb_on");
    }
}
