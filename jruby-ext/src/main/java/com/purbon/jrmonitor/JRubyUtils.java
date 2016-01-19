package com.purbon.jrmonitor;

import org.jruby.Ruby;
import org.jruby.RubyArray;
import org.jruby.RubyHash;
import org.jruby.RubyNumeric;
import org.jruby.runtime.builtin.IRubyObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Set of methods used to help during the jruby methods tranformation.
 * Created by purbon on 13/12/15.
 */
public class JRubyUtils {
    
    public static RubyHash toRubyHash(Ruby runtime, Map<String, Object> map) {
        RubyHash hash = new RubyHash(runtime);
        for (String key : map.keySet()) {
            hash.put(key, transform(runtime, map.get(key)));
        }
        return hash;
    }

    public static Map<String, String> parseOptions(Ruby runtime, IRubyObject params) {
        Map<String, String> options = new HashMap<String, String>();
        RubyHash options_hash = params.convertToHash();
        for(Object key : options_hash.keySet()) {
            options.put(String.valueOf(key), String.valueOf(options_hash.get(key)));
        }
        return options;
    }

    private static Object transform(Ruby runtime, Object object) {
        try {
            if (object instanceof ArrayList<?>) {
                RubyArray array = RubyArray.newArray(runtime);
                List values = (List)object;
                for (Object value : values) {
                    if (value instanceof String) {
                        array.append(runtime.newString(String.valueOf(value)));
                    } else  {
                       array.add(value);
                    }
                }
                return array;
            }
            return object;
        }
        catch (Exception ex) {
            return object;
        }
    }
}
