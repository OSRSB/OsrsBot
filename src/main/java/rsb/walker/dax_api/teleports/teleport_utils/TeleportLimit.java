package rsb.walker.dax_api.teleports.teleport_utils;

/**
 * Check limits of a teleport, e.g. wilderness level under 20 or 30.
 */
public interface TeleportLimit {
    boolean canCast();
}
