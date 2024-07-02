package com.example.Helm.Service;


import com.example.Helm.Kubeconfig.KubernetesConfigServiceImpl;
import com.marcnuri.helm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class chart {
    private  Helm helm;
    private KubernetesConfigServiceImpl kubernetesConfigService;

    /////////////////////////////////configurer helm with k8s////////////////////////////////
    public void HelmService() {
        // Provide the path to the Kubernetes configuration file
        String kubeConfigPath = System.getenv("HOME") + "/.kube/config";
        this.helm = new Helm(Paths.get(kubeConfigPath));
    }
    /////////////////////////////////lister releases////////////////////////////////
    public ListCommand listCharts() {
        try {
            return helm.list();
        } catch (Exception e) {
            // Handle exceptions
            throw new RuntimeException("Failed to list Helm charts", e);
        }
    }
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
    /////////////////////////////////show all info of specific  chart helm with k8s////////////////////////////////
    /*
    * will be test it via postman by giving chartPath and chartName
    * */
    public ShowCommand.ShowSubcommand showChart(String chartPath, String chartName, String[] flags) {
        try {
            // Construct the full path to the chart
            Path fullPath = Path.of(chartPath, chartName);
            ShowCommand showCommand = helm.show(fullPath.toString());

            return showCommand.all();
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute 'helm show' command", e);
        }

    }

    //////////////////////search for repo from artifacthub///////////////////////////////////////
/*by  having this keyword in postman as param */
    public List<SearchResult> searchRepo( String keyword) {
        try {
            List<SearchResult> results = Helm.search()
                    .repo()
                    .withKeyword(keyword)
                    .call();
            return results ;
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute 'helm search' command", e);
        }
    }
    ///////////////////////////////////////////////////////////////////////////
    public List<String> searchHub(String keyword) {
        List<String> searchResults = new ArrayList<>();
        ProcessBuilder processBuilder = new ProcessBuilder("helm", "search", "hub", keyword);
        try {
            Process process = processBuilder.start();

            // Read output from the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                searchResults.add(line);
            }

            // Wait for the command to complete
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Failed to execute 'helm search hub " + keyword + "'. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Failed to execute 'helm search hub " + keyword + "'", e);
        }
        return searchResults;
    }
///////////////////////////////////////////////adding repo from artifacthub/////////////////////////////////
    /*
    * giving as params in postman : name , url */
    public void addRepo(String namerepo, String url) {
        try {
            Helm.repo()
                    .add()
                    .withName(namerepo)
                    .withUrl(URI.create(url))
                    .call();
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute 'helm repo add' command", e);
        }
    }
///////////////////////////////////////////////listing repo downloaded in local /////////////////////////////////
    public List<Repository> listRepo() {
        try {
            List<Repository> repo = Helm.repo()
                    .list()
                    .call();
            return  repo;
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute 'helm repo list' command", e);
        }
    }
///////////////////////////////////////////////remove repo downloaded in local /////////////////////////////////
    public void removeRepo(String namerepo) {
        try {
           Helm.repo()
                   .remove()
                   .withRepo(namerepo)
                   .call();

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute 'helm repo remove' command", e);
        }
    }
