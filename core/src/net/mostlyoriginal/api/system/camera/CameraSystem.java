package net.mostlyoriginal.api.system.camera;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Setup and manages basic orthographic camera.
 *
 * @author Daan van Yperen
 */
public class CameraSystem extends VoidEntitySystem {

    public final OrthographicCamera camera;
    public final OrthographicCamera guiCamera;
    public final float zoomFactorInverter;

    /**
     * @param zoom How much
     */
    public CameraSystem( float zoom ) {

        zoomFactorInverter = 1f/zoom;

        camera = new OrthographicCamera(Gdx.graphics.getWidth() * zoomFactorInverter, Gdx.graphics.getHeight() * zoomFactorInverter);
        camera.setToOrtho(false, Gdx.graphics.getWidth() * zoomFactorInverter, Gdx.graphics.getHeight() * zoomFactorInverter);
        camera.update();

        guiCamera = new OrthographicCamera(Gdx.graphics.getWidth() * zoomFactorInverter, Gdx.graphics.getHeight() * zoomFactorInverter);
        guiCamera.setToOrtho(false, Gdx.graphics.getWidth() * zoomFactorInverter, Gdx.graphics.getHeight() * zoomFactorInverter);
        guiCamera.update();
    }

    public float getPixelWidth()
    {
        return Gdx.graphics.getWidth() * zoomFactorInverter;
    }

    public float getPixelHeight()
    {
        return Gdx.graphics.getHeight() * zoomFactorInverter;
    }

    @Override
    protected void processSystem() {

    }
}
