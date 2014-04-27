package net.mostlyoriginal.game.system.agent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.physics.Homing;
import net.mostlyoriginal.api.utils.SafeEntityReference;
import net.mostlyoriginal.game.component.Quest;
import net.mostlyoriginal.game.component.Questee;
import net.mostlyoriginal.game.component.agent.Clickable;
import net.mostlyoriginal.game.manager.AssetSystem;
import net.mostlyoriginal.game.manager.EntityFactorySystem;
import net.mostlyoriginal.game.system.DirectorSystem;
import net.mostlyoriginal.game.system.UIWalletSystem;

/**
 * Sends selected Qeestee on quest after the quest is clicked.
 *
 * @author Daan van Yperen
 */
@Wire
public class QuestSystem extends EntityProcessingSystem {

    ComponentMapper<Clickable> cm;
    ComponentMapper<Questee> qm;
    ComponentMapper<Quest> qum;
    ComponentMapper<Pos> pm;
    TagManager tagManager;

    Vector2 vTmp = new Vector2();
    EntityFactorySystem entityFactorySystem;

    UIWalletSystem uiWalletSystem;
    private AssetSystem assetSystem;
    private DirectorSystem directorSystem;

    public QuestSystem() {
        super(Aspect.getAspectForAll(Clickable.class, Pos.class, Quest.class, Bounds.class));
    }

    @Override
    protected void process(Entity e) {

        if ( directorSystem.gameOver )
        {
            e.deleteFromWorld();
            return;
        }

        Quest quest = qum.get(e);
        if (cm.get(e).clicked && quest.workable && tagManager.isRegistered("focus")) {
            activateQuestFor(e, tagManager.getEntity("focus"));
        }

        if (quest.workRemaining <= 0) {
            payoutQuest(e);
            e.deleteFromWorld();
        } else {
            updateSpawnMonsters(quest, e);
        }
    }

    private void updateSpawnMonsters(Quest quest, Entity e) {
        if (quest.spawnMonsters) {
            quest.monsterSpawnCooldown -= world.delta;
            if (quest.monsterSpawnCooldown <= 0) {
                quest.monsterSpawnCooldown = MathUtils.random(quest.monsterSpawnDelayMin, quest.monsterSpawnDelayMax);
                Pos pos = pm.get(e);
                entityFactorySystem.createEntity("marker-monster", (int) (pos.x + 7), (int) (pos.y + 7)).addToWorld();
            }
        }
    }

    private void payoutQuest(Entity e) {
        final Quest quest = qum.get(e);

        assetSystem.playSfx("sfx_squeekytoy");

        if (quest.gold > 0) {
            uiWalletSystem.add(quest.gold, e);
        }

        if (quest.spawnGold) {
            Pos pos = pm.get(e);
            for (int i = 0, s = 3; i < s; i++) {
                vTmp.set(9, 0).setAngle(MathUtils.random(0, 360));

                entityFactorySystem.createEntity(
                        "marker-gold",
                        (int) (pos.x + 7 + vTmp.x), (int) (pos.y + 7 + (int) vTmp.y)).addToWorld();
            }
        }
    }

    private void activateQuestFor(Entity quest, Entity actor) {
        if (qm.has(actor)) {
            final Questee questee = qm.get(actor);
            if (questee.quest == null || !questee.quest.isActive()) {
                tagManager.unregister("focus");
                if ( questee.actionSfx != null ) {
                    assetSystem.playSfx(questee.actionSfx);
                }
                questee.quest = new SafeEntityReference(quest);

                // create tracker that indicates travel to the entity.
                Homing homing = new Homing(new SafeEntityReference(quest), 5, 5);
                homing.speedFactor *=  questee.travelSpeed;

                Entity dot = entityFactorySystem.createEntity("tracker")
                        .addComponent(homing);
                Pos pos = pm.get(quest);
                Pos posDot = pm.get(dot);
                posDot.x = pos.x + 4;
                dot.addToWorld();
                questee.tracker = dot.getUuid();
            }
        }


    }
}
