package com.example.Helm.Controller;

import com.example.Helm.Service.chart;
import com.marcnuri.helm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/helm")
public class ChartController {
    private final chart chartService;

    @Autowired
    public ChartController(chart chartService) {
        this.chartService = chartService;
    }

    @GetMapping("/list")
    public ListCommand listCharts() {
        return chartService.listCharts();
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
        return chartService.listReleases(all, allNamespaces, deployed, failed, superseded, pending, uninstalled, uninstalling);
    }

    @GetMapping("/chart")
    public ShowCommand.ShowSubcommand showChart(
            @RequestParam String chartPath,
            @RequestParam String chartName,
            @RequestParam(required = false) String[] flags) {
        // Invoke the showChart function with the provided parameters
        return chartService.showChart(chartPath, chartName, flags != null ? flags : new String[0]);
    }
    @GetMapping("/search")
    public List<SearchResult> searchRepo( @RequestParam String keyword) {
        // Invoke the searchRepo function with the provided parameters
        return chartService.searchRepo(keyword);
    }

    @GetMapping("/addRepo")
    public void addRepo(
            @RequestParam String name,
            @RequestParam String url
    ) {
        // Invoke the addRepo function with the provided parameters
        chartService.addRepo(name, url);
    }

    @GetMapping("/listRepo")
    public List<Repository> listRepo() {
        return chartService.listRepo();
    }

    @DeleteMapping("/removeRepo")
    public String removeRepo(@RequestParam String namerepo) {
        try {
            chartService.removeRepo(namerepo);
            return "Repository '" + namerepo + "' has been removed successfully.";
        } catch (Exception e) {
            return "Failed to remove repository '" + namerepo + "': " + e.getMessage();
        }
    }

    @GetMapping("/updateRepo")
    public String updateRepo() {
        try {
            chartService.updateRepo();
            return "Repository has been updated successfully.";
        } catch (Exception e) {
            return "Failed to update repository: " + e.getMessage();
        }
    }

    @GetMapping("/createChart")
    public String createChart(@RequestParam String chartName, @RequestParam String directoryPath) {
        try {
            chartService.createChart(chartName, directoryPath);
            return "Chart created successfully.";
        } catch (Exception e) {
            return "Failed to create chart: " + e.getMessage();
        }
    }

    @GetMapping("/lint-chart")
    public LintResult lintChart(@RequestParam String chartPath,
                                @RequestParam(required = false, defaultValue = "false") boolean strict,
                                @RequestParam(required = false, defaultValue = "false") boolean quiet) {
        return chartService.lintChart(chartPath, strict, quiet);
    }

    @GetMapping("/installChart")
    public Release installChart(@RequestParam String chartReference,
                                @RequestParam String releaseName,
                                @RequestParam(required = false) String namespace,
                                @RequestParam(required = false) Boolean createNamespace,
                                @RequestParam(required = false) Boolean dryRun) {
        return chartService.installChart(chartReference, releaseName, namespace, createNamespace, dryRun);
    }

    @GetMapping("/upgradeChart")
    public Release upgradeChart(@RequestParam String chartReference,
                                @RequestParam String releaseName,
                                @RequestParam(required = false) String namespace,
                                @RequestParam(required = false) Boolean install,
                                @RequestParam(required = false) Boolean force,
                                @RequestParam(required = false) Boolean dryRun) {
        return chartService.upgradeChart(chartReference, releaseName, namespace, install, force, dryRun);
    }
    @GetMapping("/listDependencies")
    public List<String> listDependencies(@RequestParam String path) {
        return chartService.listDependencies(path);
    }

    @GetMapping("/updateDependencies")
    public String updateDependencies(@RequestParam String path,
                                     @RequestParam Boolean skipRefresh,
                                     @RequestParam Boolean verify,
                                     @RequestParam Boolean debug) {
        chartService.updateDependencies(path);
        return "Dependencies updated successfully";
    }

    @PostMapping("/package-chart")
    public String packageChart(@RequestParam String chartPath) {
        try {
            Path result = chartService.packageChart(chartPath);
            return "Successfully packaged chart and saved it to: " + result;
        } catch (Exception e) {
            return "Failed to package chart: " + e.getMessage();
        }
    }

    @DeleteMapping("/delete-release")
    public String uninstallRelease(@RequestParam String releaseName) {
        try {
            chartService.uninstallRelease(releaseName);
            return "Successfully uninstalled release: " + releaseName;
        } catch (Exception e) {
            return "Failed to uninstall release: " + e.getMessage();
        }
    }
    @GetMapping("/version")
    public VersionCommand getVersion() {
        return chartService.getVersion();
    }

    @GetMapping("/rollback-release")
    public String rollbackRelease(
            @RequestParam String releaseName,
            @RequestParam(required = false) Integer version) {
        chartService.rollbackRelease(releaseName, version);
        return "Rollback initiated for release: " + releaseName;
    }

    @GetMapping("/status")
    public String getStatus(@RequestParam String releaseName) {
        return chartService.getStatus(releaseName);
    }

}
