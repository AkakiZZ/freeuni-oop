// Account.java

/*
 Simple, thread-safe Account class encapsulates
 a balance and a transaction count.
*/
public class Account {
	private final int id;
	private int balance;
	private int transactions;
	
	// It may work out to be handy for the account to
	// have a pointer to its Bank.
	// (a suggestion, not a requirement)
	private final Bank bank;

	public Account(Bank bank, int id, int balance) {
		this.bank = bank;
		this.id = id;
		this.balance = balance;
		transactions = 0;
	}

	public void moneyIn(int amount) {
		updateBalance(amount);
	}

	public void moneyOut(int amount) {
		updateBalance(-amount);
	}

	public int getId() {
		return id;
	}

	public synchronized int getBalance() {
		return balance;
	}

	public synchronized int getTransactions() {
		return transactions;
	}

	public Bank getBank() {
		return bank;
	}

	private synchronized void updateBalance(int amount) {
		balance += amount;
		transactions++;
	}

	@Override
	public String toString() {
		return "Account{" +
				"id=" + id +
				", balance=" + getBalance() +
				", transactions=" + getTransactions() +
				"}";
	}
}
