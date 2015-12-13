package com.purbon.jruby.monitor;

import org.jruby.Ruby;
import org.jruby.RubyHash;

import java.util.Map;

/**
 * Created by purbon on 13/12/15.
 */
public class JRubyUtils {
    
    public static RubyHash toRubyHash(Ruby runtime, Map<String, Object> map) {
        RubyHash hash = new RubyHash(runtime);
        for(String key : map.keySet()) {
            hash.put(key, map.get(key));
        }
        return hash;
    }
}
