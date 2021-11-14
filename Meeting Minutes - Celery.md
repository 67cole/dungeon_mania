== MILESTONE 1 NOTES ==

16/10/21 (6:00 pm - 11:30 pm)
    -   This marked the start of our project. We read over the specs and discussed the varying parts of the codes to understand what we were implementing. 
    -   We then gave roles on what each person had to do, setting the deadline to the end of week 6 (24/10/21)

        Thomas: UML construction
        Colin: Assumptions so far
        Jeremy: UML construction
        Winston: Timeline (planning for the future)
        Jonathan: UML construction

    -   Thus, we started our parts for the rest of the meeting, conferring if we needed any help.
    -   This roughly took around 5 hours and 30 minutes.


22/10/21 - 23/10/21 (9:00 pm - 2:00 am) 
    -   This was our first standup of many, where we discussed how our progression was going. This took 3 hours, and the meeting was summarised to:

        -   Thomas, Jeremy and Jonathan had issues with the UML, more specifically, how far ahead they had to plan as the project was open-ended. They didn't know if they needed any extra functions yet (helpers). We discussed as a group and decided to implement everything we know for certain so far in the UML, then add more as we further progress.
        -   Colin added some assumptions he thought about for zombies and mercenaries into the file. It was also decided that everyone should contribute to the assumptions file rather than only one person.
        -   We also discussed about what patterns to use for the project; state and composite.

    -   Afterwards, we decided to meet again in a couple of days to start the project as we were pretty much finished with Milestone 1. 


== MILESTONE 2 NOTES ==

24/10/21 - 25/10/21 (6:00 pm - 2:00 am)
    -   This meeting took 8 hours, due to the group just beginning to start the project. We decided to assign functions and roles for people to do:

        Thomas: DungeonManiaController.java (functions including and helping others that relate to the controller)
        Colin: everything to do with Moving Entities (character, spider, zombie toast, mercenary)
        Jonathan: everything to do with Collectables and Buildables (treasure, key, health potion, invincibility potion, invisiblity potion, wood, arrow, bomb, sword, armour, one ring, bow, shield)
        Winston: everything to do with Static Entities (wall, exit, boulder, floor switch, door, portal, zombie toast spawner)
        Jeremy: assisted with Jonathan in Collectables and Buildables

    -   After deciding who was doing what, we pair programmed for the rest of the time, as most of our functions intertwined so we needed each other's help. 
    -   Colin, Thomas and Winston coded for the rest of the night together as the rest went to sleep. 
    -   We placed questions we didn't know into a folder to ask our tutor for next tutorial


27/10/21 - 28/10/21 (6:00 pm - 3:00 am)
    -   This meeting took 9 hours, we had our second standup to discuss what progress we made, where we were at and if we needed any help. The standup included:

        -   Colin finished most of moving entities. He explained how he used inheritence and polymorphism to implement the moving entities (using a super class and subclasses). However, as he had work in the weekends, he gave the Spider functionalities to Jeremy (45 mins)
        -   Jonathan implemented most of the Collectables (everything besides invincibility and invisiblity potions). He explained that he worked it similar to Colin's moving entities, however, he was stuck on invincibility potion. We decided that Colin was going to do it as it worked similarly to the mercenary movements. (50 minutes)
        -   Winston finished all his static entity functions. He explained all his code, however, there was a few errors we luckily pointed out, and got fixed immediately. We discussed what to do next - this involves working with Colin to make interactions work between the character and the static entities. (1 hour)
        -   Thomas implemented functions within the controller, such as saveGame and loadGame. As they were the more difficult functions, we allocated a lot of time to help him do the functions by group programming instead. Thomas also helped with other functions, for example, starting the base for the tick function. (1 hour 30 minutes)

    -   Overall, today, we made alot of progress. Our project is kicking off and our frontend is somewhat working. 
    -    We pair programmed for the rest of the night, as most functions overlapped with one another. 
    -   As Jonathan was almost complete with Collectables, we decided Jeremy should work on the Spider functionality but also battling with other moving entities.


30/10/21 (12:00 pm - 8:00 pm)
    -   This meeting lasted 8 hours and we had our third standup to discuss progression, help and anything related to the project. The standup included:

        -   As the specs required us to build a MVP (minimum viable product), we decided to leave out 3 functions for Milestone 3. This included interact, build and gameModes. The rest of the project was to be done by Sunday.  
        -   Colin finished all of his moving entity. He had trouble in moving the mercenary, but was assisted by the group and finished. He also finished the invinciblity potion in which he explained the movements and functionality behind it. (40 minutes)
        -   Jonathan finished his Collectables and explained how he did invisibility potion. He helped Jeremy with battle, then cleaned up his code along with his tests. He also showed the group his updated UML, where we contributed more on his diagram as we added a few more helper functions. (1 hour 20 minutes)
        -   As we added more functions to the controller, Thomas had trouble with saving the game then loading it again. As a group, we helped him out by describing the purpose of each function, what integers to pass through and save. We worked on saveGame and loadGame for the rest of the night. (2 hours)
        -   Winston finished off implementing the interactions with Character and his static entities with Colin. He raised a question with the portal interaction, asking how portals would work with different portals and we figured it was with the colours the JSON file was giving us. Thus, portals was implemented correctly. Winston then asked for each of our progression on the project, so that he could fill in the timeline correctly. After finishing, he showed it to us and we were happy :)
        -   Jeremy implemented spider and demonstrated his code to us; he had problems in the movement of the spider, especially the condition where spiders cannot spawn below boulders, so Colin and Winston helped him out with it. After successfully finishing spider, he explained his code of battling but also had a few problems with the character or entity dying. Jonathan helped him out for the rest of the night.

    -   After our standup meeting, we pair programmed for the rest of the night, finishing off our respective functions. Our plan was to complete it all on Sunday, leaving us a day leeway for fixing and cleaning up any code/tests, and adding javadocs.


