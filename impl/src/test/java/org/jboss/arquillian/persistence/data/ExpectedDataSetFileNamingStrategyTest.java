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
package org.jboss.arquillian.persistence.data;

import static org.fest.assertions.Assertions.*;

import org.jboss.arquillian.persistence.data.descriptor.Format;
import org.jboss.arquillian.persistence.data.naming.ExpectedDataSetFileNamingStrategy;
import org.junit.Test;

public class ExpectedDataSetFileNamingStrategyTest
{

   @Test
   public void should_produce_default_file_name_of_expected_data_set_for_test_using_full_class_name_and_method_name() throws Exception
   {
      // given
      ExpectedDataSetFileNamingStrategy defaultFileNamingStrategy = new ExpectedDataSetFileNamingStrategy(Format.XML);

      // when
      String fileName = defaultFileNamingStrategy.createFileName(DummyClass.class, DummyClass.class.getMethod("shouldPass"));

      // then
      assertThat(fileName).isEqualTo("expected-org.jboss.arquillian.persistence.data.DummyClass#shouldPass.xml");
   }

   @Test
   public void should_produce_proper_file_extension_based_on_format() throws Exception
   {
      // given
      ExpectedDataSetFileNamingStrategy defaultFileNamingStrategy = new ExpectedDataSetFileNamingStrategy(Format.XML);

      // when
      String fileExtension = defaultFileNamingStrategy.getFileExtension();

      // then
      assertThat(fileExtension).isEqualTo("xml");
   }

}
