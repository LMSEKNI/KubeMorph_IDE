package com.example.Helm.Controller;

import com.example.Helm.Service.ChartServ;
import com.example.Helm.Service.ChartServImpl;
import com.example.Helm.Service.ReleaseServ;
import com.marcnuri.helm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")


@RestController
@RequestMapping("/api/helm")
public class ChartController {

    private final ChartServ chartServ;
    private final ReleaseServ releaseServ ;

    @Autowired
    public ChartController(ChartServ chartServ,
                           ReleaseServ releaseServ) {
        this.chartServ = chartServ;
        this.releaseServ = releaseServ;
    }

    /////////////////////////////////////Chart Controller/////////////////////////

    @GetMapping("/chart")
    public ShowCommand.ShowSubcommand showChart(
            @RequestParam String chartPath,
            @RequestParam String chartName,
            @RequestParam(required = false) String[] flags) {
        // Invoke the showChart function with the provided parameters
        return chartServ.showChart(chartPath, chartName, flags != null ? flags : new String[0]);
    }
    @GetMapping("/helm-search")
    public List<String> searchHub(@RequestParam String keyword) {
        return chartServ.searchHub(keyword);
    }
    @GetMapping("/search")
    public List<SearchResult> searchRepo( @RequestParam String keyword) {
        // Invoke the searchRepo function with the provided parameters
        return chartServ.searchRepo(keyword);
    }

    @GetMapping("/addRepo")
    public void addRepo(
            @RequestParam String name,
            @RequestParam String url
    ) {
        // Invoke the addRepo function with the provided parameters
        chartServ.addRepo(name, url);
    }

    @GetMapping("/listRepo")
    public List<Repository> listRepo() {
        return chartServ.listRepo();
    }

    @DeleteMapping("/removeRepo")
    public String removeRepo(@RequestParam String namerepo) {
        try {
            chartServ.removeRepo(namerepo);
            return "Repository '" + namerepo + "' has been removed successfully.";
        } catch (Exception e) {
            return "Failed to remove repository '" + namerepo + "': " + e.getMessage();
        }
    }

    @GetMapping("/updateRepo")
    public String updateRepo() {
        try {
            chartServ.updateRepo();
            return "Repository has been updated successfully.";
        } catch (Exception e) {
            return "Failed to update repository: " + e.getMessage();
        }
    }

    @GetMapping("/createChart")
    public String createChart(@RequestParam String chartName,
                              @RequestParam String directoryPath) {
        try {
            chartServ.createChart(chartName, directoryPath);
            return "Chart created successfully.";
        } catch (Exception e) {
            return "Failed to create ChartServImpl: " + e.getMessage();
        }
    }

    @GetMapping("/lint-chart")
    public LintResult lintChart(@RequestParam String chartPath,
                                @RequestParam(required = false, defaultValue = "false") boolean strict,
                                @RequestParam(required = false, defaultValue = "false") boolean quiet) {
        return chartServ.lintChart(chartPath, strict, quiet);
    }

    @GetMapping("/installChart")
    public Release installChart(@RequestParam String chartReference,
                                @RequestParam String releaseName,
                                @RequestParam(required = false) String namespace,
                                @RequestParam(required = false) Boolean createNamespace,
                                @RequestParam(required = false) Boolean dryRun) {
        return chartServ.installChart(chartReference, releaseName, namespace, createNamespace, dryRun);
    }

    @GetMapping("/upgradeChart")
    public Release upgradeChart(@RequestParam String chartReference,
                                @RequestParam String releaseName,
                                @RequestParam(required = false) String namespace,
                                @RequestParam(required = false) Boolean install,
                                @RequestParam(required = false) Boolean force,
                                @RequestParam(required = false) Boolean dryRun) {
        return chartServ.upgradeChart(chartReference, releaseName, namespace, install, force, dryRun);
    }
    @GetMapping("/listDependencies")
    public List<String> listDependencies(@RequestParam String path) {
        return chartServ.listDependencies(path);
    }

    @GetMapping("/updateDependencies")
    public String updateDependencies(@RequestParam String path,
                                     @RequestParam Boolean skipRefresh,
                                     @RequestParam Boolean verify,
                                     @RequestParam Boolean debug) {
        chartServ.updateDependencies(path);
        return "Dependencies updated successfully";
    }

    @PostMapping("/package-chart")
    public String packageChart(@RequestParam String chartPath) {
        try {
            Path result = chartServ.packageChart(chartPath);
            return "Successfully packaged ChartServImpl and saved it to: " + result;
        } catch (Exception e) {
            return "Failed to package ChartServImpl: " + e.getMessage();
        }
    }


    @GetMapping("/version")
    public VersionCommand getVersion() {
        return chartServ.getVersion();
    }
    private static final Logger logger = LoggerFactory.getLogger(ChartController.class);
//////////////////////Release Controller /////////////////////////////////
    @GetMapping("/list")
    public ListCommand listCharts() {
        return releaseServ.listRelease();
    }

    @GetMapping("/listrevisite")
    public List<Release> listReleases(
            @RequestParam(required = false, defaultValue = "false") boolean all,
            @RequestParam(required = false, defaultValue = "false") boolean allNamespaces,
            @RequestParam(required = false, defaultValue = "true") boolean deployed,
            @RequestParam(required = false, defaultValue = "false") boolean failed,
            @RequestParam(required = false, defaultValue = "false") boolean superseded,
            @RequestParam(required = false, defaultValue = "false") boolean pending,
            @RequestParam(required = false, defaultValue = "false") boolean uninstalled,
            @RequestParam(required = false, defaultValue = "false") boolean uninstalling) {
        return releaseServ.listReleases(all, allNamespaces, deployed, failed, superseded, pending, uninstalled, uninstalling);
    }

    @DeleteMapping("/delete-release")
    public String uninstallRelease(@RequestParam String releaseName) {
        try {
            releaseServ.uninstallRelease(releaseName);
            return "Successfully uninstalled release: " + releaseName;
        } catch (Exception e) {
            return "Failed to uninstall release: " + e.getMessage();
        }
    }

    @GetMapping("/rollback-release")
    public ResponseEntity<String> rollbackRelease(
            @RequestParam String releaseName,
            @RequestParam(required = false) Integer version) {
        logger.info("Received rollback request for release: {}, version: {}", releaseName, version);
        try {
            String result = releaseServ.rollbackRelease(releaseName, version);
            logger.info("Rollback response: {}", result);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            logger.error("Error during rollback: {}", e.getMessage());
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus(@RequestParam String releaseName) {
        String status = releaseServ.getStatus(releaseName);
        return ResponseEntity.ok().body(status);
    }

}
