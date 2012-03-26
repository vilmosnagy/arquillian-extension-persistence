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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.arquillian.persistence.exception.PersistenceExtensionInitializationException;

public class PropertiesSerializer
{

   private final String prefix;

   public PropertiesSerializer()
   {
      this("");
   }

   public PropertiesSerializer(String prefix)
   {
      this.prefix = prefix;
   }

   public ByteArrayOutputStream serializeToProperties(final Map<String, String> properties)
   {
      final ByteArrayOutputStream output = new ByteArrayOutputStream();
      try
      {
         for (Entry<String, String> entry : properties.entrySet())
         {
            output.write(serializeAsProperty(entry).getBytes());
         }
      }
      catch (IOException e)
      {
         throw new PersistenceExtensionInitializationException("Unable to serialize dbunit properties", e);
      }
      return output;
   }

   private String serializeAsProperty(Entry<String, String> entry)
   {
      String serializedAsProperty;
      final StringBuilder sb = new StringBuilder();
      sb.append(prefix)
        .append(entry.getKey())
        .append("=")
        .append(entry.getValue())
        .append('\n');
      serializedAsProperty = sb.toString();
      return serializedAsProperty;
   }


}
