package git_operations;

import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

public interface GitOperations {
    void init() throws GitAPIException, IOException;
    void push() throws GitAPIException;
    void pushTag() throws GitAPIException;
}
