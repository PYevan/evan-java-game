package haohaiTeam.game.element.transport;

import haohaiTeam.game.element.GameElement;

public abstract class TransportMode extends GameElement {
    protected int carbonFootprint;

    public TransportMode(int x, int y) {
        super(x, y);
        this.carbonFootprint = 0; // These are the defaults properties
    }


}