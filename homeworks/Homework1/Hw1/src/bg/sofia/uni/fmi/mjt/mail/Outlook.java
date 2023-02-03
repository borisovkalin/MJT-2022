package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.managers.FileSystemManager;
import bg.sofia.uni.fmi.mjt.mail.managers.RuleManager;
import bg.sofia.uni.fmi.mjt.mail.util.MatchChecker;
import bg.sofia.uni.fmi.mjt.mail.util.StringExtractor;
import bg.sofia.uni.fmi.mjt.mail.util.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Outlook implements MailClient {

    private final Map<String, Account> accounts;
    private final FileSystemManager fileSystem;
    private final RuleManager rules;

    public Outlook() {
        accounts = new HashMap<>();
        fileSystem = new FileSystemManager();
        rules = new RuleManager();
    }

    @Override
    public Account addNewAccount(String accountName, String email) {
        Validator.validateObject(accountName, email);
        Validator.validateString(accountName, email);
        checkAccountProperties(accountName, email);

        Account newAcc = new Account(accountName, email);

        accounts.put(accountName, newAcc);
        accounts.put(email, newAcc);

        fileSystem.addNewAccountMail(newAcc);
        rules.createRuleSet(newAcc);
        return newAcc;
    }

    @Override
    public void createFolder(String accountName, String path) {
        Validator.validateObject(accountName, path);
        Validator.validateString(accountName, path);
        checkAccount(accountName);

        Account currentAccount = accounts.get(accountName);

        if (fileSystem.checkIfPathAlreadyExists(currentAccount, path)) {
            throw new FolderAlreadyExistsException("Trying to add existing path-folder");
        }

        fileSystem.validateIntermediateFolders(currentAccount, path);
        fileSystem.createFolder(currentAccount, path);
    }

    @Override
    public void addRule(String accountName, String folderPath, String ruleDefinition, int priority) {
        Validator.validateObject(accountName, folderPath, ruleDefinition);
        Validator.validateString(accountName, folderPath, ruleDefinition);
        Validator.validatePriorityBounds(priority);
        checkAccount(accountName);

        Account currentAccount = accounts.get(accountName);
        if (!fileSystem.checkIfPathAlreadyExists(currentAccount, folderPath)) {
            throw new FolderNotFoundException("Could not find folder with given path");
        }

        Rule addedRule = new Rule(folderPath, ruleDefinition, priority);

        enforceRule(currentAccount, addedRule);
        rules.addRuleToAccount(currentAccount, addedRule);
    }

    @Override
    public void receiveMail(String accountName, String mailMetadata, String mailContent) {
        Validator.validateObject(accountName, mailMetadata, mailContent);
        Validator.validateString(accountName, mailMetadata, mailContent);
        checkAccount(accountName);

        Mail mail = parseMetadataToMail(mailMetadata, mailContent);
        Account account = accounts.get(accountName);
        fileSystem.addMailToAccountPath(mail, account, FileSystemManager.DEFAULT_INBOX_PATH);

        Set<Rule> iteratorRules = rules.getRuleMapForAccount(account);

        for (Rule rule : iteratorRules) {
            if (enforceRule(account, rule)) {
                break;
            }
        }
    }

    @Override
    public Collection<Mail> getMailsFromFolder(String account, String folderPath) {
        Validator.validateObject(account, folderPath);
        Validator.validateString(account, folderPath);
        checkAccount(account);
        Account currentAccount = accounts.get(account);

        if (!fileSystem.checkIfPathAlreadyExists(currentAccount, folderPath)) {
            throw new FolderNotFoundException("Could not find folder with given path");
        }

        return fileSystem.getAccountFileSystem(currentAccount).get(folderPath);
    }

    @Override
    public void sendMail(String accountName, String mailMetadata, String mailContent) {
        Validator.validateObject(accountName, mailMetadata, mailContent);
        Validator.validateString(accountName, mailMetadata, mailContent);
        checkAccount(accountName);

        Account account = accounts.get(accountName);
        Mail mail = parseMetadataToMail(mailMetadata, mailContent);

        String[] recipients = StringExtractor.extractRecipients(mailMetadata);
        if (recipients == null) {
            throw new IllegalArgumentException("No recipients field found in metadata");
        }

        fileSystem.addMailToAccountPath(mail, account, FileSystemManager.DEFAULT_SENT_PATH);

        for (String email : recipients) {
            Account recipientAccount = accounts.get(email);
            if (recipientAccount != null) {
                receiveMail(email, mailMetadata, mailContent);
            }
        }

    }

    private void checkAccountProperties(String accountName, String email) {
        if (accounts.containsKey(accountName)) {
            throw new AccountAlreadyExistsException("Account with the same accountName exists in the database");
        }

        if (accounts.containsKey(email)) {
            throw new AccountAlreadyExistsException("Account with the same email exists in the database");
        }
    }

    public void checkAccount(String accountName) {
        if (!accounts.containsKey(accountName)) {
            throw new AccountNotFoundException("Account with that name was not found");
        }
    }

    private boolean enforceRule(Account account, Rule rule) {
        List<Mail> inboxMails = fileSystem.getAccountFileSystem(account).get(FileSystemManager.DEFAULT_INBOX_PATH);
        List<Mail> rulePathMails = fileSystem.getAccountFileSystem(account).get(rule.folderPath());
        List<Mail> parsedMails = new ArrayList<>();

        for (Mail mail : inboxMails) {
            if (rules.checkIfMailMatchesRule(mail, rule)) {
                parsedMails.add(mail);
                rulePathMails.add(mail);
            }
        }

        inboxMails.removeAll(parsedMails);
        fileSystem.getAccountFileSystem(account).put(FileSystemManager.DEFAULT_INBOX_PATH, inboxMails);
        fileSystem.getAccountFileSystem(account).put(rule.folderPath(), rulePathMails);

        return !parsedMails.isEmpty();
    }

    private Mail parseMetadataToMail(String mailMetadata, String mailContent) {
        //Account sender, Set<String> recipients, String subject, String body, LocalDateTime received
        Account sender = null;
        Set<String> recipients = new HashSet<>();
        String subject = "";
        LocalDateTime time = null;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String[] lines = mailMetadata.split(System.lineSeparator());
        for (String line : lines) {
            if (line.startsWith(MatchChecker.MAIL_SENDER)) {
                sender = accounts.get(line.substring(MatchChecker.MAIL_SENDER.length()).trim());
            }
            if (line.startsWith(MatchChecker.MAIL_RECIPIENTS)) {
                recipients.addAll(List.of(line.substring(MatchChecker.MAIL_RECIPIENTS.length())
                        .trim().split(",")));
            }
            if (line.startsWith(MatchChecker.MAIL_SUBJECT)) {
                subject = line.substring(MatchChecker.MAIL_SUBJECT.length()).trim();
            }
            if (line.startsWith(MatchChecker.MAIL_TIME)) {
                time = LocalDateTime.parse(line.substring(MatchChecker.MAIL_TIME.length()).trim(), format);
            }
        }
        return new Mail(sender, recipients, subject, mailContent, time);
    }
}
