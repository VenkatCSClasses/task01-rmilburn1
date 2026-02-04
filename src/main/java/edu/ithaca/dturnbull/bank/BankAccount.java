package edu.ithaca.dturnbull.bank;

public class BankAccount {

    private String email;
    private double balance;

    /**
     * @throws IllegalArgumentException if email is invalid
     */
    public BankAccount(String email, double startingBalance){
        if (!isEmailValid(email)){
            throw new IllegalArgumentException("Email address: " + email + " is invalid, cannot create account");
        }
        else if (!isAmountValid(startingBalance)) {
            throw new IllegalArgumentException("Balance: " + startingBalance + " is invalid, cannot create account");
        }
        else {
            this.email = email;
            this.balance = startingBalance;
            
        }
    }

    /**
     * Equivalence cases:
     *  Balance is zero
     *  Balance is positive
     *  Balance is negative (is this possible?)
     *  Balance is double
     * @return balance of BankAccount object
    */
    public double getBalance(){
        return balance;
    }

    public String getEmail(){
        return email;
    }

    /**
     * @post reduces the balance by amount if amount is non-negative and smaller than balance
     * @throws InsufficientFundsException if amount is larger than balance
     * @throws InsufficientFundsException if amount is negative 
     * 
     * Equivalence Cases: 
     *    amount is negative, 
     *    amount is positive, 
     *    amount is smaller than base, 
     *    amount is larger than base
     */
    public void withdraw (double amount) throws InsufficientFundsException {
        if (!isAmountValid(amount)) {
            throw new IllegalArgumentException("Balance: " + amount + " is invalid, cannot withdraw");
        }
        else if (amount > 0 && balance >= amount) {
            balance = balance - amount;
        }
        else {
            throw new InsufficientFundsException("Not enough money");
        }
    }


    public static boolean isEmailValid(String email){
        //must not be blank
        if (email == null || email.equals("")) {
            return false;
        }

        //must have one and only one @ symbol
        int atCount = 0;
        for (int i = 0; i < email.length(); i++) {
            if (email.charAt(i) == '@') {
                atCount++;
            }
        }

        if (atCount != 1) {
            return false;
        }

        //split prefix and domain for specific checking
        int atSymbol = email.indexOf('@');
        String prefix = "";
        String domain = "";

        for (int i = 0; i < email.length(); i++) {
            if (i < atSymbol) {
                prefix += email.charAt(i);
            } else if (i > atSymbol) {
                domain += email.charAt(i);
            }
        }

        //prefix and domain must not be empty
        if (prefix.length() == 0 || domain.length() == 0) {
            return false;
        }
        
        //no ".." anywhere
        for (int i = 0; i < email.length() - 1; i++) {
            if (email.charAt(i) == '.' && email.charAt(i + 1) == '.') {
                return false;
            }
        }

        //prefix start & end character rules (., -, _ can't be first or last)
        char prefixFirst = prefix.charAt(0);
        char prefixLast = prefix.charAt(prefix.length() - 1);
        if (prefixFirst == '.' || prefixFirst == '-' || prefixFirst == '_' ||
            prefixLast == '.' || prefixLast == '-' || prefixLast == '_') {
            return false;
        }

        //domain start & end character rules (., -, _, @ can't be first or last)
        char domainFirst = domain.charAt(0);
        char domainLast = domain.charAt(domain.length() - 1);
        if (domainFirst == '.' || domainFirst == '-' || domainFirst == '_' || domainFirst == '@' ||
            domainLast == '.' || domainLast == '-' || domainLast == '_' || domainLast == '@') {
            return false;
        }

        //must have at least two characters following the dot in the domain
        int lastDot = -1;
        for (int i = 0; i < domain.length(); i++) {
            if (domain.charAt(i) == '.') {
                lastDot = i;
            }
        }

        if (lastDot == -1 || domain.length() - lastDot - 1 < 2) {
            return false;
        }

        //validate characters in prefix, ensure no special characters
        for (int i = 0; i < prefix.length(); i++) {
            char current = prefix.charAt(i);
            if ((current < 'a' || current > 'z') &&
                (current < 'A' || current > 'Z') &&
                (current < '0' || current > '9') &&
                (current != '.' && current != '_' && current != '-')) {
                    return false;
                }
        }

        //validate characters in domain, ensure no special characters
        for (int i = 0; i < domain.length(); i++) {
            char current = domain.charAt(i);
            if ((current < 'a' || current > 'z') &&
                (current < 'A' || current > 'Z') &&
                (current < '0' || current > '9') &&
                (current != '.' && current != '_' && current != '-')) {
                    return false;
                }
        }

        return true;
    }

    /**
     * @param amount double amount -- currency (ex. 2.50)
     * @return true if amount is not negative and has no more than two decimal places, false otherwise
     */
    public static boolean isAmountValid(double amount) {
        String amountString = Double.toString(amount);
        if (amount < 0.00 || amountString.substring(amountString.indexOf('.')).length() >3 || amountString.indexOf('.') == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * @param amount must be a monetary value which is positive, non-zero, and has no more than two decimal places
     * @throws IllegalArgumentException if amount is negative, zero, or has more than two decimal places
     */
    public void deposit (double amount) throws IllegalArgumentException {
        if(isAmountValid(amount)) {
            balance += amount;
        }
        else {
            throw new IllegalArgumentException(amount + " is not a valid amount. Please try again.");
        }
    }

    /**
     * @param amount must be a monetary value which is positive, non-zero, and has no more than two decimal places
     * @param destination must be a valid BankAccount object
     * @throws InsufficientFundsException if amount is less than current balance
     * @throws IllegalArgumentException if either parameter is invalid
     */
    public void transfer (double amount, BankAccount destination) throws InsufficientFundsException, IllegalAccessException {
        if (isAmountValid(amount)) {
            withdraw(amount);
            destination.deposit(amount);
        }
        else {
            throw new IllegalArgumentException(amount + " is not a valid amount. Please try again.");
        }
    }
}