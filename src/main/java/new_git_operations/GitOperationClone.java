package new_git_operations;

import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

public interface GitOperationClone {
    void init() throws GitAPIException, IOException;
}
