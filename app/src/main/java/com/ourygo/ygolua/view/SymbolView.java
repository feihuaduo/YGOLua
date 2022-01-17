package com.ourygo.ygolua.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.myopicmobile.textwarrior.adapter.SymbolBQAdapter;
import com.myopicmobile.textwarrior.bean.Symbol;
import com.myopicmobile.textwarrior.util.ScaleUtils;
import com.ourygo.ygolua.R;

import java.util.ArrayList;
import java.util.List;

/*
 符号栏类
 */
public class SymbolView {
    private final int TILE_WIDTH = 60;
    private PopupWindow popupWindow;
    private View rootView;
    private OnSymbolViewClick onSymbolViewClick;
    private boolean visible = false;
    private InputMethodManager inputMethodManager;
    private boolean isFirst = true;
    private int maxLayoutHeight = 0;//布局总长
    private int currentLayoutHeight = 0;//当前布局高

    @SuppressLint("ClickableViewAccessibility")
    public SymbolView(final Context context, final View rootView) {
        this.rootView = rootView;
        popupWindow = new PopupWindow(context);
        inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = LayoutInflater.from(context).inflate(R.layout.symbol_view, null);
//        LinearLayout linearLayout = view.findViewById(R.id.linear_container);
//        final float[] tempPoint = new float[2];
//        String symbol = "⇥{}();,.=\"|'&![]<>+-\\/*?:_";
//        for (int i = 0; i < symbol.length(); i++) {
//            TextView textView = new TextView(context);
//            textView.setGravity(Gravity.CENTER);
//            textView.setText(String.valueOf(symbol.charAt(i)));
//            textView.setClickable(true);
//            textView.setTextSize(25);
//            textView.setWidth(TILE_WIDTH);
//            textView.setPadding(ScaleUtils.dp2px(context,5),ScaleUtils.dp2px(context,5),ScaleUtils.dp2px(context,5),ScaleUtils.dp2px(context,5));
//            textView.setOnTouchListener((v, event) -> {
//                int color = v.getDrawingCacheBackgroundColor();
//                int motionEvent = event.getAction();
//                TextView tv = (TextView) v;
//
//                if (motionEvent == MotionEvent.ACTION_DOWN) {
//                    tempPoint[0] = event.getX();
//                    tempPoint[1] = event.getY();
//                    tv.setBackgroundColor(0xffcecfd1);
//
//                } else if (motionEvent == MotionEvent.ACTION_UP || motionEvent == MotionEvent.ACTION_CANCEL) {
//                    tv.setBackgroundColor(c(context,R.color.symbolBackground));
//                    if (Math.abs(event.getX() - tempPoint[0]) < TILE_WIDTH) {
//                        if (onSymbolViewClick != null)
//                            onSymbolViewClick.onClick(tv, tv.getText().toString());
//                    }
//                }
//                return true;
//            });
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            linearLayout.setGravity(Gravity.CENTER);
//            linearLayout.addView(textView, layoutParams);
//
//        }


        RecyclerView rv_symbol = view.findViewById(R.id.rv_symbol);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        rv_symbol.setLayoutManager(linearLayoutManager);

        List<Symbol> data = new ArrayList<>();
        data.add(new Symbol("⇥"));
        data.add(new Symbol("{"));
        data.add(new Symbol("}"));
        data.add(new Symbol("("));
        data.add(new Symbol(")"));
        data.add(new Symbol(";"));
        data.add(new Symbol(","));
        data.add(new Symbol("."));
        data.add(new Symbol("="));
        data.add(new Symbol("\""));
        data.add(new Symbol("|"));
        data.add(new Symbol("&"));
        data.add(new Symbol("!"));
        data.add(new Symbol("["));
        data.add(new Symbol("]"));
        data.add(new Symbol("<"));
        data.add(new Symbol(">"));
        data.add(new Symbol("+"));
        data.add(new Symbol("-"));
        data.add(new Symbol("/"));
        data.add(new Symbol("*"));
        data.add(new Symbol("?"));
        data.add(new Symbol(":"));
        data.add(new Symbol("_"));

        SymbolBQAdapter symbolAdp = new SymbolBQAdapter(data);
        rv_symbol.setAdapter(symbolAdp);

        symbolAdp.setOnItemClickListener((adapter, view1, position) -> {
            Symbol symbol = symbolAdp.getItem(position);
//            int index = yet_lua.getSelectionStart();
//            Editable editable = yet_lua.getText();
            String symbolMessage = symbol.getSymbol();
            boolean isMove = false;
            if (symbolMessage.equals("{")) {
                symbolMessage += "}";
                isMove = true;
            }else if (symbolMessage.equals("⇥")){
//                symbolMessage="\u3000";
                symbolMessage="\t";
            }
            if (onSymbolViewClick != null)
                onSymbolViewClick.onClick(view1, symbolMessage,isMove);
//            if (isMove)
//                yet_lua.setSelection(yet_lua.getSelectionStart() - 1);
        });

        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //popupWindow.setHeight(EditCodeActivity.height);
        popupWindow.getBackground().setAlpha(0);//窗口完全透明
//        view.setBackgroundColor(c(context,R.color.symbolBackground));//视图不完全透明

        popupWindow.setContentView(view);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
                    Rect r = new Rect();
                    rootView.getWindowVisibleDisplayFrame(r);
                    if (isFirst) {
                        maxLayoutHeight = r.bottom;//初始化时为布局的最高高度
                        currentLayoutHeight = r.bottom;//当前弹出的布局高
                        isFirst = false;
                    } else {
                        currentLayoutHeight = r.bottom;//当前弹出的布局高
                    }
                    if (currentLayoutHeight == maxLayoutHeight || !visible) {
                        hide();
                    } else if (currentLayoutHeight < maxLayoutHeight) {
                        show(rootView.getHeight() - r.bottom);
                    }
                });
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    private void show(int bottom) {
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, bottom);
    }

    private void hide() {
        popupWindow.dismiss();
    }

    public void setOnSymbolViewClick(OnSymbolViewClick onSymbolViewClick) {
        this.onSymbolViewClick = onSymbolViewClick;
    }


    public interface OnSymbolViewClick {
        void onClick(View view, String text,boolean isMoveLeft);
    }


}
