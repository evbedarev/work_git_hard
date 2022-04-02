package git_operations;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GitLog {
    private Map<String,String> arg_map;
    private Git git;
    private CredentialsProvider creds;
    public GitLog(Map<String,String> arg_map) {
       this.arg_map = arg_map;
       creds = new UsernamePasswordCredentialsProvider(arg_map.get("un"),arg_map.get("pw"));
    }

    public void init() throws GitAPIException, IOException {
        Path                git_path = Paths.get(arg_map.get("target-dir"));
        Repository          repo;
        TextProgressMonitor consoleProgressMonitor;
        System.out.println(git_path.getFileName());

        if (!Files.exists(git_path)) {
            consoleProgressMonitor = new TextProgressMonitor(new PrintWriter(System.out));
            System.out.printf("\n>>> Cloning repository: %s \n",arg_map.get("repo-url"));
            repo = Git.cloneRepository().setProgressMonitor(consoleProgressMonitor).setDirectory(git_path.toFile())
                    .setURI(arg_map.get("repo-url"))
                    .setCredentialsProvider(creds)
                    .call().getRepository();
        } else {
            Git.open(git_path.toFile()).pull().setCredentialsProvider(creds).call();//.call().getRepository();
            repo = Git.open(git_path.toFile()).getRepository();
            consoleProgressMonitor = new TextProgressMonitor(new PrintWriter(System.out));
        }
        try {
            //System.out.printf("\n>>> Checkout branch: %s \n",arg_map.get("branch"));
            changeGitBranch(new Git(repo), consoleProgressMonitor, arg_map.get("branch"));
        } catch (RefAlreadyExistsException exRef) {
            System.out.println("\n>>> Branch already exists <<< \n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        git = new Git(repo);
    }

    public void push() throws GitAPIException {
        System.out.printf("\n >>> Git add files: %s <<<\n",arg_map.get("file-pattern"));
        git.add().addFilepattern(arg_map.get("file-pattern")).setUpdate(true).call();
        System.out.printf("\n >>> Git commit message: %s <<<\n",arg_map.get("commit-msg"));
        git.commit().setMessage(arg_map.get("commit-msg")).call();
        System.out.printf("\n >>> Git push to repo %s <<<\n",arg_map.get("repo-url") );
        git.push().setCredentialsProvider(creds).setPushAll().call();
    }

    public void pushTag() throws GitAPIException {
        System.out.printf("\n >>> Git set tag annotation: %s, message: %s  <<<\n",arg_map.get("annotate"),
                arg_map.get("commit-msg"));
        git.tag().setName(arg_map.get("annotate")).setMessage(arg_map.get("commit-msg")).call();
        System.out.print("\n >>> Git push tag <<<\n");
        git.push().setCredentialsProvider(creds).setPushTags().call();
    }

    private void changeGitBranch(Git git, TextProgressMonitor consoleProgressMonitor, String defaultBranch) throws GitAPIException {
        Optional<String> developBranch = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call()
                .stream().map(r -> r.getName()).filter(n -> n.contains(defaultBranch)).findAny();
        if (developBranch.isPresent()) {
            System.out.printf("\n Checking out %s branch \n",defaultBranch);
            git.checkout().setProgressMonitor(consoleProgressMonitor).setCreateBranch(true).setName(defaultBranch)
                    .setStartPoint(developBranch.get()).call();
        }
    }
}
