package no.brunostengine.components;

/**
 * The NonInteractable class is a part of the {@link Component} system and is meant to act as a flag for items that are
 * not meant to be collided with by any external objects or the player, but still accessible to events (such as mouse clicking, etc.).
 * This only works during runtime, meaning that GameObjects are still interactable through code.
 * <br>It has no form of implementation and only acts as a "category" for GameObjects.
 * <br><br>If you wish for objects to not be interacted with by events, see {@link NonInteractable}.
 */
public class NonCollideable extends Component{
}
