/*******************************************************************************
 * Copyright (c) 2019 École Polytechnique de Montréal
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License 2.0 which
 * accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package org.eclipse.tracecompass.incubator.scripting.core.analysis;

import java.util.Objects;

import org.eclipse.ease.modules.WrapToScript;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.tmf.core.analysis.IAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceManager;

/**
 * Provide an API to create an analysis. Using an analysis links the outputs of
 * the analysis with the trace. For example, the backends (state systems,
 * segment store) filled by the script will be available under the trace.
 * <p>
 * Typical use of this module is for scripts who wish to parse the events of a
 * trace or experiment and save data in a backend to be displayed later.
 * </p>
 * <p>
 * Example scripts with data providers can be found here:
 * </p>
 * <ul>
 * <li><a href="../doc-files/scriptedDataProvider.js">A scripted time graph data
 * provider</a> with script-defined entries and arrows (javascript)</li>
 * <li><a href="../doc-files/basicAnalysis.js">A basic analysis</a> building an
 * state system and showing its data in a time graph (javascript)</li>
 * <li><a href="../doc-files/basicAnalysis.py">Same basic analysis as
 * before</a>, building a state system and showing its data in a time graph, in
 * python</li>
 * </ul>
 *
 * @author Geneviève Bastien
 */
public class AnalysisScriptingModule {

    /**
     * A wrapper method to get a specified analysis of a trace. It returns an
     * existing analysis, whether builtin or data-driven, for the trace. These
     * analyses cannot create state systems or anything else, but their results
     * can be queried and visualized in a script.
     * <p>
     * The analyses can be queried by ID, or by name, ie the text that is
     * displayed in the Trace Compass UI.
     * </p>
     *
     * @param trace
     *            The trace being analyzed.
     * @param analysisName
     *            The analysis module name or ID to get.
     * @return The module
     */
    @WrapToScript
    public @Nullable IAnalysisModule getTraceAnalysis(ITmfTrace trace, String analysisName) {
        for (ITmfTrace childTrace : TmfTraceManager.getTraceSetWithExperiment(trace)) {
            for (IAnalysisModule module : childTrace.getAnalysisModules()) {
                if (module.getName().equals(analysisName) || module.getId().equals(analysisName)) {
                    return module;
                }
            }
        }
        return null;
    }

    /**
     * Create a scripted analysis with the given name for a trace. If the trace
     * is <code>null</code>, it will throw a {@link NullPointerException}.
     *
     * @param trace
     *            The trace being analyzed.
     * @param analysisName
     *            The name of the analysis to create. If backends like state
     *            systems are saved by this analysis, this name will be used to
     *            retrieve the previous data.
     * @return The new analysis for this trace.
     */
    @WrapToScript
    public ScriptedAnalysis createScriptedAnalysis(@Nullable ITmfTrace trace, String analysisName) {
        return new ScriptedAnalysis(Objects.requireNonNull(trace, "Trace should not be null"), analysisName); //$NON-NLS-1$
    }
}
