package me.onlyfire.yukigram.ui;

import static android.view.View.IMPORTANT_FOR_ACCESSIBILITY_NO;

import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.SlideChooseView;
import org.telegram.ui.Components.StickerImageView;
import org.telegram.ui.Components.UndoView;

import java.util.ArrayList;

import me.onlyfire.yukigram.android.AlertController;
import me.onlyfire.yukigram.android.MonetIconController;
import me.onlyfire.yukigram.android.YukiConfig;

public class OwlgramExperimentalSettings extends BaseSettingsActivity {

    private int checkBoxExperimentalRow;
    private int headerImageRow;
    private int headerExperimental;
    private int betterAudioCallRow;
    private int sendLargePhotosRow;
    private int maxRecentStickersRow;
    private int reduceCameraXLatency;
    private int experimentalMessageAlert;
    private int monetIconRow;
    private int downloadDividersRow;
    private int headerDownloadSpeed;
    private int downloadSpeedBoostRow;
    private int uploadSpeedBoostRow;
    private int altNavigationRow;
    private int bottomSpaceRow;

    @Override
    protected String getActionBarTitle() {
        return LocaleController.getString("Experimental", R.string.Experimental);
    }

    @Override
    protected void onItemClick(View view, int position, float x, float y) {
        if (position == betterAudioCallRow) {
            YukiConfig.toggleBetterAudioQuality();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(YukiConfig.betterAudioQuality);
            }
        } else if (position == maxRecentStickersRow) {
            int[] counts = {20, 30, 40, 50, 80, 100, 120, 150, 180, 200};
            ArrayList<String> types = new ArrayList<>();
            for (int count : counts) {
                if (count <= getMessagesController().maxRecentStickersCount) {
                    types.add(String.valueOf(count));
                }
            }
            AlertController.show(types, LocaleController.getString("MaxRecentStickers", R.string.MaxRecentStickers), types.indexOf(String.valueOf(YukiConfig.maxRecentStickers)), context, i -> {
                YukiConfig.setMaxRecentStickers(Integer.parseInt(types.get(i)));
                listAdapter.notifyItemChanged(maxRecentStickersRow, PARTIAL);
            });
        } else if (position == checkBoxExperimentalRow) {
            if (view instanceof TextCheckCell) {
                TextCheckCell textCheckCell = (TextCheckCell) view;
                if (MonetIconController.isSelectedMonet()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                    builder.setMessage(LocaleController.getString("DisableExperimentalAlert", R.string.DisableExperimentalAlert));
                    builder.setPositiveButton(LocaleController.getString("AutoDeleteConfirm", R.string.AutoDeleteConfirm), (dialogInterface, i) -> {
                        MonetIconController.switchToMonet();
                        toggleExperimentalMode(textCheckCell);
                        AlertDialog progressDialog = new AlertDialog(getParentActivity(), 3);
                        progressDialog.show();
                        AndroidUtilities.runOnUIThread(progressDialog::dismiss, 2000);
                    });
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    builder.show();
                } else {
                    toggleExperimentalMode(textCheckCell);
                }
            }
        } else if (position == monetIconRow) {
            MonetIconController.switchToMonet();
            AlertDialog progressDialog = new AlertDialog(getParentActivity(), 3);
            progressDialog.show();
            AndroidUtilities.runOnUIThread(progressDialog::dismiss, 2000);
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(MonetIconController.isSelectedMonet());
            }
        } else if (position == uploadSpeedBoostRow) {
            YukiConfig.toggleUploadSpeedBoost();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(YukiConfig.uploadSpeedBoost);
            }
        } else if (position == altNavigationRow) {
            YukiConfig.toggleAlternativeNavigation();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(YukiConfig.alternativeNavigation);
            }
            restartTooltip.showWithAction(0, UndoView.ACTION_NEED_RESTART, null, null);
        } else if (position == sendLargePhotosRow) {
            YukiConfig.toggleSendLargePhotos();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(YukiConfig.sendLargePhotos);
            }
        } else if (position == reduceCameraXLatency) {
            YukiConfig.toggleReduceCameraXLatency();
            if (view instanceof TextCheckCell) {
                ((TextCheckCell) view).setChecked(YukiConfig.reduceCameraXLatency);
            }
        }
    }

    private void toggleExperimentalMode(TextCheckCell textCheckCell) {
        YukiConfig.toggleDevOpt();
        boolean isEnabled = YukiConfig.isDevOptEnabled();
        textCheckCell.setChecked(isEnabled);
        textCheckCell.setText(isEnabled ? LocaleController.getString("OnModeCheckTitle", R.string.OnModeCheckTitle) : LocaleController.getString("OffModeCheckTitle", R.string.OffModeCheckTitle));
        textCheckCell.setBackgroundColorAnimated(isEnabled, Theme.getColor(isEnabled ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
        if (isEnabled) {
            listAdapter.notifyItemRemoved(experimentalMessageAlert);
            updateRowsId();
            listAdapter.notifyItemRangeInserted(headerImageRow, rowCount - headerImageRow);
        } else {
            listAdapter.notifyItemRangeRemoved(headerImageRow, rowCount - headerImageRow);
            updateRowsId();
            listAdapter.notifyItemInserted(experimentalMessageAlert);
        }
    }

    @Override
    protected void updateRowsId() {
        super.updateRowsId();
        headerImageRow = -1;
        headerExperimental = -1;
        betterAudioCallRow = -1;
        sendLargePhotosRow = -1;
        reduceCameraXLatency = -1;
        maxRecentStickersRow = -1;
        monetIconRow = -1;
        downloadDividersRow = -1;
        headerDownloadSpeed = -1;
        downloadSpeedBoostRow = -1;
        uploadSpeedBoostRow = -1;
        altNavigationRow = -1;
        experimentalMessageAlert = -1;
        bottomSpaceRow = -1;

        checkBoxExperimentalRow = rowCount++;
        if (YukiConfig.isDevOptEnabled()) {
            headerImageRow = rowCount++;
            headerExperimental = rowCount++;
            betterAudioCallRow = rowCount++;
            sendLargePhotosRow = rowCount++;
            reduceCameraXLatency = rowCount++;
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.S || Build.VERSION.SDK_INT == Build.VERSION_CODES.S_V2) {
                monetIconRow = rowCount++;
            }
            maxRecentStickersRow = rowCount++;
            altNavigationRow = rowCount++;
            downloadDividersRow = rowCount++;
            headerDownloadSpeed = rowCount++;
            downloadSpeedBoostRow = rowCount++;
            uploadSpeedBoostRow = rowCount++;
            bottomSpaceRow = rowCount++;
        } else {
            experimentalMessageAlert = rowCount++;
        }
    }

    @Override
    protected BaseListAdapter createAdapter() {
        return new ListAdapter();
    }

    private class ListAdapter extends BaseListAdapter {

        @Override
        protected void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, boolean partial) {
            switch (ViewType.fromInt(holder.getItemViewType())) {
                case SWITCH:
                    TextCheckCell textCheckCell = (TextCheckCell) holder.itemView;
                    if (position == betterAudioCallRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("MediaStreamVoip", R.string.MediaStreamVoip), YukiConfig.betterAudioQuality, true);
                    } else if (position == checkBoxExperimentalRow) {
                        boolean isEnabled = YukiConfig.isDevOptEnabled();
                        textCheckCell.setDrawCheckRipple(true);
                        textCheckCell.setTextAndCheck(isEnabled ? LocaleController.getString("OnModeCheckTitle", R.string.OnModeCheckTitle) : LocaleController.getString("OffModeCheckTitle", R.string.OffModeCheckTitle), isEnabled, false);
                        textCheckCell.setBackgroundColor(Theme.getColor(isEnabled ? Theme.key_windowBackgroundChecked : Theme.key_windowBackgroundUnchecked));
                        textCheckCell.setColors(Theme.key_windowBackgroundCheckText, Theme.key_switchTrackBlue, Theme.key_switchTrackBlueChecked, Theme.key_switchTrackBlueThumb, Theme.key_switchTrackBlueThumbChecked);
                        textCheckCell.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                        textCheckCell.setHeight(56);
                    } else if (position == monetIconRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("MonetIcon", R.string.MonetIcon), LocaleController.getString("MonetIconDesc", R.string.MonetIconDesc), MonetIconController.isSelectedMonet(), true, true);
                    } else if (position == uploadSpeedBoostRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("FasterUploadSpeed", R.string.FasterUploadSpeed), YukiConfig.uploadSpeedBoost, false);
                    } else if (position == altNavigationRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("AltNavigation", R.string.AltNavigation), LocaleController.getString("AltNavigationDesc", R.string.AltNavigationDesc), YukiConfig.alternativeNavigation, true, true);
                    } else if (position == sendLargePhotosRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("HRPhotos", R.string.HRPhotos), LocaleController.getString("HRPhotosDesc", R.string.HRPhotosDesc), YukiConfig.sendLargePhotos, true, true);
                    } else if (position == reduceCameraXLatency) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("ZeroShutterLag", R.string.ZeroShutterLag), LocaleController.getString("ZeroShutterLagDesc", R.string.ZeroShutterLagDesc), YukiConfig.reduceCameraXLatency, true, true);
                    }
                    break;
                case TEXT_HINT_WITH_PADDING:
                    TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell) holder.itemView;
                    if (position == experimentalMessageAlert) {
                        textInfoPrivacyCell.setText(LocaleController.getString("ExperimentalOff", R.string.ExperimentalOff));
                    }
                    break;
                case HEADER:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == headerExperimental) {
                        headerCell.setText(LocaleController.getString("General", R.string.General));
                    } else if (position == headerDownloadSpeed) {
                        headerCell.setText(LocaleController.getString("DownloadSpeed", R.string.DownloadSpeed));
                    }
                    break;
                case SETTINGS:
                    TextSettingsCell textCell = (TextSettingsCell) holder.itemView;
                    textCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    if (position == maxRecentStickersRow) {
                        textCell.setTextAndValue(LocaleController.getString("MaxRecentStickers", R.string.MaxRecentStickers), String.valueOf(YukiConfig.maxRecentStickers), partial, true);
                    }
                    break;
            }
        }

        @Override
        protected boolean isEnabled(ViewType viewType, int position) {
            return viewType == ViewType.SWITCH && position != checkBoxExperimentalRow || viewType == ViewType.SETTINGS;
        }

        @Override
        protected View onCreateViewHolder(ViewType viewType) {
            View view = null;
            switch (viewType) {
                case IMAGE_HEADER:
                    LinearLayout stickerHeaderCell = new LinearLayout(context);
                    stickerHeaderCell.setOrientation(LinearLayout.VERTICAL);
                    StickerImageView backupImageView = new StickerImageView(context, currentAccount);
                    backupImageView.setStickerPackName("UtyaDuckFull");
                    backupImageView.setStickerNum(24);
                    stickerHeaderCell.addView(backupImageView, LayoutHelper.createLinear(140, 140, Gravity.CENTER, 0, 20, 0, 5));
                    TextView textView = new TextView(context);
                    textView.setText(LocaleController.getString("ExperimentalDesc", R.string.ExperimentalDesc));
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                    textView.setGravity(Gravity.CENTER);
                    textView.setPadding(0, AndroidUtilities.dp(10), 0, AndroidUtilities.dp(17));
                    textView.setTextColor(getThemedColor(Theme.key_windowBackgroundWhiteGrayText4));
                    textView.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);
                    stickerHeaderCell.addView(textView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.CENTER | Gravity.TOP, 25, 0, 25, 0));
                    view = stickerHeaderCell;
                    break;
                case SLIDE_CHOOSE:
                    SlideChooseView slideChooseView = new SlideChooseView(context);
                    view = slideChooseView;
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    ArrayList<String> arrayList = new ArrayList<>();
                    ArrayList<Integer> types = new ArrayList<>();
                    arrayList.add(LocaleController.getString("DownloadSpeedDefault", R.string.DownloadSpeedDefault));
                    types.add(YukiConfig.DOWNLOAD_BOOST_DEFAULT);
                    arrayList.add(LocaleController.getString("DownloadSpeedFast", R.string.DownloadSpeedFast));
                    types.add(YukiConfig.DOWNLOAD_BOOST_FAST);
                    arrayList.add(LocaleController.getString("DownloadSpeedExtreme", R.string.DownloadSpeedExtreme));
                    types.add(YukiConfig.DOWNLOAD_BOOST_EXTREME);
                    slideChooseView.setCallback(index -> YukiConfig.setDownloadSpeedBoost(types.get(index)));
                    slideChooseView.setOptions(types.indexOf(YukiConfig.downloadSpeedBoost), arrayList.toArray(new String[0]));
                    slideChooseView.setDivider(true);
                    break;
            }
            return view;
        }

        @Override
        public ViewType getViewType(int position) {
            if (position == downloadDividersRow) {
                return ViewType.SHADOW;
            } else if (position == betterAudioCallRow || position == checkBoxExperimentalRow || position == monetIconRow ||
                    position == uploadSpeedBoostRow || position == sendLargePhotosRow || position == reduceCameraXLatency || position == altNavigationRow) {
                return ViewType.SWITCH;
            } else if (position == headerImageRow) {
                return ViewType.IMAGE_HEADER;
            } else if (position == experimentalMessageAlert) {
                return ViewType.TEXT_HINT_WITH_PADDING;
            } else if (position == headerExperimental || position == headerDownloadSpeed) {
                return ViewType.HEADER;
            } else if (position == maxRecentStickersRow) {
                return ViewType.SETTINGS;
            } else if (position == downloadSpeedBoostRow) {
                return ViewType.SLIDE_CHOOSE;
            } else if (position == bottomSpaceRow) {
                return ViewType.SHADOW;
            }
            throw new IllegalArgumentException("Invalid position");
        }
    }
}
