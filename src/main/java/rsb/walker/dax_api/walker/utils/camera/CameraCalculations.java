package rsb.walker.dax_api.walker.utils.camera;


import rsb.methods.Web;
import rsb.wrappers.subwrap.WalkerTile;
import rsb.wrappers.RSCharacter;
import rsb.wrappers.common.Positionable;

public class CameraCalculations {

    public static int normalizeAngle(int angle) {
        return Calculations.limitRange(angle, 0, 100);
    }

    public static int normalizeRotation(int rotation) {
        return (rotation) % 360;
    }

    public static int distanceBetweenTwoAngles(int alpha, int beta) {
        int phi = Math.abs(beta - alpha) % 360;       // This is either the distance or 360 - distance
        return phi > 180 ? 360 - phi : phi;
    }

    public static int getAngleToTile(Positionable tile) {
        return 100 - (int) (Math.min(new WalkerTile(new WalkerTile(Web.methods.players.getMyPlayer().getLocation())).distanceToDouble(tile), 15) * 4);
    }

    public static int getRotationToTile(RSCharacter target) {
        WalkerTile location = new WalkerTile(target.getLocation());
        RSCharacter.DIRECTION direction = target.getDirectionFacing();
        int cameraRotation = Web.methods.camera.getCharacterAngle(target);
        switch (direction) {
            case N:
                cameraRotation = Web.methods.camera.getTileAngle(location.translate(0, 1));
                break;
            case E:
                cameraRotation = Web.methods.camera.getTileAngle(location.translate(1, 0));
                break;
            case S:
                cameraRotation = Web.methods.camera.getTileAngle(location.translate(0, -1));
                break;
            case W:
                cameraRotation = Web.methods.camera.getTileAngle(location.translate(-1, 0));
                break;
            case NE:
                cameraRotation = Web.methods.camera.getTileAngle(location.translate(1, 1));
                break;
            case NW:
                cameraRotation = Web.methods.camera.getTileAngle(location.translate(-1, 1));
                break;
            case SE:
                cameraRotation = Web.methods.camera.getTileAngle(location.translate(1, -1));
                break;
            case SW:
                cameraRotation = Web.methods.camera.getTileAngle(location.translate(-1, -1));
                break;

        }
        int currentCameraRotation = Web.methods.camera.getAngle();
        return cameraRotation + (distanceBetweenTwoAngles(cameraRotation + 45, currentCameraRotation) < distanceBetweenTwoAngles(cameraRotation - 45, currentCameraRotation) ? 45 : -45);
    }

    public static int getRotationToTile(Positionable target) {
        int cameraRotation = Web.methods.camera.getTileAngle(target.getLocation());
        int currentCameraRotation = Web.methods.camera.getAngle();
        return cameraRotation + (distanceBetweenTwoAngles(cameraRotation + 45, currentCameraRotation) < distanceBetweenTwoAngles(cameraRotation - 45, currentCameraRotation) ? 45 : -45);
    }

}
