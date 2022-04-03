package git_operations;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;
import java.util.*;

public class GitWorker extends GitWorkerAbs implements GitOperations {
    public GitWorker(Map<String,String> arg_map) {
        super(arg_map);
    }

    @Override
    public void push() throws GitAPIException {
        System.out.printf("\n >>> Git add files: %s <<<\n",arg_map.get("file-pattern"));
        git.add().addFilepattern(arg_map.get("file-pattern")).setUpdate(true).call();
        System.out.printf("\n >>> Git commit message: %s <<<\n",arg_map.get("commit-msg"));
        git.commit().setMessage(arg_map.get("commit-msg")).call();
        System.out.printf("\n >>> Git push to repo %s <<<\n",arg_map.get("repo-url") );
        git.push().setCredentialsProvider(creds).setPushAll().call();
    }

    @Override
    public void pushTag() throws GitAPIException {
        System.out.printf("\n >>> Git set tag annotation: %s, message: %s  <<<\n",arg_map.get("annotate"),
                arg_map.get("commit-msg"));
        git.tag().setName(arg_map.get("annotate")).setMessage(arg_map.get("commit-msg")).call();
        System.out.print("\n >>> Git push tag <<<\n");
        git.push().setCredentialsProvider(creds).setPushTags().call();
    }

    @Override
    void changeGitBranch(Git git, TextProgressMonitor consoleProgressMonitor, String defaultBranch) throws GitAPIException {
        Optional<String> developBranch = git.branchList().setListMode(ListBranchCommand.ListMode.REMOTE).call()
                .stream().map(r -> r.getName()).filter(n -> n.contains(defaultBranch)).findAny();
        if (developBranch.isPresent()) {
            System.out.printf("\n Checking out %s branch \n",defaultBranch);
            git.checkout().setProgressMonitor(consoleProgressMonitor).setCreateBranch(true).setName(defaultBranch)
                    .setStartPoint(developBranch.get()).call();
        }
    }
}
