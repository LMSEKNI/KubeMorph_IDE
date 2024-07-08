package com.example.Helm.Service;

import com.marcnuri.helm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

public interface ChartServ {
    public ShowCommand.ShowSubcommand showChart(String chartPath, String chartName, String[] flags) ;
    public List<SearchResult> searchRepo(String keyword) ;
    public List<String> searchHub(String keyword) ;
    public void addRepo(String namerepo, String url) ;
    public List<Repository> listRepo() ;
    public void removeRepo(String namerepo) ;
    public String updateRepo() ;
    public void createChart(String chartName, String directoryPath) ;
    public LintResult lintChart(String chartPath, boolean strict, boolean quiet) ;
    public Release installChart(String chartReference, String releaseName, String namespace, Boolean createNamespace, Boolean dryRun) ;
    public Release upgradeChart(String chartReference, String releaseName, String namespace, Boolean install, Boolean force, Boolean dryRun) ;
    public List<String> listDependencies(String path) ;
    public void updateDependencies(String chartPath) ;
    public Path packageChart(String chartPath) ;
    public VersionCommand getVersion() ;




    }
