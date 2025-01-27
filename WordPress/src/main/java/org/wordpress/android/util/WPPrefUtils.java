package org.wordpress.android.util;

import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.TextView;

import org.wordpress.android.R;
import org.wordpress.android.fluxc.Dispatcher;
import org.wordpress.android.fluxc.generated.SiteActionBuilder;
import org.wordpress.android.fluxc.model.SiteModel;
import org.wordpress.android.fluxc.store.SiteStore;
import org.wordpress.android.fluxc.store.SiteStore.DesignateMobileEditorPayload;
import org.wordpress.android.ui.prefs.AppPrefs;

import java.util.List;

/**
 * Design guidelines for Calypso-styled Site Settings (and likely other screens)
 */

public class WPPrefUtils {
    /**
     * Gets a preference and sets the {@link android.preference.Preference.OnPreferenceChangeListener}.
     */
    public static Preference getPrefAndSetClickListener(PreferenceFragment prefFrag,
                                                        int id,
                                                        Preference.OnPreferenceClickListener listener) {
        Preference pref = prefFrag.findPreference(prefFrag.getString(id));
        if (pref != null) {
            pref.setOnPreferenceClickListener(listener);
        }
        return pref;
    }

    /**
     * Gets a preference and sets the {@link android.preference.Preference.OnPreferenceChangeListener}.
     */
    public static Preference getPrefAndSetChangeListener(PreferenceFragment prefFrag,
                                                         int id,
                                                         Preference.OnPreferenceChangeListener listener) {
        Preference pref = prefFrag.findPreference(prefFrag.getString(id));
        if (pref != null) {
            pref.setOnPreferenceChangeListener(listener);
        }
        return pref;
    }

    /**
     * Removes a {@link Preference} from the {@link PreferenceCategory} with the given key.
     */
    public static void removePreference(PreferenceFragment prefFrag, int parentKey, int prefKey) {
        String parentName = prefFrag.getString(parentKey);
        String prefName = prefFrag.getString(prefKey);
        PreferenceGroup parent = (PreferenceGroup) prefFrag.findPreference(parentName);
        Preference child = prefFrag.findPreference(prefName);

        if (parent != null && child != null) {
            parent.removePreference(child);
        }
    }

    /**
     * Styles a {@link TextView} to display a large title against a dark background.
     */
    public static void layoutAsLightTitle(TextView view) {
        int size = view.getResources().getDimensionPixelSize(R.dimen.text_sz_extra_large);
        setTextViewAttributes(view, size, android.R.color.white);
    }

    /**
     * Styles a {@link TextView} to display a large title against a light background.
     */
    public static void layoutAsDarkTitle(TextView view) {
        int size = view.getResources().getDimensionPixelSize(R.dimen.text_sz_extra_large);
        setTextViewAttributes(view, size, R.color.neutral_700);
    }

    /**
     * Styles a {@link TextView} to display medium sized text as a header with sub-elements.
     */
    public static void layoutAsSubhead(TextView view) {
        int color = view.isEnabled() ? R.color.neutral_700 : R.color.neutral_200;
        int size = view.getResources().getDimensionPixelSize(R.dimen.text_sz_large);
        setTextViewAttributes(view, size, color);
    }

    /**
     * Styles a {@link TextView} to display smaller text.
     */
    public static void layoutAsBody1(TextView view) {
        int color = view.isEnabled() ? R.color.neutral : R.color.neutral_200;
        int size = view.getResources().getDimensionPixelSize(R.dimen.text_sz_medium);
        setTextViewAttributes(view, size, color);
    }

    /**
     * Styles a {@link TextView} to display smaller text with a dark grey color.
     */
    public static void layoutAsBody2(TextView view) {
        int size = view.getResources().getDimensionPixelSize(R.dimen.text_sz_medium);
        setTextViewAttributes(view, size, R.color.neutral);
    }

    /**
     * Styles a {@link TextView} to display very small helper text.
     */
    public static void layoutAsCaption(TextView view) {
        int size = view.getResources().getDimensionPixelSize(R.dimen.text_sz_small);
        setTextViewAttributes(view, size, R.color.neutral_400);
    }

    /**
     * Styles a {@link TextView} to display text in a button.
     */
    public static void layoutAsFlatButton(TextView view) {
        int size = view.getResources().getDimensionPixelSize(R.dimen.text_sz_medium);
        setTextViewAttributes(view, size, R.color.primary_400);
    }

    /**
     * Styles a {@link TextView} to display text in a button.
     */
    public static void layoutAsRaisedButton(TextView view) {
        int size = view.getResources().getDimensionPixelSize(R.dimen.text_sz_medium);
        setTextViewAttributes(view, size, android.R.color.white);
    }

    /**
     * Styles a {@link TextView} to display text in an editable text field.
     */
    public static void layoutAsInput(EditText view) {
        int size = view.getResources().getDimensionPixelSize(R.dimen.text_sz_large);
        setTextViewAttributes(view, size, R.color.neutral_700);
        view.setHintTextColor(view.getResources().getColor(R.color.neutral_200));
        view.setTextColor(view.getResources().getColor(R.color.neutral_700));
        view.setSingleLine(true);
    }

    /**
     * Styles a {@link TextView} to display selected numbers in a {@link android.widget.NumberPicker}.
     */
    public static void layoutAsNumberPickerSelected(TextView view) {
        int size = view.getResources().getDimensionPixelSize(R.dimen.text_sz_triple_extra_large);
        setTextViewAttributes(view, size, R.color.primary_400);
    }

    /**
     * Styles a {@link TextView} to display non-selected numbers in a {@link android.widget.NumberPicker}.
     */
    public static void layoutAsNumberPickerPeek(TextView view) {
        int size = view.getResources().getDimensionPixelSize(R.dimen.text_sz_large);
        setTextViewAttributes(view, size, R.color.neutral_700);
    }

    /**
     * Styles a {@link TextView} to display text in a dialog message.
     */
    public static void layoutAsDialogMessage(TextView view) {
        int size = view.getResources().getDimensionPixelSize(R.dimen.text_sz_small);
        setTextViewAttributes(view, size, R.color.neutral);
    }

    public static void setTextViewAttributes(TextView textView, int size, int colorRes) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        textView.setTextColor(textView.getResources().getColor(colorRes));
    }

    public static void setMobileEditorPreferenceToRemote(final Dispatcher dispatcher, final SiteStore siteStore) {
        final List<SiteModel> sitesAccessedViaWPComRest = siteStore.getSitesAccessedViaWPComRest();
        final boolean setDelay = sitesAccessedViaWPComRest.size() > 5;
        final String editorSetting = AppPrefs.isGutenbergDefaultForNewPosts() ? "gutenberg" : "aztec";
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (SiteModel currentSite : sitesAccessedViaWPComRest) {
                    dispatcher.dispatch(SiteActionBuilder.newDesignateMobileEditorAction(
                            new DesignateMobileEditorPayload(currentSite, editorSetting)));
                    if (setDelay) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            // no-op
                        }
                    }
                }
            }
        }).start();
    }
}
