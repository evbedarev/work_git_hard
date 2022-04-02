package enter;
//Evgeniy Bedarev

import java.io.FileNotFoundException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
public class ReadArgs {
    private Map<String,String> map_args = new HashMap<>();
    public Map<String,String> readArgs(String[] args) throws FileNotFoundException {
        printInitMessage();
        putToMap("un", args[0]);
        putToMap("pw", args[1]);
        putToMap("operation", args[2]);
        if (args[2].split(":")[1].equals("clone")) {
            putToMap("repo-url", args[3]);
            putToMap("target-dir",args[4]);
            putToMap("branch",args[5]);
        }
        if (args[2].split(":")[1].equals("push")) {
            putToMap("repo-url", args[3]);
            putToMap("target-dir",args[4]);
            putToMap("branch",args[5]);

            putToMap("file-pattern",args[6]);
            putToMap("commit-msg",args[7]);
        }
        if (args[2].split(":")[1].equals("pushTag")) {
            putToMap("repo-url", args[3]);
            putToMap("target-dir",args[4]);
            putToMap("branch",args[5]);

            putToMap("annotate",args[6]);
            putToMap("commit-msg",args[7]);
        }
        return map_args;
    }
    private void putToMap(String arg_name, String arg) throws NoSuchElementException {
        if (!arg.contains(":")) {
            throw new NoSuchElementException("get Incorrect parameter " + arg);
        }
        if (arg.split(":")[0].equals("pw") || arg.split(":")[0].equals("un")) {
            byte[] secText = Base64.getDecoder().decode(arg.split(":")[1]);
            map_args.put(arg_name, new String(secText));
        } else if (arg.split(":")[0].equals(arg_name)) {
            int sizeM = arg.split(":")[0].length();
            map_args.put(arg_name,arg.substring(sizeM + 1));
        } else {
            throw new NoSuchElementException("No parameter " + arg_name + "  gived");
        }
    }
    private void printInitMessage() {
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
