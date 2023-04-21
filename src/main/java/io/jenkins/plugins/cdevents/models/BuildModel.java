/**
 * Copyright FMR LLC <opensource@fidelity.com>
 * SPDX-License-Identifier: Apache-2.0
 */

package io.jenkins.plugins.cdevents.models;

import java.io.Serializable;
import java.util.Map;

public class BuildModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fullUrl;
    private int number;
    private long queueId;
    private long duration;
    private String status;
    private String url;
    private String displayName;
    private Map<String, String> parameters;
    private ScmState scmState;

    public BuildModel() {
        super();
    }

    public BuildModel(BuildModel that) {
        this.fullUrl = that.getFullUrl();
        this.number = that.getNumber();
        this.queueId = that.getQueueId();
        this.duration = that.getDuration();
        this.status = that.getStatus();
        this.url = that.getUrl();
        this.displayName = that.getDisplayName();
        this.parameters = that.getParameters() == null ? null : Map.copyOf(that.getParameters());
        this.scmState = that.getScmState() == null ? null : new ScmState(that.getScmState());
    }

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException();
        }
        this.number = number;
    }

    public long getQueueId() {
        return queueId;
    }

    public void setQueueId(long queueId) {
        if (queueId <= 0) {
            throw new IllegalArgumentException();
        }
        this.queueId = queueId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Map<String, String> getParameters() {
        if (parameters == null) {
            return null;
        }
        return Map.copyOf(parameters);
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = Map.copyOf(parameters);
    }

    public ScmState getScmState() {
        if (scmState == null) {
            return null;
        }
        return new ScmState(this.scmState);
    }

    public void setScmState(ScmState scmState) {
        this.scmState = new ScmState(scmState);
    }
}