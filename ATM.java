import java.util.*;
// Card class;
// Bank class;
// Person
// ATM class
// Buttons
abstract class Transaction {
    protected String transactionId;
    protected int amount;
    protected Account account;
    public Transaction(String transactionId, int amount, Account account)
    {
        this.transactionId = transactionId;
        this.amount = amount;
        this.account = account;
    }
    public abstract void execute();
}
class Account {
    private int balance;
    private String accountNumber;
    public Account(int balance, String accountNumber)
    {
        this.balance = balance;
        this.accountNumber = accountNumber;
    }
    public String getAccountNumber()
    {
        return accountNumber;
    }
    public int getBalance()
    {
        return balance;
    }
    public void debit(int amount)
    {
        this.balance-=amount;
    }
    public void credit(int amount)
    {
        this.balance+=amount;
    }
}
class WithdrawalTransaction extends Transaction {
    
    public WithdrawalTransaction(String transactionId, int amount, Account account)
    {
        super(transactionId, amount, account);
    }
    public void execute()
    {
        this.account.debit(amount);
    }
}


class Bank {
    private String bank;

}
class Card {
    private final String cardNumber;
    private final String pin;
    public Card(String cardNumber, String pin)
    {
        this.cardNumber = cardNumber;
        this.pin = pin;
    }
    public String getPin()
    {
        return pin;
    }
    public String getCardNumber()
    {
        return cardNumber;
    }
}
class BankingService {
    private final Map<String, Account> accounts = new HashMap<>();
    public void CreateAccount(String accountNumber, int balance)
    {
        Account account = new Account(balance, accountNumber);
        accounts.put(accountNumber, account);
    }
    public Account getAccount(String accountNumber)
    {
        return accounts.get(accountNumber);
    }  
    public void processTransaction(Transaction transaction)
    {
        transaction.execute();
    }
}
class CashDispencer {
    private int availableCash;
    public CashDispencer(int amount)
    {
        availableCash = amount;
    }
    public synchronized void dispenseCash(int amount)
    {
        if(amount > availableCash)
        throw new IllegalArgumentException("Insufficient cash");
        availableCash-=amount;
        System.out.println("Cash Dispensed " + amount);
    }
}
class ATM {
    private CashDispencer cashDispencer;
    private BankingService bankingService;
    private static final int count = 0;
    
    public ATM(CashDispencer cashDispencer, BankingService bankingService)
    {
        this.cashDispencer = cashDispencer;
        this.bankingService = bankingService;
    }
    
    public int checkBalance(String accountNumber)
    {
        Account acc = bankingService.getAccount(accountNumber);
        return acc.getBalance();
    }
    
    public void WithdraCash(String accountNumber, int cash)
    {
        Account ac = bankingService.getAccount(accountNumber);
        WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction("txn"+ count, cash, ac);
        bankingService.processTransaction(withdrawalTransaction);
        cashDispencer.dispenseCash(cash);        
    }
}
class Solution {
    public static void main(String[] args) {
        System.out.println("ATM design ");
        BankingService bankingService = new BankingService();
        CashDispencer cashDispencer = new CashDispencer(1000);
        ATM atm = new ATM(cashDispencer, bankingService);
        bankingService.CreateAccount("123", 120);
        bankingService.CreateAccount("2312", 1000);
        atm.WithdraCash("123", 10);
        int balance = atm.checkBalance("123");
        System.out.print("Balance for 123 "+balance);
    }
}
