package new_git_operations;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;

import java.util.Map;
import java.util.Optional;

public class GitWorkerClone extends GitWorker implements GitOperationClone{
    public GitWorkerClone(Map<String, String> arg_map) {
        super(arg_map);
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
