package net.mostlyoriginal.game.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.mostlyoriginal.api.component.basic.Bounds;
import net.mostlyoriginal.api.component.basic.Pos;
import net.mostlyoriginal.api.component.graphics.Anim;
import net.mostlyoriginal.api.system.camera.CameraSystem;
import net.mostlyoriginal.game.component.Incappable;
import net.mostlyoriginal.game.component.Level;
import net.mostlyoriginal.game.component.agent.Focusable;
import net.mostlyoriginal.game.manager.FontManager;

/**
 * @author Daan van Yperen
 */
@Wire
public class UILevelSystem extends EntityProcessingSystem {

    public Color DARK_SCORE_COLOR;

    UIWalletSystem uiWalletSystem;

    ComponentMapper<Anim> am;
    ComponentMapper<Focusable> fm;
    ComponentMapper<Pos> pm;
    ComponentMapper<Bounds> bm;
    ComponentMapper<Incappable> im;
    ComponentMapper<Level> lm;

    public UILevelSystem() {
        super(Aspect.getAspectForAll(Level.class, Pos.class, Bounds.class, Anim.class));
    }

    private SpriteBatch batch = new SpriteBatch();
    private CameraSystem cameraSystem;
    FontManager fontManager;

    @Override
    protected void begin() {
        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch.begin();
    }

    @Override
    protected void end() {
        batch.end();
    }

    @Override
    protected void process(Entity e) {

        Level level = lm.get(e);
        Pos pos = pm.get(e);
        Bounds bounds = bm.get(e);
        Anim anim = am.get(e);

        // only render when main anim is visible.
        if ( !anim.visible ) return;

        batch.setColor(1f, 1f, 1f, 1f);
        fontManager.font.setColor(1f,1f,1f,1f);

        String msg = im.has(e) & im.get(e).incapacitated ? "Zz" : "L1";
        fontManager.font.setColor(1f,1f,1f, anim.color.a);
        fontManager.font.draw(batch, msg, pos.x + bounds.cx() - fontManager.font.getBounds(msg).width/2 - 1, pos.y - 2);
    }
}
