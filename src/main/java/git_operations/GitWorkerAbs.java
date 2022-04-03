package git_operations;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.TextProgressMonitor;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public abstract class GitWorkerAbs {
    Map<String,String> arg_map;
    Git git;
    CredentialsProvider creds;

    public GitWorkerAbs(Map<String,String> arg_map) {
        this.arg_map = arg_map;
        creds = new UsernamePasswordCredentialsProvider(arg_map.get("un"),arg_map.get("pw"));
    }

    public void init() throws GitAPIException, IOException {
        Path git_path = Paths.get(arg_map.get("target-dir"));
        Repository repo;
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
            System.out.printf("\n>>> Repository exists at %s \n",arg_map.get("target-dir"));
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
    abstract void changeGitBranch(Git git, TextProgressMonitor consoleProgressMonitor, String defaultBranch) throws GitAPIException;
}
