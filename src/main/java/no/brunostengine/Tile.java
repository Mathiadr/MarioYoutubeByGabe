package no.brunostengine;

import no.brunostengine.components.Component;

/**
 * The Tile class represents a single grid-square within a {@link Tilemap} instance and contains information where
 * it is located.
 */
public class Tile extends Component {
    public transient Tilemap parentTilemap;
    public int row, column;
}
