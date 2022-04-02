package new_git_operations;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;

import java.util.Map;
import java.util.Optional;

public class GitWorkerPushTag extends GitWorker implements GitOperationPushTag{
    public GitWorkerPushTag(Map<String, String> arg_map) {
        super(arg_map);
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
