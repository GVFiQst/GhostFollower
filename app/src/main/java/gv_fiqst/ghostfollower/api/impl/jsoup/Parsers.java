package gv_fiqst.ghostfollower.api.impl.jsoup;


import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Parsers {
    private Map<Type, PageParser> mParsers;

    public Parsers(Map<Type, PageParser> parsers) {
        mParsers = parsers == null ? new LinkedHashMap<>() : parsers;
    }

    public <T> PageParser<T> getForClass(Class<T> cls) {
        return mParsers.get(cls);
    }

    public PageParser getForType(Type type) {
        return mParsers.get(type);
    }
}
