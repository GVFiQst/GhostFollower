package gv_fiqst.ghostfollower.internal.colorreveal;


import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.XmlRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import gv_fiqst.ghostfollower.R;
import gv_fiqst.ghostfollower.util.MyCompat;


public class PagerRevealUtil extends CircularColorRevealUtil implements ViewPager.OnPageChangeListener {
    private ViewPager mPager;
    private TabLayout mTabs;

    private Point[] mPoints;
    private Point mLastPoint;
    private int mCurrentPage = 0;

    private List<Colorable> mColorables;

    public PagerRevealUtil(AppBarLayout target, ViewPager pager, TabLayout tabs) {
        super(target);

        mPager = pager;
        mTabs = tabs;
    }

    private List<Colorable> getColorablesRecursive(ViewGroup target) {
        List<Colorable> list = new ArrayList<>();

        for (int i = 0; i < target.getChildCount(); i++) {
            View view = target.getChildAt(i);

            if (view instanceof ViewGroup) {
                list.addAll(getColorablesRecursive((ViewGroup) view));
            }

            if (view instanceof Toolbar) {
                list.add(new Colorable.ColorableToolbar((Toolbar) view));
            }

            if (view instanceof TextView) {
                list.add(new Colorable.ColorableTextView((TextView) view));
            }

            if (view.getClass().getSimpleName().equals("SlidingTabStrip")) {
                list.add(new Colorable.ColorableSlidingTabStrip(view));
            }
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public void setupPager(Context ctx, FragmentManager fm, @XmlRes int xmlRes) {
        List<TabInfo> tabs = new ArrayList<>();
        XmlResourceParser xmlParser = null;
        Exception ex = null;
        try {
            xmlParser = ctx.getResources().getXml(xmlRes);

            int type = xmlParser.getEventType();
            boolean tab = false;
            TabInfo info = null;
            while (type != XmlPullParser.END_DOCUMENT) {
                if (type == XmlPullParser.START_TAG) {
                    String name = xmlParser.getName();

                    switch (name) {
                        case "tabs":
                            tab = true;
                            break;
                        case "info":
                            if (!tab) {
                                throw new XmlPullParserException("Xml must starts with tabs tag!", xmlParser, null);
                            }

                            String text = xmlParser.getAttributeValue(null, "text");
                            String clazz = xmlParser.getAttributeValue(null, "class");
                            int color = Color.parseColor(xmlParser.getAttributeValue(null, "color"));
                            int textColor = Color.parseColor(xmlParser.getAttributeValue(null, "textColor"));

                            tabs.add(info = new TabInfo(
                                    color, textColor, text,
                                    (Class<? extends Fragment>) Class.forName(clazz)
                            ));
                            break;
                        case "arg":
                            if (info != null) {
                                String argName = xmlParser.getAttributeValue(null, "name");
                                String argValue = xmlParser.getAttributeValue(null, "value");
                                String argType = xmlParser.getAttributeValue(null, "type");

                                info.addArgument(argName, argValue, argType);
                            }
                            break;
                    }
                }

                type = xmlParser.next();
            }

            setupPager(ctx, fm, tabs.toArray(new TabInfo[0]));
        } catch (Exception e) {
            ex = e;
        } finally {
            if (xmlParser != null) {
                xmlParser.close();
            }
        }

        if (ex != null) {
            throw new RuntimeException(ex);
        }
    }

    public void setupPager(Context ctx, FragmentManager fm, final TabInfo... infoArr) {
        mPager.setAdapter(new FragmentAdapterImpl(fm, infoArr));
        mPager.addOnPageChangeListener(this);
        mTabs.setupWithViewPager(mPager);

        for (int i = 0; i < mTabs.getTabCount(); i++) {
            TabLayout.Tab tab = mTabs.getTabAt(i);

            tab.setCustomView(new CustomTab(ctx, infoArr[i]));
            tab.getCustomView().setOnTouchListener(this);
        }

        mPoints = new Point[infoArr.length];
        mTabs.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            @SuppressWarnings("ConstantConditions")
            public void onGlobalLayout() {
                MyCompat.get().removeOnGlobalLayoutListener(
                        mTabs.getViewTreeObserver(), this
                );

                for (int i = 0; i < mTabs.getTabCount(); i++) {
                    View view = mTabs.getTabAt(i).getCustomView();

                    int[] xy = new int[2];
                    view.getLocationOnScreen(xy);

                    mPoints[i] = new Point(
                            xy[0] + view.getWidth() / 2,
                            xy[1] + view.getHeight() / 2
                    );
                }
            }
        });

        mColorables = getColorablesRecursive((ViewGroup) mTarget);
        setMainColor(infoArr[0].color);
        for (Colorable colorable : mColorables) {
            colorable.setColor(infoArr[0].textColor);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v instanceof CustomTab) {
            int[] xy = new int[2];
            mTarget.getLocationOnScreen(xy);
            mLastPoint = new Point(
                    (int) (event.getRawX() - xy[0]), (int) (event.getRawY() - xy[1])
            );
        }

        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(final int position) {
        Point last = mLastPoint;
        mLastPoint = null;

        if (mCurrentPage != position) {
            int oldPosition = mCurrentPage;
            mCurrentPage = position;

            if (last == null) {
                last = mPoints[position];
            }

            //noinspection ConstantConditions
            final TabInfo info = ((CustomTab) mTabs.getTabAt(position).getCustomView()).getTabInfo();
            //noinspection ConstantConditions
            final int color = ((CustomTab) mTabs.getTabAt(oldPosition).getCustomView()).getTabInfo().textColor;

            if (last != null) {
                startReveal(info.color, last.x, last.y, new ColorRevealDrawable.AnimationListener() {
                    private final ArgbEvaluator mEvaluator = new ArgbEvaluator();
                    private final int fromColor = color;
                    private final int toColor = info.textColor;

                    @Override
                    public void onUpdateAnimation(float progress) {
                        if (fromColor == toColor) {
                            return;
                        }

                        for (Colorable colorable : mColorables) {
                            colorable.setColor((Integer) mEvaluator.evaluate(
                                    progress, fromColor, toColor
                            ));
                        }
                    }
                });
            } else {
                setMainColor(info.color);
                for (Colorable colorable : mColorables) {
                    colorable.setColor(info.textColor);
                }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void release() {
        mTabs = null;
        mPager = null;

        for (Colorable colorable : mColorables) {
            colorable.release();
        }
        mColorables.clear();
        mColorables = null;
    }

    public static class TabInfo {
        public int color;
        public int textColor;
        public String text;
        public Class<? extends Fragment> clazz;
        public List<TabArgument> args = new ArrayList<>();

        public TabInfo(int color, int textColor, String text, Class<? extends Fragment> clazz) {
            this.color = color;
            this.textColor = textColor;
            this.text = text;
            this.clazz = clazz;
        }

        public void addArgument(String name, String value, String type) {
            args.add(new TabArgument(name, value, type));
        }
    }

    private static class TabArgument {
        public String mKey;
        public String mVal;
        public String mType;

        public TabArgument(String key, String val, String type) {
            this.mKey = key;
            this.mVal = val;
            this.mType = type;
        }
    }

    private static class CustomTab extends FrameLayout {
        private TabInfo mTabInfo;

        public CustomTab(Context context, TabInfo info) {
            super(context);

            mTabInfo = info;
            LayoutInflater.from(context)
                    .inflate(R.layout.tab, this, true);

            TextView textView = findViewById(R.id.txt);
            textView.setText(info.text);
        }

        public TabInfo getTabInfo() {
            return mTabInfo;
        }

    }

    private static class FragmentAdapterImpl extends FragmentPagerAdapter {
        private final TabInfo[] mInfo;

        public FragmentAdapterImpl(FragmentManager fm, TabInfo... infoArr) {
            super(fm);
            mInfo = infoArr;
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mInfo[position];

            try {
                Fragment fragment = info.clazz.newInstance();
                Bundle data = new Bundle();

                for (Iterator<TabArgument> iterator = info.args.iterator();
                     iterator.hasNext(); ) {
                    TabArgument arg = iterator.next();
                    if (arg != null) {
                        switch (arg.mType.toLowerCase()) {
                            default:
                                throw new XmlPullParserException("unknown type " + arg.mType);

                            case "string":
                                data.putString(arg.mKey, arg.mVal);
                                break;

                            case "color":
                                data.putInt(arg.mKey, Color.parseColor(arg.mVal));
                                break;

                            case "int":
                                data.putInt(arg.mKey, Integer.parseInt(arg.mVal));
                                break;
                        }
                    }

                    iterator.remove();
                }

                fragment.setArguments(data);
                return fragment;
            } catch (Exception e) {
                Log.e("lox", "Error instantiating fragment ", e);
                return null;
            }
        }

        @Override
        public int getCount() {
            return mInfo.length;
        }
    }
}
