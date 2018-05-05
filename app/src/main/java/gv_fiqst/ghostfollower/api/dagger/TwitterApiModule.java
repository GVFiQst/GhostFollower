package gv_fiqst.ghostfollower.api.dagger;

import android.util.Log;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import gv_fiqst.ghostfollower.api.impl.TwitterApiManager;
import gv_fiqst.ghostfollower.api.impl.jsoup.JsoupConverterFactory;
import gv_fiqst.ghostfollower.api.impl.jsoup.PageParser;
import gv_fiqst.ghostfollower.api.impl.service.TwitterService;
import gv_fiqst.ghostfollower.api.model.twitter.TwitterPost;
import gv_fiqst.ghostfollower.api.model.twitter.TwitterPostLink;
import gv_fiqst.ghostfollower.api.model.twitter.TwitterUser;
import gv_fiqst.ghostfollower.domain.repo.DbChooser;
import gv_fiqst.ghostfollower.util.Applier;
import gv_fiqst.ghostfollower.util.TypeToken;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
public class TwitterApiModule {
    private static final String TWITTER_BASE_URL = "https://twitter.com/";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm a dd MMM yyyy", Locale.ENGLISH);

    @Provides
    @Singleton
    @Named("tweet")
    JsoupConverterFactory provideJsoupConverterFactory() {
        return JsoupConverterFactory.builder()
                .registerParser(new TypeToken<List<TwitterUser>>(){}.getType(),
                        (element, parsers) -> {
                            Elements elements = element.getElementsByClass("ProfileCard-content");

                            List<TwitterUser> list = new ArrayList<>();
                            PageParser<TwitterUser> userParser = parsers.getForClass(TwitterUser.class);
                            for (Element e : elements) {
                                list.add(userParser.parse(e, parsers));
                            }

                            return list;
                        })
                .registerParser(TwitterUser.class, (e, parsers) -> new TwitterUser(
                        e.getElementsByClass("username").get(0).text(),
                        e.getElementsByClass("fullname").get(0).text(),
                        e.getElementsByClass("ProfileCard-avatarImage").get(0).attr("src")
                ))
                .registerParser(new TypeToken<List<TwitterPostLink>>(){}.getType(),
                        (element, parsers) -> Applier.transformList(
                                element.getElementsByClass("timeline").get(0).getElementsByClass("tweet"),
                                e -> new TwitterPostLink(e.attr("href"))
                        )
                ).registerParser(TwitterPost.class, (element, parsers) -> Applier.transform(
                        element.getElementsByClass("permalink-tweet-container").get(0),
                        element.getElementsByClass("permalink-tweet-container").get(0)
                                .getElementsByClass("content").get(0),
                        (body, sender) -> new TwitterPost(
                                sender.getElementsByClass("fullname").text(),
                                sender.getElementsByClass("username").get(0).text(),
                                sender.getElementsByClass("avatar").attr("src"),
                                body.getElementsByClass("tweet-text").get(0).text(),
                                Applier.transform(
                                        body.getElementsByClass("AdaptiveMedia-photoContainer"),
                                        attach -> attach.size() > 0 ? attach.attr("data-image-url") : ""
                                ),
                                Applier.transform(
                                        body.getElementsByClass("metadata").text(),
                                        date -> {
                                            if (!date.isEmpty()) {
                                                String[] arr = date.split("- ");
                                                if (arr.length > 1) {
                                                    date = arr[0] + arr[1];
                                                }

                                                try {
                                                    return DATE_FORMAT
                                                            .parse(date)
                                                            .getTime();
                                                } catch (ParseException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }

                                            return 0L;
                                        }, t -> Log.e("lox", "Error getting timestamp: ", t)
                                )
                        )
                ))
                .build();
    }

    @Provides
    @Singleton
    @Named("tweet")
    Retrofit provideTwitterRetrofit(@Named("tweet") JsoupConverterFactory jcf) {
        return new Retrofit.Builder()
                .baseUrl(TWITTER_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(jcf)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    TwitterApiManager provideTwitterApiManager(TwitterService service, DbChooser chooser) {
        return new TwitterApiManager(service, chooser);
    }
}
