package me.onlyfire.yukigram.ui.Cells;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.browser.Browser;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.ColoredImageSpan;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.StickerImageView;

import java.util.Objects;

import me.onlyfire.yukigram.android.entities.EntitiesHelper;

public class DatacenterHeader extends LinearLayout {
    public DatacenterHeader(Context context) {
        super(context);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setOrientation(VERTICAL);
        StickerImageView rLottieImageView = new StickerImageView(context, UserConfig.selectedAccount);
        rLottieImageView.setStickerPackName("UtyaDuck");
        rLottieImageView.setStickerNum(31);
        rLottieImageView.getImageReceiver().setAutoRepeat(1);
        addView(rLottieImageView, LayoutHelper.createLinear(120, 120, Gravity.CENTER_HORIZONTAL, 0, 20, 0, 0));
    }
}
