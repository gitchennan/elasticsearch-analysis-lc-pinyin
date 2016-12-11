package org.elasticsearch.plugin.analysis.lc;

import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.LcAnalysisBinderProcessor;
import org.elasticsearch.indices.analysis.LcPinyinIndicesAnalysisModule;
import org.elasticsearch.plugins.AbstractPlugin;

import java.util.Collection;

public class AnalysisLcPinyinPlugin extends AbstractPlugin {

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
    public Collection<Class<? extends Module>> modules() {
        return ImmutableList.<Class<? extends Module>>of(LcPinyinIndicesAnalysisModule.class);
    }

    public void onModule(AnalysisModule module) {
        module.addProcessor(new LcAnalysisBinderProcessor());
    }
}
