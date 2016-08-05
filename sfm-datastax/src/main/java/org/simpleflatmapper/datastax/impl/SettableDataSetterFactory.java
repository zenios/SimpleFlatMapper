package org.simpleflatmapper.datastax.impl;

import com.datastax.driver.core.*;
import org.simpleflatmapper.datastax.DataTypeHelper;
import org.simpleflatmapper.datastax.DatastaxColumnKey;

import org.simpleflatmapper.core.map.Mapper;
import org.simpleflatmapper.core.map.MapperConfig;
import org.simpleflatmapper.core.map.column.FieldMapperColumnDefinition;
import org.simpleflatmapper.core.map.mapper.ColumnDefinition;
import org.simpleflatmapper.core.map.mapper.PropertyMapping;
import org.simpleflatmapper.core.reflect.setter.ConvertDelegateSetter;
import org.simpleflatmapper.core.reflect.ReflectionService;
import org.simpleflatmapper.core.reflect.Setter;
import org.simpleflatmapper.core.reflect.SetterFactory;
import org.simpleflatmapper.converter.Converter;
import org.simpleflatmapper.converter.ConverterFactory;
import org.simpleflatmapper.datastax.impl.setter.BigDecimalSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.BigIntegerSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.ByteSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.ConverterToTupleValueMapper;
import org.simpleflatmapper.datastax.impl.setter.ConverterToUDTValueMapper;
import org.simpleflatmapper.datastax.impl.setter.DateSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.DoubleSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.FloatSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.InetAddressSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.IntSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.ListSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.ListWithConverterSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.LongSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.MapSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.MapWithConverterSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.OrdinalEnumSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.SetSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.SetWithConverterSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.ShortSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.StringEnumSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.StringSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.TimeSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.TimestampSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.TupleValueSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.UDTObjectSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.UDTValueSettableDataSetter;
import org.simpleflatmapper.datastax.impl.setter.UUIDSettableDataSetter;
import org.simpleflatmapper.util.TypeHelper;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;

import java.util.*;


public class SettableDataSetterFactory
        implements
        SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>

