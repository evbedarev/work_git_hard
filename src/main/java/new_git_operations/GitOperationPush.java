package new_git_operations;

import org.eclipse.jgit.api.errors.GitAPIException;
import java.io.IOException;

public interface GitOperationPush {
    void init() throws GitAPIException, IOException;
    void push() throws GitAPIException;
}
