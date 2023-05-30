package no.brunostengine.components;

/**
 * The NonInteractable class is a part of the {@link Component} system and is meant to act as a flag for items that are
 * not meant to be interacted with by external objects, the player, or events (for example collision, mouse clicking, etc.).
 * <br>It has no form of implementation and only acts as a "category" for GameObjects.
 * <br><br>If you wish for objects to be only non-collideable, see {@link NonCollideable}.
 */
public class NonInteractable extends NonCollideable {
}
