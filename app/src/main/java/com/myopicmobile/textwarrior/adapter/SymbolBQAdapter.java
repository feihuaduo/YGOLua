package com.myopicmobile.textwarrior.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.myopicmobile.textwarrior.bean.Symbol;
import com.ourygo.ygolua.R;

import java.util.List;

/**
 * Create By feihua  On 2022/1/15
 */
public class SymbolBQAdapter extends BaseQuickAdapter<Symbol, BaseViewHolder> {
    public SymbolBQAdapter(List<Symbol> data) {
        super(R.layout.symbol_item,data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, Symbol symbol) {
        baseViewHolder.setText(R.id.tv_message,symbol.getName());
    }
}
