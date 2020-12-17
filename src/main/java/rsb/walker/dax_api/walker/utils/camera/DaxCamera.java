package rsb.walker.dax_api.walker.utils.camera;

import rsb.methods.Web;
import rsb.util.StdRandom;
import rsb.walker.dax_api.walker.utils.AccurateMouse;
import rsb.wrappers.common.Positionable;

import java.awt.*;

import static rsb.walker.dax_api.walker.utils.camera.CameraCalculations.distanceBetweenTwoAngles;


public class DaxCamera {

    private static float PIXEL_TO_ANGLE_RATIO = 2.253731343283582F, PIXEL_TO_ROTATION_RATIO = 2.966666666666667F;

    public static void focus(Positionable positionable){
        positionCamera(
		        CameraCalculations.getAngleToTile(positionable), CameraCalculations.getRotationToTile(positionable));
    }

    public static void positionCamera(int angle, int rotation){
        if (!CameraAction.isMiddleMouseCameraOn()){
            return;
        }
        int currentAngle = Web.methods.camera.getPitch(), currentRotation = Web.methods.camera.getAngle();

        int cameraAngleDifference = angle - currentAngle;
        int cameraRotationDifference =  distanceBetweenTwoAngles(currentRotation, rotation), rotationDirection;
        if (CameraCalculations.normalizeRotation(currentRotation + cameraRotationDifference) == rotation){
            rotationDirection = -1; //TURN RIGHT
        } else {
            rotationDirection = 1;
        }
        Point point = new Point(Web.methods.mouse.getLocation().getX(), Web.methods.mouse.getLocation().getY());
        if (!getGameScreen().contains(point)){
            Web.methods.mouse.move(AccurateMouse.getRandomPoint((getGameScreen())));
        }

        point = new Point(Web.methods.mouse.getLocation().getX(), Web.methods.mouse.getLocation().getY());
        Point startingPoint = point;
        Point endingPoint = new Point(startingPoint);

        int dx = rotationDirection * cameraRotationDifference;
        int dy = cameraAngleDifference;

        endingPoint.translate(rotationToPixel(dx), angleToPixel(dy));


        Web.methods.mouse.drag((int) endingPoint.getX(), (int) endingPoint.getY());

    }

    public static Rectangle getGameScreen(){
        return new Rectangle(0,0, 765, 503);
    }

    private static int rotationToPixel(int rotation){
        return (int) (rotation * PIXEL_TO_ROTATION_RATIO);
    }

    private static int angleToPixel(int angle){
        return (int) (angle * PIXEL_TO_ANGLE_RATIO);
    }

    private static Point generatePoint(Rectangle rectangle){
        return new Point(StdRandom.uniform(rectangle.x, rectangle.x + rectangle.width), StdRandom.uniform(rectangle.y, rectangle.y + rectangle.height));
    }
}
