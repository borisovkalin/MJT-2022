package bg.sofia.uni.fmi.mjt.mail.managers;

import bg.sofia.uni.fmi.mjt.mail.Account;
import bg.sofia.uni.fmi.mjt.mail.Mail;
import bg.sofia.uni.fmi.mjt.mail.exceptions.InvalidPathException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSystemManager {

    public static final String DEFAULT_INBOX_PATH = "/inbox";
    public static final String DEFAULT_SENT_PATH = "/sent";
    private final Map<Account, Map<String, List<Mail>>> accountFileSystem;

    public FileSystemManager() {
        accountFileSystem = new HashMap<>();
    }

    public void addNewAccountMail(Account newAcc) {
        accountFileSystem.put(newAcc, new HashMap<>());
        accountFileSystem.get(newAcc).put(DEFAULT_INBOX_PATH, new ArrayList<>());
        accountFileSystem.get(newAcc).put(DEFAULT_SENT_PATH, new ArrayList<>());
    }

    public void createFolder(Account account, String path) {
        var accountFileSystem = this.accountFileSystem.get(account);
        accountFileSystem.put(path, new ArrayList<>());
    }

    public void addMailToAccountPath(Mail mail, Account account, String path) {
        accountFileSystem.get(account).get(path).add(mail);
    }

    public boolean checkIfPathAlreadyExists(Account account, String path) {
        return accountFileSystem.get(account).containsKey(path);
    }

    public void validateIntermediateFolders(Account account, String path) {
        // "/inbox/flat/out/noob"
        String[] folderArr = path.split("/");
        String[] pathToFolderSplit = Arrays.copyOfRange(folderArr, 1, folderArr.length - 1);

        StringBuilder currentPath = new StringBuilder();
        for (String str : pathToFolderSplit) {
            currentPath.append("/").append(str);
            if (!accountFileSystem.get(account).containsKey(currentPath.toString())) {
                throw new InvalidPathException("Folder: " + currentPath + "-> not found");
            }
        }
    }

    public Map<String, List<Mail>> getAccountFileSystem(Account account) {
        return accountFileSystem.get(account);
    }
}
