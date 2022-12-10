package bg.sofia.uni.fmi.mjt.mail.managers;

import bg.sofia.uni.fmi.mjt.mail.Account;
import bg.sofia.uni.fmi.mjt.mail.Mail;
import bg.sofia.uni.fmi.mjt.mail.exceptions.InvalidPathException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FileSystemManager {

    public static final String DEFAULT_INBOX_PATH = "/inbox";
    public static final String DEFAULT_SENT_PATH = "/sent";
    private final Map<Account, Map<String, List<Mail>>> accountFileSystemMap;

    public FileSystemManager() {
        accountFileSystemMap = new HashMap<>();
    }

    public void addNewAccountMail(Account newAcc) {
        accountFileSystemMap.put(newAcc, new TreeMap<>());
        accountFileSystemMap.get(newAcc).put(DEFAULT_INBOX_PATH, new LinkedList<>());
        accountFileSystemMap.get(newAcc).put(DEFAULT_SENT_PATH, new LinkedList<>());
    }

    public void createFolder(Account account, String path) {
        var accountFileSystem = accountFileSystemMap.get(account);
        accountFileSystem.put(path, new LinkedList<>());
    }

    public void addMailToAccountPath(Mail mail, Account account, String path) {
        accountFileSystemMap.get(account).get(path).add(mail);
    }

    public boolean checkIfPathAlreadyExists(Account account, String path) {
        return accountFileSystemMap.get(account).containsKey(path);
    }

    public void validateIntermediateFolders(Account account, String path) {
        // "/inbox/flat/out/noob"
        String[] folderArr = path.split("/");
        String[] pathToFolderSplit = Arrays.copyOfRange(folderArr, 1, folderArr.length - 1);

        StringBuilder currentPath = new StringBuilder();
        for (String str : pathToFolderSplit) {
            currentPath.append("/").append(str);
            if (!accountFileSystemMap.get(account).containsKey(currentPath.toString())) {
                throw new InvalidPathException("Folder: " + currentPath + "-> not found");
            }
        }
    }

    public  Map<String, List<Mail>> getAccountFileSystem(Account account) {
        return accountFileSystemMap.get(account);
    }
}
