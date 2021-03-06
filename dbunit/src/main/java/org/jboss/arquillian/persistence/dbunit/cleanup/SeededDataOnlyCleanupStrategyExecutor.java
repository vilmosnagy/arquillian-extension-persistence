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
package org.jboss.arquillian.persistence.dbunit.cleanup;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.operation.DatabaseOperation;
import org.jboss.arquillian.persistence.dbunit.DataSetUtils;
import org.jboss.arquillian.persistence.dbunit.configuration.DBUnitConfiguration;
import org.jboss.arquillian.persistence.dbunit.dataset.DataSetRegister;
import org.jboss.arquillian.persistence.dbunit.exception.DBUnitDataSetHandlingException;
import org.jboss.arquillian.persistence.dbunit.filter.TableFilterResolver;
import org.jboss.arquillian.persistence.spi.dbunit.filter.TableFilterProvider;

import java.util.logging.Logger;

public class SeededDataOnlyCleanupStrategyExecutor implements CleanupStrategyExecutor {

    private static final Logger LOG = Logger.getLogger(SeededDataOnlyCleanupStrategyExecutor.class.getName());

    private final DatabaseConnection connection;

    private final DataSetRegister dataSetRegister;

    private final DBUnitConfiguration dbUnitConfiguration;

    public SeededDataOnlyCleanupStrategyExecutor(DatabaseConnection connection, DataSetRegister dataSetRegister, DBUnitConfiguration dbUnitConfiguration) {
        this.connection = connection;
        this.dataSetRegister = dataSetRegister;
        this.dbUnitConfiguration = dbUnitConfiguration;
    }

    @Override
    public void cleanupDatabase(String... tablesToExclude) {
        if (dataSetRegister.getInitial() == null || dataSetRegister.getInitial().isEmpty()) {
            LOG.warning("Attempted to cleanup database using USED_ROWS strategy, but no data sets were used.");
            return;
        }
        try {
            final IDataSet mergeDataSets = DataSetUtils.mergeDataSets(dataSetRegister.getInitial());
            IDataSet dataSet = DataSetUtils.excludeTables(mergeDataSets, tablesToExclude);
            if (dbUnitConfiguration.isFilterTables()) {
                final TableFilterProvider sequenceFilterProvider = new TableFilterResolver(dbUnitConfiguration).resolve();
                final ITableFilter tableFilter = sequenceFilterProvider.provide(connection, dataSet.getTableNames());
                dataSet = new FilteredDataSet(tableFilter, dataSet);
            }
            DatabaseOperation.DELETE.execute(connection, dataSet);
        } catch (Exception e) {
            throw new DBUnitDataSetHandlingException("Unable to clean database.", e);
        }
    }

}
