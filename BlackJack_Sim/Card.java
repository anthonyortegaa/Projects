package BlackJack_Sim;

class Card {
    String value;
    String type;

    Card(String value, String type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public String toString() {
        return value + "_" + type;
    }

    public int getValue() {
        if ("AJQK".contains(value)) { // A, J, Q, K
            if (value.equals("A")) {
                return 11;
            }
            return 10;
        }
        return Integer.parseInt(value); // 2-10
    }

    public boolean isAce() {
        return value.equals("A");
    }

    public String getImagePath() {
        return "./Cards/" + toString() + ".png";
    }
}