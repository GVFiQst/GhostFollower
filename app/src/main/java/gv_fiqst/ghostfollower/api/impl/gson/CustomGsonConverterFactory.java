package gv_fiqst.ghostfollower.api.impl.gson;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;


public class CustomGsonConverterFactory extends Converter.Factory implements Builder {
    private final List<Type> mAllowedTypes = new ArrayList<>();
    private Gson mGson;

    public static Builder builder() {
        return new CustomGsonConverterFactory();
    }

    private CustomGsonConverterFactory() {
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (!mAllowedTypes.contains(type)) {
            return null;
        }

        return new ResponseConverter<>(mGson, mGson.getAdapter(TypeToken.get(type)));
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        if (!mAllowedTypes.contains(type)) {
            return null;
        }

        return new RequestConverter<>(mGson, mGson.getAdapter(TypeToken.get(type)));
    }

    @Override
    public Builder allow(Type type) {
        mAllowedTypes.add(type);

        return this;
    }

    @Override
    public Builder withGson(Gson gson) {
        mGson = gson;
        return this;
    }

    @Override
    public CustomGsonConverterFactory build() {
        if (mGson == null) {
            return withGson(new Gson())
                    .build();
        }

        return this;
    }
}
