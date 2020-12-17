package rsb.walker.dax_api.shared;

import rsb.wrappers.subwrap.WalkerTile;
import rsb.walker.dax_api.shared.jsonSimple.JSONObject;
import rsb.walker.dax_api.shared.jsonSimple.JSONValue;

import java.util.ArrayList;

@Deprecated
public class GetPathResponseContainer {

    private Status status;
    private String response;
    private ArrayList<String> path;
    private PlayerInformation playerInformation;
    private ArrayList<WalkerTile> WalkerTilePath;

    public static GetPathResponseContainer CLIENT_ERROR = new GetPathResponseContainer(Status.CLIENT_ERROR, "No problem with server. Client issue.", new ArrayList<>(), null, true);

    public GetPathResponseContainer(Status status, String response, ArrayList<PathFindingNode> path, PlayerInformation playerInformation){
        this(status, response, new ArrayList<>(), playerInformation, false);
        if (path != null) {
            for (PathFindingNode pathFindingNode : path) {
                this.path.add(pathFindingNode.getX() + " " + pathFindingNode.getY() + " " + pathFindingNode.getZ());
            }
        }
    }

    private GetPathResponseContainer(Status status, String response, ArrayList<String> path, PlayerInformation playerInformation, boolean parsed){
        this.status = status;
        this.response = response;
        this.path = path;
        this.playerInformation = playerInformation;
    }

    public Status getStatus() {
        return status;
    }

    public String getResponse() {
        return response;
    }

    public ArrayList<String> getPath() {
        return path;
    }

    public enum Status {
        UNWALKABLE,
        NOT_MAPPED,
        NO_WEB_PATH,
        COULD_NOT_FIND_PATH,
        SUCCESS,
        UNKNOWN,
        CLIENT_ERROR
        ;
        private static final long serialVersionUID = 42L;
        private static Status fromString(String s){
            for (Status status : values()){
                if (s.equals(status.toString())){
                    return status;
                }
            }
            return UNKNOWN;
        }
    }

    public ArrayList<WalkerTile> getWalkerTilePath(){
        if (WalkerTilePath != null){
            return WalkerTilePath;
        }
        WalkerTilePath = new ArrayList<>();
        for (String tile : path){
            String[] coords = tile.split(" ");
            int x = Integer.parseInt(coords[0]), y = Integer.parseInt(coords[1]), z = Integer.parseInt(coords[2]);
            WalkerTilePath.add(new WalkerTile(x, y, z));
        }
        return WalkerTilePath;
    }

    public static GetPathResponseContainer fromJSONString(String s){
        try {
            JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(s);
            Status status = Status.fromString(jsonObject.get("status").toString());
            String response = (String) jsonObject.get("response");
            ArrayList<String> path = (ArrayList<String>) jsonObject.get("path");
            PlayerInformation playerInformation = PlayerInformation.fromJSONString(jsonObject.get("playerInformation"));
            return new GetPathResponseContainer(status, response, path, playerInformation, true);
        } catch (Exception e){
            return new GetPathResponseContainer(Status.UNKNOWN, "[ERROR] " + s, new ArrayList<>(), null, true);
        }
    }

    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", status.toString());
        jsonObject.put("response", response);
        jsonObject.put("pathLength", path.size());
        jsonObject.put("path", path);
        jsonObject.put("playerInformation", playerInformation != null ? playerInformation.toJSON() : null);
        return jsonObject;
    }

    @Override
    public String toString(){
        return toJson().toString();
    }

}
