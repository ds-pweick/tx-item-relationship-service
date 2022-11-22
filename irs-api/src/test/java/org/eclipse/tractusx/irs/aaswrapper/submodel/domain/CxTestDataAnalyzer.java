/********************************************************************************
 * Copyright (c) 2021,2022
 *       2022: Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *       2022: ZF Friedrichshafen AG
 *       2022: ISTOS GmbH
 * Copyright (c) 2021,2022 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0. *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ********************************************************************************/
package org.eclipse.tractusx.irs.aaswrapper.submodel.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.tractusx.irs.component.enums.BomLifecycle;
import org.eclipse.tractusx.irs.component.enums.Direction;
import org.eclipse.tractusx.irs.configuration.local.CxTestDataContainer;
import org.eclipse.tractusx.irs.util.LocalTestDataConfigurationAware;
import org.junit.jupiter.api.Test;

@Slf4j
class CxTestDataAnalyzer extends LocalTestDataConfigurationAware {

    private final CxTestDataContainer cxTestDataContainer;

    CxTestDataAnalyzer() throws IOException {
        super();

        cxTestDataContainer = localTestDataConfiguration.cxTestDataContainer();;
    }

    @Test
    void parseAndPrintExpectedDataResultsAsBuilt() {
        final TestParameters testParameters = TestParameters.builder()
                .globalAssetId("urn:uuid:a4a2ba57-1c50-48ad-8981-7a0ef032146b")
                .bomLifecycle(BomLifecycle.AS_BUILT)
                .direction(Direction.DOWNWARD)
                .shouldCountAssemblyPartRelationship(Boolean.TRUE)
                .shouldCountSerialPartTypization(Boolean.TRUE)
                .shouldCountBatch(Boolean.TRUE)
                .shouldCountMaterialForRecycling(Boolean.TRUE)
                .shouldCountProductDescription(Boolean.TRUE)
                .shouldCountPhysicalDimension(Boolean.TRUE)
                .build();

        long expectedNumberOfRelationships = countExpectedNumberOfRelationshipsFor(testParameters.globalAssetId, RelationshipAspect.from(testParameters.bomLifecycle, testParameters.direction));
        long expectedNumberOfSubmodels = countExpectedNumberOfSubmodelsFor(testParameters.globalAssetId, testParameters);

        log.info("Results for globalAssetId {} and bomLifecycle {} with direction {}", testParameters.globalAssetId, testParameters.bomLifecycle, testParameters.direction);
        log.info("Expected number of relationships: " + expectedNumberOfRelationships);
        log.info("Expected number of submodels: " + expectedNumberOfSubmodels);

        assertThat(expectedNumberOfRelationships).isNotNull();
        assertThat(expectedNumberOfSubmodels).isNotNull();
    }

    @Test
    void parseAndPrintExpectedDataResultsAsPlanned() {
        final TestParameters testParameters = TestParameters.builder()
                                                            .globalAssetId("urn:uuid:aad27ddb-43aa-4e42-98c2-01e529ef127c")
                                                            .bomLifecycle(BomLifecycle.AS_PLANNED)
                                                            .direction(Direction.DOWNWARD)
                                                            .shouldCountSingleLevelBomAsPlanned(Boolean.TRUE)
                                                            .shouldCountPartAsPlanned(Boolean.TRUE)
                                                            .build();

        long expectedNumberOfRelationships = countExpectedNumberOfRelationshipsFor(testParameters.globalAssetId, RelationshipAspect.from(testParameters.bomLifecycle, testParameters.direction));
        long expectedNumberOfSubmodels = countExpectedNumberOfSubmodelsFor(testParameters.globalAssetId, testParameters);

        log.info("Results for globalAssetId {} and bomLifecycle {} with direction {}", testParameters.globalAssetId, testParameters.bomLifecycle, testParameters.direction);
        log.info("Expected number of relationships: " + expectedNumberOfRelationships);
        log.info("Expected number of submodels: " + expectedNumberOfSubmodels);

        assertThat(expectedNumberOfRelationships).isNotNull();
        assertThat(expectedNumberOfSubmodels).isNotNull();
    }

