// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Bank {
	public static final int ACCOUNTS = 20;	 // number of accounts
	public static final Transaction NULL_TRANSACTION = new Transaction(-1, -1, -1);
	
	private final List<Account> accounts = new ArrayList<>();
	private BlockingQueue<Transaction> transactions = new LinkedBlockingQueue<>();
	private final List<Worker> workers = new ArrayList<>();

	private CountDownLatch latch;

	public Bank() {
		for (int i = 0; i < ACCOUNTS; i++) {
			accounts.add(new Account(this, i, 1000));
		}
	}

	/*
	 Reads transaction data (from/to/amt) from a file for processing.
	 (provided code)
	 */
	public void readFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));

		// Use stream tokenizer to get successive words from file
		StreamTokenizer tokenizer = new StreamTokenizer(reader);

		while (true) {
			int read = tokenizer.nextToken();
			if (read == StreamTokenizer.TT_EOF) break;  // detect EOF
			int from = (int)tokenizer.nval;

			tokenizer.nextToken();
			int to = (int)tokenizer.nval;

			tokenizer.nextToken();
			int amount = (int)tokenizer.nval;

			// Use the from/to/amount
			transactions.add(new Transaction(from, to, amount));
			// YOUR CODE HERE
		}
	}

	/*
	 Processes one file of transaction data
	 -fork off workers
	 -read file into the buffer
	 -wait for the workers to finish
	*/
	public void processFile(String file, int numWorkers) throws InterruptedException, IOException {
		latch = new CountDownLatch(numWorkers);
		for (int i = 0; i < numWorkers; i++) {
			workers.add(new Worker());
			workers.get(i).start();
		}

		readFile(file);

		for (int i = 0; i < numWorkers; i++) {
			transactions.put(NULL_TRANSACTION);
		}

		latch.await();

		printResults();
	}

	private void printResults() {
		for (Account acc : accounts) {
			System.out.println(acc);
		}
	}

	public List<Account> getAccounts() throws InterruptedException {
		return accounts;
	}
	
	/*
	 Looks at commandline args and calls Bank processing.
	*/
	public static void main(String[] args) throws InterruptedException, IOException {
		// deal with command-lines args
		if (args.length == 0) {
			throw new IllegalArgumentException("Args: transaction-file [num-workers [limit]]");
		}

		String file = args[0];
		
		int numWorkers = 1;
		if (args.length >= 2) {
			numWorkers = Integer.parseInt(args[1]);
		}

		Bank bank = new Bank();
		bank.processFile(file, 4);
	}

	public class Worker extends Thread {
		@Override
		public void run() {
			while (true) {
				Transaction transaction = null;
				try {
					transaction = transactions.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (transaction == NULL_TRANSACTION) {
						break;
					}

					int firstAccountId = transaction.getFrom();
					int secondAccountId = transaction.getTo();
					int amount = transaction.getAmount();

					accounts.get(firstAccountId).moneyOut(amount);
					accounts.get(secondAccountId).moneyIn(amount);


				}
			latch.countDown();
		}
	}
}

