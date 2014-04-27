package net.mostlyoriginal.game.system.agent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.managers.UuidEntityManager;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.api.component.physics.Physics;
import net.mostlyoriginal.api.utils.EntityUtil;
import net.mostlyoriginal.game.component.Damage;
import net.mostlyoriginal.game.component.Quest;
import net.mostlyoriginal.game.component.Questee;
import net.mostlyoriginal.game.component.agent.Clickable;
import net.mostlyoriginal.game.component.agent.Focusable;
import net.mostlyoriginal.game.system.DamageSystem;

/**
 * Questee entities work towards assigned quests.
 *
 * @author Daan van Yperen
 */
@Wire
public class QuesteeWorkSystem extends EntityProcessingSystem {

    ComponentMapper<Clickable> cm;
    ComponentMapper<Focusable> fm;
    ComponentMapper<Questee> qm;
    ComponentMapper<Damage> dm;
    ComponentMapper<Quest> qum;
    ComponentMapper<Physics> pm;
    TagManager tagManager;
    DamageSystem damageSystem;

    UuidEntityManager uuidEntityManager;

    public QuesteeWorkSystem() {
        super(Aspect.getAspectForAll(Questee.class));
    }

    @Override
    protected void process(Entity e) {

        Questee questee = qm.get(e);
        if ( questee.quest != null )
        {
            Entity tracker = uuidEntityManager.getEntity(questee.tracker);

            if ( questee.quest.isActive() ) {
                Entity questEntity = questee.quest.get();
                Quest quest = qum.get(questEntity);

                // only allow working on quest when near it.
                if (quest.workRemaining > 0 && tracker != null && tracker.isActive() && (EntityUtil.distance(tracker, questEntity) < 20)) {
                    workOnQuest(e, quest);
                }

                // forbid focus while working.
                if ( fm.has(e))
                {

                    e.removeComponent(Focusable.class).changedInWorld();
                }
            } else {

                // restore focus when done working.
                if ( !fm.has(e))
                {
                    if ( tracker != null && tracker.isActive() )
                    {
                        surfaceTracker(tracker);
                    }

                    e.addComponent(new Focusable()).changedInWorld();
                }
            }
        }
    }

    private void surfaceTracker(Entity tracker) {


        // shoot tracker up and purge.
        pm.get(tracker).friction=0;
        pm.get(tracker).vy=10;
        // @todo move to tracker system.
        tracker.deleteFromWorld();
    }

    private void workOnQuest(Entity actor, Quest quest) {
        quest.workRemaining -= world.delta;
        if ( quest.dangerous )
        {
            // create damage effect in the last second.
            if ( quest.workRemaining <= 1 ) {
                damageSystem.makeDamaged(actor);
            }
        }
    }
}
