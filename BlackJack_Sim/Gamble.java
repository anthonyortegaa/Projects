package BlackJack_Sim;

public class Gamble {
    private int playerBalance;

    public Gamble() {
        this.playerBalance = 100;
    }

    public int getPlayerBalance() {
        return playerBalance;
    }

    public boolean placeBet(int amount) {
        // Checks if the bet is valid and deducts it from the balance
        if (amount <= 0) {
            System.out.println("Bet amount must be greater than $0.");
            return false;
        }
        if (amount > playerBalance) {
            System.out.println("Insufficient balance to place this bet.");
            return false;
        }
        if (amount % 50 != 0 && amount % 10 != 0 && amount % 1 != 0) {
            System.out.println("Bet must be in $50, $10, or $1 increments.");
            return false;
        }
         playerBalance -= amount;
        // System.out.println("Bet placed: $" + amount + ". Remaining balance: $" + playerBalance);
        return true;
    }

    public void addWinnings(int amount) {
        if (amount > 0) {
            playerBalance += amount;
            System.out.println("New balance: $" + playerBalance);
        }
    }

    public void resetBalance() {
        playerBalance = 100;
        System.out.println("Balance reset to $100.");
    }
    public boolean addToBet(int amount) {
    if (amount <= playerBalance) {
        playerBalance -= amount;
        return true;
    } else {
        return false;
    }
    }

    public int finalizeBet(int currentBet) {
        return currentBet;
    }

    public void clearBet(int currentBet) {
        playerBalance += currentBet;
    }

}