///////////////////////////////////////////////update repo downloaded in local /////////////////////////////////

    public String updateRepo() {
        StringBuilder output = new StringBuilder();
        try {
            Process process = new ProcessBuilder("helm", "repo", "update").start();

            try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                String s;
                while ((s = stdInput.readLine()) != null) {
                    output.append(s).append(System.lineSeparator());
                }
                while ((s = stdError.readLine()) != null) {
                    output.append(s).append(System.lineSeparator());
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Failed to execute 'helm repo update' command");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute 'helm repo update' command", e);
        }
        return output.toString();
    }
    ///////////////////////////////////////////////create  chart downloaded in local /////////////////////////////////
    private static final Logger logger = LoggerFactory.getLogger(chart.class);

    public void createChart(String chartName, String directoryPath) {
        logger.info("Attempting to create chart with name: {} in directory: {}", chartName, directoryPath);

        try {
            // Trim the directory path to remove any leading or trailing spaces
            directoryPath = directoryPath.trim();
            Path directory = Paths.get(directoryPath);
            logger.debug("Directory path resolved to: {}", directory.toAbsolutePath());

            // Validate the directory
            if (!Files.exists(directory)) {
                logger.error("Directory does not exist: {}", directory.toAbsolutePath());
                throw new IllegalArgumentException("Directory does not exist: " + directory.toAbsolutePath());
            }

            Helm.create()
                    .withName(chartName)
                    .withDir(directory)
                    .call();

            logger.info("Successfully created chart: {}", chartName);
        } catch (Exception e) {
            logger.error("Failed to execute 'helm create' command", e);
            throw new RuntimeException("Failed to execute 'helm create' command", e);
        }
    }

    //////////////////////////////////////////// helm lint . /////////////////////////////////////////////////////////////
    public LintResult lintChart(String chartPath, boolean strict, boolean quiet) {
        logger.info("Attempting to lint chart at path: {}", chartPath);

        try {
            Path path = Paths.get(chartPath);
            logger.debug("Chart path resolved to: {}", path.toAbsolutePath());

            LintCommand lintCommand = new Helm(path).lint();

            if (strict) {
                lintCommand.strict();
            }

            if (quiet) {
                lintCommand.quiet();
            }

            LintResult result = lintCommand.call();

            logger.info("Linting completed for chart: {}", chartPath);
            return result;
        } catch (Exception e) {
            logger.error("Failed to execute 'helm lint' command", e);
            throw new RuntimeException("Failed to execute 'helm lint' command", e);
        }
    }

    /////////////////////////////////////////////////////// install chartt /////////////////////////////////////////////////////////////////////

    /*  test inpostmen : chartReference : /home/asus/Bureau/Intedrnship_KubMorph/testpostmen
    *     and  releaseName :testpostmenrelease  give as output helm install releasename chartpath
    *  */
    public Release installChart(String chartReference, String releaseName, String namespace, Boolean createNamespace, Boolean dryRun) {
        try {
            logger.info("Installing chart with parameters: chartReference={}, releaseName={}, namespace={}, createNamespace={}, dryRun={}",
                    chartReference, releaseName, namespace, createNamespace, dryRun);

            // Validate chart reference format
            if (!chartReference.contains("/")) {
                throw new IllegalArgumentException("Chart reference should be in the form of repo_name/chart_name, got: " + chartReference);
            }

            InstallCommand installCommand = Helm.install(chartReference)
                    .withName(releaseName)
                    .withDescription("the-description")
                    .devel()
                    .dependencyUpdate()
                    .plainHttp()
                    .debug();

            // Use default namespace if none provided
            if (namespace != null && !namespace.isEmpty()) {
                installCommand.withNamespace(namespace);
            } else {
                installCommand.withNamespace("default");
            }

            if (createNamespace != null && createNamespace) {
                installCommand.createNamespace();
            }

            if (dryRun != null && dryRun) {
                installCommand.dryRun().withDryRunOption(DryRun.CLIENT);
            }

            Release release = installCommand.call();
            logger.info("Chart installed successfully: {}", release.getName());
            return release;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid chart reference provided", e);
            throw new RuntimeException("Invalid chart reference provided: " + chartReference, e);
        } catch (Exception e) {
            logger.error("Failed to execute 'helm install' command", e);
            throw new RuntimeException("Failed to execute 'helm install' command", e);
        }
    }

    //////////////////////////////////////////upgrade release //////////////////////////////////////////////////
