package com.datastax.driver.mapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.transport.DataType;

import com.datastax.driver.core.ConsistencyLevel;

/**
 * Holds the definition of an entity mapping between a Java class and a 
 * Cassandra table.
 */
class EntityDefinition<T> {

    final Class<T> entityClass;
    String tableName;
    String keyspaceName;

    ConsistencyLevel defaultReadCL;
    ConsistencyLevel defaultWriteCL;

    final List<ColumnDefinition> columns = new ArrayList<EntityDefinition.ColumnDefinition>();

    String inheritanceColumn;
    final List<SubEntityDefinition<T>> subEntities = new ArrayList<EntityDefinition.SubEntityDefinition<T>>();

    EntityDefinition(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    static class ColumnDefinition {
        String columnName;
        String fieldName;
        Method readMethod;
        Method writeMethod;
        Class<?> javaType;
        //DataType dbType;
    }

    static class SubEntityDefinition<T> {
        final EntityDefinition<T> parentEntity;
        final Class<? extends T> subEntityClass;
        String inheritanceColumnValue;
        final List<ColumnDefinition> columns = new ArrayList<EntityDefinition.ColumnDefinition>();

        SubEntityDefinition(EntityDefinition<T> parentEntity, Class<? extends T> subEntityClass) {
            this.parentEntity = parentEntity;
            this.subEntityClass = subEntityClass;
        }
    }

    static class EnumColumnDefinition extends ColumnDefinition {
        final Map<Object, Enum<?>> valueToEnum = new HashMap<Object, Enum<?>>();
        final Map<Enum<?>, Object> enumToValue = new HashMap<Enum<?>, Object>();
        boolean hasCustomValues;
    }

    static class CounterColumnDefinition extends ColumnDefinition {
        CounterMappingType mappingType;
    }
}
