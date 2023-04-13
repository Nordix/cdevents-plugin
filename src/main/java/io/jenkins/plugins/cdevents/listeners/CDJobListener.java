/**
 * Copyright FMR LLC <opensource@fidelity.com>
 * SPDX-License-Identifier: Apache-2.0
 */

package io.jenkins.plugins.cdevents.listeners;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import io.jenkins.plugins.cdevents.EventState;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Extension
public class CDJobListener extends RunListener<Run> {

    private static final boolean RUN_ASYNC = true;

    public CDJobListener() {
        super();
    }

    @Override
    public void onStarted(Run run, TaskListener listener) {
        CompletableFuture future = FutureRunner.captureEvent(EventState.STARTED, run, listener, "pipelineRun");
        if (!RUN_ASYNC) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onCompleted(Run run, TaskListener listener) {
        CompletableFuture future = FutureRunner.captureEvent(EventState.FINISHED, run, listener, "pipelineRun");
        if (!RUN_ASYNC) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
