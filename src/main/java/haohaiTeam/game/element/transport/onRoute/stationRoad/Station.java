package haohaiTeam.game.element.transport.onRoute.stationRoad;

import haohaiTeam.game.element.GameElement;
import haohaiTeam.game.element.Player;
import haohaiTeam.game.element.PopUp;
import haohaiTeam.game.element.transport.onRoute.auto.AutoMoveTransport;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public abstract class Station extends Road {
    public AutoMoveTransport transportReference; // Reference to the transport

    public static final double CO2_PER_CELL = 0.5;
    public int distanceNext = 99;

    public Station(int x, int y) {
        super(x, y);
        this.walkable = false;
        this.transportReference = null;
    }

    protected void clearTransportReference() {
        this.transportReference = null;
    }

    public void setDistanceNextStation(int distance) {
        this.distanceNext = distance;
    }

    public int getDistanceNextStation() {
        return distanceNext;
    }

    @Override
    public void goingToBeWalkedOverBy(GameElement gameElement) {
        if (gameElement instanceof AutoMoveTransport transport) {
            correctStationMethod(transport);
        }
    }

    public void correctStationMethod(AutoMoveTransport transport) {
        this.transportReference = transport;
        this.transportReference.toggleAutoStation();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                transport.toggleAutoStation();
                clearTransportReference();
            }
        }, 3000);
    }

    @Override
    public void handleNearbyElement(GameElement element) {
//        System.out.println("The next station is at " + distanceNext);
//        new PopUp(this.X, this.Y, "The next station cost " + calculateCO2(distanceNext) + " CO2", 500);
    }

    public int calculateCO2(int distance) {
        return (int) (distance * CO2_PER_CELL); // Calculate CO2 emissions based on distance
    }

    @Override
    public void onBeingCollidedOnYou(GameElement element) {
        String popupMessage = PopUpGenerator.generateRandomPopUp();
        new PopUp(this.X, this.Y, "<html>The next station cost " + calculateCO2(distanceNext) + " CO2<br>" + popupMessage + "</html>", 500);
        if (element instanceof Player player) {
            if (transportReference != null) {
                this.commandListener.onPickedCoin(this);
                this.commandListener.onCO2Generated(calculateCO2(distanceNext));
                this.transportReference.linkElement(element);
                element.linkElement(this.transportReference);
                element.setBeingControlled(false);
                element.moveToLinked();
            }
        }
    }
}
