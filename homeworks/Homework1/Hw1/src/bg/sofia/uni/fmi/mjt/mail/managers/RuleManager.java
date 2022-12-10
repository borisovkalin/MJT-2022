package bg.sofia.uni.fmi.mjt.mail.managers;

import bg.sofia.uni.fmi.mjt.mail.Account;
import bg.sofia.uni.fmi.mjt.mail.Mail;
import bg.sofia.uni.fmi.mjt.mail.Rule;
import bg.sofia.uni.fmi.mjt.mail.comparator.RuleComparator;
import bg.sofia.uni.fmi.mjt.mail.exceptions.RuleAlreadyDefinedException;
import bg.sofia.uni.fmi.mjt.mail.util.MailMatchesRuleChecker;
import bg.sofia.uni.fmi.mjt.mail.util.StringExtractor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class RuleManager {
    private final Map<Account, Set<Rule>> ruleMap;

    public RuleManager() {
        ruleMap = new HashMap<>();
    }

    public void addEmptyRuleSetToNewAccount(Account account) {
        ruleMap.put(account, new TreeSet<>(new RuleComparator()));
    }

    public void addRuleToAccount(Account account, Rule rule) {
        ruleMap.get(account).add(rule);
    }

    public Set<Rule> getRuleMapForAccount(Account account) {
        return ruleMap.get(account);
    }

    public boolean checkIfMailMatchesRule(Mail mail, Rule rule) {
        Set<String> set = new HashSet<>();
        String[] ruleLines = rule.ruleDefinition().split(System.lineSeparator());

        for (String line : ruleLines) {
            String prefix;
            prefix = StringExtractor.extractStartPrefix(line);
            if (prefix == null) {
                throw new IllegalArgumentException("Rule not defined properly");
            }
            if (set.contains(prefix)) {
                throw new RuleAlreadyDefinedException("Trying to add same rule twice");
            }

            set.add(prefix);
            if (!validateMailByRules(mail, line, prefix)) {
                return false;
            }
        }
        return true;
    }

    private boolean validateMailByRules(Mail mail, String line, String prefix) {
        switch (prefix) {
            case StringExtractor.SUBJECT_INCLUDE -> {
                if (!MailMatchesRuleChecker.checkMailBySubjectRule(mail, line)) {
                    return false;
                }
            }
            case StringExtractor.SUBJECT_OR_BODY_INCLUDE -> {
                if (!MailMatchesRuleChecker.checkMailBySubjectOrBodyRule(mail, line)) {
                    return false;
                }
            }
            case StringExtractor.FROM_INCLUDE -> {
                if (!MailMatchesRuleChecker.checkMailByFromRule(mail, line)) {
                    return false;
                }
            }
            case StringExtractor.RECEPIENTS_INCLUDE -> {
                if (!MailMatchesRuleChecker.checkMailByRecepientsRule(mail, line)) {
                    return false;
                }
            }
        }
        return true;
    }

}
