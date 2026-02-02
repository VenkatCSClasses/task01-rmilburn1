package edu.ithaca.dturnbull.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class BankAccountTest {

    @Test
    void getBalanceTest() {
        /* 
        *  Equivalence cases:
        *  Balance is zero
        *  Balance is positive
        *  Balance is negative (is this possible?)
        *  Balance is double 
        */
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        assertEquals(200, bankAccount.getBalance(), 0.001); // Balance is positive, is double

        BankAccount bankAccountTwo = new BankAccount("a@b.com", 0);
        assertEquals(0, bankAccountTwo.getBalance(), 0.001); // Balance is zero

        BankAccount bankAccountThree = new BankAccount("a@b.com", -5);
        assertEquals(-5, bankAccountThree.getBalance(), 0.001); // Balance is negative

        BankAccount bankAccountFour = new BankAccount("a@b.com", 200.00);
        assertEquals(200, bankAccountFour.getBalance(), 0.001); // Balance is double, truncated

        BankAccount bankAccountFive = new BankAccount("a@b.com", 0.00);
        assertEquals(0, bankAccountFive.getBalance(), 0.001); // Balance is zero, double, truncated


    }

    @Test
    void withdrawTest() throws InsufficientFundsException{
        /*
         * Equivalence Cases: 
         *  amount is negative, 
         *  amount is positive, 
         *  amount is less than balance, 
         *  amount is greater than balance
         */

        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        bankAccount.withdraw(100);

        assertEquals(100, bankAccount.getBalance(), 0.001);
        assertThrows(InsufficientFundsException.class, () -> bankAccount.withdraw(300));

        // Equivalence case: amount is positive
        bankAccount.withdraw(0.01); // Equivalence case: positive and less than balance
        assertEquals(99.99, bankAccount.getBalance(), 0.001); // Border case: minimum valid withdrawal
        bankAccount.withdraw(10); // Equivalence case: positive and less than balance
        assertEquals(89.99, bankAccount.getBalance(), 0.001); // Middle case: valid withdrawal
        bankAccount.withdraw(89.99); // Equivalence case: positive and equal to balance
        assertEquals(0, bankAccount.getBalance(), 0.001); // Border case: maximum valid withdrawal


        BankAccount bankAccountTwo = new BankAccount("a@b.com", 100);
        // Equivalence case: amount is negative
        assertThrows(InsufficientFundsException.class, () -> bankAccountTwo.withdraw(-0.01)); // Border case: minimum negative amount
        assertThrows(InsufficientFundsException.class, () -> bankAccountTwo.withdraw(-50)); // Middle case: negative value < one cent
        assertThrows(InsufficientFundsException.class, () -> bankAccountTwo.withdraw(-100.01)); // Border case: minimum negative value < (balance * -1)

        // Equivalence case: amount is greater than balance
        assertThrows(InsufficientFundsException.class, () -> bankAccountTwo.withdraw(100.01)); // Equivalence case: positive and greater than balance // Border case: minimum withdrawal value that is > balance
        assertThrows(InsufficientFundsException.class, () -> bankAccountTwo.withdraw(200)); // Equivalence case: positive and greater than balance // Middle case: withdrawal value > balance
        bankAccountTwo.withdraw(0.01); // Equivalence case: positive and less than balance
        assertEquals(99.99, bankAccountTwo.getBalance(), 0.001); // Border case: minimum valid withdrawal
    }

    @Test
    void isEmailValidTest(){
        assertTrue(BankAccount.isEmailValid( "a@b.com")); //equivalence class - base case, valid format
        assertTrue(BankAccount.isEmailValid( "aaa@b.com")); //equivalence class - "middle" case (neither min nor max prefix length), valid format
        assertFalse( BankAccount.isEmailValid("")); //equivalence class - null input (border case, minimum length of email)

        // CHECKING @ SYMBOL
        assertFalse( BankAccount.isEmailValid("ab.com")); //equivalence class - missing @ symbol, border case (min number of @ symbols)
        assertFalse( BankAccount.isEmailValid("a@b@c.com")); //equivalence class - multiple @ symbols, middle case (>1 @ symbol)
        assertFalse( BankAccount.isEmailValid("ab.com@")); //equivalence class - improper @ symbol position (border case, minimum length of domain)

        // CHECKING PREFIX
        assertFalse( BankAccount.isEmailValid("@b.com")); //equivalence class - missing prefix (border case, minimum length of prefix)
        assertFalse( BankAccount.isEmailValid("a@bcom")); //equivalence class - missing . (border case: 1> .)
        assertFalse( BankAccount.isEmailValid("a@b..com")); //equivalence class - multiple . in domain (border case: min number of invalid periods >1)
        assertFalse( BankAccount.isEmailValid("a..b@c.com")); //equivalence class - multiple . in prefix (border case: >1 . consecutively)
        assertFalse( BankAccount.isEmailValid("a...b@c.com")); //equivalence class - multiple . in prefix (middle case: >1 . consecutively)
        assertFalse(BankAccount.isEmailValid(".a@b.com")); //equivalence class - improper . position in prefix

        assertTrue( BankAccount.isEmailValid("A@b.com")); //equivalence class - base case, valid format
        assertTrue( BankAccount.isEmailValid("AAA@b.com")); //equivalence class - middle case (prefix len >1), valid format
        assertTrue( BankAccount.isEmailValid("AaA@b.com")); //equivalence class - middle case (prefix len >1 for mixed-case prefix), valid format        

        // CHECKING DOMAIN
        assertFalse( BankAccount.isEmailValid("a@.com")); //equivalence class - missing part of domain
        assertFalse( BankAccount.isEmailValid("a@b.")); //equivalence class - missing part of domain
        assertFalse( BankAccount.isEmailValid("a@b.c")); //equivalence class - domain length less than 2 (border case, minimum length of domain)

        assertTrue( BankAccount.isEmailValid("a@b.co")); //equivalence class - base case, valid format (border case, minimum length of domain)
        assertTrue( BankAccount.isEmailValid("a@b-c.com")); //equivalence class - proper - usage in domain
        assertTrue( BankAccount.isEmailValid("a@b.org")); //equivalence class - different type of domain

        // CHECKING SPECIAL CHARACTERS
        assertFalse( BankAccount.isEmailValid("a#@b.com")); //equivalence class - invalid character, # in prefix, (border case: min number of invalid special chars)
        assertFalse( BankAccount.isEmailValid("a##@b.com")); //equivalence class - invalid character, # in prefix, (middle case: number of invalid special chars >1)
        assertFalse( BankAccount.isEmailValid("a@b!.com"));  //equivalence class - invalid character, ! in domain (border case: min number of invalid special chars)
        assertFalse( BankAccount.isEmailValid("a@b!#.com")); //equivalence class - invalid character, # in prefix, (middle case: number of invalid special chars >1)
        assertFalse( BankAccount.isEmailValid("a-@b.com")); //equivalence class - invalid character, - at end of prefix

        assertTrue( BankAccount.isEmailValid("a_b@c.com")); //equivalence class - valid character, _ in middle of prefix, (border case: min length prefix with valid _)
        assertTrue( BankAccount.isEmailValid("a.b@c.com")); //equivalence class - valid character, . in middle of prefix, (border case: min length prefix with valid .)
        assertTrue( BankAccount.isEmailValid("a-b@c.com")); //equivalence class - valid character, - in middle of prefix, (border case: min length prefix with valid -)
        assertTrue( BankAccount.isEmailValid("a_bc@d.com")); //equivalence class - valid character, _ in middle of prefix, (middle case: prefix len >3)
        assertTrue( BankAccount.isEmailValid("a.bc@d.com")); //equivalence class - valid character, . in middle of prefix, (middle case: prefix len >3)
        assertTrue( BankAccount.isEmailValid("a-bc@d.com")); //equivalence class - valid character, - in middle of prefix, (middle case: prefix len >3)

        // CHECK SPACES - equivalence class for all is improper spaces throughout prefixes and domains
        // All are border cases: min number of spaces to render address invalid
        assertFalse(BankAccount.isEmailValid("a @b.com"));
        assertFalse(BankAccount.isEmailValid("a@ b.com"));
        assertFalse(BankAccount.isEmailValid("a@b. com"));
        assertFalse(BankAccount.isEmailValid("a@b.c om"));
        assertFalse(BankAccount.isEmailValid("a@b.com "));
        assertFalse(BankAccount.isEmailValid(" a@b.com"));
        assertFalse(BankAccount.isEmailValid("a b@b.com"));
        // All are middle cases: >1 space
        assertFalse(BankAccount.isEmailValid("a  @b.com"));
        assertFalse(BankAccount.isEmailValid("a@  b.com"));
        assertFalse(BankAccount.isEmailValid("a@b.  com"));
        assertFalse(BankAccount.isEmailValid("a@b.c  om"));
        assertFalse(BankAccount.isEmailValid("a@b.com  "));
        assertFalse(BankAccount.isEmailValid("  a@b.com"));
        assertFalse(BankAccount.isEmailValid("a  b@b.com"));
        assertFalse(BankAccount.isEmailValid("a @b.c om"));
        assertFalse(BankAccount.isEmailValid("a@ b.com "));

        // Missing equivalence classes: (Added for Task-03)
        assertFalse(BankAccount.isEmailValid(null)); //  If email is null rather than an empty string
        assertTrue(BankAccount.isEmailValid("a@B.com")); //  Uppercase letters in domain, (border case: min domain len)
        assertTrue(BankAccount.isEmailValid("a@BC.com")); //  Uppercase letters in domain, (middle case: domain len >1)  
        assertTrue(BankAccount.isEmailValid("a@b.COM")); //  Uppercase letters in suffix, (middle case: suffix len >2)
        assertTrue(BankAccount.isEmailValid("a@b.CO")); //  Uppercase letters in suffix, (border case: min suffix len)


    }

    @Test
    void constructorTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals("a@b.com", bankAccount.getEmail());
        assertEquals(200, bankAccount.getBalance(), 0.001);
        //check for exception thrown correctly
        assertThrows(IllegalArgumentException.class, ()-> new BankAccount("", 100));
    }

    @Test
    void isAmountValidTest() {
        // Base case
        assertTrue(BankAccount.isAmountValid(0.00));

        // Valid cases
        assertTrue(BankAccount.isAmountValid(1.00)); // Middle case
        assertTrue(BankAccount.isAmountValid(10.00)); // Middle case
        assertTrue(BankAccount.isAmountValid(0.01)); // Edge case (minimum valid amount)
        assertTrue(BankAccount.isAmountValid(0.10)); // Middle case
        assertTrue(BankAccount.isAmountValid(1)); // Middle case
        assertTrue(BankAccount.isAmountValid(10)); // Middle case

        // Amount is not negative
        assertFalse(BankAccount.isAmountValid(-0.01)); // Edge case (minimum invalid amount)
        assertFalse(BankAccount.isAmountValid(-1)); // Middle case (checking without decimals)
        assertFalse(BankAccount.isAmountValid(-1.00)); // Middle case (checking with decimals)
        
        // Amount has no more than two decimal places
        assertFalse(BankAccount.isAmountValid(0.000)); // Edge case (minimum invalid amount of decimal places)
        assertFalse(BankAccount.isAmountValid(0.001)); // Edge case (minimum invalid amount)
        assertFalse(BankAccount.isAmountValid(-0.000)); // Edge case (minimum invalid amount of decimal places, negative)
        assertFalse(BankAccount.isAmountValid(-0.001)); // Edge case (minimum invalid amount, negative)
        assertFalse(BankAccount.isAmountValid(1.000)); // Middle case
        assertFalse(BankAccount.isAmountValid(1.00000)); // Middle case
    }

}