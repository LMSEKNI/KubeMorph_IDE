package com.example.Helm.Service;

import com.marcnuri.helm.ListCommand;
import com.marcnuri.helm.Release;

import java.util.List;

public interface ReleaseServ {
    public ListCommand listRelease() ;
    public String rollbackRelease(String releaseName, Integer version) ;
    public String getStatus(String releaseName) ;
    public void uninstallRelease(String releaseName) ;
    public List<Release> listReleases(
            boolean all,
            boolean allNamespaces,
            boolean deployed,
            boolean failed,
            boolean superseded,
            boolean pending,
            boolean uninstalled,
            boolean uninstalling);
}
