package edu.ithaca.dturnbull.bank;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class BankAccountTest {

    @Test
    void getBalanceTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals(200, bankAccount.getBalance(), 0.001);
    }

    @Test
    void withdrawTest() throws InsufficientFundsException{
        BankAccount bankAccount = new BankAccount("a@b.com", 200);
        bankAccount.withdraw(100);

        assertEquals(100, bankAccount.getBalance(), 0.001);
        assertThrows(InsufficientFundsException.class, () -> bankAccount.withdraw(300));
    }

    @Test
    void isEmailValidTest(){
        assertTrue(BankAccount.isEmailValid( "a@b.com"));   // valid email address *
        assertFalse( BankAccount.isEmailValid(""));         // empty string *

        // CHECKING @ SYMBOL
        assertFalse( BankAccount.isEmailValid("ab.com"));    // missing @ symbol *
        assertFalse( BankAccount.isEmailValid("a@b@c.com")); // multiple @ symbols *
        assertFalse( BankAccount.isEmailValid("ab.com@"));   // @ symbol at end

        // CHECKING PREFIX
        assertFalse( BankAccount.isEmailValid("@b.com"));    // missing prefix
        assertFalse( BankAccount.isEmailValid("a@bcom"));    // missing period
        assertFalse( BankAccount.isEmailValid("a@b..com"));    // too many periods in domain
        assertFalse( BankAccount.isEmailValid("a..b@c.com"));    // too many periods in prefix
        assertFalse(BankAccount.isEmailValid(".a@b.com"));   // period at start of prefix

        assertTrue( BankAccount.isEmailValid("A@b.com"));    // valid uppercase letter in prefix

        // CHECKING DOMAIN
        assertFalse( BankAccount.isEmailValid("a@.com"));    // missing domain
        assertFalse( BankAccount.isEmailValid("a@b."));      // missing top-level domain
        assertFalse( BankAccount.isEmailValid("a@b.c"));     // top-level domain too short

        assertTrue( BankAccount.isEmailValid("a@b.co"));    // minimum valid email address
        assertTrue( BankAccount.isEmailValid("a@b-c.com"));    // correct dash in domain
        assertTrue( BankAccount.isEmailValid("a@b.org"));    // valid top-level domain

        // CHECKING SPECIAL CHARACTERS
        assertFalse( BankAccount.isEmailValid("a#@b.com"));  // invalid character in prefix
        assertFalse( BankAccount.isEmailValid("a@b!.com"));  // invalid character in domain
        assertFalse( BankAccount.isEmailValid("a-@b.com")); // invalid character at end of prefix

        assertTrue( BankAccount.isEmailValid("a_b@c.com"));    // valid underscore in prefix
        assertTrue( BankAccount.isEmailValid("a.b@c.com"));    // valid period in prefix
        assertTrue( BankAccount.isEmailValid("a-b@c.com"));    // valid dash in prefix

    }

    @Test
    void constructorTest() {
        BankAccount bankAccount = new BankAccount("a@b.com", 200);

        assertEquals("a@b.com", bankAccount.getEmail());
        assertEquals(200, bankAccount.getBalance(), 0.001);
        //check for exception thrown correctly
        assertThrows(IllegalArgumentException.class, ()-> new BankAccount("", 100));
    }

}