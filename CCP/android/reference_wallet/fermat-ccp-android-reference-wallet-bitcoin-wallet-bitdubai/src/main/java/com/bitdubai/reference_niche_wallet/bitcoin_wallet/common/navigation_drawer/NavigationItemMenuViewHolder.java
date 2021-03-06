package com.bitdubai.reference_niche_wallet.bitcoin_wallet.common.navigation_drawer;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitdubai.android_fermat_ccp_wallet_bitcoin.R;
import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;


/**
 * Created by Matias Furszyfer 22/09/2015
 */


public class NavigationItemMenuViewHolder extends FermatViewHolder {

    private TextView label;
    private ImageView icon;
    private LinearLayout row_container;
    private View navigation_row_divider;

    public NavigationItemMenuViewHolder(View itemView) {
        super(itemView);

        label = (TextView) itemView.findViewById(R.id.textView_label);
        icon = (ImageView) itemView.findViewById(R.id.imageView_icon);
        row_container = (LinearLayout) itemView.findViewById(R.id.row_container);
        navigation_row_divider = (View) itemView.findViewById(R.id.navigation_row_divider);

    }

    public TextView getLabel() {
        return label;
    }

    public ImageView getIcon() {
        return icon;
    }

    public LinearLayout getRow_container() {
        return row_container;
    }

    public View getNavigation_row_divider() {
        return navigation_row_divider;
    }
}