    private long countExpectedNumberOfRelationshipsFor(final String catenaXId, final RelationshipAspect relationshipAspect) {
        final Optional<CxTestDataContainer.CxTestData> cxTestData = cxTestDataContainer.getByCatenaXId(catenaXId);

        Optional<Map<String, Object>> submodelData = Optional.empty();

        if (relationshipAspect.equals(RelationshipAspect.AssemblyPartRelationship)) {
            submodelData = cxTestData.flatMap(CxTestDataContainer.CxTestData::getAssemblyPartRelationship);
        } else if (relationshipAspect.equals(RelationshipAspect.SingleLevelBomAsPlanned)) {
            submodelData = cxTestData.flatMap(CxTestDataContainer.CxTestData::getSingleLevelBomAsPlanned);
        }

        if (submodelData.isPresent()) {
            final RelationshipSubmodel relationshipSubmodel = objectMapper.convertValue(submodelData, relationshipAspect.getSubmodelClazz());
            final long countRelationships = relationshipSubmodel.asRelationships().size();
            final AtomicLong counter = new AtomicLong(countRelationships);

            relationshipSubmodel.asRelationships().forEach(relationship -> {
                final String childGlobalAssetId = relationship.getLinkedItem().getChildCatenaXId().getGlobalAssetId();
                final long expectedNumberOfChildRelationships = countExpectedNumberOfRelationshipsFor(childGlobalAssetId, relationshipAspect);
                counter.addAndGet(expectedNumberOfChildRelationships);
            });

            return counter.get();
        }

        return 0;
    }

    private long countExpectedNumberOfSubmodelsFor(final String catenaXId, final TestParameters testParameters) {
        final Optional<CxTestDataContainer.CxTestData> cxTestData = cxTestDataContainer.getByCatenaXId(catenaXId);
        final RelationshipAspect relationshipAspect = RelationshipAspect.from(testParameters.bomLifecycle, testParameters.direction);

        Optional<Map<String, Object>> submodelData = Optional.empty();

        final AtomicLong counter = new AtomicLong();

        if (relationshipAspect.equals(RelationshipAspect.AssemblyPartRelationship)) {
            submodelData = cxTestData.flatMap(CxTestDataContainer.CxTestData::getAssemblyPartRelationship);
            checkAndIncrementCounter(testParameters.shouldCountAssemblyPartRelationship, submodelData, counter);

            checkAndIncrementCounter(testParameters.shouldCountSerialPartTypization,
                    cxTestData.flatMap(CxTestDataContainer.CxTestData::getSerialPartTypization), counter);
            checkAndIncrementCounter(testParameters.shouldCountBatch,
                    cxTestData.flatMap(CxTestDataContainer.CxTestData::getBatch), counter);
            checkAndIncrementCounter(testParameters.shouldCountMaterialForRecycling,
                    cxTestData.flatMap(CxTestDataContainer.CxTestData::getMaterialForRecycling), counter);
            checkAndIncrementCounter(testParameters.shouldCountProductDescription,
                    cxTestData.flatMap(CxTestDataContainer.CxTestData::getProductDescription), counter);
            checkAndIncrementCounter(testParameters.shouldCountPhysicalDimension,
                    cxTestData.flatMap(CxTestDataContainer.CxTestData::getPhysicalDimension), counter);
        } else if (relationshipAspect.equals(RelationshipAspect.SingleLevelBomAsPlanned)) {
            submodelData = cxTestData.flatMap(CxTestDataContainer.CxTestData::getSingleLevelBomAsPlanned);
            checkAndIncrementCounter(testParameters.shouldCountSingleLevelBomAsPlanned, submodelData, counter);

            checkAndIncrementCounter(testParameters.shouldCountPartAsPlanned,
                    cxTestData.flatMap(CxTestDataContainer.CxTestData::getPartAsPlanned), counter);
        }

        if (submodelData.isPresent()) {
            final RelationshipSubmodel relationshipSubmodel = objectMapper.convertValue(submodelData, relationshipAspect.getSubmodelClazz());
            relationshipSubmodel.asRelationships().forEach(relationship -> {
                final String childGlobalAssetId = relationship.getLinkedItem().getChildCatenaXId().getGlobalAssetId();
                final long expectedNumberOfChildSubmodels = countExpectedNumberOfSubmodelsFor(childGlobalAssetId, testParameters);
                counter.addAndGet(expectedNumberOfChildSubmodels);
            });
            return counter.get();
        }
        return 0;
    }

    private void checkAndIncrementCounter(final boolean shouldCountSubmodel, final Optional<Map<String, Object>> submodel, final AtomicLong counter) {
        if (shouldCountSubmodel && submodel.isPresent()) {
            counter.incrementAndGet();
        }
    }

    @Builder
    private static class TestParameters {
        final String globalAssetId;
        final BomLifecycle bomLifecycle;
        final Direction direction;
        final boolean shouldCountAssemblyPartRelationship;
        final boolean shouldCountSerialPartTypization;
        final boolean shouldCountBatch;
        final boolean shouldCountMaterialForRecycling;
        final boolean shouldCountProductDescription;
        final boolean shouldCountPhysicalDimension;
        final boolean shouldCountSingleLevelBomAsPlanned;
        final boolean shouldCountPartAsPlanned;
    }

}