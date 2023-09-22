package com.hcl.appscan.maven.plugin.targets;

import com.hcl.appscan.sdk.scanners.sast.targets.DefaultTarget;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdditionalTarget extends DefaultTarget {

    private File m_targetFile;
    private Map<String, String> m_properties = new HashMap<String, String>();

    public AdditionalTarget() {
    }

    @Override
    public File getTargetFile() {
        return m_targetFile;
    }

    @Override
    public Map<String, String> getProperties() {
        return m_properties;
    }

    public void setTargetFile(File m_targetFile) {
        this.m_targetFile = m_targetFile;
    }

    public void setProperties(Map<String, String> m_properties) {
        this.m_properties = m_properties;
    }

    public void setExclusionPatterns(List<String> exclusionPatterns) {
        getExclusionPatterns().clear();
        getExclusionPatterns().addAll(exclusionPatterns);
    }

    public void setInclusionPatterns(List<String> inclusionPatterns) {
        getInclusionPatterns().clear();
        getInclusionPatterns().addAll(inclusionPatterns);
    }
}
