package com.example.Helm.Service;


import com.example.Helm.Kubeconfig.KubernetesConfigServiceImpl;
import com.marcnuri.helm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ChartServImpl implements ChartServ{
    private  Helm helm;


    /////////////////////////////////configurer helm with k8s////////////////////////////////
    public void HelmConfiguration() {
        String kubeConfigPath = System.getenv("HOME") + "/.kube/config";
        this.helm = new Helm(Paths.get(kubeConfigPath));
    }


    /////////////////////////////////show all info of specific  ChartServImpl helm with k8s////////////////////////////////
    /*
    * will be test it via postman by giving chartPath and chartName
    * */
    @Override
    public ShowCommand.ShowSubcommand showChart(String chartPath, String chartName, String[] flags) {
        try {
            // Construct the full path to the ChartServImpl
            Path fullPath = Path.of(chartPath, chartName);
            ShowCommand showCommand = helm.show(fullPath.toString());

            return showCommand.all();
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute 'helm show' command", e);
        }

    }

    //////////////////////search for repo from artifacthub///////////////////////////////////////
/*by  having this keyword in postman as param */
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    @Override
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
    ///////////////////////////////////////////////create  ChartServImpl downloaded in local /////////////////////////////////

    private static final Logger logger = LoggerFactory.getLogger(ChartServImpl.class);
    @Override
    public void createChart(String chartName, String directoryPath) {
        logger.info("Attempting to create ChartServImpl with name: {} in directory: {}", chartName, directoryPath);

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

            logger.info("Successfully created ChartServImpl: {}", chartName);
        } catch (Exception e) {
            logger.error("Failed to execute 'helm create' command", e);
            throw new RuntimeException("Failed to execute 'helm create' command", e);
        }
    }

    //////////////////////////////////////////// helm lint . /////////////////////////////////////////////////////////////
    @Override
    public LintResult lintChart(String chartPath, boolean strict, boolean quiet) {
        logger.info("Attempting to lint ChartServImpl at path: {}", chartPath);

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

            logger.info("Linting completed for ChartServImpl: {}", chartPath);
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
    @Override
    public Release installChart(String chartReference, String releaseName, String namespace, Boolean createNamespace, Boolean dryRun) {
        try {
            logger.info("Installing ChartServImpl with parameters: chartReference={}, releaseName={}, namespace={}, createNamespace={}, dryRun={}",
                    chartReference, releaseName, namespace, createNamespace, dryRun);

            // Validate ChartServImpl reference format
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
            logger.error("Invalid ChartServImpl reference provided", e);
            throw new RuntimeException("Invalid ChartServImpl reference provided: " + chartReference, e);
        } catch (Exception e) {
            logger.error("Failed to execute 'helm install' command", e);
            throw new RuntimeException("Failed to execute 'helm install' command", e);
        }
    }

    //////////////////////////////////////////upgrade release //////////////////////////////////////////////////
/*
    * chartReference: The ChartServImpl reference in the form repo_name/chart_name.
    releaseName: The name of the release.
    * exemple : chartReference : /home/asus/Bureau/Intedrnship_KubMorph/testpostmen
        *     and  releaseName :testpostmenrelease */
    @Override
    public Release upgradeChart(String chartReference, String releaseName, String namespace, Boolean install, Boolean force, Boolean dryRun) {
        try {
            logger.info("Upgrading ChartServImpl with parameters: chartReference={}, releaseName={}, namespace={}, install={}, force={}, dryRun={}",
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
* will have path to ChartServImpl directory as params in postman */
    @Override
    public List<String> listDependencies(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path is required");
        }

        try {
            logger.info("Listing dependencies for ChartServImpl at path: {}", path);

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
    @Override
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
    ////////////////////////////////////// helm package ChartServImpl//////////////////////////////////////
    /*postman didn't work this function ::
    * Failed to execute 'helm package' command: class com.marcnuri.helm.Helm cannot be cast to class java.nio.file.Path (com.marcnuri.helm.Helm is in unnamed module of loader 'app'; java.nio.file.Path is in module java.base of loader 'bootstrap')
     */
    @Override
    public Path packageChart(String chartPath) {
        // Trim the chartPath and convert it to a Path object
        chartPath = chartPath.trim();
        Path chart = Paths.get(chartPath);

        // Check if the ChartServImpl directory exists
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
            System.out.println("Successfully packaged ChartServImpl and saved it to: " + result.toAbsolutePath());

            return result;
        } catch (Exception e) {
            // Print error message
            System.err.println("Failed to execute 'helm package' command: " + e.getMessage());
            throw new RuntimeException("Failed to execute 'helm package' command", e);
        }
    }

    ////////////////////////////////////version ///////////////////////////////
    @Override
    public VersionCommand getVersion() {
        try {
            return Helm.version();
        } catch (Exception e) {
            // Log any exceptions that occur during the version retrieval process
            throw new RuntimeException("Failed to retrieve Helm version", e);
        }
    }


}
