package org.elasticsearch;

import junit.framework.TestCase;
import org.elasticsearch.common.inject.Binder;
import org.elasticsearch.common.inject.binder.AnnotatedBindingBuilder;
import org.elasticsearch.indices.analysis.LcPinyinIndicesAnalysis;
import org.elasticsearch.indices.analysis.LcPinyinIndicesAnalysisModule;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.internal.util.reflection.Whitebox.setInternalState;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LcPinyinIndicesAnalysisModule.class, Binder.class})
public class PinyinIndicesAnalysisModuleTest extends TestCase {
    @PrepareForTest
    public final void testConfigure() throws Exception {
        // given
        AnnotatedBindingBuilder annotatedBindingBuilderMock = mock(AnnotatedBindingBuilder.class);
        Binder binderMock = mock(Binder.class);
        doReturn(annotatedBindingBuilderMock).when(binderMock).bind(LcPinyinIndicesAnalysis.class);
        LcPinyinIndicesAnalysisModule pinyinIndicesAnalysisModule = new LcPinyinIndicesAnalysisModule();
        setInternalState(pinyinIndicesAnalysisModule, "binder", binderMock);

        // when
        pinyinIndicesAnalysisModule.configure();

        // then
        verifyPrivate(pinyinIndicesAnalysisModule).invoke("bind", LcPinyinIndicesAnalysis.class);
    }
}
