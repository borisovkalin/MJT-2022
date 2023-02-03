package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.InvalidPathException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.RuleAlreadyDefinedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OutlookTest {
    private final Outlook OUTLOOK = new Outlook();

    private static final String DEFAULT_ACCOUNT_NAME = "Seb";

    private static final String DEFAULT_EMAIL = "seb@gmail.com";

    private static final String DEFAULT = "DEFAULT";

    private static final String DEFAULT_INBOX_PATH = "/inbox";
    private static final String DEFAULT_SENT_PATH = "/sent";
    private static final String DEFAULT_RULE = "subject-includes: mjt, izpit, 2022"
            + System.lineSeparator() +
            "subject-or-body-includes: izpit"
            + System.lineSeparator() +
            "from: stoyo@fmi.bg";

    private static final String DEFAULT_METADATA = "sender: stoyo@fmi.bg" + System.lineSeparator() +
            "subject: mjt, izpit 2022!" + System.lineSeparator() +
            "recipients: pesho@gmail.com, gosho@gmail.com," + System.lineSeparator() +
            "received: 2022-12-08 14:14";

    @BeforeEach
    void init() {
        OUTLOOK.addNewAccount(DEFAULT_ACCOUNT_NAME,DEFAULT_EMAIL);
    }

    @Test
    void testAddAccountWithNullAccountName() {
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addNewAccount(null, DEFAULT_EMAIL)
        ,"Expected IllegalArgumentException when calling method addNewAccount with null accountName");
    }

    @Test
    void testAddAccountWithNullEmail() {
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addNewAccount(DEFAULT_ACCOUNT_NAME,null)
                ,"Expected IllegalArgumentException when calling method addNewAccount with null email");
    }

    @Test
    void testAddAccountWithEmptyEmail() {
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addNewAccount(DEFAULT_ACCOUNT_NAME,"")
                ,"Expected IllegalArgumentException when calling method addNewAccount with empty email");
    }

    @Test
    void testAddAccountWithEmptyAccountName() {
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addNewAccount("", DEFAULT_EMAIL)
                ,"Expected IllegalArgumentException when calling method addNewAccount with empty email");
    }

    @Test
    void testAddAccountWithBlankEmail() {
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addNewAccount(DEFAULT_ACCOUNT_NAME,"     ")
                ,"Expected IllegalArgumentException when calling method addNewAccount with blank email");
    }

    @Test
    void testAddAccountWithBlankAccountName() {
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addNewAccount("       ", DEFAULT_EMAIL)
                ,"Expected IllegalArgumentException when calling method addNewAccount with blank email");
    }

    @Test
    void testAddAccountWithAlreadyExistingAccountName() {
        assertThrows(AccountAlreadyExistsException.class, () -> OUTLOOK.addNewAccount(DEFAULT_ACCOUNT_NAME, DEFAULT),
                "Expected exception when trying to add non unique account name");

    }

    @Test
    void testAddAccountWithAlreadyExistingEmail() {
        assertThrows(AccountAlreadyExistsException.class, () -> OUTLOOK.addNewAccount(DEFAULT, DEFAULT_EMAIL),
                "Expected exception when trying to add non unique email");

    }

    @Test
    void testCreateFolderWithNonExistentNestedFolders() {
        assertThrows(InvalidPathException.class, () ->  OUTLOOK.createFolder(DEFAULT_ACCOUNT_NAME, "/inbox/path/to/flex"),
                "Expected an exception when trying to add Non-existent nest paths /inbox/(path/to)/flex");
    }

    @Test
    void testCreateFolderWithNonExistentAccount() {
        assertThrows(AccountNotFoundException.class, () ->  OUTLOOK.createFolder(DEFAULT, "/inbox/fleet"),
                "Expected an exception when trying to add Non-existent nest paths /inbox/fleet");
    }

    @Test
    void testCreateFolderWithAlreadyExistingFolder() {
        OUTLOOK.createFolder(DEFAULT_ACCOUNT_NAME,"/inbox/fleet");
        assertThrows(FolderAlreadyExistsException.class, () ->  OUTLOOK.createFolder(DEFAULT_ACCOUNT_NAME, "/inbox/fleet"),
                "Expected an exception when trying to add already existing folder /inbox/fleet -> /inbox/fleet");
    }

    @Test
    void testGetMailsFromFolderWithNullAccountName() {
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.getMailsFromFolder(null, DEFAULT_INBOX_PATH),
                "Expected an exception when trying to get Mails from null account");
    }

    @Test
    void testGetMailsFromFolderWithNullPath() {
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.getMailsFromFolder(DEFAULT_ACCOUNT_NAME,null),
                "Expected an exception when trying to get Mails from null path");
    }

    @Test
    void testGetMailFromFolderWithBlankOrEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.getMailsFromFolder("     ", DEFAULT_INBOX_PATH),
                "Expected an exception when trying to get Mails from blank account name");
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.getMailsFromFolder("", DEFAULT_INBOX_PATH),
                "Expected an exception when trying to get Mails from empty account name");
    }

    @Test
    void testGetMailFromFolderWithBlankOrEmptyPath() {
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.getMailsFromFolder(DEFAULT_ACCOUNT_NAME,"     "),
                "Expected an exception when trying to get Mails from blank account name");
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.getMailsFromFolder(DEFAULT_ACCOUNT_NAME,""),
                "Expected an exception when trying to get Mails from empty account name");
    }

    @Test
    void testGetMailsFromFolderNonExistent() {
        assertThrows(FolderNotFoundException.class, () -> OUTLOOK.getMailsFromFolder(DEFAULT_ACCOUNT_NAME,"/inbox/bat"),
                "Expected an exception when trying to get Mails from null path");
    }

    @Test
    void testGetMailsFromFolderAccountNonExistent() {
        assertThrows(AccountNotFoundException.class, () -> OUTLOOK.getMailsFromFolder(DEFAULT,"/inbox"),
                "Expected an exception when trying to get Mails from null path");
    }

    @Test
    void testAddRuleWithNullAccountNameOrPathFolderOrRuleDefinition() {
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addRule(null, DEFAULT_INBOX_PATH,DEFAULT_RULE,3),
                "Expected exception when trying to call addRule with null accountName");
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, null ,DEFAULT_RULE,3),
                "Expected exception when trying to call addRule with null path");
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH,null,3),
                "Expected exception when trying to call addRule with null ruleDefinition");
    }

    @Test
    void testAddRuleWithBlankAndEmptyAccountNameAndPathFolderAndRuleDefinition() {
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addRule("", DEFAULT_INBOX_PATH,DEFAULT_RULE,3),
                "Expected exception when trying to call addRule with empty accountName");
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, "" ,DEFAULT_RULE,3),
                "Expected exception when trying to call addRule with empty path");
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH,"",3),
                "Expected exception when trying to call addRule with empty ruleDefinition");
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addRule("    ", DEFAULT_INBOX_PATH,DEFAULT_RULE,3),
                "Expected exception when trying to call addRule with blank accountName");
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, "      " ,DEFAULT_RULE,3),
                "Expected exception when trying to call addRule with blank path");
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH,"     ",3),
                "Expected exception when trying to call addRule with blank ruleDefinition");
    }

    @Test
    void testAddRuleWithNegativePriority() {
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH,DEFAULT_RULE,-1),
                "Expected exception when trying to call addRule with negative priority");
    }

    @Test
    void testAddRuleWithExceedingCapacityPriority() {
        assertThrows(IllegalArgumentException.class, () -> OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH,DEFAULT_RULE,11),
                "Expected exception when trying to call addRule with Exceeded capacity priority");
    }
    @Test
    void testAddRuleWithNonExistentPath() {
        assertThrows(FolderNotFoundException.class, () -> OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME,"/inbox/robSnyder",DEFAULT_RULE,1),
                "Expected exception when trying to call addRule with negative priority");
    }

    @Test
    void testAddRuleWithPathToInboxOne() {
        OUTLOOK.addNewAccount("Stoyo", "stoyo@fmi.bg");

        OUTLOOK.createFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one");

        OUTLOOK.receiveMail(DEFAULT_ACCOUNT_NAME,DEFAULT_METADATA,"BOO GHOSTS");

        OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH +"/one", DEFAULT_RULE,1);

        Collection<Mail> mail = OUTLOOK.getMailsFromFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one");

        Iterator<Mail> iter = mail.iterator();

        Mail m = iter.next();

        assertEquals("BOO GHOSTS", m.body(),"Expected same body of same mails");
    }

    @Test
    void testAddRuleWithSameRulesAddedTwice() {
        OUTLOOK.addNewAccount("Stoyo", "stoyo@fmi.bg");

        OUTLOOK.createFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one");

        OUTLOOK.receiveMail(DEFAULT_ACCOUNT_NAME,DEFAULT_METADATA,"BOO GHOSTS");

        String repeatingRule = DEFAULT_RULE + System.lineSeparator() + DEFAULT_RULE;

        assertThrows(RuleAlreadyDefinedException.class, () -> OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME , DEFAULT_INBOX_PATH + "/one",
                repeatingRule,1),
                "Trying to add Same rule configs twice");

    }

    @Test
    void testAddRuleWithIllegalRule() {
        OUTLOOK.addNewAccount("Stoyo", "stoyo@fmi.bg");

        OUTLOOK.createFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one");

        OUTLOOK.receiveMail(DEFAULT_ACCOUNT_NAME,DEFAULT_METADATA,"BOO GHOSTS");

        String repeatingRule = DEFAULT_RULE + System.lineSeparator() + "unknown_rule: ";

        assertThrows(IllegalArgumentException.class, () ->
                        OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME , DEFAULT_INBOX_PATH + "/one",
                                repeatingRule,1),
                "Trying to add Same rule configs twice");

    }

    @Test
    void testAddRuleWithNullRecipientTwice() {
        OUTLOOK.addNewAccount("Stoyo", "stoyo@fmi.bg");

        OUTLOOK.createFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one");

        OUTLOOK.receiveMail(DEFAULT_ACCOUNT_NAME, DEFAULT_METADATA,"BOO GHOSTS");

        String repeatingRule = DEFAULT_RULE + System.lineSeparator() + "recipients-includes: Flower "
                + System.lineSeparator() + "recipients-includes:  Flower";

        assertThrows(RuleAlreadyDefinedException.class, () ->
                        OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME , DEFAULT_INBOX_PATH + "/one",
                                repeatingRule,1),
                "Trying to add Same rule configs twice");

    }

    @Test
    void testReceiveOneMailWithRuleActive() {
        OUTLOOK.addNewAccount("Stoyo", "stoyo@fmi.bg");

        OUTLOOK.createFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one");
        OUTLOOK.createFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/two");

        OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one", DEFAULT_RULE, 1);

        OUTLOOK.receiveMail(DEFAULT_ACCOUNT_NAME, DEFAULT_METADATA,"BOO GHOSTS");
        assertEquals(OUTLOOK.getMailsFromFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one").size(),
                1,"Expected mail to be moved from /inbox to /inbox/one -> rule not applied");
    }

    @Test
    void testReceiveOneMailWithNoRuleActive() {
        OUTLOOK.receiveMail(DEFAULT_ACCOUNT_NAME, DEFAULT_METADATA,"BOO GHOSTS");
        assertEquals(OUTLOOK.getMailsFromFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH).size(),
                1,"Expected mail to be moved from /inbox to /inbox/one -> rule not applied");
    }

    @Test
    void testReceiveOneMailWithTwoRuleActiveWithDifferentPriority() {
        OUTLOOK.addNewAccount("Stoyo", "stoyo@fmi.bg");

        OUTLOOK.createFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one");
        OUTLOOK.createFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/two");

        OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one", DEFAULT_RULE, 1);
        OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/two",DEFAULT_RULE, 2);

        OUTLOOK.receiveMail(DEFAULT_ACCOUNT_NAME, DEFAULT_METADATA,"BOO GHOSTS");
        assertEquals(OUTLOOK.getMailsFromFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one").size(),
                1,"Expected mail to be moved from /inbox to /inbox/one -> rule not applied");
    }



    @Test
    void testReceiveOneMailWithDifferentRulesActive() {
        OUTLOOK.addNewAccount("Stoyo", "stoyo@fmi.bg");

        OUTLOOK.createFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one");
        OUTLOOK.createFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/two");

        OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one", DEFAULT_RULE, 1);
        OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/two","subject-includes: mjt, izpit, 2022"
                + System.lineSeparator() +
                "subject-or-body-includes: izpit"
                + System.lineSeparator() +
                "from: NOTstoyo@fmi.bg", 2);

        OUTLOOK.receiveMail(DEFAULT_ACCOUNT_NAME, DEFAULT_METADATA,"BOO GHOSTS");
        assertEquals(OUTLOOK.getMailsFromFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one").size(),
                1,"Expected mail to be moved from /inbox to /inbox/one -> rule not applied");
    }

    @Test
    void testReceiveOneMailWithTwoDifferentRulesActiveWithLowPriorities() {
        OUTLOOK.createFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one");
        OUTLOOK.createFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/two");

        OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one", DEFAULT_RULE, 10);
        OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/two","subject-includes: mjt, izpit, 2022"
                + System.lineSeparator() +
                "subject-or-body-includes: izpit"
                + System.lineSeparator() +
                "from: " + DEFAULT_ACCOUNT_NAME, 7);

        OUTLOOK.receiveMail(DEFAULT_ACCOUNT_NAME,  "sender: " + DEFAULT_ACCOUNT_NAME + System.lineSeparator() +
                "subject: mjt, izpit 2022!" + System.lineSeparator() +
                "recipients: pesho@gmail.com, gosho@gmail.com," + System.lineSeparator() +
                "received: 2022-12-08 14:14","BOO GHOSTS");
        assertEquals(OUTLOOK.getMailsFromFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/two").size(),
                1,"Expected mail to be moved from /inbox to /inbox/two -> rule not applied correctly");
    }

    @Test
    void testReceiveOneMailWithOneBlankRule() {
        OUTLOOK.addNewAccount("SoyoBoyo_v1","oldstoyo@fmi.bg");
        OUTLOOK.addNewAccount("SoyoBoyo_v2","newstoyo@fmi.bg");

        OUTLOOK.createFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one");

        OUTLOOK.addRule(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH + "/one", "from: NEWstoyo@fmi.bg", 1);

        OUTLOOK.receiveMail(DEFAULT_ACCOUNT_NAME,"sender: oldstoyo@fmi.bg",DEFAULT);
        assertEquals(OUTLOOK.getMailsFromFolder(DEFAULT_ACCOUNT_NAME, DEFAULT_INBOX_PATH).size(),1,
                "Expected the Email to stay in /inbox when it doesn't match any rules");
    }
    @Test
    void testSendMailWithTwoRecepients() {
        OUTLOOK.addNewAccount("SoyoBoyo","stoyo@fmi.bg");
        OUTLOOK.addNewAccount("Pe6o","pesho@gmail.com");
        OUTLOOK.addNewAccount("Go6o", "gosho@gmail.com");

        OUTLOOK.createFolder("Pe6o", DEFAULT_INBOX_PATH + "/one");
        OUTLOOK.createFolder("Pe6o", DEFAULT_INBOX_PATH + "/two");
        OUTLOOK.createFolder("Go6o", DEFAULT_INBOX_PATH + "/one");

        OUTLOOK.addRule("Pe6o", DEFAULT_INBOX_PATH + "/two", "from: stoyo@fmi.bg", 1);
        OUTLOOK.addRule("Go6o", DEFAULT_INBOX_PATH + "/one", "from: boystoyo@fmi.bg", 1);

        String MailMetadata = "recipients: pesho@gmail.com, gosho@gmail.com" + System.lineSeparator() + "sender: stoyo@fmi.bg";

        OUTLOOK.sendMail("SoyoBoyo",MailMetadata ,DEFAULT);

        assertEquals(1,OUTLOOK.getMailsFromFolder("Pe6o", DEFAULT_INBOX_PATH + "/two").size(),
                "Expected the mail to be received in the right folder. Check Rule definition enforcement");
        assertEquals(1,OUTLOOK.getMailsFromFolder("Go6o", DEFAULT_INBOX_PATH).size(),
                "Expected the mail to be received in the right folder. Check Rule definition enforcement");
        assertEquals(1,OUTLOOK.getMailsFromFolder("SoyoBoyo", DEFAULT_SENT_PATH).size(),
                "Expected the mail to be in the sent folder for sender account");

    }

    @Test
    void testSendMailWithNoRecepientsField() {
        assertThrows(IllegalArgumentException.class, () ->OUTLOOK.sendMail("Seb","from: stoyo@abv.bg",DEFAULT)
                ,"Expected an exception to be thrown when there are no recipients for mail");
    }
}
