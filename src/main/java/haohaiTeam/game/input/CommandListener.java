package haohaiTeam.game.input;

import haohaiTeam.game.element.GameElement;


public interface CommandListener {
    void onPickedCoin(GameElement element);

    void onPickedGem(GameElement element);

    void onTick();

    void onCO2Generated(int value);

}
