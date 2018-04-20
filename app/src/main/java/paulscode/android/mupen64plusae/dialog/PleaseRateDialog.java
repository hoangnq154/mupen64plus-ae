package paulscode.android.mupen64plusae.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog.Builder;
import android.util.Log;

import org.mupen64plusae.v3.fzurita.R;

public class PleaseRateDialog extends DialogFragment
{
    /**
     * The listener interface for handling confirmations.
     */
    public interface PromptRateListener
    {
        /**
         * Handle the user's confirmation.
         */
        void onPromptRateDialogClosed(int which);
    }

    public static PleaseRateDialog newInstance()
    {
        PleaseRateDialog frag = new PleaseRateDialog();
        Bundle args = new Bundle();

        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        setRetainInstance(true);

        final String title = getString(R.string.confirmRate);
        final String message = getString(R.string.confirmPleaseRate);

        // When the user clicks Ok, notify the downstream listener
        OnClickListener internalListener = new OnClickListener()
        {
            @Override
            public void onClick( DialogInterface dialog, int which )
            {
                if (getActivity() instanceof PromptRateListener)
                {
                    ((PromptRateListener) getActivity()).onPromptRateDialogClosed(which);
                }
                else
                {
                    Log.e("PleaseRateDialog", "Activity doesn't implement PromptConfirmListener");
                }
            }
        };

        Builder builder = new Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(getActivity().getString(R.string.confirmRate), internalListener);
        builder.setNeutralButton(getActivity().getString(R.string.confirmRemindMeLater), internalListener);
        builder.setNegativeButton(getActivity().getString(R.string.confirmNoThanks), internalListener);
        
        return builder.create();
    }
    
    @Override
    public void onCancel(DialogInterface dialog)
    {
        if (getActivity() instanceof PromptRateListener)
        {
            ((PromptRateListener) getActivity()).onPromptRateDialogClosed(DialogInterface.BUTTON_NEUTRAL);
        }
        else
        {
            Log.e("PleaseRateDialog", "Activity doesn't implement PromptConfirmListener");
        }
    }

    @Override
    public void onDestroyView()
    {
        // This is needed because of this:
        // https://code.google.com/p/android/issues/detail?id=17423

        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }
}