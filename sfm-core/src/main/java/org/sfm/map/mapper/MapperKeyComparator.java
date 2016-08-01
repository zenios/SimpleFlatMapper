package org.sfm.map.mapper;

import org.sfm.jdbc.JdbcColumnKey;
import org.sfm.map.FieldKey;

import java.util.Comparator;

public abstract class MapperKeyComparator<K extends FieldKey<K>>  implements Comparator<MapperKey<K>> {

    public static final MapperKeyComparator<JdbcColumnKey> jdbcColumnKeyComparator() {
        return new JdbcColumnKeyMapperKeyComparator();
    }



    private static final class JdbcColumnKeyMapperKeyComparator extends MapperKeyComparator<JdbcColumnKey> {
        @Override
        public int compare(MapperKey<JdbcColumnKey> m1, MapperKey<JdbcColumnKey> m2) {
            JdbcColumnKey[] keys1 = m1.getColumns();
            JdbcColumnKey[] keys2 = m2.getColumns();
            int d = keys1.length - keys2.length;
            if (d != 0){
                return d;
            }
            return compareKeys(keys1, keys2);
        }

        private int compareKeys(JdbcColumnKey[] keys1, JdbcColumnKey[] keys2) {
            for(int i = 0; i < keys1.length; i++) {
                int d = compare(keys1[i], keys2[i]);
                if (d!= 0) {
                    return d;
                }
            }
            return 0;
        }

        protected int compare(JdbcColumnKey k1, JdbcColumnKey k2) {
            int d = k1.getIndex() - k2.getIndex();
            if (d != 0) return d;
            d = k1.getName().compareTo(k2.getName());
            if (d != 0) return d;
            return k1.getSqlType() - k2.getSqlType();
        }
    }
}