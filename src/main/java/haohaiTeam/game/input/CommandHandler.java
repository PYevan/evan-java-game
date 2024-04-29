package haohaiTeam.game.input;

import haohaiTeam.game.element.GameElement;

public class CommandHandler implements CommandListener {
    @Override
    public void onPickedCoin(GameElement element) {
        System.out.println("Coin picked!");
        // Handle coin picked event
    }

    @Override
    public void onPickedGem(GameElement element) {
        System.out.println("Gem picked!");
        // Handle gem picked event
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onCO2Generated(int value) {
        System.out.println("Added CO2 " + value);
    }
}
