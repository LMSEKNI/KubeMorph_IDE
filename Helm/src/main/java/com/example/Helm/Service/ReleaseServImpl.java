package com.example.Helm.Service;

import com.marcnuri.helm.Helm;
import com.marcnuri.helm.ListCommand;
import com.marcnuri.helm.Release;
import com.marcnuri.helm.UninstallCommand;
import org.intellij.lang.annotations.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReleaseServImpl implements ReleaseServ
{
    private  Helm helm;
    private static final Logger logger = LoggerFactory.getLogger(ChartServImpl.class);

    public void HelmConfiguration() {
        String kubeConfigPath = System.getenv("HOME") + "/.kube/config";
        this.helm = new Helm(Paths.get(kubeConfigPath));
    }
    @Override
    public ListCommand listRelease() {
        try {
            return helm.list();
        } catch (Exception e) {
            // Handle exceptions
            throw new RuntimeException("Failed to list Helm charts", e);
        }
    }
    //////////////////////////////other implementataion for listrelease ///////////////////////
    @Override
    public List<Release> listReleases(
            boolean all,
            boolean allNamespaces,
            boolean deployed,
            boolean failed,
            boolean superseded,
            boolean pending,
            boolean uninstalled,
            boolean uninstalling) {
        try {
            ListCommand listCommand = helm.list();

            if (all) {
                listCommand.all();
            }
            if (allNamespaces) {
                listCommand.allNamespaces();
            }

            if (deployed) {
                listCommand.deployed();
            }
            if (failed) {
                listCommand.failed();
            }

            if (pending) {
                listCommand.pending();
            }
            if (superseded) {
                listCommand.superseded();
            }
            if (uninstalled) {
                listCommand.superseded();
            }
            if (uninstalling) {
                listCommand.superseded();
            }

            return listCommand.call();
        } catch (Exception e) {
            throw new RuntimeException("Failed to list Helm releases", e);
        }
    }
    @Override
    public String rollbackRelease(String releaseName, Integer version) {
        try {
            logger.info("Performing rollback for release: {}", releaseName);

            List<String> command = new ArrayList<>();
            command.add("helm");
            command.add("rollback");
            command.add(releaseName);
            if (version != null) {
                command.add(version.toString());
            }

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
                output.append(line);
            }
            while ((line = errorReader.readLine()) != null) {
                logger.error(line);
                output.append(line);
            }

            int exitCode = process.waitFor();
            logger.info("Rollback exit code: {}", exitCode);

            if (exitCode != 0) {
                String errorMessage = output.toString();
                if (errorMessage.contains("release has no")) {
                    return "Error: release has no previous version to rollback to.";
                }
                throw new RuntimeException("Failed to execute 'helm rollback' command: " + errorMessage);
            }

            logger.info("Rollback was successful for release: {}", releaseName);
            return "Rollback initiated for release: " + releaseName;
        } catch (Exception e) {
            logger.error("Failed to execute 'helm rollback' command", e);
            throw new RuntimeException("Failed to execute 'helm rollback' command", e);
        }
    }

    @Override
    public String getStatus(String releaseName) {
        try {
            Process process = new ProcessBuilder("helm", "status", releaseName).start();
            StringBuilder output = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Failed to execute 'helm status' command");
            }

            return output.toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to execute 'helm status' command", e);
        }
    }
    @Override
    public void uninstallRelease(String releaseName) {
        try {
            // Log the start of the function
            logger.info("Uninstalling release: {}", releaseName);

            // Execute the uninstall command without dry run
            Helm.uninstall(releaseName)
                    .noHooks()
                    .ignoreNotFound()
                    .keepHistory()
                    .withCascade(UninstallCommand.Cascade.BACKGROUND)
                    .withNamespace("default")
                    .debug()
                    .call();

            // Log a success message
            logger.info("Successfully uninstalled release: {}", releaseName);
        } catch (Exception e) {
            // Log any exceptions that occur during the uninstallation process
            logger.error("Failed to uninstall release: {}", releaseName, e);
            throw new RuntimeException("Failed to uninstall release: " + releaseName, e);
        }
    }
}
