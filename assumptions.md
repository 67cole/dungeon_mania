Character

    -   The character's starting health is 10
    -   The chracter's starting attack is 2
    -   The character starts off with no inventory
    -   The character starts off with no buildables

Zombie

    -   The zombie's starting health is 3
    -   The zombie's starting attack is 1
    -   The zombie spawner won't spawn anything if there is no avaiable white   space around it
    -   The zombie spawns will check for any white spaces CLOCKWISE, thus zombies will spawn in a clockwise manner 
    -   Zombies can't interact with anything (i.e. moving a boulder)
    -   10 percent chance of spawning with armour

Spider

    -   The spider's starting health is 1
    -   The spider's starting attack is 1
    -   Spiders can't spawn on edge of dungeon 
    -   The max amount of spiders is 8
    -   A spider cannot spawn on or under a boulder
    -   When the player drinks an invincibility potion, the spiders will break out of the cycle and run as far as it can. Once invincibility potion ends, it will begin the cycle again at its new location
    -   The spider can run outside the map whilst invincibility is still active

Mercenary

    -   The mercenary's starting health is 3
    -   The mercenary's starting attack is 2
    -   The mercenary would spawn every 75 ticks
    -   Mercenaries can't interact with anything (i.e. moving a boulder )
    -   Mercenaries will always have a valid spot to move to, it cannot be trapped
    -   10 percent chance of spawning with armour

Portal 

    -   There will always be a corresponding portal to a portal given

Json

    -   The json files provided in testing would be correct

Endings 

    -   Once the requirements for an ending is met the game will automatically end  

TheOneRing
    -   Assumes the inventory is kept after respawn
    -   5 percent chance to receive after a successfull battle

InvincibilityPotion
    -   Assumes durability is not affected
    -   The invincibility potion will last for 10 ticks

InvisibilityPotion
    -   The invisibility potion will last for 10 ticks
    -   If the invincibility potion is taken during invisibility phase, the invisibility phase should take priority

Armour
    -   20 percent chance to receive after a successfull battle


Goals
    - If goals are not given for a dungeon, assume it cannot be completed