package com.purbon.jrmonitor;

import org.jruby.Ruby;
import org.jruby.RubyHash;
import org.jruby.runtime.builtin.IRubyObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by purbon on 13/12/15.
 */
public class JRubyUtils {
    
    public static RubyHash toRubyHash(Ruby runtime, Map<String, Object> map) {
        RubyHash hash = new RubyHash(runtime);
        for (String key : map.keySet()) {
            hash.put(key, map.get(key));
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
}
