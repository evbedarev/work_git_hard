package new_enter;

import enter.ReadArgs;
import new_git_operations.*;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.Map;

public class RunChecker {
    public static void main(String[] args) throws GitAPIException, IOException {
        enter.ReadArgs readArgs = new ReadArgs();
        Map<String,String> arg_map = readArgs.readArgs(args);
        if (args[0].equals("--help") || args[0].equals("-help")) {
            printHelp();
            return;
        }
        if (arg_map.get("operation").equals("push")) {
            GitOperationPush gitOperationPush = new GitWorkerPush(arg_map);
        } else if (arg_map.get("operation").equals("pushTag")){
            GitOperationPushTag gitOperationPushTag = new GitWorkerPushTag(arg_map);
        } else if (arg_map.get("operation").equals("clone")) {
            GitOperationClone gitOperationClone = new GitWorkerClone(arg_map);
        }
    }
    private static void printHelp() {
        System.out.println("===================================================================================================||");
        System.out.println("===================================================================================================||");
        System.out.println("Argumenst of this program:                                                                         ||");
        System.out.println("java -jar workgit.jar un:USERNAME pw:PASSWORD operation:[clone|push] repo-url:URL_OF_REPO \\        ||");
        System.out.println("target-dir:DESTINATION_REPO_DIR branch:NAME_OF_BRANCH                                              ||");
        System.out.println("If operation:push addition parameters are: file-pattern:'Pattern of file will be added to commit   ||");
        System.out.println("use './*' commit-msg:'Message of commit'                                                           ||");
        System.out.println("un and pw - username and password in base64 encoding                                               ||");
        System.out.println("Recipients is array in format 'Recipient1,Recipient2'                                              ||");
        System.out.println("===================================================================================================||\n");
    }
}