{
    private final Map<Class<?>, SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>> factoryPerClass =
            new HashMap<Class<?>, SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>>();
    private final MapperConfig<DatastaxColumnKey, FieldMapperColumnDefinition<DatastaxColumnKey>> mapperConfig;
    private final ReflectionService reflectionService;

    {
        factoryPerClass.put(short.class, new SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public <P> Setter<SettableByIndexData, P> getSetter(PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>> arg) {
                return (Setter<SettableByIndexData, P>) new ShortSettableDataSetter(arg.getColumnKey().getIndex());
            }
        });
        factoryPerClass.put(Short.class, factoryPerClass.get(short.class));

        factoryPerClass.put(byte.class, new SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public <P> Setter<SettableByIndexData, P> getSetter(PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>> arg) {
                return (Setter<SettableByIndexData, P>) new ByteSettableDataSetter(arg.getColumnKey().getIndex());
            }
        });
        factoryPerClass.put(Byte.class, factoryPerClass.get(byte.class));

        factoryPerClass.put(int.class, new SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public <P> Setter<SettableByIndexData, P> getSetter(PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>> arg) {
                return (Setter<SettableByIndexData, P>) new IntSettableDataSetter(arg.getColumnKey().getIndex());
            }
        });
        factoryPerClass.put(Integer.class, factoryPerClass.get(int.class));

        factoryPerClass.put(long.class, new SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public <P> Setter<SettableByIndexData, P> getSetter(PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>> arg) {
                if (DataTypeHelper.isTime(arg.getColumnKey().getDataType().getName())) {
                    return (Setter<SettableByIndexData, P>)new TimeSettableDataSetter(arg.getColumnKey().getIndex());
                }
                return (Setter<SettableByIndexData, P>) new LongSettableDataSetter(arg.getColumnKey().getIndex());
            }
        });
        factoryPerClass.put(Long.class, factoryPerClass.get(long.class));

        factoryPerClass.put(float.class, new SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public <P> Setter<SettableByIndexData, P> getSetter(PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>> arg) {
                return (Setter<SettableByIndexData, P>) new FloatSettableDataSetter(arg.getColumnKey().getIndex());
            }
        });
        factoryPerClass.put(Float.class, factoryPerClass.get(float.class));

        factoryPerClass.put(double.class, new SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public <P> Setter<SettableByIndexData, P> getSetter(PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>> arg) {
                return (Setter<SettableByIndexData, P>) new DoubleSettableDataSetter(arg.getColumnKey().getIndex());
            }
        });
        factoryPerClass.put(Double.class, factoryPerClass.get(double.class));


        factoryPerClass.put(String.class, new SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public <P> Setter<SettableByIndexData, P> getSetter(PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>> arg) {
                return (Setter<SettableByIndexData, P>) new StringSettableDataSetter(arg.getColumnKey().getIndex());
            }
        });

        factoryPerClass.put(Date.class, new SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public <P> Setter<SettableByIndexData, P> getSetter(PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>> arg) {
                return (Setter<SettableByIndexData, P>) new TimestampSettableDataSetter(arg.getColumnKey().getIndex());
            }
        });

        factoryPerClass.put(UUID.class, new SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public <P> Setter<SettableByIndexData, P> getSetter(PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>> arg) {
                return (Setter<SettableByIndexData, P>) new UUIDSettableDataSetter(arg.getColumnKey().getIndex());
            }
        });

        factoryPerClass.put(BigDecimal.class, new SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public <P> Setter<SettableByIndexData, P> getSetter(PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>> arg) {
                return (Setter<SettableByIndexData, P>) new BigDecimalSettableDataSetter(arg.getColumnKey().getIndex());
            }
        });

        factoryPerClass.put(BigInteger.class, new SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public <P> Setter<SettableByIndexData, P> getSetter(PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>> arg) {
                return (Setter<SettableByIndexData, P>) new BigIntegerSettableDataSetter(arg.getColumnKey().getIndex());
            }
        });

        factoryPerClass.put(InetAddress.class, new SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public <P> Setter<SettableByIndexData, P> getSetter(PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>> arg) {
                return (Setter<SettableByIndexData, P>) new InetAddressSettableDataSetter(arg.getColumnKey().getIndex());
            }
        });

        factoryPerClass.put(TupleValue.class, new SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>() {
            @SuppressWarnings("unchecked")
            @Override
            public <P> Setter<SettableByIndexData, P> getSetter(PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>> arg) {
                return (Setter<SettableByIndexData, P>) new TupleValueSettableDataSetter(arg.getColumnKey().getIndex());
            }
        });

        if (DataTypeHelper.localDateClass != null) {
            factoryPerClass.put(DataTypeHelper.localDateClass, new SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>>() {
                @SuppressWarnings("unchecked")
                @Override
                public <P> Setter<SettableByIndexData, P> getSetter(PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>> arg) {
                    return (Setter<SettableByIndexData, P>) new DateSettableDataSetter(arg.getColumnKey().getIndex());
                }
            });
        }
    }

    public SettableDataSetterFactory(MapperConfig<DatastaxColumnKey, FieldMapperColumnDefinition<DatastaxColumnKey>> mapperConfig, ReflectionService reflectionService) {
        this.mapperConfig = mapperConfig;
        this.reflectionService = reflectionService;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <P> Setter<SettableByIndexData, P> getSetter(PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>> arg) {
        Setter<SettableByIndexData, P> setter = null;

        Type propertyType = arg.getPropertyMeta().getPropertyType();

        final DataType dataType = arg.getColumnKey().getDataType();
        Type type = dataType != null ? DataTypeHelper.asJavaClass(dataType) : null;
        if (type == null) {
            type = propertyType;
        }

        if (TypeHelper.isEnum(propertyType)) {
            if (TypeHelper.isClass(type, String.class)) {
                return (Setter<SettableByIndexData, P>) new StringEnumSettableDataSetter(arg.getColumnKey().getIndex());
            } else {
                return (Setter<SettableByIndexData, P>) new OrdinalEnumSettableDataSetter(arg.getColumnKey().getIndex());
            }
        }

        SetterFactory<SettableByIndexData, PropertyMapping<?, ?, DatastaxColumnKey, ? extends ColumnDefinition<DatastaxColumnKey, ?>>> setterFactory =
                this.factoryPerClass.get(TypeHelper.toClass(type));

        if (setterFactory != null) {
            setter = setterFactory.getSetter(arg);

            if (!TypeHelper.areEquals(TypeHelper.toBoxedClass(type), TypeHelper.toBoxedClass(propertyType))) {
                Converter<?, ?> converter = getConverter(propertyType, TypeHelper.toClass(type), dataType, arg.getColumnDefinition());
                if (converter != null) {
                    setter = (Setter<SettableByIndexData, P>) new ConvertDelegateSetter<SettableByIndexData, Object, P>(setter, (Converter<Object, P>) converter);
                } else {
                    setter = null;
                }
            }
        }

        if (setter == null && dataType != null) {
            if (dataType instanceof UserType) {
                if (propertyType.equals(UDTValue.class)) {
                    setter = (Setter<SettableByIndexData, P>) new UDTValueSettableDataSetter(arg.getColumnKey().getIndex());
                } else {
                    setter = (Setter<SettableByIndexData, P>) UDTObjectSettableDataSetter.newInstance(propertyType, (UserType) dataType, arg.getColumnKey().getIndex(),  mapperConfig, reflectionService);
                }
            } else if (TypeHelper.isAssignable(List.class, type) && TypeHelper.isAssignable(List.class, propertyType)) {

                DataType dataTypeElt = dataType.getTypeArguments().get(0);
                Class<?> dEltType = DataTypeHelper.asJavaClass(dataTypeElt);
                Type lEltType = TypeHelper.getComponentTypeOfListOrArray(propertyType);
                if (TypeHelper.areEquals(lEltType, dEltType)) {
                    setter = new ListSettableDataSetter(arg.getColumnKey().getIndex());
                } else {
                    Converter<?, ?> converter = getConverter(lEltType, dEltType, dataTypeElt, arg.getColumnDefinition());
                    if (converter != null) {
                        setter = new ListWithConverterSettableDataSetter(arg.getColumnKey().getIndex(), converter);
                    }
                }
            } else if (TypeHelper.isAssignable(Set.class, type) && TypeHelper.isAssignable(Set.class, propertyType)) {

                DataType dataTypeElt = dataType.getTypeArguments().get(0);
                Class<?> dEltType = DataTypeHelper.asJavaClass(dataTypeElt);
                Type lEltType = TypeHelper.getComponentTypeOfListOrArray(propertyType);
                if (TypeHelper.areEquals(lEltType, dEltType)) {
                    setter = new SetSettableDataSetter(arg.getColumnKey().getIndex());
                } else {
                    Converter<?, ?> converter = getConverter(lEltType, dEltType, dataTypeElt, arg.getColumnDefinition());
                    if (converter != null) {
                        setter = new SetWithConverterSettableDataSetter(arg.getColumnKey().getIndex(), converter);
                    }
                }
            } else if (TypeHelper.isAssignable(Map.class, type) && TypeHelper.isAssignable(Map.class, propertyType)) {

                DataType dtKeyType = dataType.getTypeArguments().get(0);
                DataType dtValueType = dataType.getTypeArguments().get(1);

                TypeHelper.MapEntryTypes keyValueTypeOfMap = TypeHelper.getKeyValueTypeOfMap(propertyType);

                if (areSame(dtKeyType, keyValueTypeOfMap.getKeyType()) && areSame(dtValueType, keyValueTypeOfMap.getValueType())) {
                    setter = new MapSettableDataSetter(arg.getColumnKey().getIndex());
                } else {
                    setter = new MapWithConverterSettableDataSetter(arg.getColumnKey().getIndex(),
                            getConverter(keyValueTypeOfMap.getKeyType(), DataTypeHelper.asJavaClass(dtKeyType), dtKeyType, arg.getColumnDefinition()),
                            getConverter(keyValueTypeOfMap.getValueType(), DataTypeHelper.asJavaClass(dtValueType), dtValueType, arg.getColumnDefinition())
                            );
                }
            }

        }

        return setter;
    }

    private boolean areSame(DataType dtKeyType, Type element0) {
        return TypeHelper.areEquals(element0, DataTypeHelper.asJavaClass(dtKeyType));
    }

    @SuppressWarnings("unchecked")
    private Converter<?, ?> getConverter(Type elementType, Class<?> dataTypeElt, DataType dtElt, ColumnDefinition<DatastaxColumnKey, ?> columnDefinition) {
        if (dtElt != null) {
            if (UDTValue.class.equals(dataTypeElt)) {
                Mapper mapper = UDTObjectSettableDataSetter.newUDTMapper(elementType, (UserType) dtElt, mapperConfig, reflectionService);
                return new ConverterToUDTValueMapper(mapper, (UserType) dtElt);
            }
            if (TupleValue.class.equals(dataTypeElt)) {
                Mapper mapper = TupleValueSettableDataSetter.newTupleMapper(elementType, (TupleType) dtElt, mapperConfig, reflectionService);
                return new ConverterToTupleValueMapper(mapper, (TupleType) dtElt);
            }
        }
        return ConverterFactory.getConverter(TypeHelper.toClass(elementType), dataTypeElt, columnDefinition.properties());
    }
}
