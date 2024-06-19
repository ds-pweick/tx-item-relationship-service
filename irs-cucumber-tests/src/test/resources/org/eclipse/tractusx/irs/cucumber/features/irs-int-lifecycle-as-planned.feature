#
# Copyright (c) 2022,2024 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
# Copyright (c) 2021,2024 Contributors to the Eclipse Foundation
#
# See the NOTICE file(s) distributed with this work for additional
# information regarding copyright ownership.
#
# This program and the accompanying materials are made available under the
# terms of the Apache License, Version 2.0 which is available at
# https://www.apache.org/licenses/LICENSE-2.0.
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
# License for the specific language governing permissions and limitations
# under the License.
#
# SPDX-License-Identifier: Apache-2.0

@TRI-512
Feature: BomLifecycle 'asPlanned' implementation

  Background:
    Given the IRS URL "https://irs.int.demo.catena-x.net"
    And the admin user api key


  @INT @INTEGRATION_TEST
  @TRI-894 @TRI-1452 @TRI-874
  Scenario: End 2 End for BomLifecycle 'asPlanned' Vehicle Model C for SAP [BPNL00000003AZQP]
    Given I register an IRS job for globalAssetId "urn:uuid:65e1554e-e5cd-4560-bac1-1352582122fb" and BPN "BPNL00000003AZQP"
    And collectAspects "true"
    And depth 10
    And direction "downward"
    And bomLifecycle "asPlanned"
    And aspects :
      | urn:bamm:io.catenax.part_as_planned:1.0.1#PartAsPlanned |
    When I get the job-id
    Then I check, if the job has status "COMPLETED" within 20 minutes
    And I check, if "relationships" are equal to "TRI-894-expected-relationships.json"
    And I check, if "submodels" are equal to "TRI-894-expected-submodels.json"

  @INT @INTEGRATION_TEST
  @TRI-892 @TRI-1452 @TRI-874
  Scenario: End 2 End for BomLifecycle 'asPlanned' Vehicle Model B for MercedesBenz [BPNL00000003AVTH]
    Given I register an IRS job for globalAssetId "urn:uuid:68904173-ad59-4a77-8412-3e73fcafbd8b" and BPN "BPNL00000003AVTH"
    And collectAspects "true"
    And depth 10
    And direction "downward"
    And bomLifecycle "asPlanned"
    And aspects :
      | urn:bamm:io.catenax.part_as_planned:1.0.1#PartAsPlanned |
    When I get the job-id
    Then I check, if the job has status "COMPLETED" within 20 minutes
    And I check, if "relationships" are equal to "TRI-892-expected-relationships.json"
    And I check, if "submodels" are equal to "TRI-892-expected-submodels.json"

  @INT @INTEGRATION_TEST
  @TRI-872 @TRI-1452 @TRI-874
  Scenario: End 2 End for BomLifecycle 'asPlanned' Vehicle Model A for BMW [BPNL00000003AYRE]
    Given I register an IRS job for globalAssetId "urn:uuid:0733946c-59c6-41ae-9570-cb43a6e4c79e" and BPN "BPNL00000003AYRE"
    And collectAspects "true"
    And depth 10
    And direction "downward"
    And bomLifecycle "asPlanned"
    And aspects :
      | urn:bamm:io.catenax.part_as_planned:1.0.1#PartAsPlanned |
    When I get the job-id
    Then I check, if the job has status "COMPLETED" within 20 minutes
    And I check, if "relationships" are equal to "TRI-872-expected-relationships.json"
    And I check, if "submodels" are equal to "TRI-872-expected-submodels.json"