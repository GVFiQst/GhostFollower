package gv_fiqst.ghostfollower.api.impl.gson;

import com.google.gson.Gson;

import java.lang.reflect.Type;


public interface Builder {
    Builder allow(Type type);
    Builder withGson(Gson gson);
    CustomGsonConverterFactory build();
}
