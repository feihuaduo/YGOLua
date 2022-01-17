package com.myopicmobile.textwarrior.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.ListPopupWindow;

import com.feihua.dialogutils.util.DialogUtils;
import com.myopicmobile.textwarrior.bean.LuaConstant;
import com.myopicmobile.textwarrior.bean.LuaFunction;
import com.myopicmobile.textwarrior.bean.LuaMessage;
import com.myopicmobile.textwarrior.common.Flag;
import com.myopicmobile.textwarrior.common.Language;
import com.myopicmobile.textwarrior.common.LanguageNonProg;
import com.myopicmobile.textwarrior.util.LuaUtil;
import com.ourygo.ygolua.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 自动提示类
 */
public class AutoCompletePanel {

    private static Language _globalLanguage = LanguageNonProg.getInstance();
    private final int PADDING = 20;
    private final boolean DEBUG = false;
    private FreeScrollingTextField _textField;
    private Context _context;
    private ListPopupWindow _autoCompletePanel;
    private AutoPanelAdapter _adapter;
    private Filter _filter;
    private int _verticalOffset;
    private int _height;
    private int _horizontal;
    private CharSequence _constraint;
    private int _backgroundColor;
    private GradientDrawable gd;
    private int _textColor;
    private boolean isShow = false;
    private DialogUtils dialogUtils;

    public AutoCompletePanel(FreeScrollingTextField textField) {
        _textField = textField;
        _context = textField.getContext();
        initAutoCompletePanel();
        dialogUtils = DialogUtils.getInstance(_context);
    }

    synchronized public static Language getLanguage() {
        return _globalLanguage;
    }

    synchronized public static void setLanguage(Language lang) {
        _globalLanguage = lang;
    }

    public void setTextColor(int color) {
        _textColor = color;
        gd.setStroke(1, color);
        _autoCompletePanel.setBackgroundDrawable(gd);
    }

    public void setBackgroundColor(int color) {
        _backgroundColor = color;
        gd.setColor(color);
        _autoCompletePanel.setBackgroundDrawable(gd);
    }

    public void setBackground(Drawable color) {
        _autoCompletePanel.setBackgroundDrawable(color);
    }

