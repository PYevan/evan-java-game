package haohaiTeam.game.element.transport.onRoute.stationRoad;

import java.util.Random;

public class PopUpGenerator {

    private static final String[] POPUP_MESSAGES = {
            "Use public transit, bike.",
            "Walk, bike, share, reduce emissions.",
            "Carpool, bike, walk, share rides.",
            "Promote biking, walking, ride-sharing.",
            "Choose transit, walk, share rides.",
            "Reduce traffic: walk, bike, share.",
            "Bike, walk, share, ease congestion.",
            "Eco-commute: bike, walk, share.",
            "Smart transit, bike, walk.",
            "Opt for biking, walking, sharing."
    };

    public static String generateRandomPopUp() {
        Random random = new Random();
        int index = random.nextInt(POPUP_MESSAGES.length);
        return POPUP_MESSAGES[index];
    }

    public static void main(String[] args) {
        // Example usage:
        for (int i = 0; i < 5; i++) {
            String randomPopUp = generateRandomPopUp();
            System.out.println("Random PopUp Message: " + randomPopUp);
        }
    }
}
