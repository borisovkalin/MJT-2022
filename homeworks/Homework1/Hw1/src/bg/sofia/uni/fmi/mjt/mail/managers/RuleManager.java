package bg.sofia.uni.fmi.mjt.mail.managers;

import bg.sofia.uni.fmi.mjt.mail.Account;
import bg.sofia.uni.fmi.mjt.mail.Mail;
import bg.sofia.uni.fmi.mjt.mail.Rule;
import bg.sofia.uni.fmi.mjt.mail.comparator.RuleComparator;
import bg.sofia.uni.fmi.mjt.mail.exceptions.RuleAlreadyDefinedException;
import bg.sofia.uni.fmi.mjt.mail.util.MatchChecker;
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

    public void createRuleSet(Account account) {
        ruleMap.put(account, new TreeSet<>(new RuleComparator()));
    }

    public void addRuleToAccount(Account account, Rule rule) {
        ruleMap.get(account).add(rule);
    }

    public Set<Rule> getRuleMapForAccount(Account account) {
        return ruleMap.get(account);
    }

    public boolean checkIfMailMatchesRule(Mail mail, Rule rule) {
        String[] ruleLines = rule.ruleDefinition().split(System.lineSeparator());

        checkIfRuleIsValid(ruleLines);

        for (String line : ruleLines) {
            String prefix = StringExtractor.extractStartPrefix(line);

            if (prefix == null) {
                throw new IllegalArgumentException("Rule not defined properly");
            }

            if (!checkMailBySpecifiedPrefixForRule(mail, line, prefix)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkMailBySpecifiedPrefixForRule(Mail mail, String line, String prefix) {
        return switch (prefix) {
            case StringExtractor.RULE_SUBJECT_INCLUDE -> MatchChecker.checkMailBySubjectRule(mail, line);
            case StringExtractor.RULE_SUBJECT_OR_BODY_INCLUDE -> MatchChecker.checkMailBySubjectOrBodyRule(mail, line);
            case StringExtractor.RULE_FROM_INCLUDE -> MatchChecker.checkMailByFromRule(mail, line);
            case StringExtractor.RULE_RECEPIENTS_INCLUDE -> MatchChecker.checkMailByRecepientsRule(mail, line);
            default -> true;
        };
    }

    private void checkIfRuleIsValid(String[] ruleLines) {
        Set<String> set = new HashSet<>();
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
        }
    }
}