    @SuppressWarnings("ResourceType")
    private void initAutoCompletePanel() {
        _autoCompletePanel = new ListPopupWindow(_context);
        _autoCompletePanel.setAnchorView(_textField);
        _adapter = new AutoPanelAdapter(_context);
        _autoCompletePanel.setAdapter(_adapter);
        _filter = _adapter.getFilter();
        _autoCompletePanel.setContentWidth(ListPopupWindow.WRAP_CONTENT);
        //setHeight(300);

        TypedArray array = _context.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.colorBackground,
                android.R.attr.textColorPrimary,
        });
        int backgroundColor = array.getColor(0, 0xFF00FF);
        int textColor = array.getColor(1, 0xFF00FF);
        array.recycle();
        gd = new GradientDrawable();
        gd.setColor(backgroundColor);
        gd.setCornerRadius(4);
        gd.setStroke(1, textColor);
        setTextColor(textColor);
        _autoCompletePanel.setBackgroundDrawable(gd);
        _autoCompletePanel.setOnItemClickListener((p1, p2, p3, p4) -> {
            // TODO: Implement this method
            select(p3);
        });

    }

    public void selectFirst() {
        select(0);
    }

    public void select(int pos) {
        LuaMessage luaMessage = _adapter.getItem(pos).getLuaMessage();
        View view = _adapter.getView(pos, null, null);
        TextView textView = view.findViewById(R.id.auto_panel_text);
        String text = luaMessage.getName();
        String commitText = null;
        boolean isFunc = luaMessage.getType() == LuaMessage.TYPE_FUNCTION;
        if (isFunc)
            commitText = text + "()";
        else
            commitText = text;
//        boolean isFunc = text.contains("(");
//        if (isFunc) {
//            commitText = text.substring(0, text.indexOf('(')) + "()";
//        } else {
//            commitText = text;
//        }
        _textField.replaceText(_textField.getCaretPosition() - _constraint.length(), _constraint.length(), commitText);
        _adapter.abort();
        dismiss();
        if (isFunc) {
            _textField.moveCaretLeft();
        }
    }

    public void setWidth(int width) {
        // TODO: Implement this method
        _autoCompletePanel.setWidth(width);
    }

    private void setHeight(int height) {
        // TODO: Implement this method

        if (_height != height) {
            _height = height;
            _autoCompletePanel.setHeight(height);
        }
    }

    private void setHorizontalOffset(int horizontal) {
        // TODO: Implement this method
        horizontal = Math.min(horizontal, _textField.getWidth() / 2);
        if (_horizontal != horizontal) {
            _horizontal = horizontal;
            _autoCompletePanel.setHorizontalOffset(horizontal);
        }
    }

    private void setVerticalOffset(int verticalOffset) {
        // TODO: Implement this method
        //verticalOffset=Math.min(verticalOffset,_textField.getWidth()/2);
        int max = 0 - _autoCompletePanel.getHeight();
        if (verticalOffset > max) {
            _textField.scrollBy(0, verticalOffset - max);
            verticalOffset = max;
        }
        if (_verticalOffset != verticalOffset) {
            _verticalOffset = verticalOffset;
            _autoCompletePanel.setVerticalOffset(verticalOffset);
        }
    }

    public void update(CharSequence constraint) {
        _adapter.restart();
        _filter.filter(constraint);
    }

    public void show() {
        if (!_autoCompletePanel.isShowing())
            _autoCompletePanel.show();
        _autoCompletePanel.getListView().setFadingEdgeLength(0);
        isShow = true;
    }

    public void dismiss() {
        if (_autoCompletePanel.isShowing()) {
            isShow = false;
            _autoCompletePanel.dismiss();
        }

    }

    public boolean isShow() {
        return _autoCompletePanel.isShowing();
    }

    private void log(String log) {
        if (DEBUG) {
            System.out.println("-------------->AutoCompletePanel:" + log);
        }
    }

    class ListItem {
        private Bitmap bitmap;
        private LuaMessage luaMessage;

        public ListItem(Bitmap bitmap, LuaMessage luaMessage) {
            this.bitmap = bitmap;
            this.luaMessage = luaMessage;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public LuaMessage getLuaMessage() {
            return luaMessage;
        }

        public void setLuaMessage(LuaMessage luaMessage) {
            this.luaMessage = luaMessage;
        }
    }

    /**
     * Adapter定义
     */
    class AutoPanelAdapter extends BaseAdapter implements Filterable {

        private int _h;
        private Flag _abort;
        private DisplayMetrics dm;
        private List<ListItem> listItems;
        private Bitmap bitmap;

        public AutoPanelAdapter(Context context) {
            _abort = new Flag();
            listItems = new ArrayList<>();
            dm = context.getResources().getDisplayMetrics();
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_method);
        }

        public void abort() {
            _abort.set();
        }


        private int dp(float n) {
            // TODO: Implement this method
            return (int) TypedValue.applyDimension(1, n, dm);
        }

        @Override
        public int getCount() {
            return listItems.size();
        }

        @Override
        public ListItem getItem(int i) {
            return listItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View tempView = null;
            if (view == null) {
                View rootView = LayoutInflater.from(_context).inflate(R.layout.item_auto_panel, null);
                tempView = rootView;
            } else {
                tempView = view;
            }
            TextView textView = tempView.findViewById(R.id.auto_panel_text);
            ImageView imageView = tempView.findViewById(R.id.auto_panel_icon);
            ImageView iv_wiki = tempView.findViewById(R.id.iv_wiki);
            LuaMessage luaMessage = getItem(i).getLuaMessage();
            String text = luaMessage.getName();
            SpannableString spannableString = null;
            ForegroundColorSpan foregroundColorSpan = null;
            log(text);

            iv_wiki.setVisibility(View.GONE);

            switch (luaMessage.getType()) {
                case LuaMessage.TYPE_FUNCTION:
                    text += "() : " + luaMessage.getReturnValue();
                    //函数
                    spannableString = new SpannableString(text);
                    foregroundColorSpan = new ForegroundColorSpan(LuaUtil.c(R.color.textNormal));
                    spannableString.setSpan(foregroundColorSpan, text.indexOf('('), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    iv_wiki.setVisibility(View.VISIBLE);
                    break;
                case LuaMessage.TYPE_CONSTANT:
                    spannableString = new SpannableString(text);
                    iv_wiki.setVisibility(View.VISIBLE);
                    break;
                case LuaMessage.TYPE_KEY:
                    //log("key:"+text);
                    //设置自动提示字体颜色（关键字）
                    foregroundColorSpan = new ForegroundColorSpan(0xff2c82c8);
//                    int idx = text.indexOf("[keyword]");
//                    text = text.substring(0, idx);
                    spannableString = new SpannableString(text);
                    spannableString.setSpan(foregroundColorSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                default:
                    //其他
                    spannableString = new SpannableString(text);
                    foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
                    spannableString.setSpan(foregroundColorSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            iv_wiki.setOnClickListener(v -> {
                String message = "";

                switch (luaMessage.getType()) {
                    case LuaMessage.TYPE_FUNCTION:
                        message="所属："+luaMessage.getAscription()
                                +"\n参数："+luaMessage.getArgs()
                                +"\n返回："+luaMessage.getReturnValue()
                                +"\n说明："+luaMessage.getDoc();
                        break;
                    case LuaMessage.TYPE_CONSTANT:
                        message= "值："+luaMessage.getValue()
                                +"\n说明："+luaMessage.getDoc();
                        break;
                }

                View[] views = dialogUtils.dialogt(luaMessage.getName(), message);
                Button b1, b2;
                b1 = (Button) views[0];
                b2 = (Button) views[1];
                b1.setText("关闭");
                b2.setText("插入");
                dialogUtils.getMessageTextView().setGravity(Gravity.LEFT);
                b1.setOnClickListener(v1 -> {
                    dialogUtils.dis();
                });

                b2.setOnClickListener(v12 -> {
                    dialogUtils.dis();
                    select(i);
                });

            });
//            if (luaMessage.getType() == LuaMessage.TYPE_FUNCTION) {
//                     } else if (text.contains("[keyword]")) {
//
//            } else {
//
//            }
            textView.setText(spannableString);
            imageView.setImageBitmap(getItem(i).getBitmap());
            return tempView;
        }

        public void restart() {
            // TODO: Implement this method
            _abort.clear();
        }

        /**
         * 计算列表高
         *
         * @return
         */
        public int getItemHeight() {
            if (_h != 0)
                return _h;
            LayoutInflater inflater = LayoutInflater.from(_context);
            View rootView = inflater.inflate(R.layout.item_auto_panel, null);
            rootView.measure(0, 0);
            _h = rootView.getMeasuredHeight();

            return _h;
        }

        /**
         * 实现自动完成的过滤算法
         */
        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                /**
                 * 本方法在后台线程执行，定义过滤算法
                 */
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    // 此处实现过滤
                    // 过滤后利用FilterResults将过滤结果返回
                    ArrayList<LuaMessage> buf = new ArrayList<LuaMessage>();
                    String input = String.valueOf(constraint).toLowerCase();

                    LuaMessage[] keywords = _globalLanguage.getUserWord();
                    for (LuaMessage k : keywords) {
                        if (k.getName().toLowerCase().startsWith(input))
                            buf.add(k);
                    }
                    keywords = _globalLanguage.getKeywords();
                    for (LuaMessage k : keywords) {
                        if (k.getName().indexOf(input) == 0)
                            buf.add(k);
                    }

                    LuaConstant[] conList = _globalLanguage.getConstant();
                    for (LuaConstant k : conList) {
                        if (k.getName().toLowerCase().startsWith(input))
                            buf.add(k);
                    }

                    LuaFunction[] funList = _globalLanguage.getNames();
                    for (LuaFunction k : funList) {
                        if (k.getName().toLowerCase().startsWith(input))
                            buf.add(k);
                    }
                    _constraint = input;
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = buf;   // results是上面的过滤结果
                    filterResults.count = buf.size();  // 结果数量
                    return filterResults;
                }

                /**
                 * 本方法在UI线程执行，用于更新自动完成列表
                 */
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0 && !_abort.isSet()) {
                        // 有过滤结果，显示自动完成列表
                        listItems.clear();   // 清空旧列表
                        ArrayList<LuaMessage> stringArrayList = (ArrayList<LuaMessage>) results.values;
                        for (int i = 0; i < stringArrayList.size(); i++) {
                            LuaMessage itemText = stringArrayList.get(i);
                            switch (itemText.getType()) {
                                case LuaMessage.TYPE_KEY:
                                    listItems.add(new ListItem(null, itemText));
                                    break;
                                case LuaMessage.TYPE_CONSTANT:
                                    listItems.add(new ListItem(null, itemText));
                                    break;
                                case LuaMessage.TYPE_FUNCTION:
                                    listItems.add(new ListItem(bitmap, itemText));
                                    break;
                            }
                        }
                        int y = _textField.getCaretY() + _textField.rowHeight() / 2 - _textField.getScrollY();
                        setHeight(getItemHeight() * Math.min(5, results.count));

                        setHorizontalOffset(PADDING);
                        setWidth(_textField.getWidth() - PADDING * 2);
                        setVerticalOffset(y - _textField.getHeight());//_textField.getCaretY()-_textField.getScrollY()-_textField.getHeight());
                        notifyDataSetChanged();
                        show();
                    } else {
                        // 无过滤结果，关闭列表
                        notifyDataSetInvalidated();
                    }
                }

            };
            return filter;
        }
    }
}

