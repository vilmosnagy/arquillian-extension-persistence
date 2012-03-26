/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.persistence.configuration;

import static org.fest.assertions.Assertions.assertThat;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.persistence.TransactionMode;
import org.jboss.arquillian.persistence.configuration.PersistenceConfiguration;
import org.jboss.arquillian.persistence.data.descriptor.Format;
import org.junit.Test;

public class ConfigurationImporterFromXmlTest
{

   @Test
   public void should_extract_default_data_source_from_external_configuration_file() throws Exception
   {
      // given
      String expectedDataSource = "Ike";
      ArquillianDescriptor descriptor = TestConfigurationLoader.createArquillianDescriptorFromDefaultConfigurationFile();
      PersistenceConfiguration configuration = new PersistenceConfiguration();

      // when
      Configuration.importTo(configuration).loadFrom(descriptor);

      // then
      assertThat(configuration.getDefaultDataSource()).isEqualTo(expectedDataSource);
   }

   @Test
   public void should_extract_init_statement_from_external_configuration_file() throws Exception
   {
      // given
      String expectedInitStatement = "SELECT * FROM ARQUILLIAN_TESTS";
      ArquillianDescriptor descriptor = TestConfigurationLoader.createArquillianDescriptorFromDefaultConfigurationFile();
      PersistenceConfiguration configuration = new PersistenceConfiguration();

      // when
      Configuration.importTo(configuration).loadFrom(descriptor);

      // then
      assertThat(configuration.getInitStatement()).isEqualTo(expectedInitStatement);
   }

   @Test
   public void should_extract_default_data_set_format_defined_in_property_file() throws Exception
   {
      // given
      Format expectedFormat = Format.EXCEL;
      ArquillianDescriptor descriptor = TestConfigurationLoader.createArquillianDescriptorFromDefaultConfigurationFile();
      PersistenceConfiguration configuration = new PersistenceConfiguration();

      // when
      Configuration.importTo(configuration).loadFrom(descriptor);

      // then
      assertThat(configuration.getDefaultDataSetFormat()).isEqualTo(expectedFormat);
   }

   @Test
   public void should_use_xml_as_default_data_set_format_when_not_defined_in_configuration() throws Exception
   {
      // given
      Format expectedFormat = Format.XML;
      ArquillianDescriptor descriptor = TestConfigurationLoader.createArquillianDescriptor("arquillian-without-persistence-properties.xml");
      PersistenceConfiguration configuration = new PersistenceConfiguration();

      // when
      Configuration.importTo(configuration).loadFrom(descriptor);

      // then
      assertThat(configuration.getDefaultDataSetFormat()).isEqualTo(expectedFormat);
   }

   @Test
   public void should_obtain_default_transaction_mode() throws Exception
   {
      // given
      TransactionMode expectedMode = TransactionMode.ROLLBACK;
      ArquillianDescriptor descriptor = TestConfigurationLoader.createArquillianDescriptor("arquillian.xml");
      PersistenceConfiguration configuration = new PersistenceConfiguration();

      // when
      Configuration.importTo(configuration).loadFrom(descriptor);

      // then
      assertThat(configuration.getDefaultTransactionMode()).isEqualTo(expectedMode);
   }

   @Test
   public void should_have_commit_as_default_transaction_mode_if_not_defined_in_configuration_file() throws Exception
   {
      // given
      TransactionMode expectedMode = TransactionMode.COMMIT;
      ArquillianDescriptor descriptor = TestConfigurationLoader.createArquillianDescriptor("arquillian-without-persistence-properties.xml");
      PersistenceConfiguration configuration = new PersistenceConfiguration();

      // when
      Configuration.importTo(configuration).loadFrom(descriptor);

      // then
      assertThat(configuration.getDefaultTransactionMode()).isEqualTo(expectedMode);
   }

   @Test
   public void should_be_able_to_turn_on_database_dumps() throws Exception
   {
      // given
      ArquillianDescriptor descriptor = TestConfigurationLoader.createArquillianDescriptor("arquillian.xml");
      PersistenceConfiguration configuration = new PersistenceConfiguration();

      // when
      Configuration.importTo(configuration).loadFrom(descriptor);

      // then
      assertThat(configuration.isDumpData()).isTrue();
   }

   @Test
   public void should_have_database_dumps_disabled_by_default() throws Exception
   {
      // given
      ArquillianDescriptor descriptor = TestConfigurationLoader.createArquillianDescriptor("arquillian-without-persistence-properties.xml");
      PersistenceConfiguration configuration = new PersistenceConfiguration();

      // when
      Configuration.importTo(configuration).loadFrom(descriptor);

      // then
      assertThat(configuration.isDumpData()).isFalse();
   }

   public void should_have_system_temp_dir_defined_as_default_dump_directory() throws Exception
   {
      // given
      String systemTmpDir = System.getProperty("java.io.tmpdir");
      ArquillianDescriptor descriptor = TestConfigurationLoader.createArquillianDescriptor("arquillian-without-persistence-properties.xml");
      PersistenceConfiguration configuration = new PersistenceConfiguration();

      // when
      Configuration.importTo(configuration).loadFrom(descriptor);

      // then
      assertThat(configuration.getDumpDirectory()).isEqualTo(systemTmpDir);
   }

   @Test
   public void should_be_able_to_define_dump_directory() throws Exception
   {
      // given
      String dumpDirectory = "/home/ike/dump";
      ArquillianDescriptor descriptor = TestConfigurationLoader.createArquillianDescriptor("arquillian.xml");
      PersistenceConfiguration configuration = new PersistenceConfiguration();

      // when
      Configuration.importTo(configuration).loadFrom(descriptor);

      // then
      assertThat(configuration.getDumpDirectory()).isEqualTo(dumpDirectory);
   }

   @Test
   public void should_be_able_to_define_user_transaction_jndi() throws Exception
   {
      // given
      String expectedUserTransactionJndi = "java:jboss/UserTransaction";
      ArquillianDescriptor descriptor = TestConfigurationLoader.createArquillianDescriptor("arquillian.xml");
      PersistenceConfiguration configuration = new PersistenceConfiguration();

      // when
      Configuration.importTo(configuration).loadFrom(descriptor);

      // then
      assertThat(configuration.getUserTransactionJndi()).isEqualTo(expectedUserTransactionJndi);
   }

   @Test
   public void should_have_default_user_transaction_jndi() throws Exception
   {
      // given
      String expectedUserTransactionJndi = "java:comp/UserTransaction";
      ArquillianDescriptor descriptor = TestConfigurationLoader.createArquillianDescriptor("arquillian-without-persistence-properties.xml");
      PersistenceConfiguration configuration = new PersistenceConfiguration();

      // when
      Configuration.importTo(configuration).loadFrom(descriptor);

      // then
      assertThat(configuration.getUserTransactionJndi()).isEqualTo(expectedUserTransactionJndi);
   }
}
