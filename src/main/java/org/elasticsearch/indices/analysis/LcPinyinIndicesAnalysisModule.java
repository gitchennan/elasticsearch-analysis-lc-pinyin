package org.elasticsearch.indices.analysis;

import org.elasticsearch.common.inject.AbstractModule;

public class LcPinyinIndicesAnalysisModule extends AbstractModule {
    @Override
    public void configure() {
        bind(LcPinyinIndicesAnalysis.class).asEagerSingleton();
    }
}
