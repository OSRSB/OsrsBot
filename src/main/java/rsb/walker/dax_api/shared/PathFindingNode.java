package rsb.walker.dax_api.shared;


import rsb.wrappers.subwrap.WalkerTile;

import java.util.Collection;
import java.util.HashSet;

public abstract class PathFindingNode implements Comparable {

    public abstract int getX();
    public abstract int getY();
    public abstract int getZ();

    public abstract Collection<PathFindingNode> getNeighbors();
    public abstract Collection<PathFindingNode> getNeighbors(HashSet<RSRegion> limit);

    @Override
    public int compareTo(Object obj){
        if (!(obj instanceof PathFindingNode)){
            return -1;
        }
        return Integer.compare(NodeInfo.create(this).f, NodeInfo.create((PathFindingNode) obj).f);
    }

    public int distance(PathFindingNode pathFindingNode){
        return (int)((Math.sqrt(
		        Math.pow((pathFindingNode.getX() - getX()), 2) + (Math.pow((pathFindingNode.getY() - getY()), 2)))) * 10);
    }

    public int moveCostDistance(PathFindingNode pathFindingNode){
        return distance(pathFindingNode);
    }

    public WalkerTile getWalkerTile(){
        return new WalkerTile(getX(), getY(), getZ());
    }

    @Override
    public int hashCode(){
        long bits = 7L;
        bits = 31L * bits + Double.doubleToLongBits(getX());
        bits = 31L * bits + Double.doubleToLongBits(getY());
        bits = 31L * bits + Double.doubleToLongBits(getZ());
        return (int) (bits ^ (bits >> 32));
    }

    @Override
    public String toString(){
        return "(" + getX() + "," + getY() + "," + getZ() + ")";
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        if (!(obj instanceof PathFindingNode)){
            return false;
        }
        PathFindingNode pathFindingNode = (PathFindingNode) obj;
        return pathFindingNode.getX() == getX() && pathFindingNode.getY() == getY() && pathFindingNode.getZ() == getZ();

    }
}
