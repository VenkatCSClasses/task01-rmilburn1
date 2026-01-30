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
        assertTrue(BankAccount.isEmailValid( "a@b.com")); //equivalence class - base case, valid format
        assertFalse( BankAccount.isEmailValid("")); //equivalence class - null input (border case, minimum length of email)

        // CHECKING @ SYMBOL
        assertFalse( BankAccount.isEmailValid("ab.com")); //equivalence class - missing @ symbol
        assertFalse( BankAccount.isEmailValid("a@b@c.com")); //equivalence class - multiple @ symbols
        assertFalse( BankAccount.isEmailValid("ab.com@")); //equivalence class - improper @ symbol position (border case, minimum length of domain)

        // CHECKING PREFIX
        assertFalse( BankAccount.isEmailValid("@b.com")); //equivalence class - missing prefix (border case, minimum length of prefix)
        assertFalse( BankAccount.isEmailValid("a@bcom")); //equivalence class - missing .
        assertFalse( BankAccount.isEmailValid("a@b..com")); //equivalence class - multiple . in domain
        assertFalse( BankAccount.isEmailValid("a..b@c.com")); //equivalence class - multiple . in prefix
        assertFalse(BankAccount.isEmailValid(".a@b.com")); //equivalence class - improper . position in prefix

        assertTrue( BankAccount.isEmailValid("A@b.com")); //equivalence class - base case, valid format

        // CHECKING DOMAIN
        assertFalse( BankAccount.isEmailValid("a@.com")); //equivalence class - missing part of domain
        assertFalse( BankAccount.isEmailValid("a@b.")); //equivalence class - missing part of domain
        assertFalse( BankAccount.isEmailValid("a@b.c")); //equivalence class - domain length less than 2 (border case, minimum length of domain)

        assertTrue( BankAccount.isEmailValid("a@b.co")); //equivalence class - base case, valid format (border case, minimum length of domain)
        assertTrue( BankAccount.isEmailValid("a@b-c.com")); //equivalence class - proper - usage in domain
        assertTrue( BankAccount.isEmailValid("a@b.org")); //equivalence class - different type of domain

        // CHECKING SPECIAL CHARACTERS
        assertFalse( BankAccount.isEmailValid("a#@b.com")); //equivalence class - invalid character, # in prefix
        assertFalse( BankAccount.isEmailValid("a@b!.com"));  //equivalence class - invalid character, ! in domain
        assertFalse( BankAccount.isEmailValid("a-@b.com")); //equivalence class - invalid character, - at end of prefix

        assertTrue( BankAccount.isEmailValid("a_b@c.com")); //equivalence class - valid character, _ in middle of prefix
        assertTrue( BankAccount.isEmailValid("a.b@c.com")); //equivalence class - valid character, . in middle of prefix
        assertTrue( BankAccount.isEmailValid("a-b@c.com")); //equivalence class - valid character, - in middle of prefix

        // CHECK SPACES - equivalence class for all is improper spaces throughout prefixes and domains
        assertFalse(BankAccount.isEmailValid("a @b.com"));
        assertFalse(BankAccount.isEmailValid("a@ b.com"));
        assertFalse(BankAccount.isEmailValid("a@b. com"));
        assertFalse(BankAccount.isEmailValid("a@b.c om"));
        assertFalse(BankAccount.isEmailValid("a@b.com "));
        assertFalse(BankAccount.isEmailValid(" a@b.com"));
        assertFalse(BankAccount.isEmailValid("a b@b.com"));

        // Missing equivalence classes:
        //  If email is null rather than an empty string
        //  Uppercase letters in domain

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