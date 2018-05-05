package gv_fiqst.ghostfollower.api.impl.jsoup;


import android.support.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class JsoupConverterFactory extends Converter.Factory {
    private Parsers mParsers;

    private JsoupConverterFactory(BuilderImpl builder) {
        mParsers = new Parsers(builder.mParsers);
    }

    public static Builder builder() {
        return new BuilderImpl();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        final PageParser parser = mParsers.getForType(type);
        if (parser == null) {
            return null;
        }

        final Converter<ResponseBody, String> delegate
                = retrofit.nextResponseBodyConverter(this, String.class, annotations);

        return value -> {
            String html = delegate.convert(value);
            Document document = Jsoup.parse(html);

            return parser.parse(document, mParsers);
        };
    }


    public interface Builder {
        <T> Builder registerParser(Class<T> cls, PageParser<T> pageParser);
        <T> Builder registerParser(Type type, PageParser<T> pageParser);

        JsoupConverterFactory build();
    }

    private static class BuilderImpl implements Builder {
        private final Map<Type, PageParser> mParsers = new LinkedHashMap<>();

        @Override
        public <T> Builder registerParser(Class<T> cls, PageParser<T> pageParser) {
            return registerParser((Type) cls, pageParser);
        }

        @Override
        public <T> Builder registerParser(Type type, PageParser<T> pageParser) {
            mParsers.put(type, pageParser);
            return this;
        }

        @Override
        public JsoupConverterFactory build() {
            return new JsoupConverterFactory(this);
        }
    }
}
