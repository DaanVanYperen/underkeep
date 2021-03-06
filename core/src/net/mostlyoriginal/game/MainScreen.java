package net.mostlyoriginal.game;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.managers.UuidEntityManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.system.anim.ColorAnimationSystem;
import net.mostlyoriginal.api.system.camera.CameraShakeSystem;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.api.system.camera.EntityCameraSystem;
import net.mostlyoriginal.api.system.interact.AimSystem;
import net.mostlyoriginal.api.system.mouse.MouseCursorSystem;
import net.mostlyoriginal.api.system.physics.*;
import net.mostlyoriginal.api.system.render.AnimRenderSystem;
import net.mostlyoriginal.api.system.script.EntitySpawnerSystem;
import net.mostlyoriginal.api.system.script.SchedulerSystem;
import net.mostlyoriginal.game.manager.AssetSystem;
import net.mostlyoriginal.game.manager.EntityFactorySystem;
import net.mostlyoriginal.game.manager.FontManager;
import net.mostlyoriginal.game.manager.ServantManager;
import net.mostlyoriginal.game.system.*;
import net.mostlyoriginal.game.system.agent.*;

/**
 * @author Daan van Yperen
 */
public class MainScreen implements Screen {

    private final World world;

    public MainScreen( MyGame game) {

        world = new World();

        /** UTILITY - MANAGERS */

        world.setManager(new GroupManager());
        world.setManager(new TagManager());
        world.setManager(new FontManager());
        world.setManager(new UuidEntityManager());
        world.setManager(new ServantManager());

        /** UTILITY - PASSIVE */

        world.setSystem(new CollisionSystem());
        world.setSystem(new EntityFactorySystem());
        world.setSystem(new AssetSystem());
        world.setSystem(new CameraSystem(G.CAMERA_ZOOM_FACTOR));

        /** CONTROL */

        /** Agency Systems (Control and Interact) */
        world.setSystem(new ClickableSystem());
        world.setSystem(new FocusableSystem());

        /** Acting Systems (Control and Interact) */
        //world.setSystem(new PluckableSystem());
        world.setSystem(new SchedulerSystem());
        world.setSystem(new EntitySpawnerSystem());

        world.setSystem(new QuestSystem());
        world.setSystem(new QuesteeWorkSystem());

        world.setSystem(new EruptSystem());

        world.setSystem(new ExpansionBuySystem());
        world.setSystem(new ExpansionPointSystem());
        world.setSystem(new CastleSystem());
        world.setSystem(new BestCastleSystem(game));

        world.setSystem(new IncapacitateSystem());
        world.setSystem(new CastleLevelSystem());
        world.setSystem(new QueenTaxSystem());
        world.setSystem(new HintSystem());

        /** SIMULATE */

        /** Physics systems that apply a vector on an entity */
        world.setSystem(new HomingSystem());
        world.setSystem(new GravitySystem());
        /** Physics systems that constrain the movement*/
        world.setSystem(new ClampedSystem());
        /** Physics systems that move the entity to an absolute location. */
        world.setSystem(new AttachmentSystem());
        world.setSystem(new InbetweenSystem());
        world.setSystem(new MouseCursorSystem());
        world.setSystem(new QuesteePositioningSystem());
        world.setSystem(new BobbingSystem());
        /** apply velocity */
        world.setSystem(new PhysicsSystem());

        /** Post Physics Simulations */
        world.setSystem(new AimSystem());

        /** PRE-RENDER */

        world.setSystem(new ColorAnimationSystem());

        /** RENDER */

        /** Camera */
        world.setSystem(new EntityCameraSystem());
        world.setSystem(new CameraShakeSystem());

        /** Rendering */
        //world.setSystem(new MapRenderSystem());
        world.setSystem(new AnimRenderSystem(G.CAMERA_ZOOM_FACTOR));

        world.setSystem(new UIWalletSystem());
        world.setSystem(new UICostSystem());
        world.setSystem(new UILevelSystem());
        world.setSystem(new UITitleSystem());
        world.setSystem(new UIHintSystem());
        world.setSystem(new UITutorialSystem());

        world.setSystem(new DirectorSystem());

        world.initialize();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
  		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // limit world delta to prevent clipping through walls.
        world.setDelta(MathUtils.clamp(delta, 0, 1 / 15f));
        //world.delta = 0.25f;
        world.process();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