/*
    * chartReference: The chart reference in the form repo_name/chart_name.
    releaseName: The name of the release.
    * exemple : chartReference : /home/asus/Bureau/Intedrnship_KubMorph/testpostmen
        *     and  releaseName :testpostmenrelease */

    public Release upgradeChart(String chartReference, String releaseName, String namespace, Boolean install, Boolean force, Boolean dryRun) {
        try {
            logger.info("Upgrading chart with parameters: chartReference={}, releaseName={}, namespace={}, install={}, force={}, dryRun={}",
                    chartReference, releaseName, namespace, install, force, dryRun);

            UpgradeCommand upgradeCommand = Helm.upgrade(chartReference)
                    .withName(releaseName)
                    .withNamespace(namespace)
                    .install()
                    .force()
                    .resetValues()
                    .reuseValues()
                    .resetThenReuseValues()
                    .atomic()
                    .cleanupOnFail()
                    .createNamespace()
                    .withDescription("the-description")
                    .devel()
                    .dependencyUpdate()
                    .dryRun()
                    .withDryRunOption(DryRun.CLIENT)
                    .waitReady()
                    .set("key", "value")
                    .withCertFile(Paths.get("path", "to", "cert"))
                    .plainHttp()
                    .debug();

            if (namespace != null && !namespace.isEmpty()) {
                upgradeCommand.withNamespace(namespace);
            }


            // Add more optional configurations here if needed

            Release release = upgradeCommand.call();
            logger.info("Chart upgraded successfully: {}", release.getName());
            return release;
        } catch (Exception e) {
            logger.error("Failed to execute 'helm upgrade' command", e);
            throw new RuntimeException("Failed to execute 'helm upgrade' command", e);
        }
    }
    //////////////////////////////// helm dependency list //////////////////////////////////////*
/*
* will have path to chart directory as params in postman */

    public List<String> listDependencies(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path is required");
        }

        try {
            logger.info("Listing dependencies for chart at path: {}", path);

            ProcessBuilder processBuilder = new ProcessBuilder("helm", "dependency", "list", path);
            processBuilder.directory(Paths.get(path).toFile());
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            List<String> dependencies = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                dependencies.add(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Failed to execute 'helm dependency list' command");
            }

            logger.info("Dependencies listed successfully");
            return dependencies;
        } catch (Exception e) {
            logger.error("Failed to execute 'helm dependency list' command", e);
            throw new RuntimeException("Failed to execute 'helm dependency list' command", e);
        }
    }


    ///////////////////////
    public void updateDependencies(String chartPath) {
        try {
            // Create a ProcessBuilder to execute the 'helm dependency update' command
            ProcessBuilder processBuilder = new ProcessBuilder("helm", "dependency", "update");
            processBuilder.directory(new File(chartPath));

            // Start the process and capture the output
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            // Read the output from the process
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // Check the exit code to determine if the command was successful
            if (exitCode == 0) {
                System.out.println("Update Complete. ⎈Happy Helming!⎈");
            } else {
                System.out.println("Error: Failed to update dependencies. Exit code: " + exitCode);
            }
        } catch (InterruptedException | IOException e) {
            System.out.println("Error: Failed to execute 'helm dependency update' command");
            e.printStackTrace();
        }
    }
    ////////////////////////////////////// helm package chart//////////////////////////////////////
    /*postman didn't work this function ::
    * Failed to execute 'helm package' command: class com.marcnuri.helm.Helm cannot be cast to class java.nio.file.Path (com.marcnuri.helm.Helm is in unnamed module of loader 'app'; java.nio.file.Path is in module java.base of loader 'bootstrap')
     */
    public static Path packageChart(String chartPath) {
        // Trim the chartPath and convert it to a Path object
        chartPath = chartPath.trim();
        Path chart = Paths.get(chartPath);

        // Check if the chart directory exists
        if (!Files.exists(chart)) {
            throw new IllegalArgumentException("Chart directory does not exist: " + chart.toAbsolutePath());
        }

        try {
            // Initialize Helm and PackageCommand
            Helm helm = new Helm(chart);
            PackageCommand packageCommand = helm.packageIt();

            // Execute the package command
            Path result = (Path) packageCommand.call();

            // Print success message
            System.out.println("Successfully packaged chart and saved it to: " + result.toAbsolutePath());

            return result;
        } catch (Exception e) {
            // Print error message
            System.err.println("Failed to execute 'helm package' command: " + e.getMessage());
            throw new RuntimeException("Failed to execute 'helm package' command", e);
        }
    }
    //////////////////////////////////unsintall ///////////////////////////////
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
    ////////////////////////////////////version ///////////////////////////////
    public VersionCommand getVersion() {
        try {
            return Helm.version();
        } catch (Exception e) {
            // Log any exceptions that occur during the version retrieval process
            throw new RuntimeException("Failed to retrieve Helm version", e);
        }
    }
    /////////////////////////////////////helm rollback //////////////////////////////////////////
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

    ///////////////////////////////////
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


}