31/10/21 (7:00 pm - 11:00 pm)
    -   As the final meeting before Milestone 1/2 was due, we decided to have one last meeting, discussing our project. This meeting lasted for 4 hours. Our fourth standup was for us to provide any suggestions to make the code neater:
        
        -   Finish javadoc for all respective functions
        -   To avoid overnesting, place code in helper functions to make it neater
        -   Move the helper functions to the bottom of the file
        -   Add comments for tests to label what they do

    -   We also checked up on our UML, timeline and assumptions, making sure it was in line and up to date with our project. Any additional updates were done so. 
    -   Our git issues board was cleaned up also, closing issues that we've finished. 
    -   This whole meeting was about improving the project visually and cleaning up what we've done so far, in which we thought we cleaned up well. 

    -   Before our meeting ended, we had one merging error with the master and Jeremy's branch, where we worked together to fix. This took us roughly 30 minutes as the merge conflicts were ugly (partly due to the previous merging of other branches concurrently).

    -   Overall, we were happy with the progress we've made with Milestone 1 and 2. The code was sufficient enough to build an MVP! :)  


== MILESTONE 3 NOTES ==

4/11/21 (6:00 pm - 12:00 am)
    - This meeting lasted 6 hours as we began to read/understand the spec, and delegate tasks for the team to do;

        Colin: DungeonGenerator, Hydra, Assassin, Interact (from milestone 2)
        Jeremy: Dijkstra Path Finding
        Thomas: Time Turner, Time travelling portal, time rules, save game and load game
        Jonathan: Collectable Entities (Sunstone, anduril, sceptre, midnight armour)
        Winston: Swamp tiles, lightbulbs, switch doors, wires, and their logic
        For all: Any design/animation for frontend

    - After we decided what to do, we pair programmed to create tests first.
    - Assumptions were made for any confusing things from the spec for Milestone 3
    - UML was also started by Jonathan for any new functions that we have planned
    - Once tests were done, some implementation was started, with review from each team member

    - Overall, today was a lot of progress as we spent a lot of time planning/perfecting

11/11/21 (4:00pm to 10:00 am)
    - This meeting lasted 6 hours, and the purpose was to discuss progression, and ask for any assistance or advice for people's respective tasks

    - Colin and Winston took around 1-2 hours to help Jeremy with his implementation of Dijkstra. This was because interact needed this to be finished first.
    - Jonathan worked on implementing and understanding the new collectable entities that need to be made
    - Thomas began implementing time turner, using code that was already been made for save/load game as a framework
    - The rest of the time, we were focused on working on our respective functions
    - Jeremy added some changes to the frontend, including front scren music, player png, and in game music

    - Overall, today was useful in progressing with our Milestone 3 implementation

13/11/21 (6:00pm to 1:00 am)
    - This meeting lasted 7 hours, and the purpose was to finalise our code as the deadline approaches quickly
    - There were a lot of issues with interact and mercenary, particularly when the mercenary has been bribed - and how this applies with Dijkstra
    - Colin and Jeremy continued to work on bugs with Dijkstra, mercenary and assassin
    - Thomas finished his time turner functions and started working on Time Travelling portals
    - Winston worked on switches/lightbulbs/trapdoors and how they interact with each other. He succeeded getting the first half of the milestone 3 requirements, and decided that the second half (involving XOR, etc) was too time-consuming. Instead, we had him help for swamp tiles and problems that Colin and Jeremy had
    - Jonathan finalised his collectable items and needed to connect them to mercenary/assassin bribing
    - Ultimately, we realised alot of our problems stemmed from Mercenary and Dijkstra. More testing was needed tomorrow (day before deadline)

14/11/21 (12:00pm to 1:00am)
    - This meeting lasted 13 hours. Note: this is because people came in and out
    - As the last day, we needed to finish everything up. 
    - Thomas realised we had some issues that stemmed from our Milestone 2 and spent 2-3 hours trying to find the issue.
        - He realised it was due to some errors with loading in the json files from main/resources instead of test/resources. 
    - Thomas and Jeremy added animations into the frontend, including a health bar and animating the movement of the player to be smooth
    - Winston worked on polishing the timeline for submission
    - Colin succesfully completed the dungeon generator, and was working on fixing any issues with interact and bribing
    - The rest of time was spent debugging for minor issues with some functions and ensuring that the autotests can run properly.

    Overall, it was a good day of finalisation and we hope that we do well for Milestone 3!
