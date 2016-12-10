package org.elasticsearch.plugin.analysis.lc;

import org.elasticsearch.common.inject.Module;
import org.elasticsearch.indices.analysis.LcPinyinIndicesAnalysisModule;
import org.elasticsearch.plugins.Plugin;

import java.util.Collection;
import java.util.Collections;

public class AnalysisLcPinyinPlugin extends Plugin {

    public static String PLUGIN_NAME = "analysis-lc-pinyin";

    @Override
    public String name() {
        return PLUGIN_NAME;
    }

    @Override
    public String description() {
        return PLUGIN_NAME;
    }

    @Override
    public Collection<Module> nodeModules() {
        return Collections.<Module>singletonList(new LcPinyinIndicesAnalysisModule());
    }
}
